package hg.yellowdoing.communityui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment  implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private RecyclerView mRecyclerView;
    private CommunityAdapter mAdapter;
    private List<CommunityBean> mCommunityBeanList;
    private BGARefreshLayout mRefreshLayout;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_communicty, container, false);
initViw();return mView;
    }


    public void initViw(){

                mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_community);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

                //设置下拉刷新并开始刷新
                mRefreshLayout = (BGARefreshLayout) mView.findViewById(R.id.bga_refresh_layout);
                mRefreshLayout.setDelegate(CommunityFragment.this);
                BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
                mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
                mRefreshLayout.setIsShowLoadingMoreView(true);

                mCommunityBeanList = new ArrayList<>();
                CommunityBean communityBean = new CommunityBean();
                communityBean.setContent("会计核算会计的哈我");
                communityBean.setName("客户端开始减肥");
                mCommunityBeanList.add(communityBean);
                mAdapter = new CommunityAdapter(getActivity(),mCommunityBeanList);
                mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)mView.getParent()).removeAllViews();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    /**
     * 获取社区列表
     */
    private void initData(){
/*
        CommunityService.getCommunityList(1, 1, new HttpUtil.Callback() {
            @Override
            public void getResponse(String response) {

            }
        });
*/



    }


}
