package najfi.ali67.ChattingApp.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import najfi.ali67.ChattingApp.Models.MessagesModel;
import najfi.ali67.whatsappclone.R;

public class ChatAdapter extends RecyclerView.Adapter {

    List<MessagesModel> messagesModel;
    String receiverId;
    public static final int RECEIVER_VIEW_TYPE = 1;
    public static final int SENDER_VIEW_TYPE = 2;

    public ChatAdapter(List<MessagesModel> messagesModel, String receiverId) {
        this.messagesModel = messagesModel;
        this.receiverId = receiverId;
    }

    public ChatAdapter(List<MessagesModel> messagesModel) {
        this.messagesModel = messagesModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView;
        if (viewType == SENDER_VIEW_TYPE) {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(rootView);
        } else {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(rootView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesMode = messagesModel.get(position);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete")
                        .setMessage("Are You sure you want to delete")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() +
                                        receiverId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messagesMode.getMessageId())
                                        .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });


        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder) holder).senderMessage.setText(messagesMode.getMessage());
        } else {
            ((ReceiverViewHolder) holder).receiverMessage.setText(messagesMode.getMessage());
        }


    }


    @Override
    public int getItemViewType(int position) {
        if (messagesModel.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else
            return RECEIVER_VIEW_TYPE;

    }

    @Override
    public int getItemCount() {
        return messagesModel.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMessage, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMessage = itemView.findViewById(R.id.tvMessageReceiver);
            receiverTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.tvSender);
            senderTime = itemView.findViewById(R.id.tvTimeSender);
        }
    }
}
