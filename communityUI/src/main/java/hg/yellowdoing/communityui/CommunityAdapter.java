package hg.yellowdoing.communityui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

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

    public void add(List<Community> communityList){
        mCommunityBeanList.addAll(communityList);
        notifyDataSetChanged();
    }

    public void reset(List<Community> communityList){
        mCommunityBeanList = communityList;
        notifyDataSetChanged();
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityViewHolder(mInflater.inflate(R.layout.list_item_community,null));
    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, int position) {

        final Community communityBean = mCommunityBeanList.get(position);


        BmobQuery<User> query = new BmobQuery<>();


        query.getObject(communityBean.getAuthor().getObjectId(), new QueryListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    holder.mTvNickName.setText(user.getNickName());
                    Glide.with(mContext).load(user.getAvatar()).centerCrop().into(holder.mIvAvatar);
               }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


        holder.mTvContent.setText(communityBean.getContent());


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
