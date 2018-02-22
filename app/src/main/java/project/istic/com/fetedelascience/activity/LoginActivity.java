package project.istic.com.fetedelascience.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.util.UIHelper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.login_input_email)
    EditText mInputEmail;

    @BindView(R.id.login_input_password)
    EditText mInputPassword;

    @BindView(R.id.login_btn_signin_default)
    Button btnSubmit;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setTitle(getString(R.string.login_activity_title));
        if(getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mInputEmail.setText("admin@app.com");
        mInputPassword.setText("123456");

        mAuth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(v -> {
            String email = mInputEmail.getText().toString();
            String password = mInputPassword.getText().toString();
            if (!email.equals("") && !password.equals("")) {
                signIn(email, password);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Try to sign in user from email & password
     * @param email Email
     * @param password Password
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail:success");
                finish();
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                UIHelper.showSnackbar(findViewById(android.R.id.content), getApplicationContext(), getString(R.string.login_activity_error_login));
            }
        });
    }

}
