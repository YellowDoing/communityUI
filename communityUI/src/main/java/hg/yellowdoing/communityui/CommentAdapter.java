package hg.yellowdoing.communityui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YellowDoing on 2017/11/18.
 *
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReplyViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Comment> mComments;
    private Callback mCallback;
    private String mCommunityId;

    public CommentAdapter(Context context,String communityId, List<Comment> comments, Callback callback) {
        mContext = context;
        mComments = new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
        mCallback = callback;
        mCommunityId = communityId;

        for (Comment comment : comments){
            if(comment.getParentId().equals(communityId))
                mComments.add(comment);
        }

        for (Comment comment : mComments){
            for (Comment co : comments){
                if (comment.getId().equals(co.getParentId())){
                    comment.addChildComments(co);
                }
            }
        }
    }

    public void add(List<Comment> list){
        mComments.addAll(list);
        notifyDataSetChanged();
    }

    public void set(List<Comment> list){
        mComments = list;
        notifyDataSetChanged();
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReplyViewHolder(mInflater.inflate(R.layout.list_item_reply,parent,false));
    }

    @Override
    public void onBindViewHolder(ReplyViewHolder holder, int position) {
        final Comment comment = mComments.get(position);
        Glide.with(mContext).load(comment.getAvatar()).centerCrop().into(holder.mCvAvatar);
        holder.mTvContent.setText(comment.getContent());
        holder.mTvNickName.setText(comment.getNickName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.getCommentId(comment.getNickName(),comment.getId());
            }
        });
        holder.mLvReply.setAdapter(new ChildReplyAdapter(comment.getChildComments(),mContext));
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView mCvAvatar;
        TextView mTvNickName,mTvContent;
        ListView mLvReply;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            mCvAvatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            mTvNickName = (TextView) itemView.findViewById(R.id.tv_nick_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mLvReply = (ListView) itemView.findViewById(R.id.lv_child_reply);
        }
    }

    interface Callback{
        void getCommentId(String name,String id);
    }
}
