package bmob.yellowdoing.com.bmobcommunity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.droi.sdk.DroiCallback;
import com.droi.sdk.DroiError;
import com.droi.sdk.DroiProgressCallback;
import com.droi.sdk.core.DroiFile;
import com.droi.sdk.core.DroiPermission;
import com.droi.sdk.core.DroiUser;

import java.io.ByteArrayOutputStream;


import me.iwf.photopicker.PhotoPicker;

/**
 * Created by YellowDoing on 2017/11/8.
 *
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivAvatar;
    private EditText etNickName;
    private DroiFile mFile;
    private AlertDialog.Builder mBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        ivAvatar.setOnClickListener(this);
        etNickName.setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

        User user = DroiUser.getCurrentUser(User.class);
        if (user.getNickName() != null)
            etNickName.setText(user.getNickName());
        if (user.getAvatar() != null)
            Glide.with(this).load(user.getAvatar()).centerCrop().into(ivAvatar);

        mBuilder = new AlertDialog.Builder(this).setView(new ProgressBar(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoPicker.REQUEST_CODE:  //选择图片返回
                    String path = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).get(0);
                    mFile = new DroiFile(Bitmap2Bytes(decodeSampledBitmapFromPath(path, 80, 80)));
                    Glide.with(this).load(path).centerCrop().into(ivAvatar);
                    break;
            }
        }
    }


    private void saveUser(AlertDialog dialog) {
        User user = DroiUser.getCurrentUser(User.class);
        if (mFile != null)
            user.setAvatar(mFile);

        user.setNickName(etNickName.getText().toString());
        DroiError error = user.save();
        if (error.isOk()) {
            Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(UserInfoActivity.this, error.getAppendedMessage(), Toast.LENGTH_SHORT).show();
        dialog.cancel();
    }

    private void uploadAvatar() {
        final AlertDialog dialog =  mBuilder.show();

        if (mFile != null) {
            DroiPermission permission = new DroiPermission();
            permission.setPublicReadPermission(true);
            mFile.setPermission(permission);
            mFile.saveInBackground(new DroiCallback<Boolean>() {
                @Override
                public void result(Boolean aBoolean, DroiError droiError) {
                    if (aBoolean)
                        saveUser(dialog);
                    else
                        Toast.makeText(UserInfoActivity.this, droiError.getAppendedMessage(), Toast.LENGTH_SHORT).show();
                }
            }, new DroiProgressCallback() {
                @Override
                public void progress(Object o, long l, long l1) {
                    Log.d("aaaa", "上传了: " + (int)((l*100)/l1) + "%");
                }
            });
        } else
            saveUser(dialog);
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
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
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
        bm.compress(Bitmap.CompressFormat.PNG, 95, baos);
        return baos.toByteArray();
    }

}
