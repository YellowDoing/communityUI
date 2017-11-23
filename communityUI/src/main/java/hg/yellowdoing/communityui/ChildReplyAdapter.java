package hg.yellowdoing.communityui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YellowDoing on 2017/11/22.
 *
 */

public class ChildReplyAdapter extends BaseAdapter {

    private List<Comment> mCommentList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ChildReplyAdapter(List<Comment> commentList, Context context) {
        mCommentList = commentList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
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
        View view = mInflater.inflate(R.layout.list_item_child_reply,null);

        Comment comment = mCommentList.get(position);

        TextView tv_author_name = (TextView) view.findViewById(R.id.tv_author_name);
        TextView tv_reply = (TextView) view.findViewById(R.id.tv_reply);
        TextView tv_beReplied = (TextView) view.findViewById(R.id.tv_beReplied);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        //comment.

        return view;
    }
}
