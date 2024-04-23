package com.example.calculatorapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AnswerDAO {
    @Insert
    void insert(Answer answer);

    @Query("SELECT * FROM answer where id = :answerId")
    Answer getById(int answerId);

    @Update
    void update(Answer Answer);

    @Delete
    void delete(Answer answer);
}
