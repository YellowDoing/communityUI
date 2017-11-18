package bmob.yellowdoing.com.bmobcommunity;

import com.avos.avoscloud.AVUser;

/**
 * Created by YellowDoing on 2017/11/18.
 */

public class User extends AVUser{


    private String avatar;
    private String nickName;
    private String fileId;

    public String getFileId() {
        return  this.getString("fileId");
    }

    public void setFileId(String fileId) {
        this.put("fileId",fileId);
    }

    public String getAvatar() {
        return  this.getString("avatar");
    }

    public void setAvatar(String avatar) {
        this.put("avatar",avatar);
    }

    public String getNickName() {
        return  this.getString("nickName");
    }

    public void setNickName(String nickName) {
        this.put("nickName",nickName);
    }
}
