package hg.yellowdoing.communityui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/11/13.
 */

public class ComminityDetialActivity extends Activity implements View.OnClickListener {

    private Community mCommunity;
    private CircleImageView mIvHead;
    private RecyclerView mIvImages, mRvReplies;
    private TextView mTvContent, mTvNickName, mTvReply;
    private EditText mEtReply;

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
        mTvNickName = (TextView) findViewById(R.id.tv_nick_name);
        mTvReply = (TextView) findViewById(R.id.tv_reply);
        mEtReply = (EditText) findViewById(R.id.et_reply);
    }

    private void initData() {
        mCommunity = (Community) getIntent().getSerializableExtra("communityBean");
        Glide.with(this).load(mCommunity.getAuthor().getAvatar()).centerCrop().into(mIvHead);
        mTvNickName.setText(mCommunity.getAuthor().getNickName());
        mTvContent.setText(mCommunity.getContent());

        if (mCommunity.getImagePaths() != null && mCommunity.getImagePaths().size() > 0) {
            mIvImages.setLayoutManager(new GridLayoutManager(this, 3));
            mIvImages.setAdapter(new ImageAdapter(this, mCommunity.getImagePaths()));
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back)
            finish();
        else if (id == R.id.tv_reply)
            reply();
    }

    private void reply(){

    }



}
