package najfi.ali67.ChattingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import najfi.ali67.ChattingApp.Adapters.ChatAdapter;
import najfi.ali67.ChattingApp.Models.MessagesModel;
import najfi.ali67.whatsappclone.R;
import najfi.ali67.whatsappclone.databinding.ActivityGroupChatBinding;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;
    FirebaseDatabase database;
    ChatAdapter adapter;
    List<MessagesModel> messagesModellist;
    FirebaseAuth auth;
    String senderId;
    MessagesModel messagesModel;
    public static final String GROUP_CHAT = "Group Chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.ivGroupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        binding.tvGroupUserName.setText("My Group");

        messagesModellist = new ArrayList<>();
        adapter = new ChatAdapter(messagesModellist);
        binding.rcGroupChatDetail.setAdapter(adapter);
        binding.rcGroupChatDetail.setLayoutManager(new LinearLayoutManager(this));


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        senderId = auth.getUid();


        database.getReference().child(GROUP_CHAT).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModellist.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            messagesModel = dataSnapshot.getValue(MessagesModel.class);
                            messagesModellist.add(messagesModel);
                        }
                      adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GroupChatActivity.this,
                                "DataBase Error", Toast.LENGTH_LONG).show();

                    }
                });

        binding.ivGroupSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.etGroupMessage.getText().toString();
                MessagesModel model = new MessagesModel(senderId,message);
                model.setTimeStamp(new Date().getTime());

                binding.etGroupMessage.setText("");

                database.getReference().child(GROUP_CHAT)
                        .push()
                        .setValue(model).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

            }
        });


    }
}