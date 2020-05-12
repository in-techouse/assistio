package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.Contact;

public class ShowContactAdapter extends RecyclerView.Adapter<ShowContactAdapter.ContactHolder> {
    private LayoutInflater inflater;
    private List<Contact> contacts;

    public ShowContactAdapter(Context c) {
        inflater = LayoutInflater.from(c);
        contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.item_contact, viewGroup, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder contactHolder, final int i) {
        final Contact contact = contacts.get(i);

        contactHolder.name.setText(contact.getName());
        contactHolder.number.setText(contact.getNumber());
        contactHolder.selected.setChecked(contact.isSelected());
        contactHolder.selected.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addContacts(List<Contact> data) {
        contacts.clear();
        notifyDataSetChanged();
        contacts.addAll(data);
        notifyDataSetChanged();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        private TextView name, number;
        private CheckBox selected;

        ContactHolder(@NonNull View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.selected);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
        }
    }
}
