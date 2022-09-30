package najfi.ali67.ChattingApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import najfi.ali67.ChattingApp.Models.Users;
import najfi.ali67.whatsappclone.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    // auth is used for getting email and password for signing up
    private FirebaseAuth auth;
    // Database is used for getting values from edit text and can save them in firebase
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Dialogue");
        progressDialog.setMessage("Creating your Dialouge");


        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),
                        binding.etPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                 Users user = new Users(binding.etUsername.getText().toString(),
                                         binding.etUsername.getText().toString(),
                                         binding.etPassword.getText().toString());

                                 String id = task.getResult().getUser().getUid();
                                 database.getReference().child("Child").child(id).setValue(user);




                                 Toast.makeText(SignupActivity.this,
                                         "User Create in Firebase", Toast.LENGTH_LONG).show();
                             }else {
                                 Toast.makeText(SignupActivity.this,
                                         task.getException().getMessage(), Toast.LENGTH_LONG).show();
                             }
                             }
                        });
            }
        });

        binding.tvAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,SignInActivity.class);
                startActivity(intent);

            }
        });





    }
}