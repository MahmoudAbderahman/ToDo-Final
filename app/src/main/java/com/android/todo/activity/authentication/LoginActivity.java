package com.android.todo.activity.authentication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.todo.R;
import com.android.todo.activity.authentication.presenter.LoginDelegate;
import com.android.todo.activity.authentication.presenter.LoginPresenter;
import com.android.todo.activity.home.HomeActivity;
import com.android.todo.model.dto.UserDTO;
import com.android.todo.model.sqlite.UsersDatabaseManager;
import com.android.todo.utils.SharedPreferencesUtility;
import com.androidnetworking.AndroidNetworking;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginDelegate {

    private static final String TAG = "LoginActivity";

    // UI references
    private View mLoginFormView;
    private TextInputLayout mEmailTextInput;
    private TextInputLayout mPasswordTextInput;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private TextView mRegisterTextView;
    private TextView mErorrMessageTextView;
    private UsersDatabaseManager usersDatabaseManager;

    // Instance variables
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = findViewById(R.id.login_progress);

        // SQLite data configuration
        usersDatabaseManager = new UsersDatabaseManager(this);
        usersDatabaseManager.open();

        // check if user already logged In
        if (SharedPreferencesUtility.getLoggedStatus(this)) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            initializeViews();
        }

        hideSoftKeyboard();

        AndroidNetworking.initialize(getApplicationContext());
        presenter = new LoginPresenter(this);
    }

    private void initializeViews() {
        // Login form
        mLoginFormView = findViewById(R.id.login_form);
        mLoginFormView.setVisibility(View.VISIBLE);

        // Setup Login form
        mEmailTextInput = findViewById(R.id.email_text_input);
        mPasswordTextInput = findViewById(R.id.password_text_input);
        mEmailEditText = findViewById(R.id.email_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mErorrMessageTextView = findViewById(R.id.error_message_text_view);
        mLoginButton.setOnClickListener(this);

        mEmailEditText.addTextChangedListener(new FormTextWatcher(mEmailEditText));
        mPasswordEditText.addTextChangedListener(new FormTextWatcher(mPasswordEditText));

        String sourceString = "Don't have an account? <font color='#FFFFFF'><b> Sign up </b></font>";
        mRegisterTextView = findViewById(R.id.register_text_view);
        mRegisterTextView.setText(Html.fromHtml(sourceString));
        mRegisterTextView.setOnClickListener(this);
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        mErorrMessageTextView.setText("");
        String emailAddress = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        switch (view.getId()) {
            case R.id.login_button:
                presenter.login(emailAddress, password);
                break;
        }
    }

    @Override
    public void showProgressBar(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void validateEmailSuccess() {
        mEmailTextInput.setErrorEnabled(false);
    }

    @Override
    public void validateEmailFailed() {
        mEmailTextInput.setError(getString(R.string.error_invalid_email));
        requestFocus(mEmailTextInput);
    }

    @Override
    public void validatePasswordSuccess() {
        mPasswordTextInput.setErrorEnabled(false);
    }

    @Override
    public void validatePasswordFailed() {
        mPasswordTextInput.setError(getString(R.string.error_invalid_password));
        requestFocus(mPasswordTextInput);
    }

    @Override
    public void loginSuccess(UserDTO userDTO) {
        // check if it's a new user
        if (!SharedPreferencesUtility.isSaved(this)) {
            // insert static user
            usersDatabaseManager.insert(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
            SharedPreferencesUtility.savedUser(this, true);
        }

        SharedPreferencesUtility.saveUserData(this, userDTO.getUsername(), userDTO.getUserId());
        SharedPreferencesUtility.setLoggedIn(this, true);
    }

    @Override
    public void redirectHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String message) {
        mErorrMessageTextView.setText(message);
//        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private class FormTextWatcher implements TextWatcher {

        private View view;

        private FormTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String emailAddress = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            switch (view.getId()) {
                case R.id.email_edit_text:
                    presenter.validateEmailAddress(emailAddress);
                    break;
                case R.id.password_edit_text:
                    presenter.validatePassword(password);
                    break;
            }
        }
    }
}
