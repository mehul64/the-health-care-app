package com.thc.app.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.thc.app.BaseActivity;
import com.thc.app.BaseFragment;
import com.thc.app.R;
import com.thc.app.fragments.LoginFragment;
import com.thc.app.fragments.RegisterFragment;

public class LoginActivity extends BaseActivity {


    private BaseFragment currentSelectedFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showLoginFragment();
    }

    public void showRegisterFragment() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        showThisFragment(registerFragment);
    }

    public void showLoginFragment() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        showThisFragment(loginFragment);
    }


    private void showThisFragment(BaseFragment fragment) {
        currentSelectedFragment = fragment;
//        getSupportActionBar().setTitle(fragment.getFragmentTitle());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

}
