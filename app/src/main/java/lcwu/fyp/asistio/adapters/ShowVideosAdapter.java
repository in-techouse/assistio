package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.firebase.database.core.view.View;

import com.bumptech.glide.Glide;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.activities.ShowSingleVideo;
import lcwu.fyp.asistio.activities.ShowVideos;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;
import lcwu.fyp.asistio.services.DownloadService;

public class ShowVideosAdapter extends BaseAdapter {
    private List<UserFile> videos;
    private Context context;
    private ImageView playBtn , downloadBtn;

    private LayoutInflater thisInflater;

    public  ShowVideosAdapter(Context context , List<UserFile> videos )  {

        Log.e("adapter", "constructor : "+videos);
        this.context = context;
        this.thisInflater = LayoutInflater.from(context);
        this.videos = videos;
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
        MediaController ctlr;
        final UserFile userFile = videos.get(position);
        playBtn = convertView.findViewById(R.id.playBtn);
        downloadBtn = convertView.findViewById(R.id.downloadBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("play" , "button captured "+videos.get(position));
                Log.e("play" , "button captured "+videos.get(position).getName());

                Intent in  = new Intent(context , ShowSingleVideo.class);
                ListUserFile listUserFile = new ListUserFile();
                listUserFile.setUserFiles(videos);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", listUserFile);
                bundle.putInt("index", position);

                in.putExtras(bundle);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url , name , type;
                url = videos.get(position).getDownload_url();
                name = videos.get(position).getName();
                type = videos.get(position).getType();
                Intent intent = new Intent(context , DownloadService.class);
                intent.putExtra("DocURL" , url);
                intent.putExtra("DocName" , name);
                intent.putExtra("DocType" , type);
                context.startService(intent);

            }
        });
        TextView textHeading =  convertView.findViewById(R.id.txt);
        //Native Video Player
//        VideoView videoView = convertView.findViewById(R.id.VideoView);
////        videoView.setVideoPath(userFile.getDownload_url());
//        Uri uri = Uri.parse(userFile.getDownload_url());
//        videoView.setVideoURI(uri);
//        videoView.seekTo( 1 );
//        ctlr = new MediaController(context);
//        ctlr.setMediaPlayer(videoView);
//        videoView.setMediaController(ctlr);
//        videoView.requestFocus();
//        ctlr.setAnchorView(videoView);

        ImageView thumbnailImage =  convertView.findViewById(R.id.flag);
        textHeading.setText(userFile.getName());
        Log.e("text" , "hText :"+textHeading);
        //Glide method
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.isMemoryCacheable();
//        Glide.with(context).load(userFile.getDownload_url()).into(thumbnailImage);
//        Glide.with(context).setDefaultRequestOptions(requestOptions).load(userFile.getDownload_url()).into(thumbnailImage);

        //Suzi method
//        Glide.with(context).load(userFile.getDownload_url()).into(thumbnailImage);
//        SuziLoader loader = new SuziLoader(); //Create it for once
//        loader.with(context)//Context
//                .load(userFile.getDownload_url()) //Video path
//                .into(thumbnailImage) // imageview to load the thumbnail
//                .type("mini") // mini or micro
//                .show();
//        Log.e("thumbnail" , "thumbnail " + thumbnailImage);

        //Picaso
//        thumbnailImage.setImageResource(R.drawable.calendar);

//        Picasso.get()
//                .load(userFile.getDownload_url().toString())
//                .resize(50, 50)
//                .centerCrop()
//                .into(thumbnailImage);


        //Very Slow
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        //give YourVideoUrl below
//        retriever.setDataSource(userFile.getDownload_url(), new HashMap<String, String>());
//        // this gets frame at 2nd second
//        Bitmap image = retriever.getFrameAtTime(500000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//        thumbnailImage.setImageBitmap(image);
//        //use this bitmap image

//        Bitmap bmThumbnail;
//        bmThumbnail = ThumbnailUtils.createVideoThumbnail(userFile.getDownload_url(),
//                MediaStore.Video.Thumbnails.MINI_KIND);
//
//        if (bmThumbnail != null) {
//            Log.d("VideoAdapter","video thumbnail found");
//            thumbnailImage.setImageBitmap(bmThumbnail);
//        } else {
//            Log.d("VideoAdapter","video thumbnail not found");
//        }




        return convertView;
    }



}


