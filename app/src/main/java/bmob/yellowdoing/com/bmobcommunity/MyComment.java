package bmob.yellowdoing.com.bmobcommunity;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;
import com.droi.sdk.core.DroiObjectName;
import com.droi.sdk.core.DroiReference;

/**
 * Created by YellowDoing on 2017/11/21.
 */
@DroiObjectName("Comment")
public class MyComment extends DroiObject {
    @DroiReference
    private User author;
    @DroiExpose
    private String content;
    @DroiExpose
    private String commentId;
    @DroiExpose
    private String communityId;
    @DroiExpose
    private String parentId;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
