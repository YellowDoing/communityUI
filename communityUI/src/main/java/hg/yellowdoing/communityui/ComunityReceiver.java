package hg.yellowdoing.communityui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YellowDoing on 2017/10/18.
 */

public class ComunityReceiver extends BroadcastReceiver {

    public final static String COMMUNITY_ACTION = "Comunicaty";
    public final static int CREATE_NEW_POST = 0; //发表新帖

    private CommunityFragment mCommunityFragment;

    public ComunityReceiver(CommunityFragment communityFragment) {
        mCommunityFragment = communityFragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getIntExtra(COMMUNITY_ACTION,-1)){
            case CREATE_NEW_POST: //发表新帖
                break;
        }
    }
}
