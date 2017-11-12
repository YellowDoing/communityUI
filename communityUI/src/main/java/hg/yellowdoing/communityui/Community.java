package hg.yellowdoing.communityui;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by YellowDoing on 2017/10/18.
 */

public class Community extends BmobObject implements Serializable {

    private User author;
    private ArrayList<String> imagePaths;
    private Integer likeNum;
    private Integer replyNum;
    private String content;
    private boolean isLike;


    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Community{" +
                "author=" + author +
                ", imagePaths=" + imagePaths +
                ", likeNum=" + likeNum +
                ", replyNum=" + replyNum +
                ", content='" + content + '\'' +
                '}';
    }
}
