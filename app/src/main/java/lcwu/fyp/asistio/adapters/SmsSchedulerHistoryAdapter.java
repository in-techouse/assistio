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
import lcwu.fyp.asistio.activities.SmsSchedulerHistory;
import lcwu.fyp.asistio.model.MesssageScheduler;

public class SmsSchedulerHistoryAdapter extends RecyclerView.Adapter<SmsSchedulerHistoryAdapter.SmsSchedulerHistoryHolder> {

    private List<MesssageScheduler> schedulers;
    private SmsSchedulerHistory smsScheduler;

    public SmsSchedulerHistoryAdapter(SmsSchedulerHistory smsScheduler) {
        this.schedulers = new ArrayList<>();
        this.smsScheduler = smsScheduler;
    }

    public void setSchedulers(List<MesssageScheduler> schedulers) {
        this.schedulers = schedulers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SmsSchedulerHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_scheduler, parent, false);
        return new SmsSchedulerHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsSchedulerHistoryHolder holder, int position) {
        final MesssageScheduler scheduler = schedulers.get(position);
        holder.message.setText(scheduler.getMessage());
        holder.time.setText(scheduler.getTime());

        String str = "";
        for (int i = 0; i < scheduler.getContactList().size(); i++) {
            str = str + scheduler.getContactList().get(i).getName() + ", " + scheduler.getContactList().get(i).getNumber() + "\n";
        }
        holder.contacts.setText(str);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsScheduler.deleteItem(scheduler.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedulers.size();
    }

    class SmsSchedulerHistoryHolder extends RecyclerView.ViewHolder {
        TextView message, time, contacts;
        ImageView delete;

        SmsSchedulerHistoryHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            contacts = itemView.findViewById(R.id.contacts);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
