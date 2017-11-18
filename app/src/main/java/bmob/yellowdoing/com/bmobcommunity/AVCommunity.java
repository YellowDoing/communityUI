package bmob.yellowdoing.com.bmobcommunity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YellowDoing on 2017/11/18.
 */

@AVClassName("Community")
public class AVCommunity extends AVObject {


    private List<String> imagePaths;
    private String content;
    private User author;

    public User getAuthor() {
        return this.getAVUser("author",User.class);
    }

    public void setAuthor(User author) {
        try {
            this.put("author", AVUser.createWithoutData(User.class, author.getObjectId()));
        }catch (AVException e){
            e.printStackTrace();
        }
    }

    public String getContent() {
        return  this.getString("content");
    }

    public void setContent(String content) {
        this.put("content",content);
    }

    public List<String> getImagePaths() {
        return  (List<String>)this.getList("imagePaths");
    }

    public void setImagePaths(List<String> imagePaths) {
        this.put("imagePaths",imagePaths);
    }
}
