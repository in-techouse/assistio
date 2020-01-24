package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.core.view.View;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.UserFile;

public class ShowVideosAdapter extends BaseAdapter {
    private List<UserFile> videos;
    private Context context;
//    Bitmap bm1 , bm2;
//    private Bitmap[] myimges = {
//            bm1,
//            bm2
////            retriveVideoFrameFromVideo(videos.get(0)),
////            R.string.enter_last_name,
////            R.string.enable_random_adaptation,
////            R.string.error_drm_unknown,
////            R.string.app_name,
////            R.string.app_name,
////            R.string.app_name,
//    };
    private LayoutInflater thisInflater;

    public  ShowVideosAdapter(Context context , List<UserFile> videos )  {

        Log.e("adapter", "constructor : "+videos);
        this.context = context;
        this.thisInflater = LayoutInflater.from(context);
        this.videos = videos;
//        bm1 = retriveVideoFrameFromVideo(videos.get(0));
//        bm2 = retriveVideoFrameFromVideo(videos.get(1));
    }



    @Override
    public int getCount() {
        return this.videos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate( R.layout.gridview_layout, parent, false );
        }

        final UserFile userFile = videos.get(position);
        TextView textHeading =  convertView.findViewById(R.id.txt);
        ImageView thumbnailImage =  convertView.findViewById(R.id.flag);
        textHeading.setText("Id: " + userFile.getName());

//        Bitmap bm = ThumbnailUtils.createVideoThumbnail(String.valueOf(videos.get(0)),MediaStore.Video.Thumbnails.MINI_KIND);

//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        //give YourVideoUrl below
//        retriever.setDataSource(userFile.getDownload_url(), new HashMap<String, String>());
//        // this gets frame at 2nd second
//        Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);


//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(userFile.getDownload_url(), MediaStore.Video.Thumbnails.MICRO_KIND);
//        thumbnailImage.setImageBitmap(retriveVideoFrameFromVideo(userFile.getDownload_url()));
//        Log.e("Bitmap" , "Bitmap"+image);

//        long thumb = getLayoutPosition()*1000;
//        RequestOptions options = new RequestOptions().frame(thumb);
//        Bitmap bm;

        Glide
                .with(context)
                .load(userFile.getDownload_url())
                .thumbnail(0.1f)
                .into(thumbnailImage);
//
//        Glide.with(context).load(userFile.getDownload_url())
////                .apply(options)
//                .into(thumbnailImage);

//        thumbnailImage.setImageBitmap();
//        thumbnailImage.setImageDrawable();
//        thumbnailImage.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));

        //
//
//            thumbnailImage.setImageResource( myimges[position] );
        return convertView;
    }


    private Bitmap retriveVideoFrameFromVideo(String videoPath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Log.e("BitMap", "Video Path: " + videoPath);

        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("BitMap", "Exception Occur: " + e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
            else{
                Log.e("BitMap", "Media meta data is null");
            }
        }
        return bitmap;
    }


}


