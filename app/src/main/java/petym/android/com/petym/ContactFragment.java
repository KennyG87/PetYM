package petym.android.com.petym;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    private List<Contact> contactList;

    public ContactFragment() {
        contactList = new ArrayList<>();
        contactList.add(new Contact("Contact A", "03-12345567,1"));
        contactList.add(new Contact("Contact B", "03-12345567,2"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new ContactAdapter(inflater));
        return view;
    }


    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        private LayoutInflater inflater;

        public ContactAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvPhone;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.contact_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            Contact contact = contactList.get(position);
            viewHolder.tvName.setText(contact.getName());
            viewHolder.tvPhone.setText(contact.getPhone());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = viewHolder.tvPhone.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return contactList.size();
        }
    }
}
