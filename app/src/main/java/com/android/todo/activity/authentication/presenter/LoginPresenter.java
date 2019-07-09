package com.android.todo.activity.authentication.presenter;

import com.android.todo.model.dto.UserDTO;
import com.android.todo.network.NetworkRouter;
import com.android.todo.utils.Validation;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LoginPresenter {

    private LoginDelegate view;

    public LoginPresenter(LoginDelegate view) {
        this.view = view;
    }

    public void validateEmailAddress(String emailAddress) {
        if (Validation.isValidEmailAddress(emailAddress)) {
            view.validateEmailSuccess();
        } else {
            view.validateEmailFailed();
        }
    }

    public void validatePassword(String password) {
        if (Validation.isValidPassword(password)) {
            view.validatePasswordSuccess();
        } else {
            view.validatePasswordFailed();
        }
    }

    public void login(String email, String password) {
        if (Validation.isValidEmailAddress(email) &&
                Validation.isValidPassword(password)) {
            view.showProgressBar(true);

            AndroidNetworking.post(NetworkRouter.buildURLRequest("login"))
                    .addQueryParameter("email", email)
                    .addQueryParameter("password", password)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            UserDTO user = new Gson().fromJson(response.toString(), UserDTO.class);
                            view.loginSuccess(user);
                            view.showProgressBar(false);
                            view.redirectHomeScreen();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            view.showProgressBar(false);
                            view.showToast("Incorrect Credentials");
                        }
                    });
        } else {
            view.showToast("Incorrect Credentials");
        }
    }
}
