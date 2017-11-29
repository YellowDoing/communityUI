package hg.yellowdoing.communityui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/18.
 */

public class Comment implements Serializable , Cloneable{

    private String avatar;
    private String nickName;
    private String content;
    private String id;
    private long createTime;
    private String commentId; //直接对应的父评论ID
    private String parentId; //对应的一级评论ID
    private String theOtherNickName;
    private String communityId;

    public String getCommunityId() {
        return communityId;
    }

    public Comment setCommunityId(String communityId) {
        this.communityId = communityId;
        return this;
    }

    public String getTheOtherNickName() {
        return theOtherNickName;
    }

    public Comment setTheOtherNickName(String theOtherNickName) {
        this.theOtherNickName = theOtherNickName;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Comment setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCommentId() {
        return commentId;
    }

    public Comment setCommentId(String commentId) {
        this.commentId = commentId;
        return this;
    }

    private List<Comment> childComments;

    public List<Comment> getChildComments() {
        return childComments;
    }

    public Comment setChildComments(List<Comment> childComments) {
        this.childComments = childComments;
        return this;
    }

    public Comment addChildComments(Comment comment) {
        if (childComments == null)
            childComments = new ArrayList<>();
        childComments.add(comment);
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public Comment setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getId() {
        return id;
    }

    public Comment setId(String id) {
        this.id = id;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public Comment setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Comment setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", content='" + content + '\'' +
                ", parentId='" + parentId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public Comment clone() {
        Comment stu = null;
        try{
            stu = (Comment) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
