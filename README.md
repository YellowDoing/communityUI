## communityUI
一个可快速集成的朋友圈界面，支持评论、子评论、点赞、回复人数。

![](https://note.youdao.com/yws/api/personal/file/WEB065b58ab97645293aaa2f2287d10bea1?method=download&shareKey=eb82539165ecee4e1694a44d889405bf)      ![](https://note.youdao.com/yws/api/personal/file/WEB2caef7ed89cd62aa5e048c5e22892262?method=download&shareKey=163142cec3144bf4fb135fac6c0abf87)

> 建议有一个自己的后台数据库，可直接继承,示例代码用的是作者自己的简单后台

#### 集成步骤
1 . 继承CommunityInterface接口，添加fragment

```
//朋友圈列表是一个fragment，需要事务添加
public class MainActivity extends AppCompatActivity implements CommunityInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new CommunityFragment().setCommunityInterface(this))
                .commit();
 }
```	
2 . 实现接口的六个方法，进行数据转换

    void loadCommunityList(CommunitySubsriber subsriber,int page);

    void comment(CommentSubsriber2 subsriber,String communityId,String parentId,String commentId,String content);

    void loadComments(CommentSubsriber subsriber,String communityId,int page);

    void like(Subsriber subsriber,String communityId);

    void unLike(Subsriber subsriber,String communityId);

    void post(Subsriber subsriber,ArrayList<String> imagePaths,String content);
    
3 . 在Mainifest.xml文件添加activity声明


```
<activity android:name="hg.yellowdoing.communityui.PostActivity"/>
<activity android:name="me.iwf.photopicker.PhotoPickerActivity"
            android:theme="@style/Theme.AppCompat.NoActionBar" />
<activity android:name="me.iwf.photopicker.PhotoPagerActivity"
            android:theme="@style/Theme.AppCompat.NoActionBar"/>
<activity android:name=".UserInfoActivity"/>
<activity android:name="hg.yellowdoing.communityui.CommunityDetialActivity"/>
 <activity android:name="hg.yellowdoing.communityui.CommentDetailActivity"/>
```

       
