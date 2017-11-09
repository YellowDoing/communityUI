package hg.yellowdoing.communityui;


import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by YellowDoing on 2017/10/18.
 * 该类用于管理接口请求
 */
public class CommunityService {


    /**
     * 获取社区列表
     */
    public static void getCommunityList(int page,final CommunityFragment fragment) {
        BmobQuery<Community> query = new BmobQuery<>();
        query.setSkip(10 * page);
        query.findObjects(new FindListener<Community>() {
            @Override
            public void done(List<Community> list, BmobException e) {
                if (e == null)
                    fragment.addCommunityList(list);
                else
                    Log.d("aaaa", "done: " + e.getMessage());
            }
        });
    }


}
