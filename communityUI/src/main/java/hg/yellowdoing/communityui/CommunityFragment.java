package hg.yellowdoing.communityui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;



public class CommunityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView mRecyclerView;
    private View mView;
    private CommunityAdapter mAdapter;
    private int mCurrentPage;
    public static CommunityInterface sCommunityInterface;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * 必须要设置接口
     */
    public CommunityFragment setCommunityInterface(CommunityInterface communityInterface) {
        sCommunityInterface = communityInterface;
        if (mSwipeRefreshLayout != null) {
            mAdapter = new CommunityAdapter(getActivity(), sCommunityInterface);
            mRecyclerView.setAdapter(mAdapter);
            onRefresh();
        }
        return this;
    }

    public static CommunityInterface getCommunityInterface() {
        return sCommunityInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_communicty, container, false);
        initViw(mView);
        return mView;
    }


    public void initViw(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperereshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_community);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        if (sCommunityInterface != null) {
            mAdapter = new CommunityAdapter(getActivity(), sCommunityInterface);
            mRecyclerView.setAdapter(mAdapter);
            onRefresh();
        }
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        sCommunityInterface.loadCommunityList(new CommunityInterface.CommunitySubsriber() {
            @Override
            public void onComplete(List<Community> communityList) {
                mAdapter.set(communityList);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, mCurrentPage);
    }



    /*
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
    }*/

}