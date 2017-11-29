package hg.yellowdoing.communityui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YellowDoing on 2017/11/27.
 */

public class ChildReplyAdapter2 extends BaseAdapter {

    private List<Comment> mCommentList;
    private CommentDetailActivity mContext;
    private LayoutInflater mInflater;
    private Comment mComment;

    public ChildReplyAdapter2(Comment comment, CommentDetailActivity context) {
        mComment = comment;
        mCommentList = comment.getChildComments();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void add(Comment comment){
        mCommentList.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCommentList.size();
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
        View view = mInflater.inflate(R.layout.list_item_child_reply_2, null);

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

        return view;
    }
}
