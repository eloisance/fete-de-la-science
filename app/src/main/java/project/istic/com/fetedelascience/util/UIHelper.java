package project.istic.com.fetedelascience.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;


public class UIHelper {

    /**
     * Display simple Snackbar message, if no view display Toast message
     * @param view
     * @param context
     * @param message
     */
    public static void showSnackbar(View view, Context context, String message) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Display simple Snackbar message, if no view display Toast message
     * @param view
     * @param context
     * @param message
     * @param actionName
     */
    public static void showSnackbar(View view, Context context, String message, String actionName) {
        Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setAction(actionName, view1 -> {})
                .show();
    }

}
