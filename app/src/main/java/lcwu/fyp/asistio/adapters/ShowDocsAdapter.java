package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.activities.Dashboard;
import lcwu.fyp.asistio.model.UserFile;
import lcwu.fyp.asistio.services.DownloadService;
import lcwu.fyp.asistio.services.ScanMediaService;

public class ShowDocsAdapter extends RecyclerView.Adapter<ShowDocsAdapter.DocsViewHolder> {

    private List<UserFile> docs;
    private Context mcontext;
    private ImageView docDownload;

    public ShowDocsAdapter(List<UserFile> docs , Context context){
        this.docs  = docs;
        this.mcontext = context;
    }


    @NonNull
    @Override
    public DocsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_layout , parent , false);
        return  new DocsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocsViewHolder holder, int position) {
        holder.textView.setText(docs.get(position).getName());
        docDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click" , "ItemClicked"+docs.get(position).getName());
                String url , name , type;
                url = docs.get(position).getDownload_url();
                name = docs.get(position).getName();
                type = docs.get(position).getType();

                Log.e("Service" , "Going to intent");


                Intent service = new Intent(mcontext, DownloadService.class);
                service.putExtra("DocURL" , url);
                service.putExtra("DocName" , name);
                service.putExtra("DocType" , type);
                Log.e("Service" , "Going to service");
                mcontext.startService(service);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    class DocsViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public DocsViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.documentTextView);
            docDownload = itemView.findViewById(R.id.doc_download);
        }
    }
}
