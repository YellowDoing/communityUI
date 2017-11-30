package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by YellowDoing on 2017/11/18.
 */

public class PostActivity extends Activity implements View.OnClickListener {


    private RecyclerView mRecyclerView;
    private ImageSelectAdapter mAdapter;
    private EditText mEtContent;
    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
        mDialog = new AlertDialog.Builder(this).setView(new ProgressBar(this));
    }

    private void initView() {
        mEtContent = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        mAdapter = new ImageSelectAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_select_images);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoPicker.REQUEST_CODE:  //选择图片返回
                    mAdapter.add(data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS));
                    break;
                case PhotoPreview.REQUEST_CODE: //预览图片删除后返回
                    mAdapter.reset(data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS));
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) finish();
        else if (id == R.id.tv_submit) post(); //发帖
    }

    private void post() {
        final AlertDialog loadingView = mDialog.show();

        Community community = new Community();
        community.setContent(mEtContent.getText().toString());
        community.setImagePaths(mAdapter.getPaths());

        CommunityFragment.getCommunityInterface().post(new CommunityInterface.Subsriber() {
            @Override
            public void onComplete() {
                LocalBroadcastManager.getInstance(PostActivity.this).sendBroadcast(new Intent());
                loadingView.cancel();
                finish();
            }
        },community.getImagePaths(),community.getContent());
    }
}
