package project.istic.com.fetedelascience.global;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private SharedPreferences pref;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(Constants.PREF_FILE_NAME, Constants.PREF_MODE);
    }

    /**
     * Set boolean is first time launch application to false
     */
    public void setFirstTimeLaunchToFalse() {
        pref.edit().putBoolean(Constants.PREF_KEY_FIRST_LAUNCH, false).apply();
    }

    /**
     * Get boolean is first time app is launch
     * @return boolean
     */
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(Constants.PREF_KEY_FIRST_LAUNCH, Constants.PREF_DEFAULT_VALUE_FIRST_LAUNCH);
    }
}