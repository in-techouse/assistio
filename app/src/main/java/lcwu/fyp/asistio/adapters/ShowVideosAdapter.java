package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.activities.ShowSingleVideo;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;
import lcwu.fyp.asistio.services.DownloadService;

public class ShowVideosAdapter extends BaseAdapter {
    private List<UserFile> videos;
    private Context context;

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
        final UserFile userFile = videos.get(position);
        ImageView playBtn = convertView.findViewById(R.id.playBtn);
        ImageView downloadBtn = convertView.findViewById(R.id.downloadBtn);
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
        ImageView thumbnailImage =  convertView.findViewById(R.id.flag);
        //Glide method
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(userFile.getDownload_url()).into(thumbnailImage);
        return convertView;
    }
}


