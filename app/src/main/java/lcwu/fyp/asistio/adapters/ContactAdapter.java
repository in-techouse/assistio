package lcwu.fyp.asistio.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private LayoutInflater inflater;
    private List<Contact> contacts;

    public ContactAdapter(Context c) {
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
        contactHolder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactHolder.selected.isChecked()){
                    Log.e("Adapter", contact.getName() + " Selected");
                    contacts.get(i).setSelected(true);
                }
                else{
                    contacts.get(i).setSelected(false);
                    Log.e("Adapter", contact.getName() + " UnSelected");
                }
            }
        });

        contactHolder.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contacts.get(i).isSelected()){
                    contacts.get(i).setSelected(false);
                    contactHolder.selected.setChecked(false);
                }
                else{
                    contacts.get(i).setSelected(true);
                    contactHolder.selected.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addContacts(List<Contact> data){
        contacts.clear();
        notifyDataSetChanged();
        contacts.addAll(data);
        notifyDataSetChanged();
    }

    public List<Contact> getContacts(){
        return contacts;
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        private TextView name, number;
        private CheckBox selected;
        private CardView mainCard;
        ContactHolder(@NonNull View itemView) {
            super(itemView);
            selected = itemView.findViewById(R.id.selected);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            mainCard = itemView.findViewById(R.id.mainCard);
        }
    }

    }