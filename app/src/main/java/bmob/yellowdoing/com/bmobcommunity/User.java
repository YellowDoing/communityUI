package bmob.yellowdoing.com.bmobcommunity;
import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiFile;
import com.droi.sdk.core.DroiUser;

/**
 * Created by YellowDoing on 2017/11/18.
 */

public class User extends DroiUser {

    @DroiExpose
    private DroiFile avatar;
    @DroiExpose
    private String nickName;

    public DroiFile getAvatar() {
        return avatar;
    }

    public void setAvatar(DroiFile avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
