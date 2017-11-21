package bmob.yellowdoing.com.bmobcommunity;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;
import com.droi.sdk.core.DroiObjectName;
import com.droi.sdk.core.DroiReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YellowDoing on 2017/11/18.
 */

@DroiObjectName("Community")
public class MyCommunity extends DroiObject {

    @DroiExpose
    private ArrayList<String> imagePaths;
    @DroiExpose
    private String content;
    @DroiReference
    private User author;
    @DroiExpose
    private String nickName;
    @DroiExpose
    private int replyNum;
    @DroiExpose
    private List<String> likePersons;


    public List<String> getLikePersons() {
        return likePersons;
    }

    public void setLikePersons(List<String> likePersons) {
        this.likePersons = likePersons;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
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
        return "MyCommunity{" +
                "imagePaths=" + imagePaths +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", nickName='" + nickName + '\'' +
                ", replyNum=" + replyNum +
                ", likePersons=" + likePersons +
                '}';
    }
}
