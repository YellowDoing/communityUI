package hg.yellowdoing.communityui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import java.io.Serializable;
import java.util.List;

import static hg.yellowdoing.communityui.CommunityFragment.CommunityFragmentReceiver.ACTION;


public class CommunityFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private RecyclerView mRecyclerView;
    private View mView;
    private CommunityAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private int mCurrentPage;
    public static CommunityInterface sCommunityInterface;
    private CommunityFragmentReceiver mReceiver;
    private LocalBroadcastManager mManager;

    /**
     * 必须要设置接口
     */
    public CommunityFragment setCommunityInterface(CommunityInterface communityInterface) {
        sCommunityInterface = communityInterface;
        if (mRefreshLayout != null) {
            mAdapter = new CommunityAdapter(getActivity(), sCommunityInterface);
            mRecyclerView.setAdapter(mAdapter);
            mRefreshLayout.beginRefreshing();
        }
        return this;
    }

    public static CommunityInterface getCommunityInterface() {
        return sCommunityInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new CommunityFragmentReceiver();
        mManager = LocalBroadcastManager.getInstance(getActivity());
        mManager.registerReceiver(mReceiver, new IntentFilter(ACTION));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_communicty, container, false);
        initViw(mView);
        return mView;
    }


    public void initViw(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_community);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        //设置下拉刷新并开始刷新
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.bga_refresh_layout);
        mRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        if (sCommunityInterface != null) {
            mAdapter = new CommunityAdapter(getActivity(), sCommunityInterface);
            mRecyclerView.setAdapter(mAdapter);
            mRefreshLayout.beginRefreshing();
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mCurrentPage = 1;
        sCommunityInterface.loadCommunityList(new CommunityInterface.CommunitySubsriber() {
            @Override
            public void onComplete(List<Community> communityList) {
                mAdapter.set(communityList);
                mRefreshLayout.endRefreshing();
            }
        }, mCurrentPage);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mCurrentPage++;
        sCommunityInterface.loadCommunityList(new CommunityInterface.CommunitySubsriber() {
            @Override
            public void onComplete(List<Community> communityList) {
                mAdapter.add(communityList);
            }
        }, mCurrentPage);
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.unregisterReceiver(mReceiver);
    }

    public class CommunityFragmentReceiver extends BroadcastReceiver {
        public static final String ACTION = "communityFragment";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(ACTION);
            if (action.equals("loadComments")) {
                sCommunityInterface.loadComments(new CommunityInterface.CommentSubsriber() {
                    @Override
                    public void onComplete(List<Comment> communityList) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("comments", (Serializable) communityList);
                        mManager.sendBroadcast(new Intent(CommunityDetialActivity.CommunityDetailReceiver.ACTION).putExtras(bundle));
                    }
                }, intent.getStringExtra("communityId"), intent.getIntExtra("page", 1));
            } else if (action.equals("post")) {
                final Community community = (Community) intent.getSerializableExtra("community");
                sCommunityInterface.post(new CommunityInterface.Subsriber() {
                    @Override
                    public void onComplete() {
                        mManager.sendBroadcast(new Intent(PostActivity.PostReceiver.ACTION));
                        sCommunityInterface.loadCommunityList(new CommunityInterface.CommunitySubsriber() {
                            @Override
                            public void onComplete(List<Community> communityList) {
                                mAdapter.set(communityList);
                            }
                        }, 0);
                    }
                }, community.getImagePaths(), community.getContent());
            } else if (action.equals("reply")) {
                Comment comment = (Comment) intent.getSerializableExtra("comment");
                sCommunityInterface.comment(new CommunityInterface.CommentSubsriber2() {
                    @Override
                    public void onComplete(String nickName) {
                        mManager.sendBroadcast(new Intent(CommunityDetialActivity.CommunityDetailReceiver.ACTION).putExtra("isReply", true));
                    }
                }, comment.getCommunityId(), comment.getParentId(), comment.getCommentId(), comment.getContent());
            }else if (action.equals("like")){
                if (intent.getBooleanExtra("isLike",false))
                    sCommunityInterface.unLike(new CommunityInterface.Subsriber() {
                        @Override
                        public void onComplete() {
                            mManager.sendBroadcast(new Intent(CommunityDetialActivity.CommunityDetailReceiver.ACTION)
                                    .putExtra("unlikeSuccess",true));
                        }
                    },intent.getStringExtra("communityId"));
                else
                    sCommunityInterface.like(new CommunityInterface.Subsriber() {
                        @Override
                        public void onComplete() {
                            mManager.sendBroadcast(new Intent(CommunityDetialActivity.CommunityDetailReceiver.ACTION)
                                    .putExtra("likeSuccess",true));
                        }
                    },intent.getStringExtra("communityId"));
            }
        }
    }
}