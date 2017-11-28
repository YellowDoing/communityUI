package hg.yellowdoing.communityui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YellowDoing on 2017/11/22.
 */

public class ChildReplyAdapter extends BaseAdapter {

    private List<Comment> mCommentList;
    private CommunityDetialActivity mContext;
    private LayoutInflater mInflater;
    private Comment mComment;

    public ChildReplyAdapter(Comment comment, CommunityDetialActivity context) {
        mComment = comment;
        mCommentList = comment.getChildComments();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mCommentList.size() < 5 ? mCommentList.size() : 5;
    }

    @Override
    public Comment getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (position < 4) {
            view = mInflater.inflate(R.layout.list_item_child_reply, null);

            final Comment comment = mCommentList.get(position);

            TextView tv_author_name = (TextView) view.findViewById(R.id.tv_author_name);
            TextView tv_reply = (TextView) view.findViewById(R.id.tv_reply);
            TextView tv_beReplied = (TextView) view.findViewById(R.id.tv_beReplied);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);

            tv_content.setText(comment.getContent());

            if (comment.getTheOtherNickName() != null) {
                tv_reply.setVisibility(View.VISIBLE);
                tv_beReplied.setText(comment.getTheOtherNickName() + "：");
                tv_author_name.setText(comment.getNickName());
            } else {
                tv_reply.setVisibility(View.GONE);
                tv_author_name.setText(comment.getNickName() + "：");
            }
        } else {
            view = mInflater.inflate(R.layout.list_item_more, null);
            TextView more = view.findViewById(R.id.tv_more);
            more.setText("还有" + (mCommentList.size() - position) + "条回复>>");
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivityForResult(new Intent(mContext,CommentDetailActivity.class)
                    .putExtra("comment",mComment),CommunityDetialActivity.REQUEST_CODE_REPLY);
                }
            });
        }
        return view;
    }
}
