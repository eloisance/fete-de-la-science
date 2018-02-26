package project.istic.com.fetedelascience.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.util.AppCompatPreferenceActivity;

/**
 * Thanks to -> http://www.androidhive.info/2017/07/android-implementing-preferences-settings-screen/
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.navdrawer_settings));
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment  {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
