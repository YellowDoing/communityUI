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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static hg.yellowdoing.communityui.CommunityDetialActivity.CommunityDetailReceiver.ACTION;

/**
 * Created by YellowDoing on 2017/11/13.
 */

public class CommunityDetialActivity extends Activity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    private CircleImageView mIvHead;
    private RecyclerView mIvImages, mRvReplies;
    private TextView mTvContent, mTvNickName, mTvReply;
    private EditText mEtReply;
    private CommunityDetailReceiver mReceiver;
    private LocalBroadcastManager mManager;
    private int mCurrentPage;
    private BGARefreshLayout mRefreshLayout;
    private Community mCommunity;
    private CommentAdapter mAdapter;

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
        mRvReplies = (RecyclerView) findViewById(R.id.rv_replys);
        mRvReplies.setLayoutManager(new LinearLayoutManager(this));
        mRvReplies.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new CommentAdapter(this,mCommunity.getId(), new ArrayList<Comment>(), new CommentAdapter.Callback() {
            @Override
            public void getCommentId(String nick, String id) {
                mEtReply.setTag(id);
                mEtReply.setHint("回复: " + nick);
                mEtReply.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEtReply, 0);
            }
        });
        mTvNickName = (TextView) findViewById(R.id.tv_nick_name);
        mTvReply = (TextView) findViewById(R.id.tv_reply);
        mEtReply = (EditText) findViewById(R.id.et_reply);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mRvReplies.setAdapter(mAdapter);

        mTvReply.setOnClickListener(this);

        //设置下拉刷新并开始刷新
 /*       mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refrash_layout);
        mRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);*/
    }

    private void initData() {
        mCommunity = (Community) getIntent().getSerializableExtra("community");
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

        mEtReply.setTag(mCommunity.getId());

        mTvContent.setText(mCommunity.getContent());
        mTvNickName.setText(mCommunity.getNickName());

        mReceiver = new CommunityDetailReceiver();
        mManager = LocalBroadcastManager.getInstance(this);
        mManager.registerReceiver(mReceiver, new IntentFilter(ACTION));
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
    }

    private void reply() {
        mManager.sendBroadcast(new Intent(CommunityFragment.CommunityFragmentReceiver.ACTION)
                .putExtra(CommunityFragment.CommunityFragmentReceiver.ACTION, "reply")
                .putExtra("commentId", (String) mEtReply.getTag())
                .putExtra("communityId", mCommunity.getId())
                .putExtra("content", mEtReply.getText().toString()));
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
                mEtReply.setTag(mCommunity.getId());
                return;
            }
            if (mCurrentPage == 1) {
                mAdapter.set((List<Comment>) intent.getExtras().get("comments"));
                mRefreshLayout.endRefreshing();
            } else
                mAdapter.add((List<Comment>) intent.getExtras().get("comments"));
        }
    }

}
