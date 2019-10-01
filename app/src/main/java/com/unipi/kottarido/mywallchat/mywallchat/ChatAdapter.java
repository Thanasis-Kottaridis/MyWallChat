package com.unipi.kottarido.mywallchat.mywallchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private List<Message> myMessages;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //ftiaxnoume ena view to opoio tha kanei inflate to chat item pou ftia3ame
        //to opoio to pernaei san orisma ston chat holder
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_item,viewGroup,false);
        return new ChatHolder(itemView);
    }


    //dinei timi sta instances tou ChatHolder gia kathe item tis listas myMessage
    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int i) {
        Message message = myMessages.get(i);
        holder.Username.setText(message.getUsername());
        holder.Message.setText(message.getMessage());
    }

    //epistreufei to sizw tis my message
    @Override
    public int getItemCount() {
        return myMessages.size();
    }

    //constructor tou ChatAdapter gia na tou pernaw to chat list
    public ChatAdapter(List<Message> myMessages){
        this.myMessages = myMessages;
    }

    public void setMyMessages(List<Message> myMessages) {
        this.myMessages = myMessages;
    }

    class ChatHolder extends RecyclerView.ViewHolder{

        private TextView Username;
        private TextView Message;

        public ChatHolder(@NonNull View view) {
            super(view);
            Username= view.findViewById(R.id.ChatItemUsername);
            Message = view.findViewById(R.id.ChatItemMessage);

            //ftiaxnw onClickEvent sto recycle view
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //pernw tin position tou stixiou pou patithike mesa sto recycle view
                    int pos = getAdapterPosition();
                    //elenxw an exei arxikopoiithei o listener
                    //kai an iparxei to position sto Recycle view
                    if (listener != null && pos != RecyclerView.NO_POSITION){
                        //kalei tin onItemClick pernontas to item pou patithike san orisma
                        listener.onItemClick(myMessages.get(pos));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Message message);
    }

        // episis dimiourgoume kai tin methodo setOnItemClickListener
        // i opoia dexete ena instance pou kanei implement to onItemClickListener
        // gia na ipoxreosoume opoion to kalei na kanei implement to interface mas

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener =listener;
        }
}
