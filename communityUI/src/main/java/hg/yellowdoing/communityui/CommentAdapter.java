package hg.yellowdoing.communityui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by YellowDoing on 2017/11/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReplyViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Comment> mComments;

    public CommentAdapter(Context context,List<Comment> comments) {
        mContext = context;
        mComments = comments;
        mInflater = LayoutInflater.from(mContext);
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
        Comment comment = mComments.get(position);
        Glide.with(mContext).load(comment.getAvatar()).centerCrop().into(holder.mCvAvatar);
        holder.mTvContent.setText(comment.getContent());
        holder.mTvNickName.setText(comment.getNickName());
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView mCvAvatar;
        TextView mTvNickName,mTvContent;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            mCvAvatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            mTvNickName = (TextView) itemView.findViewById(R.id.tv_nick_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
