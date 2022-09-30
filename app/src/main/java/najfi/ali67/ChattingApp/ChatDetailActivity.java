package najfi.ali67.ChattingApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import najfi.ali67.ChattingApp.Adapters.ChatAdapter;
import najfi.ali67.ChattingApp.Models.MessagesModel;
import najfi.ali67.whatsappclone.R;
import najfi.ali67.whatsappclone.databinding.ActivityChatDetailBinding;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ChatAdapter adapter;
    List<MessagesModel> messagesModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        messagesModelList = new ArrayList<>();


        adapter = new ChatAdapter(messagesModelList,recieveId);
        binding.rcChatDetail.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rcChatDetail.setLayoutManager(layoutManager);






        binding.tvUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.whatsapp).
                into(binding.ivprofileimage);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });



        final String senderRoom = senderId + recieveId;
        final String receieverRoom =recieveId + senderId;


        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModelList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){

                            MessagesModel model = snapshot1.getValue(MessagesModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messagesModelList.add(model);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatDetailActivity.this, "DataBase Error",
                                Toast.LENGTH_LONG).show();

                    }
                });


        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messsage = binding.etSendMessage.getText().toString();
                final MessagesModel model = new MessagesModel(senderId,messsage);
                model.setTimeStamp(new Date().getTime());
                binding.etSendMessage.setText("");
                Toast.makeText(ChatDetailActivity.this, "Message Sent",
                        Toast.LENGTH_LONG).show();

                database.getReference().child("chats")
                        .child(senderRoom)
                        // To Create node of every message we use push()
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receieverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });

            }
        });

    }
}