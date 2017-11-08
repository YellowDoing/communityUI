package bmob.yellowdoing.com.bmobcommunity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import hg.yellowdoing.communityui.User;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by ganhuang on 2017/11/8.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivAvatar;
    private EditText etNickName;
    private BmobFile mBmobFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        ivAvatar.setOnClickListener(this);
        etNickName.setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_avatar:
                PhotoPicker.builder()
                        .setPreviewEnabled(true)
                        .setShowGif(false)
                        .setPhotoCount(1)
                        .setShowCamera(false)
                        .setGridColumnCount(3)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.save:
                uploadAvatar();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PhotoPicker.REQUEST_CODE:  //选择图片返回
                    String url = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).get(0);
                    mBmobFile = new BmobFile(new File(url));
                    Glide.with(this).load(url).centerCrop().into(ivAvatar);
                break;
            }
        }
    }


    private void saveUser() {

        User user = BmobUser.getCurrentUser(User.class);
        if (mBmobFile != null)
            user.setAvatar(mBmobFile);

        user.setNickName(etNickName.getText().toString());
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAvatar() {
        if (mBmobFile != null){
            mBmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        saveUser();
                    }else
                        Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else
            saveUser();
    }
}
