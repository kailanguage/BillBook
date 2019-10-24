package com.kailang.billbook;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CountRepository {
    private LiveData<List<Count>> allCountLive;
    private CountDao countDao;

    public CountRepository(Context context) {
        CountDatabase countDatabase = CountDatabase.getDatabase(context.getApplicationContext());
        countDao = countDatabase.getCountDao();
        allCountLive = countDao.getAllCountLive();
    }


    public void insertCount(Count... counts) {
        new InsertAsyncTask(countDao).execute(counts);
    }

    public void updateCount(Count... counts) {
        new UpdateAsyncTask(countDao).execute(counts);
    }

    public void deleteCount(Count... counts) {
        new DeleteAsyncTask(countDao).execute(counts);
    }

    public LiveData<List<Count>> getAllCountsLive() {
        return allCountLive;
    }

    public List<Count> getCountByType(int type){
        return countDao.getCountByType(type);
    }

    public LiveData<List<Count>> findCountWithPattern(String pattern) {
        return countDao.findCountWithPattern("%" + pattern + "%");
    }

    public Count getCountById(int id) {
        return countDao.getCountById(id);
    }

    static class InsertAsyncTask extends AsyncTask<Count, Void, Void> {
        private CountDao countDao;

        public InsertAsyncTask(CountDao countDao) {
            this.countDao = countDao;
        }

        @Override
        protected Void doInBackground(Count... counts) {
            countDao.insertCount(counts);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Count, Void, Void> {
        private CountDao countDao;

        public UpdateAsyncTask(CountDao countDao) {
            this.countDao = countDao;
        }

        @Override
        protected Void doInBackground(Count... counts) {
            countDao.updateCount(counts);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Count, Void, Void> {
        private CountDao countDao;

        public DeleteAsyncTask(CountDao countDao) {
            this.countDao = countDao;
        }

        @Override
        protected Void doInBackground(Count... counts) {
            countDao.deleteCount(counts);
            return null;
        }
    }
}
