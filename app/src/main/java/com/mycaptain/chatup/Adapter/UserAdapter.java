package com.mycaptain.chatup.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycaptain.chatup.Fragments.Model.Chat;
import com.mycaptain.chatup.Fragments.Model.User;
import com.mycaptain.chatup.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context mContext;
    List<User> mUsers;
    boolean chat;
    String LastMessage;

    public UserAdapter(Context mContext, List<User> mUsers, boolean chat, String lastMessage) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.chat = chat;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);

        }
        if (chat) {
            lastmessage(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }
        if (chat) {

            if (user.getStatus().equals("Online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
        holder.img_on.setVisibility(View.GONE);
        holder.img_off.setVisibility(View.GONE);}

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(mContext,message.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount(){
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        public TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewbyId(R.id.username);
            profile_image = itemView.findViewbyId(R.id.profile_image);
            img_on = itemView.findViewbyId(R.id.img_on);
            img_off = itemView.findViewbyId(R.id.img_off);
            last_msg = itemView.findViewbyId(R.id.last_msg);
        }

    }

    private void lastmessage(String userid,TextView last_msg){
        theLastMessage="default";
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if((chat.getReceiver().equals(firebaseUser.getUis())) && (chat.getSender().equals(userid)) || (chat.getSender().equals(firebaseUser.getUid())) && (chat.getReceiver().equals(firebaseUser.getUid())))
                    {
                        theLastMessage=chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError database Error){

            }
        });

    }
}
