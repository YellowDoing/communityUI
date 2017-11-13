package bmob.yellowdoing.com.bmobcommunity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import hg.yellowdoing.communityui.CircleImageView;
import hg.yellowdoing.communityui.CommunityFragment;
import hg.yellowdoing.communityui.CommunityInterface;
import hg.yellowdoing.communityui.CommunityService;
import hg.yellowdoing.communityui.PostActivity;
import hg.yellowdoing.communityui.User;

public class MainActivity extends AppCompatActivity implements CommunityInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new CommunityFragment())
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BmobUser.getCurrentUser(User.class) != null)
                    if (BmobUser.getCurrentUser(User.class).getAvatar() == null)
                        startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                else
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
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
            BmobUser.logOut();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }else if (id == R.id.user_info){
            if (BmobUser.getCurrentUser(User.class) != null)
                startActivity(new Intent(this,UserInfoActivity.class));
            else
                startActivity(new Intent(this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public ArrayList loadDataList(int page) {
        return null;
    }

    @Override
    public ArrayList<String> bindListItemView(Object o, CircleImageView imgHead, TextView nickName, TextView content, TextView replyNum, TextView likeNum, TextView createTime) {
        return null;
    }

    @Override
    public boolean isLike(Object o) {
        return false;
    }

    @Override
    public boolean like(Object o) {
        return false;
    }

    @Override
    public boolean unLike(Object o) {
        return false;
    }

    @Override
    public boolean reply(Object o, String content) {
        return false;
    }
}
