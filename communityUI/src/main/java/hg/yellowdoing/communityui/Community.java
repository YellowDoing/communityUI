package hg.yellowdoing.communityui;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YellowDoing on 2017/11/17.
 */

public class Community implements Serializable {

    private String avatar;
    private String content;
    private String nickName;
    private int likeNum;
    private boolean isLike;
    private ArrayList<String> imagePaths;
    private int replyNum;
    private String createTime;
    private String id;

    public String getId() {
        return id;
    }

    public Community setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Community setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Community setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public Community setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Community setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public Community setLikeNum(int likeNum) {
        this.likeNum = likeNum;
        return this;
    }

    public boolean isLike() {
        return isLike;
    }

    public Community setLike(boolean like) {
        isLike = like;
        return this;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public Community setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
        return this;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public Community setReplyNum(int replyNum) {
        this.replyNum = replyNum;
        return this;
    }

    @Override
    public String toString() {
        return "Community{" +
                "avatar='" + avatar + '\'' +
                ", content='" + content + '\'' +
                ", nickName='" + nickName + '\'' +
                ", likeNum=" + likeNum +
                ", isLike=" + isLike +
                ", imagePaths=" + imagePaths +
                ", replyNum=" + replyNum +
                '}';
    }
}
