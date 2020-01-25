package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.view.View;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.UserFile;

public class ShowAudioAdaptor extends RecyclerView.Adapter<ShowAudioAdaptor.AudioViewHolder> {

    private List<UserFile> audios;
    private Context context;

    public ShowAudioAdaptor(Context context, ArrayList<UserFile> audios) {
        this.context = context;
        this.audios = audios;
    }



    @Override
    public ShowAudioAdaptor.AudioViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
        this.context = context;
        android.view.View view = LayoutInflater.from(context).inflate(R.layout.audio_layout, parent, false);
        AudioViewHolder viewHolder = new AudioViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ShowAudioAdaptor.AudioViewHolder holder, int position) {
        holder.bindAudios((List<UserFile>) audios,  position);
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }


    public class AudioViewHolder extends RecyclerView.ViewHolder {

        TextView textView = view.findViewById(R.id.audioTextView);

        private Context context;

        public AudioViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bindAudios(List<UserFile> audio , int index) {
            textView.setText(audio.get(index).getName());
        }
    }

//    private LayoutInflater thisInflater;
//
//    public ShowAudioAdaptor(Context context , List<UserFile> audios ){
//        this.context = context;
//        this.audios = audios;
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public android.view.View getView(int i, android.view.View view, ViewGroup viewGroup) {
//        if(view == null){
//            view = thisInflater.inflate(R.layout.audio_layout , viewGroup ,false);
//        }
//        final UserFile userFile = audios.get(i);
//        TextView textView = view.findViewById(R.id.audioTextView);
//        textView.setText("name is  : "+userFile.getName());
//        Log.e("text" , "hText :"+textView);
//
//        return view;
//    }
}
