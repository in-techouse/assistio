package lcwu.fyp.asistio.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.UserFile;

public class ShowAudioAdaptor extends RecyclerView.Adapter<ShowAudioAdaptor.AudioViewHolder> {

    private List<UserFile> audios;
    private TextView textView;

    public ShowAudioAdaptor(List<UserFile> audios) {
        this.audios = audios;
      for(UserFile file : audios){
        Log.e("Data" , "audios in adaptor : "+file.getName());

      }

//        setHasStableIds(true);
//        setAudios(audios);
    }

//    public void setAudios(List<UserFile> audios) {
//        this.audios = audios;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_layout, parent, false);
        Log.e("pos" , "position : "+viewType);
        return new AudioViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {

        holder.textView.setText(audios.get(position).getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click" , "Onclick "+position);
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

        public AudioViewHolder(View itemView) {
            super(itemView);
//            Log.e("position" , "in new  : " +position);
            textView = itemView.findViewById(R.id.audioTextView);
//            Log.e("pos" , "position : "+position);
//            textView.setText(audios.get(1).getName());
        }
    }
}
