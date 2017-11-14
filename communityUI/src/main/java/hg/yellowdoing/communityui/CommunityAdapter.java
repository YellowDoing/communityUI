package hg.yellowdoing.communityui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by YellowDing on 2017/10/18.
 *
 */

public class CommunityAdapter<T extends Serializable> extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private List<T> mDataList;
    private Activity mContext;
    private LayoutInflater mInflater;
    private CommunityInterface<T>  mInterface;

    public CommunityAdapter(Activity context,CommunityInterface<T>  anInterface) {
        mDataList = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mInterface = anInterface;
    }

   public void add(List<T> list) {
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void set(List<T> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(mInflater.inflate(R.layout.list_item_community, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommunityAdapter.CommunityViewHolder holder, int position) {
        final T data = mDataList.get(position);

        //接口返回的图片地址
        ArrayList<String> imagePaths = mInterface.bindListItemView(data,holder.mIvAvatar,holder.mTvNickName,holder.mTvContent,holder.mTvReplyNum,holder.mTvLikeNum,holder.mTvCreateTime);

        if (imagePaths == null || imagePaths.size() == 0)
            holder.mRvImages.setVisibility(View.GONE);
        else
            holder.mRvImages.setAdapter(new ImageAdapter(mContext, imagePaths));

        //接口返回该贴是否已喜欢
        if (mInterface.isLike(data)){
            holder.mIvLike.setImageResource(R.drawable.ic_zan_hover);
            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.like_num));
        }else {
            holder.mIvLike.setImageResource(R.drawable.ic_zan);
            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.gray));
        }

        //接口回调点赞点击事件
        holder.mIvLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mInterface.isLike(data)) {
                    boolean isSuccess = mInterface.like(data);
                    if (isSuccess)
                        holder.mTvLikeNum.setText(String.valueOf(Integer.parseInt(holder.mTvLikeNum.getText().toString()) + 1));
                } else {
                    boolean isSuccess = mInterface.unLike(data);
                    if (isSuccess)
                        holder.mTvLikeNum.setText(String.valueOf(Integer.parseInt(holder.mTvLikeNum.getText().toString()) - 1));
                }
            }
        });

        //接口回调帖子点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,ComminityDetialActivity.class)
                        .putExtra("data",data));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class CommunityViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mIvAvatar;
        ImageView  mIvReply, mIvLike;
        TextView mTvNickName, mTvContent, mTvCreateTime, mTvLikeNum, mTvReplyNum;
        private RecyclerView mRvImages;

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
            mRvImages = (RecyclerView) itemView.findViewById(R.id.rv_images);
            mRvImages.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
    }

    private String dateCompare(String createTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        try {
            long day = (dateFormat.parse(dateFormat.format(now)).getTime() - dateFormat.parse(createTime).getTime()) / (86400000);
            if (day == 0) {
                Date createDate = todayFormat.parse(createTime.substring(12));
                long time = (todayFormat.parse(todayFormat.format(now)).getTime() - createDate.getTime()) / 1000;
                if (time < 60)
                    return "刚刚";
                if (60 <= time && time < 3600)
                    return (time / 60) + "分钟前";
                else
                    return (time / 3600) + "小时前";
            } else if (day == 1)
                return "昨天";
            else if (day > 1 && day < 31)
                return day + "天前";
            else if (day >= 31 && day < 365)
                return (day / 30) + "个月前";
            else
                return (day / 365) + "年前";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
