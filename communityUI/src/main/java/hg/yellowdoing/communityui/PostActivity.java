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
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

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
        else if (id == R.id.tv_submit)
            uploadFiles();
    }

    private void uploadFiles() {

        final List<String> pathList = mAdapter.getPaths();

        if (pathList.size() > 0) {
            final String paths[] = new String[pathList.size()];
            for (int i = 0; i < pathList.size(); i++)
                paths[i] = pathList.get(i);

            BmobFile.uploadBatch(paths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list1.size() == paths.length)
                        post(list1);
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(PostActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else
            post(null);
    }

    private void post(List<String> urls) {
        Community community = new Community();
        community.setAuthor(BmobUser.getCurrentUser(User.class));
        community.setLikeNum(0);
        community.setReplyNum(0);
        community.setContent(mEtContent.getText().toString());
        if (urls != null)
            community.setImagePaths(urls);
        community.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e ==null){
                    Toast.makeText(PostActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                    sendBroadcast(new Intent(ComunityReceiver.COMMUNITY_ACTION)
                            .putExtra(ComunityReceiver.COMMUNITY_ACTION,ComunityReceiver.CREATE_NEW_POST));
                    finish();
                }else
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
