package com.kailang.billbook;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CountDao {
    @Insert
    void insertCount(Count...counts);

    @Update
    void updateCount(Count... Count);

    @Delete
    void deleteCount(Count... Count);

    @Query("SELECT * FROM Count ORDER BY ID DESC")
    LiveData<List<Count>> getAllCountLive();

    @Query("SELECT * FROM COUNT WHERE id=:id")
    Count getCountById(int id);

    @Query("SELECT * FROM COUNT WHERE type=:type")
    List<Count> getCountByType(int type);

    //搜索数据库，范围包括标题和内容
    @Query("SELECT * FROM COUNT WHERE describe LIKE :pattern ORDER BY ID DESC")
    LiveData<List<Count>>findCountWithPattern(String pattern);
}
