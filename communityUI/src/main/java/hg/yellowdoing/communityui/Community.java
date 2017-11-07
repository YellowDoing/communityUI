package hg.yellowdoing.communityui;


import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by YellowDoing on 2017/10/18.
 */

public class Community implements Serializable {

    private BmobUser author;
    private List<String> imagePaths;
    private Integer likeNum;
    private Integer replyNum;
    private String content;



    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
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

    public BmobUser getAuthor() {
        return author;
    }

    public void setAuthor(BmobUser author) {
        this.author = author;
    }
}