package lcwu.fyp.asistio.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.activities.SpeechNotesHistory;
import lcwu.fyp.asistio.model.SpeechNotesObject;

public class SpeechNotesHistoryAdapter extends RecyclerView.Adapter<SpeechNotesHistoryAdapter.SpeechNotesHistoryHolder> {
    private List<SpeechNotesObject> notesList;
    private SpeechNotesHistory speechNotesHistory;

    public SpeechNotesHistoryAdapter(SpeechNotesHistory snh) {
        speechNotesHistory = snh;
        notesList = new ArrayList<>();
    }

    public void setNotesList(List<SpeechNotesObject> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpeechNotesHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speech_notes_history, parent, false);
        return new SpeechNotesHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeechNotesHistoryHolder holder, int position) {
        final SpeechNotesObject object = notesList.get(position);
        holder.note.setText(object.getNote());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechNotesHistory.deleteNote(object.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class SpeechNotesHistoryHolder extends RecyclerView.ViewHolder {
        TextView note;
        ImageView delete;

        SpeechNotesHistoryHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
