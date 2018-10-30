package com.alexandreseneviratne.roomdbtest.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alexandreseneviratne.roomdbtest.database.entity.Contact;

/**
 * Created by Alexandre SENEVIRATNE on 30/10/2018.
 */
@Database(entities = {Contact.class}, version = 1)
public abstract class ContactAppDatabase extends RoomDatabase {
    public abstract ContactDAO getContactDao();
}
