package lcwu.fyp.asistio.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SmsSchedulerHistoryHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SmsSchedulerHistoryHolder extends RecyclerView.ViewHolder {
        TextView message, date, time, contacts;
        ImageView delete;

        public SmsSchedulerHistoryHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
