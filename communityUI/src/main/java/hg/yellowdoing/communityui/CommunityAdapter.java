package hg.yellowdoing.communityui;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ufreedom.floatingview.Floating;
import com.ufreedom.floatingview.FloatingBuilder;
import com.ufreedom.floatingview.FloatingElement;

import org.w3c.dom.Text;

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
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private ArrayList<Community> mCommunityBeanList;
    private Activity mContext;
    private LayoutInflater mInflater;


    public CommunityAdapter(Activity context, ArrayList<Community> communityBeanList) {
        mCommunityBeanList = communityBeanList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public void add(List<Community> communityList) {
        mCommunityBeanList.addAll(communityList);
        notifyDataSetChanged();
    }

    public void reset(ArrayList<Community> communityList) {
        mCommunityBeanList = communityList;
        notifyDataSetChanged();
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(mInflater.inflate(R.layout.list_item_community, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, int position) {
        final Community communityBean = mCommunityBeanList.get(position);

        if (communityBean.getImagePaths() == null || communityBean.getImagePaths().size() == 0)
            holder.mRvImages.setVisibility(View.GONE);
        else
            holder.mRvImages.setAdapter(new ImageAdapter(mContext, communityBean.getImagePaths()));

        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(communityBean.getAuthor().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    holder.mTvNickName.setText(user.getNickName());
                    Glide.with(mContext).load(user.getAvatar()).centerCrop().into(holder.mIvAvatar);
                } else
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
            }
        });

        holder.mTvCreateTime.setText(dateCompare(communityBean.getUpdatedAt()));
        holder.mTvContent.setText(communityBean.getContent());
        holder.mTvLikeNum.setText(String.valueOf(communityBean.getLikeNum()));
        holder.mTvReplyNum.setText(String.valueOf(communityBean.getReplyNum()));



        holder.mIvLike.setOnClickListener(new View.OnClickListener() {
            boolean isLike;

            @Override
            public void onClick(View v) {
                if (!isLike) {
                    CommunityService.addLikeNum(communityBean, new CommunityService.Callback() {
                        @Override
                        public void callback() {
                            holder.mIvLike.setImageResource(R.drawable.ic_zan_hover);
                            holder.mTvLikeNum.setText(String.valueOf(communityBean.getLikeNum() + 1));
                            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.like_num));
                            isLike = true;
                        }
                    });
                } else {
                    CommunityService.reduceLikeNum(communityBean, new CommunityService.Callback() {
                        @Override
                        public void callback() {
                            holder.mIvLike.setImageResource(R.drawable.ic_zan);
                            holder.mTvLikeNum.setText(String.valueOf(communityBean.getLikeNum()));
                            holder.mTvLikeNum.setTextColor(mContext.getResources().getColor(R.color.gray));
                            isLike = false;
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommunityBeanList.size();
    }

    class CommunityViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvAvatar, mIvReply, mIvLike;
        TextView mTvNickName, mTvContent, mTvCreateTime, mTvLikeNum, mTvReplyNum;
        private RecyclerView mRvImages;

        public CommunityViewHolder(View itemView) {
            super(itemView);
            mIvReply = (ImageView) itemView.findViewById(R.id.iv_reply);
            mIvLike = (ImageView) itemView.findViewById(R.id.iv_like);
            mTvReplyNum = (TextView) itemView.findViewById(R.id.tv_reply_num);
            mTvLikeNum = (TextView) itemView.findViewById(R.id.tv_like_num);
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
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

    /**
     * 根据手机的分辨率从dp的单位转成为px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
