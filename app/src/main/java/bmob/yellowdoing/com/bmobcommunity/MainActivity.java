package bmob.yellowdoing.com.bmobcommunity;

import android.Manifest;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import hg.yellowdoing.communityui.CircleImageView;
import hg.yellowdoing.communityui.CommunityFragment;
import hg.yellowdoing.communityui.CommunityInterface;


public class MainActivity extends AppCompatActivity implements CommunityInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
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
               /* if (BmobUser.getCurrentUser(User.class) != null)
                    if (BmobUser.getCurrentUser(User.class).getAvatar() == null)
                        startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                else
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));*/
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
     /*   if (id == R.id.action_settings) {
            BmobUser.logOut();
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        }else if (id == R.id.user_info){
            if (BmobUser.getCurrentUser(User.class) != null)
                startActivity(new Intent(this,UserInfoActivity.class));
            else
                startActivity(new Intent(this,LoginActivity.class));
        }*/
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
}
