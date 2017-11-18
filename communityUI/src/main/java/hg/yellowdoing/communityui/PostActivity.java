package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        if (id == R.id.iv_back)
            finish();
        else if (id == R.id.tv_submit)  //发帖
            uploadFiles();
    }

    private void uploadFiles() {


    }

    private void post(ArrayList<String> urls) {


    }


}
