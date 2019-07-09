package com.android.todo.activity.authentication.presenter;

import com.android.todo.model.dto.UserDTO;

public interface LoginDelegate {

    public abstract void showProgressBar(final boolean show);

    public abstract void validateEmailSuccess();

    public abstract void validateEmailFailed();

    public abstract void validatePasswordSuccess();

    public abstract void validatePasswordFailed();

    public abstract void loginSuccess(UserDTO userDTO);

    public abstract void redirectHomeScreen();

    public abstract void showToast(String message);
}