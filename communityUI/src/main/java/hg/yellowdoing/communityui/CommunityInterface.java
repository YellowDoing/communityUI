package hg.yellowdoing.communityui;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13.
 */

public interface CommunityInterface<T> {

    ArrayList<T> loadDataList(int page);

    ArrayList<String> bindListItemView(T t, CircleImageView imgHead, TextView nickName,TextView content,TextView replyNum,TextView likeNum);

}
