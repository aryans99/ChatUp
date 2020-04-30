package com.mycaptain.chatup.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mycaptain.chatup.Adapter.UserAdapter;
import com.mycaptain.chatup.Fragments.Model.User;
import com.mycaptain.chatup.R;

import java.util.ArrayList;
import java.util.List;

public class fragmentchats extends Fragment {
        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<User> mUsers;
        private List<User>display_chat;
        FirebaseUser fuser;
        DatabaseReference reference;
        private List<String> userslist;

        public fragmentchats(){

        }



    @Override
    public void onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_fragmentchats,container,false);
        recyclerView=view.findViewById(R.layout.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        userslist=new ArrayList<String>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                userslist.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat= snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(fuser.getUid())){
                        userslist.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fuser.getUid())){
                        userslist.add(chat.getSender());
                    }
                }
                readChats();
            }

        });
        return view;
        }
        private void readChats(){
            mUsers=new ArrayList<>();
            display_chat=new ArrayList<>();
            reference=FirebaseDatabase.getInstance().getReference("Users");
            reference.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                    mUsers.clear();
                    display_chat.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        User user= snapshot.getValue(User.class);
                        for(String name: userslist){
                            if(user.getId().equals(name)){
                                display_chat.add(user);
                                break;
                            }
                        }
                    }
                    userAdapter= new UserAdapter(getContext(),display_chat,true);
                    recyclerView.setAdapter(userAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError){

                }

            });
        }
}
