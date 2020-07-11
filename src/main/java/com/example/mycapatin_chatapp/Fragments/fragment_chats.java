package com.example.mycapatin_chatapp.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycapatin_chatapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycapatin_chatapp.Adapter.UserAdapter;
import com.example.mycapatin_chatapp.Model.Chat;
import com.example.mycapatin_chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class fragment_chats extends Fragment {
     private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<User> mUsers;

        private List<User> display_chat;

        FirebaseUser fuser;
        DatabaseReference reference;

        private List<String> userslist;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userslist = new ArrayList<String>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userslist.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(fuser.getUid().equals(chat.getSender())){
                        userslist.add(chat.getReceiver());
                    }
                    if(fuser.getUid().equals(chat.getReceiver())){
                        userslist.add(chat.getSender());
                    }

                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readChats(){
        mUsers = new ArrayList<>();
        display_chat= new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                display_chat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(String name: userslist){
                        if(name.equals(user.getId())){
                            display_chat.add(user);
                            break;
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),display_chat,true);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}