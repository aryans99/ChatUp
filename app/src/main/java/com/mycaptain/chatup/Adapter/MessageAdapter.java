package com.mycaptain.chatup.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mycaptain.chatup.Fragments.Model.Chat;
import com.mycaptain.chatup.Fragments.Model.User;
import com.mycaptain.chatup.R;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    static final int MSG_TYPE_LEFT=0;
    static final int MSD_TYPE_LEFT=1;

    Context mContext;
    List<Chat> mchat;
    String imageURL;
    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mchat, String imageURL) {
        this.mContext = mContext;
        this.mchat = mchat;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        Chat chat= mchat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }
        if(position==mchat.size()-1){
            if(chat.isSeen()){
                holder.text_seen.setText("seen");
            }else{
                holder.text_seen.setText("delivered");
            }
        }else{
            holder.text_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){
        return mchat.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_message;
        ImageView profile_image;
        TextView text_seen;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            text_seen=itemView.findViewById(R.id.text_seen);

        }
    }

    @Override
    public int getItemViewType(int position){
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSD_TYPE_LEFT;
        }
    }
}
