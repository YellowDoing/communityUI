package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment<T> extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private RecyclerView mRecyclerView;
    private View mView;
    private CommunityAdapter mAdapter;
    private ArrayList<T> mDataList;
    private BGARefreshLayout mRefreshLayout;
    private int mCurrentPage;
    private CommunityInterface<T> mCommunityInterface;



    public CommunityInterface getService() {
        return mCommunityInterface;
    }

    public ArrayList<T> getDataList() {
        return mDataList;
    }

    /**
     * 必须要设置接口
     * @param communityInterface
     */
    public void setCommunityInterface(CommunityInterface<T> communityInterface){
        mCommunityInterface = communityInterface;
        if (mRefreshLayout != null)
            mRefreshLayout.beginRefreshing();
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
        mDataList = new ArrayList<>();

      if (mCommunityInterface != null)
          mRefreshLayout.beginRefreshing();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mCurrentPage = 0;
        mDataList = mCommunityInterface.loadDataList(mCurrentPage);
         /*mAdapter = new CommunityAdapter(getActivity(), mCommunityBeanList);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.beginRefreshing();*/
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mCurrentPage++;
        mDataList.addAll(mCommunityInterface.loadDataList(mCurrentPage));
        return false;
    }

 /*   public void addCommunityList(ArrayList<Community> communityList) {
        if (mCurrentPage == 0)
            mAdapter.reset(communityList);
        else
            mAdapter.add(communityList);
        mRefreshLayout.endRefreshing();
        mRefreshLayout.endLoadingMore();
    }
*/

}
