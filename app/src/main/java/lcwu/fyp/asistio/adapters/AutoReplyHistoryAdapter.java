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
import lcwu.fyp.asistio.activities.AutoReplyHistory;
import lcwu.fyp.asistio.model.AutoSmsReply;

public class AutoReplyHistoryAdapter extends RecyclerView.Adapter<AutoReplyHistoryAdapter.AutoReplyHistoryHolder> {
    private List<AutoSmsReply> replyList;
    private AutoReplyHistory autoReply;

    public AutoReplyHistoryAdapter(AutoReplyHistory ar) {
        replyList = new ArrayList<>();
        autoReply = ar;
    }

    public void setReplyList(List<AutoSmsReply> replyList) {
        this.replyList = replyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AutoReplyHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_reply_history, parent, false);
        return new AutoReplyHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AutoReplyHistoryHolder holder, int position) {
        final AutoSmsReply reply = replyList.get(position);
        holder.receivedMessage.setText(reply.getMessage());
        holder.replyMessage.setText(reply.getReplyMessage());
        String str = "";
        for (int i = 0; i < reply.getContactList().size(); i++) {
            str = str + reply.getContactList().get(i).getName() + ", " + reply.getContactList().get(i).getNumber() + "\n";
        }
        holder.contacts.setText(str);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoReply.deleteItem(reply.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    class AutoReplyHistoryHolder extends RecyclerView.ViewHolder {
        TextView receivedMessage, replyMessage, contacts;
        ImageView delete;

        AutoReplyHistoryHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessage = itemView.findViewById(R.id.receivedMessage);
            replyMessage = itemView.findViewById(R.id.replyMessage);
            contacts = itemView.findViewById(R.id.contacts);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
