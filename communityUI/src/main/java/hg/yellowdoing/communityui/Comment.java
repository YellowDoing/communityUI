package hg.yellowdoing.communityui;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/18.
 */

public class Comment implements Serializable {

    private String avatar;
    private String nickName;
    private String content;
    private String parentId;
    private String id;

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
}
