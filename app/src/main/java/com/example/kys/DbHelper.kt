package com.example.kys

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                PROFILE_ID + " INTEGER NOT NULL," +
                CONFERENCE_NAME + " TEXT," +
                CONFERENCE_TITLE + " TEXT," +
                MAIL + " TEXT," +
                CONFERENCE_DATE + " TEXT," +
                CONFERENCE_TIME + " TEXT," +
                CONFERENCE_DURATION + " TEXT," +
                ESTIMATED_CALLERS + " INTEGER," +
                CONFERENCE_TYPE + " INTEGER," +
                ONLINE_LINK + " TEXT," +
                CONFERENCE_ADDRESS + " TEXT," +
                CREATE_DATE + " TEXT," +
                CREATE_TIME + " TEXT," +
                "FOREIGN KEY (" + PROFILE_ID + ")" +
                    "REFERENCES " + TABLE_NAME1 + "(" + ID_COL + ")" +
                ")")

        val query1 = ("CREATE TABLE" + TABLE_NAME1 + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                USERNAME + " TEXT," +
                PROFILE_PHOTO + " BLOB," +
                CREATE_DATE + " TEXT," +
                CREATE_TIME + " TEXT" +
                ")")

        db.execSQL(query1)
        db.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1)
        onCreate(db)
    }

    fun addConference(profile_id : Int, conference_name : String, conference_title: String, mail: String, conference_date: String, conference_time: String, conference_duration: String, estimated_callers: Int, conference_type: Int, online_link: String, conference_address: String, create_date: String, create_time: String){

        val values = ContentValues()

        values.put(PROFILE_ID, profile_id)
        values.put(CONFERENCE_NAME, conference_name)
        values.put(CONFERENCE_TITLE, conference_title)
        values.put(MAIL, mail)
        values.put(CONFERENCE_DATE, conference_date)
        values.put(CONFERENCE_TIME, conference_time)
        values.put(CONFERENCE_DURATION, conference_duration)
        values.put(ESTIMATED_CALLERS, estimated_callers)
        values.put(CONFERENCE_TYPE, conference_type)
        values.put(ONLINE_LINK, online_link)
        values.put(CONFERENCE_ADDRESS, conference_address)
        values.put(CREATE_DATE,create_date)
        values.put(CREATE_TIME, create_time)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun addProfile(username : String, profile_photo : String, create_date : String, create_time: String ){

        val values = ContentValues()

        values.put(USERNAME, username)
        values.put(PROFILE_PHOTO, profile_photo)
        values.put(CREATE_DATE, create_date)
        values.put(CREATE_TIME, create_time)

        val db = this.writableDatabase

        db.insert(TABLE_NAME1, null, values)

        db.close()
    }

    fun getConference(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    fun getProfile(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME1, null)

    }

    companion object{
        private val DATABASE_NAME = "KYS"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "conference"
        val TABLE_NAME1 = "profile"

        val ID_COL = "id"
        val PROFILE_ID = "profile_id"


        val CONFERENCE_NAME = "conference_name"
        val CONFERENCE_TITLE = "conference_title"
        val MAIL = "mail"
        val CONFERENCE_DATE = "conference_date"
        val CONFERENCE_TIME = "conference_time"
        val CONFERENCE_DURATION = "conference_duration"
        val ESTIMATED_CALLERS = "estimated_callers"
        val CONFERENCE_TYPE = "conference_type"
        val ONLINE_LINK = "online_link"
        val CONFERENCE_ADDRESS = "coference_address"
        val CREATE_DATE = "create_date"
        val CREATE_TIME = "create_time"

        val USERNAME = "username"
        val PROFILE_PHOTO = "profile_photo"
    }
}