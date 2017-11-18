package hg.yellowdoing.communityui;


import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by YellowDoing on 2017/11/13.
 */

public interface CommunityInterface{

    void loadCommunityList(CommunitySubsriber subsriber,int page);

    void reply(Subsriber subsriber,String communityId,String content);

    void loadComments(CommentSubsriber subsriber,String communityId,int page);

    void like(Subsriber subsriber,String communityId);

    void unLike(Subsriber subsriber,String communityId);


   interface CommunitySubsriber{
       void onComplete(List<Community> communityList);
   }

    interface CommentSubsriber{
        void onComplete(List<Comment> communityList);
    }

    interface Subsriber{
        void onComplete();
    }

}
