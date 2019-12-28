package lcwu.fyp.asistio;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Switch;
import android.widget.TextView;

import java.util.jar.Attributes;

import de.hdodenhof.circleimageview.CircleImageView;
import lcwu.fyp.asistio.Director.Helpers;
import lcwu.fyp.asistio.Director.Session;
import lcwu.fyp.asistio.model.User;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Helpers helpers;
    private User user;
    private Session session;
    private CircleImageView profile_image;
    private TextView profile_name;
    private TextView profile_email;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        helpers = new Helpers();
        session = new Session(Dashboard.this);
        user = session.getUser();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

       profile_image = header.findViewById(R.id.profile_image);
       profile_name = header.findViewById(R.id.profile_name);
       profile_email = header.findViewById(R.id.profile_email);

       profile_name.setText(user.getFirst_Name()+ " "+ user.getLast_Name());
       profile_email.setText(user.getEmail());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.nav_home:{
                break;
            }
            case R.id.nav_autoreply:{
                break;
            }
            case R.id.nav_smssch:{
                break;
            }

            case R.id.nav_speech:{
                break;
            }
            case R.id.nav_logout:{
                break;
            }



        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
