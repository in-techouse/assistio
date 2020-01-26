package lcwu.fyp.asistio.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.UserFile;

public class ShowAudioAdaptor extends RecyclerView.Adapter<ShowAudioAdaptor.AudioViewHolder> {

    private List<UserFile> audios;

    public ShowAudioAdaptor(List<UserFile> audios) {
        this.audios = audios;
    }

    public void setAudios(List<UserFile> audios) {
        this.audios = audios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return audios.size();
    }


    class AudioViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public AudioViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.audioTextView);
        }
    }
}
