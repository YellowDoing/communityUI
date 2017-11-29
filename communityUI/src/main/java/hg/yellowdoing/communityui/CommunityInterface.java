package hg.yellowdoing.communityui;


import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by YellowDoing on 2017/11/13.
 */

public interface CommunityInterface{

    void loadCommunityList(CommunitySubsriber subsriber,int page);

    /**
     * @param subsriber 接口回调
     * @param communityId 帖子的Id
     * @param commentId  所评论的评论的Id
     * @param parentId 父评论Id
     * @param content 评论的内容
     */
    void comment(CommentSubsriber2 subsriber,String communityId,String parentId,String commentId,String content);

    void loadComments(CommentSubsriber subsriber,String communityId,int page);

    void like(Subsriber subsriber,String communityId);

    void unLike(Subsriber subsriber,String communityId);

    void post(Subsriber subsriber,ArrayList<String> imagePaths,String content);

   interface CommunitySubsriber{
       void onComplete(List<Community> communityList);
   }

    interface CommentSubsriber{
        void onComplete(List<Comment> communityList);
    }

    interface CommentSubsriber2{
        void onComplete(Comment comment);
    }

    interface Subsriber{
        void onComplete();
    }

}
