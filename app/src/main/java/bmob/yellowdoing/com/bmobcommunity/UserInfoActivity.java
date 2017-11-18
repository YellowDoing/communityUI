package bmob.yellowdoing.com.bmobcommunity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by YellowDoing on 2017/11/8.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivAvatar;
    private EditText etNickName;
    private AVFile mFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        ivAvatar.setOnClickListener(this);
        etNickName.setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

        User user = AVUser.getCurrentUser(User.class);
        if (user.getNickName() != null)
            etNickName.setText(user.getNickName());
        if (user.getAvatar() != null)
            Glide.with(this).load(user.getAvatar()).centerCrop().into(ivAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoPicker.REQUEST_CODE:  //选择图片返回
                    String path = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).get(0);
                    mFile = new AVFile("headImg.png", Bitmap2Bytes(decodeSampledBitmapFromPath(path, 80, 80)));
                    Glide.with(this).load(path).centerCrop().into(ivAvatar);
                    break;
            }
        }
    }


    private void saveUser() {

        User user = AVUser.getCurrentUser(User.class);
        if (mFile != null) {
            user.setAvatar(mFile.getUrl());
            user.setFileId(mFile.getObjectId());
        }

        user.setNickName(etNickName.getText().toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAvatar() {
        if (mFile != null) {
            mFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        saveUser();
                        AVObject object = new AVObject();
                        object.setObjectId(AVUser.getCurrentUser(User.class).getFileId());
                        AVFile.withAVObject(object).deleteInBackground();

                    } else
                        Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
            saveUser();
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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 75, baos);
        return baos.toByteArray();
    }

}
