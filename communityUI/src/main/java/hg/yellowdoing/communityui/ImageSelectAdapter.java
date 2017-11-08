package hg.yellowdoing.communityui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Administrator on 2017/11/7.
 */

public class ImageSelectAdapter extends RecyclerView.Adapter<ImageSelectAdapter.ImageSelectViewHolder> {


    private PostActivity mContext;
    private ArrayList<String> mPaths;
    private int dp_4,dp_72;


    public ImageSelectAdapter(PostActivity context) {
        mContext = context;
        mPaths = new ArrayList<>();
        dp_4 = dip2px(4);
        dp_72 = dip2px(72);
    }

    public void add(ArrayList<String> paths){
        mPaths.addAll(paths);
        notifyDataSetChanged();
    }

    public void reset(ArrayList<String> paths){
        mPaths = paths;
        notifyDataSetChanged();
    }

    public ArrayList<String> getPaths(){
        return mPaths;
    }

    @Override
    public ImageSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageSelectViewHolder(new ImageView(mContext));
    }

    @Override
    public void onBindViewHolder(ImageSelectViewHolder holder, final int position) {

        if (position == mPaths.size()) {
            holder.mImageView.setImageResource(R.drawable.ic_add_picture);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   PhotoPicker.builder()
                            .setPreviewEnabled(true)
                            .setShowGif(false)
                            .setPhotoCount(9 - mPaths.size())
                            .setShowCamera(false)
                            .setGridColumnCount(3)
                            .start(mContext, PhotoPicker.REQUEST_CODE);
                }
            });
        } else {
            Glide.with(mContext).load(mPaths.get(position)).centerCrop().into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoPreview.builder()
                            .setPhotos(mPaths)
                            .setShowDeleteButton(true)
                            .setCurrentItem(position)
                            .start(mContext, PhotoPreview.REQUEST_CODE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mPaths.size() < 9)
            return mPaths.size() + 1;
        else
            return 9;
    }

    class ImageSelectViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public ImageSelectViewHolder(ImageView itemView) {
            super(itemView);
            mImageView = itemView;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp_72,dp_72);
            params.setMargins(0,0,dp_4,0);
            mImageView.setLayoutParams(params);
        }

    }

    public int dip2px(float dpValue){
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
