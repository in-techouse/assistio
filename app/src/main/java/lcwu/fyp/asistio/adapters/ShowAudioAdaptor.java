package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.activities.ShowSingleVideo;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;
import lcwu.fyp.asistio.services.DownloadService;

public class ShowAudioAdaptor extends RecyclerView.Adapter<ShowAudioAdaptor.AudioViewHolder> {

    private List<UserFile> audios;
    private TextView textView;
    private Context mcontext;

    public ShowAudioAdaptor(List<UserFile> audios, Context context) {
        this.audios = audios;
        this.mcontext = context;
        for (UserFile file : audios) {
            Log.e("Data", "audios in adaptor : " + file.getName());

        }
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_layout, parent, false);
        return new AudioViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {


        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click", "ItemClicked" + audios.get(position).getName());
                String url, name, type;
                url = audios.get(position).getDownload_url();
                name = audios.get(position).getName();
                type = audios.get(position).getType();
                Log.e("Service", "Going to intent");

                Intent service = new Intent(mcontext, DownloadService.class);
                service.putExtra("DocURL", url);
                service.putExtra("DocName", name);
                service.putExtra("DocType", type);
                Log.e("Service", "Going to service");
                mcontext.startService(service);
            }
        });
        holder.textView.setText(audios.get(position).getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Onclick " + position);

                //Data Sending
                Intent in = new Intent(mcontext, ShowSingleVideo.class);
                ListUserFile listUserFile = new ListUserFile();
                listUserFile.setUserFiles(audios);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", listUserFile);
                bundle.putInt("index", position);
                in.putExtras(bundle);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);

//                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    public void addItem(List<UserFile> audios) {
        audios.addAll(audios);

        notifyItemInserted(audios.size() - 1);
    }


    class AudioViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView downloadBtn;

        public AudioViewHolder(View itemView) {
            super(itemView);
//            Log.e("position" , "in new  : " +position);
            textView = itemView.findViewById(R.id.audioTextView);
            downloadBtn = itemView.findViewById(R.id.audio_download);
//            Log.e("pos" , "position : "+position);
//            textView.setText(audios.get(1).getName());
        }
    }
}
