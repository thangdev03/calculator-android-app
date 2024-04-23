package com.example.calculatorapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "answerCalculated")
    public double answerCalculated;
}
