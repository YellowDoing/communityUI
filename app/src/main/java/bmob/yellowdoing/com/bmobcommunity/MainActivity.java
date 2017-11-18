package bmob.yellowdoing.com.bmobcommunity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import hg.yellowdoing.communityui.CommunityFragment;
import hg.yellowdoing.communityui.CommunityInterface;
import hg.yellowdoing.communityui.PostActivity;


public class MainActivity extends AppCompatActivity implements CommunityInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化LeanCloud
        AVObject.registerSubclass(AVCommunity.class);
        AVOSCloud.initialize(this, "UOpnozjpc85rVU44XhinIGBv-gzGzoHsz", "HbE1EQrjc2kPSnV3COrlUyBz");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new CommunityFragment().setCommunityInterface(this))
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AVUser.getCurrentUser(User.class) != null)
                    if (AVUser.getCurrentUser(User.class).getAvatar() == null)
                        startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                else
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AVUser.logOut();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (id == R.id.user_info) {
            if (AVUser.getCurrentUser(User.class) != null)
                startActivity(new Intent(this, UserInfoActivity.class));
            else
                startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadCommunityList(CommunitySubsriber subsriber, int page) {

    }

    @Override
    public void reply(Subsriber subsriber, String communityId, String content) {

    }

    @Override
    public void loadComments(CommentSubsriber subsriber, String communityId, int page) {

    }

    @Override
    public void like(Subsriber subsriber, String communityId) {

    }

    @Override
    public void unLike(Subsriber subsriber, String communityId) {

    }

    @Override
    public void post(final Subsriber subsriber, final ArrayList<String> imagePaths, String content) {
        final AVCommunity avCommunity = new AVCommunity();
        avCommunity.setContent(content);
        avCommunity.setAuthor(User.getCurrentUser(User.class));
        final List<String> list = new ArrayList<>();
        String imgName = AVUser.getCurrentUser(User.class).getObjectId() + ".png";
        if (imagePaths.size() == 0)
            saveCommunity(subsriber, avCommunity);
        else
            for (int i = 0; i < imagePaths.size(); i++) {
                final AVFile avFile = new AVFile(imgName, UserInfoActivity.Bitmap2Bytes(UserInfoActivity.decodeSampledBitmapFromPath(imagePaths.get(i), 80, 80)));
                avFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            list.add(avFile.getUrl());
                            if (list.size() == imagePaths.size()) {
                                avCommunity.setImagePaths(list);
                                saveCommunity(subsriber, avCommunity);
                            }
                        } else
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    private void saveCommunity(final Subsriber subsriber, AVCommunity avCommunity) {
        avCommunity.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    subsriber.onComplete();
                    Toast.makeText(MainActivity.this, "发帖成功", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
