package com.kailang.billbook;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CountViewModel extends AndroidViewModel {
    private CountRepository countRepository;

    public CountViewModel(@NonNull Application application) {
        super(application);
        countRepository = new CountRepository(application);
    }

    public LiveData<List<Count>> getAllCountsLive() {
        return countRepository.getAllCountsLive();
    }


    public LiveData<List<Count>> findCountWithPattern(String pattern) {
        return countRepository.findCountWithPattern(pattern);
    }

    public List<Count> getCountByType(int type){
        return countRepository.getCountByType(type);
    }
    public void insertCount(Count... counts) {
        countRepository.insertCount(counts);
    }

    public void updateCount(Count... counts) {
        countRepository.updateCount(counts);
    }

    public void deleteCount(Count... counts) {
        countRepository.deleteCount(counts);
    }

    public Count getCountById(int id) {
        return countRepository.getCountById(id);
    }
}
