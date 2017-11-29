package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ufreedom.uikit.FloatingText;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by YellowDoing on 2017/11/13.
 */

public class CommunityDetialActivity extends Activity implements View.OnClickListener, CommentAdapter.Callback {

    private CircleImageView mIvHead;
    private RecyclerView mIvImages, mRvReplies;
    private TextView mTvContent, mTvNickName, mTvReply, mTvLikeNum;
    private EditText mEtReply;
    private LocalBroadcastManager mManager;
    private int mCurrentPage;
    private Community mCommunity;
    private CommentAdapter mAdapter;
    private String theOhterNickName;
    private ImageView mIvLike;
    public static final int REQUEST_CODE_REPLY = 13;
    private boolean isResumed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniView();
        initData();
    }

    private void iniView() {
        setContentView(R.layout.activity_community_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mIvHead = (CircleImageView) findViewById(R.id.iv_head);
        mIvImages = (RecyclerView) findViewById(R.id.rv_images);
        mIvLike = (ImageView) findViewById(R.id.iv_like);
        mTvLikeNum = (TextView) findViewById(R.id.tv_like_num);
        mRvReplies = (RecyclerView) findViewById(R.id.rv_replys);
        mRvReplies.setLayoutManager(new LinearLayoutManager(this));
        mRvReplies.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mTvNickName = (TextView) findViewById(R.id.tv_nick_name);
        mTvReply = (TextView) findViewById(R.id.tv_reply);
        mEtReply = (EditText) findViewById(R.id.et_reply);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mRvReplies.setAdapter(mAdapter);
        mTvReply.setOnClickListener(this);
        mIvLike.setOnClickListener(this);
    }


    @Override
    public void commentSelect(String nickName, String commentId, String parentId) {
        mEtReply.setTag(new Pair<>(commentId, parentId));
        theOhterNickName = nickName;
        mEtReply.setHint("回复: " + theOhterNickName);
        mEtReply.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtReply, 0);
    }

    private void initData() {
        mCommunity = (Community) getIntent().getSerializableExtra("community");
        mAdapter = new CommentAdapter(this, mCommunity.getId(), new ArrayList<Comment>(), this);
        Glide.with(this).load(mCommunity.getAvatar()).centerCrop().into(mIvHead);
        mTvNickName.setText(mCommunity.getNickName());
        if (mCommunity.getContent() == null || mCommunity.getContent().trim().equals(""))
            mTvContent.setVisibility(View.GONE);
        else
            mTvContent.setText(mCommunity.getContent());

        if (mCommunity.getImagePaths() != null)
            if (mCommunity.getImagePaths().size() > 0) {
                mIvImages.setLayoutManager(new GridLayoutManager(this, 3));
                mIvImages.setAdapter(new ImageAdapter(this, mCommunity.getImagePaths()));
            }

        if (mCommunity.isLike()) {
            mIvLike.setBackgroundResource(R.drawable.zan_style_2);
            mIvLike.setImageResource(R.drawable.ic_zan_hover);
            mTvLikeNum.setTextColor(getResources().getColor(R.color.like_num));
        }
        mTvLikeNum.setText(mCommunity.getLikeNum() + "");

        mEtReply.setTag(new Pair<>(mCommunity.getId(), mCommunity.getId()));
        mTvContent.setText(mCommunity.getContent());
        mTvNickName.setText(mCommunity.getNickName());


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isResumed) loadComments();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = true;
    }

    /**
     * 加载评论列表
     */
    private void loadComments() {
        mCurrentPage = 1;
        CommunityFragment.getCommunityInterface().loadComments(new CommunityInterface.CommentSubsriber() {
            @Override
            public void onComplete(List<Comment> communityList) {
                if (communityList != null) {
                    mAdapter = new CommentAdapter(CommunityDetialActivity.this, mCommunity.getId(), communityList, CommunityDetialActivity.this);
                    mRvReplies.setAdapter(mAdapter);
                }
            }
        }, mCommunity.getId(), mCurrentPage);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back)
            finish();
        else if (id == R.id.tv_reply)
            comment();
        else if (id == R.id.iv_like)
            if (mCommunity.isLike())
                unLike();
            else
                like();
    }


    /**
     * 点赞
     */
    private void like() {
        mIvLike.setOnClickListener(null);
        CommunityFragment.getCommunityInterface().like(new CommunityInterface.Subsriber() {
            @Override
            public void onComplete() {
                mIvLike.setBackgroundResource(R.drawable.zan_style_2);
                mIvLike.setImageResource(R.drawable.ic_zan_hover);
                mTvLikeNum.setTextColor(getResources().getColor(R.color.like_num));
                mTvLikeNum.setText(String.valueOf(Integer.valueOf(mTvLikeNum.getText().toString()) + 1));
                mCommunity.setLike(true);
                FloatingText floatingText = new FloatingText.FloatingTextBuilder(CommunityDetialActivity.this)
                        .textColor(getResources().getColor(R.color.like_num)) // floating  text color
                        .textSize(100)   // floating  text size
                        .textContent("+1") // floating  text content
                        .build();
                floatingText.attach2Window();
                floatingText.startFloating(mTvLikeNum);
                mIvLike.setOnClickListener(CommunityDetialActivity.this);
            }
        }, mCommunity.getId());
    }

    /**
     * 取赞
     */
    private void unLike() {
        mIvLike.setOnClickListener(null);
        CommunityFragment.getCommunityInterface().unLike(new CommunityInterface.Subsriber() {
            @Override
            public void onComplete() {
                mIvLike.setBackgroundResource(R.drawable.zan_style);
                mIvLike.setImageResource(R.drawable.ic_zan);
                mTvLikeNum.setTextColor(getResources().getColor(R.color.gray));
                mTvLikeNum.setText(String.valueOf(Integer.valueOf(mTvLikeNum.getText().toString()) - 1));
                mCommunity.setLike(false);
                mIvLike.setOnClickListener(CommunityDetialActivity.this);
            }
        }, mCommunity.getId());
    }


    /**
     * 评论
     */
    private void comment() {
        Pair<String, String> pair = (Pair<String, String>) mEtReply.getTag();

        CommunityFragment.getCommunityInterface().comment(new CommunityInterface.CommentSubsriber2() {
            @Override
            public void onComplete(Comment comment) {
                Toast.makeText(CommunityDetialActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(CommunityDetialActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mEtReply.setText("");
                mEtReply.setTag(new Pair<>(mCommunity.getId(), mCommunity.getId()));
                theOhterNickName = null;
                ((CommentAdapter)mRvReplies.getAdapter()).add(comment);
            }
        },mCommunity.getId(),pair.second,pair.first,mEtReply.getText().toString());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_REPLY:

                    break;
            }
        }
    }
}
