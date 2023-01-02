package com.example.kys

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.kys.model.ConferenceListModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    private val firestoreDb = Firebase.firestore

    override fun onCreate(db: SQLiteDatabase) {

        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                PROFILE_ID + " INTEGER NOT NULL," +
                CONFERENCE_NAME + " TEXT," +
                CONFERENCE_TITLE + " TEXT," +
                MAIL + " TEXT," +
                CONFERENCE_DATE + " TEXT," +
                CONFERENCE_TIME + " TEXT," +
                CONFERENCE_DURATION + " TEXT," +
                ESTIMATED_CALLERS + " TEXT," +
                CONFERENCE_TYPE + " TEXT," +
                ONLINE_LINK + " TEXT," +
                CONFERENCE_ADDRESS + " TEXT," +
                CREATE_DATE + " TEXT," +
                CREATE_TIME + " TEXT," +
                "FOREIGN KEY (" + PROFILE_ID + ")" +
                "REFERENCES " + TABLE_NAME1 + "(" + ID_COL + ")" +
                ")")

        val query1 = ("CREATE TABLE " + TABLE_NAME1 + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                USERNAME + " TEXT," +
                PROFILE_PHOTO + " BLOB," +
                CREATE_DATE + " TEXT," +
                CREATE_TIME + " TEXT," +
                USER_UID + " TEXT" +
                ")")

        val query2 = ("CREATE TABLE " + TABLE_NAME2 + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                PROFILE_ID + " INTEGER NOT NULL," +
                CONFERENCE_ID + " INTEGER NOT NULL," +
                CREATE_DATE + " TEXT," +
                CREATE_TIME + " TEXT," +
                "FOREIGN KEY (" + PROFILE_ID + ")" +
                "REFERENCES " + TABLE_NAME1 + "(" + ID_COL + ")," +
                "FOREIGN KEY (" + CONFERENCE_ID + ")" +
                "REFERENCES " + TABLE_NAME + "(" + ID_COL + ")" +
                ")")

        db.execSQL(query1)
        db.execSQL(query)
        db.execSQL(query2)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2)
        onCreate(db)
    }


    // CONFERENCE ACTIONS
    fun addConference(conferences: ConferenceListModel): Boolean {
        // Firestore
        val conference = hashMapOf(
            ID_COL to conferences.id,
            PROFILE_ID to conferences.profile_id,
            CONFERENCE_NAME to conferences.conference_name,
            CONFERENCE_TITLE to conferences.conference_title,
            MAIL to conferences.mail,
            CONFERENCE_DATE to conferences.conference_date,
            CONFERENCE_TIME to conferences.conference_time,
            CONFERENCE_DURATION to conferences.conference_duration,
            ESTIMATED_CALLERS to conferences.estimated_callers,
            CONFERENCE_TYPE to conferences.conference_type,
            ONLINE_LINK to conferences.online_link,
            CONFERENCE_ADDRESS to conferences.conference_address,
            CREATE_DATE to conferences.create_date,
            CREATE_TIME to conferences.create_time
        )

        firestoreDb.collection("conference").document(conferences.id.toString())
            .set(conference)
            .addOnSuccessListener { Log.d("Firestore", "Döküman başarıyla yazdırıldı!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Dökümanı yazarken hata!", e) }

        val values = ContentValues()
        values.put(PROFILE_ID, conferences.profile_id)
        values.put(CONFERENCE_NAME, conferences.conference_name)
        values.put(CONFERENCE_TITLE, conferences.conference_title)
        values.put(MAIL, conferences.mail)
        values.put(CONFERENCE_DATE, conferences.conference_date)
        values.put(CONFERENCE_TIME, conferences.conference_time)
        values.put(CONFERENCE_DURATION, conferences.conference_duration)
        values.put(ESTIMATED_CALLERS, conferences.estimated_callers)
        values.put(CONFERENCE_TYPE, conferences.conference_type)
        values.put(ONLINE_LINK, conferences.online_link)
        values.put(CONFERENCE_ADDRESS, conferences.conference_address)
        values.put(CREATE_DATE, conferences.create_date)
        values.put(CREATE_TIME, conferences.create_time)

        val db = this.writableDatabase

        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun updateConference(conferences: ConferenceListModel): Boolean {
        val conference = hashMapOf(
            PROFILE_ID to conferences.profile_id,
            CONFERENCE_NAME to conferences.conference_name,
            CONFERENCE_TITLE to conferences.conference_title,
            MAIL to conferences.mail,
            CONFERENCE_DATE to conferences.conference_date,
            CONFERENCE_TIME to conferences.conference_time,
            CONFERENCE_DURATION to conferences.conference_duration,
            ESTIMATED_CALLERS to conferences.estimated_callers,
            CONFERENCE_TYPE to conferences.conference_type,
            ONLINE_LINK to conferences.online_link,
            CONFERENCE_ADDRESS to conferences.conference_address,
            CREATE_DATE to conferences.create_date,
            CREATE_TIME to conferences.create_time
        )

        firestoreDb.collection("conference").document(conferences.id.toString())
            .set(conference)
            .addOnSuccessListener { Log.d("Firestore", "Döküman başarıyla yazdırıldı!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Dökümanı yazarken hata!", e) }

        val db = this.writableDatabase
        val values = ContentValues()

        values.put(PROFILE_ID, conferences.profile_id)
        values.put(CONFERENCE_NAME, conferences.conference_name)
        values.put(CONFERENCE_TITLE, conferences.conference_title)
        values.put(MAIL, conferences.mail)
        values.put(CONFERENCE_DATE, conferences.conference_date)
        values.put(CONFERENCE_TIME, conferences.conference_time)
        values.put(CONFERENCE_DURATION, conferences.conference_duration)
        values.put(ESTIMATED_CALLERS, conferences.estimated_callers)
        values.put(CONFERENCE_TYPE, conferences.conference_type)
        values.put(ONLINE_LINK, conferences.online_link)
        values.put(CONFERENCE_ADDRESS, conferences.conference_address)
        values.put(CREATE_DATE, conferences.create_date)
        values.put(CREATE_TIME, conferences.create_time)

        val _success =
            db.update(TABLE_NAME, values, ID_COL + "=?", arrayOf(conferences.id.toString()))
                .toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }


    @SuppressLint("Range")
    fun getAllConference(): List<ConferenceListModel> {
        val conference_list = ArrayList<ConferenceListModel>()
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val conferences = ConferenceListModel()
                    conferences.id =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COL)))
                    conferences.profile_id = Integer.parseInt(
                        cursor.getString(
                            cursor.getColumnIndex(
                                PROFILE_ID
                            )
                        )
                    )
                    conferences.conference_name = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_NAME
                        )
                    )
                    conferences.conference_title = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_TITLE
                        )
                    )
                    conferences.mail = cursor.getString(
                        cursor.getColumnIndex(
                            MAIL
                        )
                    )
                    conferences.conference_date = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_DATE
                        )
                    )
                    conferences.conference_time = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_TIME
                        )
                    )
                    conferences.conference_duration = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_DURATION
                        )
                    )
                    conferences.estimated_callers = cursor.getString(
                        cursor.getColumnIndex(
                            ESTIMATED_CALLERS
                        )
                    )
                    conferences.conference_type = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_TYPE
                        )
                    )
                    conferences.online_link = cursor.getString(
                        cursor.getColumnIndex(
                            ONLINE_LINK
                        )
                    )
                    conferences.conference_address = cursor.getString(
                        cursor.getColumnIndex(
                            CONFERENCE_ADDRESS
                        )
                    )
                    conferences.create_date = cursor.getString(
                        cursor.getColumnIndex(
                            CREATE_DATE
                        )
                    )
                    conferences.create_time = cursor.getString(
                        cursor.getColumnIndex(
                            CREATE_TIME
                        )
                    )

                    conference_list.add(conferences)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return conference_list
    }

    @SuppressLint("Range")
    fun getConference(_id: Int): ConferenceListModel {
        val conferences = ConferenceListModel()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID_COL = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        conferences.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID_COL)))
        conferences.profile_id = Integer.parseInt(
            cursor.getString(
                cursor.getColumnIndex(
                    PROFILE_ID
                )
            )
        )
        conferences.conference_name = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_NAME
            )
        )
        conferences.conference_title = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_TITLE
            )
        )
        conferences.mail = cursor.getString(
            cursor.getColumnIndex(
                MAIL
            )
        )
        conferences.conference_date = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_DATE
            )
        )
        conferences.conference_time = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_TIME
            )
        )
        conferences.conference_duration = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_DURATION
            )
        )
        conferences.estimated_callers = cursor.getString(
            cursor.getColumnIndex(
                ESTIMATED_CALLERS
            )
        )
        conferences.conference_type = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_TYPE
            )
        )
        conferences.online_link = cursor.getString(
            cursor.getColumnIndex(
                ONLINE_LINK
            )
        )
        conferences.conference_address = cursor.getString(
            cursor.getColumnIndex(
                CONFERENCE_ADDRESS
            )
        )
        conferences.create_date = cursor.getString(
            cursor.getColumnIndex(
                CREATE_DATE
            )
        )
        conferences.create_time = cursor.getString(
            cursor.getColumnIndex(
                CREATE_TIME
            )
        )
        cursor.close()
        return conferences

    }


    @SuppressLint("Range")
    fun deleteConference(_id: Int): Boolean {

        firestoreDb.collection("conference").document(_id.toString())
            .delete()
            .addOnSuccessListener { Log.d("Firestore", "Dosya başarıyla silindi!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Dosya silinirken hata!", e) }

        val db = this.writableDatabase

        val _success = db.delete(TABLE_NAME, "id=?", arrayOf(_id.toString())).toLong()

        db.close()
        return Integer.parseInt("$_success") != -1
    }

    // PROFILE ACTIONS
    fun getProfile(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME1, null)

    }

    fun addProfile(
        username: String,
        profile_photo: String,
        create_date: String,
        create_time: String,
        user_uid: String
    ) {

        val values = ContentValues()

        values.put(USERNAME, username)
        values.put(PROFILE_PHOTO, profile_photo)
        values.put(CREATE_DATE, create_date)
        values.put(CREATE_TIME, create_time)
        values.put(USER_UID, user_uid)

        val db = this.writableDatabase

        db.insert(TABLE_NAME1, null, values)

    }

    fun addConfAttend(profile_id: Int, conference_id: Int) {
        val values = ContentValues()

        values.put(PROFILE_ID, profile_id)
        values.put(CONFERENCE_ID, conference_id)

        val db = this.writableDatabase

        db.insert(TABLE_NAME2, null, values)
    }


    companion object {
        private val DATABASE_NAME = "KYS"

        private val DATABASE_VERSION = 6

        val TABLE_NAME = "conference"
        val TABLE_NAME1 = "profile"
        val TABLE_NAME2 = "attended_conferences"

        val ID_COL = "id"
        val PROFILE_ID = "profile_id"
        val CONFERENCE_ID = "conference_id"

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
        val USER_UID = "user_uid"
    }
}