package hg.yellowdoing.communityui;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YellowDing on 2017/10/18.
 *
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private List<Community> mDataList;
    private Activity mContext;
    private LayoutInflater mInflater;
    private CommunityInterface mInterface;

    public CommunityAdapter(Activity context, CommunityInterface anInterface) {
        mDataList = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mInterface = anInterface;
    }


    public void add(List<Community> list) {
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void set(List<Community> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(mInflater.inflate(R.layout.list_item_community, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommunityAdapter.CommunityViewHolder holder, int position) {

        //接口返回的图片地址
        final Community community = mDataList.get(position);

        final ArrayList<String> imagePaths = community.getImagePaths();

        if (holder.mContainer.getChildCount() > 0)
            holder.mContainer.removeAllViews();

        if (imagePaths != null && imagePaths.size() > 0) {
            RecyclerView recyclerView = new RecyclerView(mContext);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setAdapter(new ImageAdapter(mContext, imagePaths));
            // TODO: 2017/11/21 空白区域点击部分需要做处理 
            holder.mContainer.addView(recyclerView);
        }

        holder.mTvReplyNum.setText(String.valueOf(community.getReplyNum()));
        holder.mTvCreateTime.setText(isNull(community.getCreateTime()));
        holder.mTvNickName.setText(isNull(community.getNickName()));
        Glide.with(mContext).load(isNull(community.getAvatar())).centerCrop().into(holder.mIvAvatar);
        holder.mTvLikeNum.setText(String.valueOf(community.getLikeNum()));
        if (isNull(community.getContent()).trim().equals("")) holder.mTvContent.setVisibility(View.GONE);
        else {
            holder.mTvContent.setVisibility(View.VISIBLE);
            holder.mTvContent.setText(community.getContent());
        }

        //接口返回该贴是否已喜欢
        if (community.isLike()) {
            holder.mIvLike.setImageResource(R.drawable.ic_zan_hover);
            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.like_num));
        } else {
            holder.mIvLike.setImageResource(R.drawable.ic_zan);
            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.gray));
        }

        //接口回调点赞点击事件
        holder.mIvLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!community.isLike()) mInterface.like(new CommunityInterface.Subsriber() {
                        @Override
                        public void onComplete() {
                            holder.mTvLikeNum.setText(String.valueOf(Integer.parseInt(holder.mTvLikeNum.getText().toString()) + 1));
                            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.like_num));
                            holder.mIvLike.setImageResource(R.drawable.ic_zan_hover);
                            community.setLike(true);
                        }
                    }, community.getId());
                else mInterface.unLike(new CommunityInterface.Subsriber() {
                        @Override
                        public void onComplete() {
                            holder.mTvLikeNum.setText(String.valueOf(Integer.parseInt(holder.mTvLikeNum.getText().toString()) - 1));
                            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.gray));
                            holder.mIvLike.setImageResource(R.drawable.ic_zan);
                            community.setLike(false);
                        }
                    }, community.getId());
            }
        });

        //帖子点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CommunityDetialActivity.class)
                        .putExtra("community", community));
            }
        });

        //回复
        holder.mIvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/11/15 回复功能
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mIvAvatar;
        private ImageView mIvReply, mIvLike;
        public TextView mTvNickName, mTvContent, mTvCreateTime, mTvLikeNum, mTvReplyNum;
        private LinearLayout mContainer;

        public CommunityViewHolder(View itemView) {
            super(itemView);
            mIvReply = (ImageView) itemView.findViewById(R.id.iv_reply);
            mIvLike = (ImageView) itemView.findViewById(R.id.iv_like);
            mTvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);
            mTvLikeNum = (TextView) itemView.findViewById(R.id.tv_like_num);
            mIvAvatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            mTvNickName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mTvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            mContainer = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }

    private String isNull(String str) {
        return str == null ? "" : str;
    }
}
