package hg.yellowdoing.communityui;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/11/8.
 */

public class User extends BmobUser {

    private String avata;
    private String nickName;

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
