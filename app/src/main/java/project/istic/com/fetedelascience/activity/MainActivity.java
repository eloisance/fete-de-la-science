package project.istic.com.fetedelascience.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.fragment.EventListviewFragment;
import project.istic.com.fetedelascience.fragment.ParcoursListViewFragment;
import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.global.PrefManager;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.util.UIHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    TextView headerEmail;

    TextView headerName;

    CircleImageView headerPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DBManager.init(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            Menu menu = navigationView.getMenu();
            MenuItem navAdmin = menu.findItem(R.id.drawer_menu_admin);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                navAdmin.setTitle(R.string.navdrawer_logout);
                headerName.setText(R.string.navdrawer_default_header_email);
                headerEmail.setText(user.getEmail());
                UIHelper.showSnackbar(findViewById(android.R.id.content), getApplicationContext(), getString(R.string.login_activity_connected), "OK");
            } else {
                Log.d(TAG,"onAuthStateChanged:signed_out");
                navAdmin.setTitle(R.string.navdrawer_admin);
                headerName.setText("");
                headerEmail.setText(R.string.navdrawer_default_header_name);
                UIHelper.showSnackbar(findViewById(android.R.id.content), getApplicationContext(), getString(R.string.login_activity_disconnected), "OK");
            }
        });

        // Init FloatingActionButton
        if(fab != null) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_24dp));

        }

        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navdrawer_open, R.string.navdrawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        if (navigationView != null) {
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            headerEmail = headerView.findViewById(R.id.nav_header_email);
            headerName = headerView.findViewById(R.id.nav_header_name);
            headerPhoto = headerView.findViewById(R.id.nav_header_photo);navigationView.setNavigationItemSelectedListener(this);
            headerName.setText(getString(R.string.navdrawer_default_header_name));
            headerEmail.setText(getString(R.string.navdrawer_default_header_email));
            headerPhoto.setImageBitmap(null);
        }

        // Default fragment
        Fragment listviewFragment = new EventListviewFragment();
        setFragment(listviewFragment, "Liste");
        this.onNavigationItemSelected(navigationView.getMenu().getItem(0));

    }

    @OnClick(R.id.fab)
    public void fabOnClick(){
        openActivity(CreateParcours.class,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Prepare new fragment
     * @param fragment New fragment to display
     * @param title Title of new fragment
     */
    public void setFragment(Fragment fragment, String title) {
        setTitle(title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_menu_liste:
                setFragment(new EventListviewFragment(), (String) item.getTitle());
                break;
            case R.id.drawer_menu_map:
                openActivity(MapActivity.class, Constants.DELAY_OPEN_ACTIVITY_FROM_NAV_DRAWER);
                break;
            case R.id.drawer_menu_route:
                setFragment(new ParcoursListViewFragment(), (String) item.getTitle());
                break;
            case R.id.drawer_menu_stats:
                openActivity(StatsActivity.class, Constants.DELAY_OPEN_ACTIVITY_FROM_NAV_DRAWER);
                break;
            case R.id.drawer_menu_admin:
                if (mAuth.getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                } else {
                    openActivity(LoginActivity.class, Constants.DELAY_OPEN_ACTIVITY_FROM_NAV_DRAWER);
                }
                break;
            case R.id.drawer_menu_settings:
                openActivity(SettingsActivity.class, Constants.DELAY_OPEN_ACTIVITY_FROM_NAV_DRAWER);
                break;
            default:
                break;
        }

        // FAB
        if (item.getItemId() == R.id.drawer_menu_route) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }

        // Close drawer
        if (this.drawer != null) {
            this.drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    /**
     * Ouvre l'activity passé en paramètre
     * @param activity à ouvrir
     * @param delay temps à attendre en ms avant d'ouvrir l'activity
     */
    private void openActivity(final Class<? extends Activity> activity, final Integer delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, activity);
                startActivity(intent);
            }
        }, delay);
    }

    @Override
    public void onBackPressed() {
        if (this.drawer != null) {
            if (this.drawer.isDrawerOpen(GravityCompat.START)) {
                this.drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }
}
