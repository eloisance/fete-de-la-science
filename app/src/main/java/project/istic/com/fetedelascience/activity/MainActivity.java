package project.istic.com.fetedelascience.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.fragment.ListviewFragment;
import project.istic.com.fetedelascience.global.Constants;
import project.istic.com.fetedelascience.global.PrefManager;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PrefManager prefManager;

    private AppBarLayout appBar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBar = (AppBarLayout) findViewById(R.id.appBar);

        // Init FloatingActionButton
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navdrawer_open, R.string.navdrawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        }


        DBManager.init(this);
        DBManager manager = DBManager.getInstance();

        List<Event> events = manager.getAllEvents();
        if (events.size() > 0) {
            System.out.println("total: " + events.size());
        } else {
            System.out.println("debug: empty");
        }

        // Default fragment
        Fragment listviewFragment = new ListviewFragment();
        setFragment(listviewFragment, "Liste");
        this.onNavigationItemSelected(navigationView.getMenu().getItem(0));

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
                setFragment(new ListviewFragment(), (String) item.getTitle());
                break;
            case R.id.drawer_menu_map:
                openActivity(MapActivity.class, Constants.DELAY_OPEN_ACTIVITY_FROM_NAV_DRAWER);
                break;
            case R.id.drawer_menu_route:
                setFragment(new ListviewFragment(), (String) item.getTitle());
                break;
            case R.id.drawer_menu_settings:
                setFragment(new ListviewFragment(), (String) item.getTitle());
                break;
            default:
                break;
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
