package com.itn.terranode.presentation.presenter.login.login_screen;

import com.google.gson.Gson;
import com.itn.terranode.data.network.dtos.DetailMessageErrorResponse;
import com.itn.terranode.data.network.dtos.LoginSuccessResponse;
import com.itn.terranode.di.app.App;
import com.itn.terranode.domain.login.login_screen.LoginInteractor;
import com.itn.terranode.presentation.view.login.login_screen.LoginScreenView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import okhttp3.ResponseBody;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginScreenView> {

    private final CompositeDisposable compositeDisposable;

    @Inject
    LoginInteractor interactor;

    public LoginPresenter() {
        compositeDisposable = new CompositeDisposable();
        App.getInstance().plusLoginComponent().inject(this);
    }

    public void login(String email, String password){
        compositeDisposable.add(
                interactor
                        .login(email, password)
                        .subscribe(response -> {
                                    switch (response.code()){
                                        case 400:{
                                            ResponseBody responseBody = response.errorBody();
                                            DetailMessageErrorResponse errorResponse = new Gson().fromJson(responseBody.string(), DetailMessageErrorResponse.class);
                                            break;
                                        }
                                        case 200:{
                                            LoginSuccessResponse successResponse = new Gson().fromJson(response.body().toString(), LoginSuccessResponse.class);
                                            saveToken(successResponse.getData().getAccessToken());
                                            getViewState().showMainActivity();
                                            break;
                                        }
                                        default:{
                                            showMessage(response.message());
                                        }
                                    }
                                },
                                throwable -> {
                                    showMessage(throwable.getMessage());
                                },
                                () -> {
                                    showMessage("Try to login later");
                                }
                        )
        );
    }

    private void saveToken(String accessToken) {
        compositeDisposable.add(interactor.saveToken(accessToken).subscribe(() -> {}, throwable -> {}));
    }

    private void showMessage(String message) {
        getViewState().showToast(message);
    }

    public void destroy() {
        compositeDisposable.clear();
        App.getInstance().clearLoginComponent();
    }
}