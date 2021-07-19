package sckdn.lisa.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import sckdn.lisa.model.UserDetailResponse;

public class UserViewModel extends ViewModel {

    private MutableLiveData<UserDetailResponse> user;

    public MutableLiveData<UserDetailResponse> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }
}
