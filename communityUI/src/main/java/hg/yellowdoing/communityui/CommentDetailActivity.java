package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


/**
 * Created by YellowDoing on 2017/11/26.
 */

public class CommentDetailActivity extends Activity {

    private MyListView mMyListView;
    private CircleImageView mCivAvatar;
    private TextView mTvNickName, mTvContent, mTvReply;
    private Comment mComment;
    private EditText mEtReply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        mMyListView = (MyListView) findViewById(R.id.list_view);
        mCivAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        mTvNickName = (TextView) findViewById(R.id.tv_nick_name);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mTvReply = (TextView) findViewById(R.id.tv_reply);
        mEtReply = (EditText) findViewById(R.id.et_reply);

        mComment = (Comment) getIntent().getSerializableExtra("comment");

        Glide.with(this).load(mComment.getAvatar()).centerCrop().into(mCivAvatar);
        mTvNickName.setText(mComment.getNickName());
        mTvContent.setText(mComment.getContent());
        mEtReply.setTag(mComment.getId());

        mMyListView.setAdapter(new ChildReplyAdapter2(mComment, this));
        mMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = mComment.getChildComments().get(position);
                mEtReply.setHint("回复：" + comment.getNickName());
                mEtReply.setTag(comment.getId());
                mEtReply.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEtReply, 0);
            }
        });

        mTvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 评论
     */
    private void comment() {
        CommunityFragment.getCommunityInterface().comment(new CommunityInterface.CommentSubsriber2() {
            @Override
            public void onComplete(Comment c) {

                Comment comment = new Comment();
                if (!mComment.getId().equals(mEtReply.getTag()))
                    comment.setTheOtherNickName(mEtReply.getHint().toString().replace("回复：", ""));

                comment.setContent(mEtReply.getText().toString());
                comment.setNickName(c.getNickName());
                ((ChildReplyAdapter2) mMyListView.getAdapter()).add(comment);

                mEtReply.setTag(mComment.getId());
                mEtReply.setText("");
                mEtReply.setHint("说点什么吧...~");
                Toast.makeText(CommentDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(CommentDetailActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, mComment.getCommunityId(), mComment.getId(), (String) mEtReply.getTag(), mEtReply.getText().toString());
    }
}
