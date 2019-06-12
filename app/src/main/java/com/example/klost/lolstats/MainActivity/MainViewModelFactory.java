package com.example.klost.lolstats.MainActivity;


import java.net.URL;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.klost.lolstats.MainActivity.MainViewModel;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final URL url;

    public MainViewModelFactory(URL url){
        this.url = url;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(url);
    }

}
