package hg.yellowdoing.communityui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by YellowDing on 2017/10/18.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private List<Community> mCommunityBeanList;
    private Context mContext;
    private LayoutInflater mInflater;

    public CommunityAdapter(Context context, List<Community> communityBeanList) {
        mCommunityBeanList = communityBeanList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void add(List<Community> communityList) {
        mCommunityBeanList.addAll(communityList);
        notifyDataSetChanged();
    }

    public void reset(List<Community> communityList) {
        mCommunityBeanList = communityList;
        notifyDataSetChanged();
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(mInflater.inflate(R.layout.list_item_community, null));
    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, int position) {

        final Community communityBean = mCommunityBeanList.get(position);

        if (holder.mImageContainer.getChildCount() > 0)
            holder.mImageContainer.removeAllViews();

        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(communityBean.getAuthor().getObjectId(), new QueryListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    holder.mTvNickName.setText(user.getNickName());
                    Glide.with(mContext).load(user.getAvatar()).centerCrop().into(holder.mIvAvatar);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

        holder.mTvCreateTime.setText(dateCompare(communityBean.getUpdatedAt()));
        holder.mTvContent.setText(communityBean.getContent());
    }

    @Override
    public int getItemCount() {
        return mCommunityBeanList.size();
    }

    class CommunityViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvAvatar;
        TextView mTvNickName, mTvContent, mTvCreateTime;
        LinearLayout mImageContainer;

        public CommunityViewHolder(View itemView) {
            super(itemView);
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            mTvNickName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mTvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            mImageContainer = (LinearLayout) itemView.findViewById(R.id.ll_images_container);
        }
    }


    private String dateCompare(String createTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm");
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
