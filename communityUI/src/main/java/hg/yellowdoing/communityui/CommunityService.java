package hg.yellowdoing.communityui;


import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by YellowDoing on 2017/10/18.
 * 该类用于管理接口请求
 */
public class CommunityService {

    public  interface Callback{
        void callback();
    }


    /**
     * 获取社区列表
     */
    public static void getCommunityList(int page,final CommunityFragment fragment) {
        BmobQuery<Community> query = new BmobQuery<>();
        query.setSkip(10 * page);
        query.order("-createdAt");
        query.findObjects(new FindListener<Community>() {
            @Override
            public void done(List<Community> list, BmobException e) {
                if (e == null)
                    fragment.addCommunityList((ArrayList<Community>) list);
                else
                    Log.d("bmob", "done: " + e.getMessage());
            }
        });
    }


    /**
     * 点赞+1
     */
    public static void addLikeNum(Community community, final Callback callback) {
        community.increment("likeNum");
        community.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    callback.callback();
                else
                    Log.d("bmob", "done: " + e.getMessage());
            }
        });
    }

    /**
     * 取消点赞
     */
    public static void reduceLikeNum(Community community, final Callback callback) {
        community.increment("likeNum",-1);
        community.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    callback.callback();
                else
                    Log.d("bmob", "done: " + e.getMessage());
            }
        });
    }

}
