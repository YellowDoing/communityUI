package hg.yellowdoing.communityui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YellowDoing on 2017/11/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReplyViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Comment> mComments, mAllComments;
    private Callback mCallback;
    private String mCommunityId;

    public CommentAdapter(Context context, String communityId, List<Comment> comments, Callback callback) {
        mContext = context;
        mComments = new ArrayList<>();
        mAllComments = comments;
        mInflater = LayoutInflater.from(mContext);
        mCallback = callback;
        mCommunityId = communityId;

        //筛选出一级评论
        for (Comment comment : comments)
            if (comment.getCommentId().equals(communityId))
                mComments.add(comment);

        //归类出所有一级评论的子评论
        for (Comment comment : mComments)
            for (Comment co : comments)
                if (comment.getId().equals(co.getParentId()))
                    comment.addChildComments(co);

        //归类出子评论所评论的人的姓名
        for (Comment comment : mComments)
            if (comment.getChildComments() != null)
                for (Comment comment1 : comment.getChildComments())
                    for (Comment comment2 : comment.getChildComments())
                        if (!comment1.getId().equals(comment.getId()) && comment1.getId().equals(comment2.getCommentId()))
                            comment2.setTheOtherNickName(comment1.getNickName());

    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReplyViewHolder(mInflater.inflate(R.layout.list_item_reply, parent, false));
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
                mCallback.commentSelect(comment.getNickName(), comment.getId(), comment.getId());
            }
        });

        if (comment.getChildComments() != null){
            final ChildReplyAdapter adapter = new ChildReplyAdapter(comment.getChildComments(), mContext);
            holder.mLvReply.setAdapter(adapter);
            holder.mLvReply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "丢按几了", Toast.LENGTH_SHORT).show();
                    mCallback.commentSelect(adapter.getItem(position).getNickName(), adapter.getItem(position).getId(), comment.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mCvAvatar;
        TextView mTvNickName, mTvContent;
        MyListView mLvReply;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            mCvAvatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            mTvNickName = (TextView) itemView.findViewById(R.id.tv_nick_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mLvReply = (MyListView) itemView.findViewById(R.id.lv_child_reply);
        }
    }

    interface Callback {
        void commentSelect(String nickName, String commentId, String parentId);
    }
}
