package lcwu.fyp.asistio.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.Contact;

public class SelectedContactsAdapter extends BaseAdapter {

    private List<Contact> contacts;

    public SelectedContactsAdapter(){
        contacts = new ArrayList<>();
    }

    public void setContacts(List<Contact> cs){
        contacts = cs;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contact contact = contacts.get(position);
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_contacts, null);
        }
        else{
            view = convertView;
        }

        TextView name = view.findViewById(R.id.name);
        TextView number = view.findViewById(R.id.number);
        name.setText(contact.getName());
        number.setText(contact.getNumber());

        return view;
    }

}
