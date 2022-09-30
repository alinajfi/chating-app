package najfi.ali67.ChattingApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import najfi.ali67.ChattingApp.Adapters.FragmentAdapter;
import najfi.ali67.whatsappclone.R;
import najfi.ali67.whatsappclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();


        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Settings:
                Intent intentSettings = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case  R.id.logout:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this,
                        SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.chat_room:
                Intent intentGroup = new Intent(MainActivity.this,
                        GroupChatActivity.class);
                startActivity(intentGroup);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}