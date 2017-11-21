package hg.yellowdoing.communityui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by YellowDoing on 2017/11/11.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Activity mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mImagePaths;
    private int viewWH;

    public ImageAdapter(Activity context, ArrayList<String> imagePaths) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mImagePaths = imagePaths;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_image, parent, false);
        if (viewWH == 0)
            if (mContext instanceof ComminityDetialActivity)
                viewWH = (getScreenWidth(mContext) - dip2px(mContext, 29)) / 3;
            else
                viewWH = (getScreenWidth(mContext) - dip2px(mContext, 72)) / 3;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewWH, viewWH);
        view.setLayoutParams(params);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        Glide.with(mContext).load(mImagePaths.get(position)).placeholder(R.drawable.hospital_bitmap).centerCrop().into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreview.builder()
                        .setPhotos(mImagePaths)
                        .setCurrentItem(position)
                        .setShowDeleteButton(false)
                        .start(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagePaths.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        String path;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private int getScreenWidth(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 根据手机的分辨率从dp的单位转成为px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
