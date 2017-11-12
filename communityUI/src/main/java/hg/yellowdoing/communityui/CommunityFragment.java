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

public class CommunityFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private RecyclerView mRecyclerView;
    private CommunityAdapter mAdapter;
    private ArrayList<Community> mCommunityBeanList;
    private BGARefreshLayout mRefreshLayout;
    private int mCurrentPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communicty, container, false);
        initViw(view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initViw(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_community);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        //设置下拉刷新并开始刷新
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.bga_refresh_layout);
        mRefreshLayout.setDelegate(CommunityFragment.this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.setIsShowLoadingMoreView(true);
        refreshViewHolder.setLoadingMoreText("加载中");

        mCommunityBeanList = new ArrayList<>();
        mAdapter = new CommunityAdapter(getActivity(), mCommunityBeanList);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.beginRefreshing();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mCurrentPage = 0;
        CommunityService.getCommunityList(mCurrentPage,this);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mCurrentPage++;
        CommunityService.getCommunityList(mCurrentPage,this);
        return true;
    }



    public void addCommunityList(ArrayList<Community> communityList){
        if (mCurrentPage == 0)
            mAdapter.reset(communityList);
        else
            mAdapter.add(communityList);
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
    }


}
