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

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static hg.yellowdoing.communityui.CommunityDetialActivity.CommunityDetailReceiver.ACTION;

/**
 * Created by YellowDoing on 2017/11/13.
 */

public class CommunityDetialActivity extends Activity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate, CommentAdapter.Callback {

    private CircleImageView mIvHead;
    private RecyclerView mIvImages, mRvReplies;
    private TextView mTvContent, mTvNickName, mTvReply, mTvLikeNum;
    private EditText mEtReply;
    private CommunityDetailReceiver mReceiver;
    private LocalBroadcastManager mManager;
    private int mCurrentPage;
    private BGARefreshLayout mRefreshLayout;
    private Community mCommunity;
    private CommentAdapter mAdapter;
    private String theOhterNickName;
    private ImageView mIvLike;

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

        //设置下拉刷新并开始刷新
 /*       mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refrash_layout);
        mRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);*/
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

        mReceiver = new CommunityDetailReceiver();
        mManager = LocalBroadcastManager.getInstance(this);
        mManager.registerReceiver(mReceiver, new IntentFilter(ACTION));
        mCurrentPage = 1;
        //mRefreshLayout.beginRefreshing();
        sendCommentBroadcast();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back)
            finish();
        else if (id == R.id.tv_reply)
            reply();
        else if (id == R.id.iv_like)
            like();
    }

    private void like() {
        mIvLike.setOnClickListener(null);
        mManager.sendBroadcast(new Intent(CommunityFragment.CommunityFragmentReceiver.ACTION)
                .putExtra(CommunityFragment.CommunityFragmentReceiver.ACTION, "like")
                .putExtra("isLike", mCommunity.isLike())
                .putExtra("communityId", mCommunity.getId()));
    }

    private void reply() {
        Pair<String, String> pair = (Pair<String, String>) mEtReply.getTag();

        Comment comment = new Comment().setContent(mEtReply.getText().toString())
                .setCommunityId(mCommunity.getId())
                .setCommentId(pair.first).setParentId(pair.second);
        if (theOhterNickName != null)
            comment.setTheOtherNickName(theOhterNickName);

        mManager.sendBroadcast(new Intent(CommunityFragment.CommunityFragmentReceiver.ACTION)
                .putExtra(CommunityFragment.CommunityFragmentReceiver.ACTION, "reply")
                .putExtra("comment", comment));
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mCurrentPage = 1;
        sendCommentBroadcast();
    }

    private void sendCommentBroadcast() {
        mManager.sendBroadcast(new Intent(CommunityFragment.CommunityFragmentReceiver.ACTION)
                .putExtra(CommunityFragment.CommunityFragmentReceiver.ACTION, "loadComments")
                .putExtra("page", mCurrentPage)
                .putExtra("communityId", mCommunity.getId()));
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mCurrentPage++;
        sendCommentBroadcast();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.unregisterReceiver(mReceiver);
    }

    public class CommunityDetailReceiver extends BroadcastReceiver {
        public static final String ACTION = "CommunityDetailReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("isReply", false)) {
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(CommunityDetialActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mEtReply.setText("");
                mEtReply.setTag(new Pair<>(mCommunity.getId(), mCommunity.getId()));
                theOhterNickName = null;
                return;
            }
            if (intent.getBooleanExtra("likeSuccess", false)) {
                mIvLike.setBackgroundResource(R.drawable.zan_style_2);
                mIvLike.setImageResource(R.drawable.ic_zan_hover);
                mTvLikeNum.setTextColor(getResources().getColor(R.color.like_num));
                mTvLikeNum.setText(String.valueOf(Integer.valueOf(mTvLikeNum.getText().toString()) + 1));
                mCommunity.setLike(true);
                FloatingText   floatingText = new FloatingText.FloatingTextBuilder(CommunityDetialActivity.this)
                        .textColor(getResources().getColor(R.color.like_num)) // floating  text color
                        .textSize(100)   // floating  text size
                        .textContent("+1") // floating  text content
                        .build();
                floatingText.attach2Window();
                floatingText.startFloating(mTvLikeNum);
                mIvLike.setOnClickListener(CommunityDetialActivity.this);
            }
            if (intent.getBooleanExtra("unlikeSuccess", false)) {
                mIvLike.setBackgroundResource(R.drawable.zan_style);
                mIvLike.setImageResource(R.drawable.ic_zan);
                mTvLikeNum.setTextColor(getResources().getColor(R.color.gray));
                mTvLikeNum.setText(String.valueOf(Integer.valueOf(mTvLikeNum.getText().toString()) - 1));
                mCommunity.setLike(false);
                mIvLike.setOnClickListener(CommunityDetialActivity.this);
            }
            if (mCurrentPage == 1) {
                List<Comment> comments = (List<Comment>) intent.getExtras().get("comments");
                if (comments != null){
                    mAdapter = new CommentAdapter(context, mCommunity.getId(), comments, CommunityDetialActivity.this);
                    mRvReplies.setAdapter(mAdapter);
                }
            }
        }
    }

}
