package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by YellowDoing on 2017/11/18.
 */

public class PostActivity extends Activity implements View.OnClickListener {


    private RecyclerView mRecyclerView;
    private ImageSelectAdapter mAdapter;
    private EditText mEtContent;
    private LocalBroadcastManager mManager;
    private PostReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
    }

    private void initView() {
        mEtContent = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        mAdapter = new ImageSelectAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_select_images);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new PostReceiver();
        mManager.registerReceiver(mReceiver, new IntentFilter(PostReceiver.ACTION));
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
        Community community = new Community();
        community.setContent(mEtContent.getText().toString());
        community.setImagePaths(mAdapter.getPaths());

        mManager.sendBroadcast(new Intent(CommunityFragment.CommunityFragmentReceiver.ACTION)
                .putExtra(CommunityFragment.CommunityFragmentReceiver.ACTION, "post")
                .putExtra("community", community));
    }


    public class PostReceiver extends BroadcastReceiver {

        public static final String ACTION = "PostReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.unregisterReceiver(mReceiver);
    }
}
