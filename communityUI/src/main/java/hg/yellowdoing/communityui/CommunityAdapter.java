package hg.yellowdoing.communityui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by YellowDing on 2017/10/18.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private List<Community>  mCommunityBeanList;
    private Context mContext;
    private LayoutInflater mInflater;

    public CommunityAdapter(Context context, List<Community> communityBeanList) {
        mCommunityBeanList = communityBeanList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(mInflater.inflate(R.layout.list_item_community,null));
    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {

        Community communityBean = mCommunityBeanList.get(position);

        holder.mTvNickName.setText(communityBean.getName());
        holder.mTvContent.setText(communityBean.getContent());
        Glide.with(mContext).load(communityBean.getAvatar()).into(holder.mIvAvatar);

    }

    @Override
    public int getItemCount() {
        return mCommunityBeanList.size();
    }

    class CommunityViewHolder extends RecyclerView.ViewHolder{

        ImageView mIvAvatar;
        TextView mTvNickName,mTvContent;
        RecyclerView mRvImages;

        public CommunityViewHolder(View itemView) {
            super(itemView);

            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            mTvNickName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mRvImages = (RecyclerView)itemView.findViewById(R.id.rv_images);
        }

    }

}
