package com.example.notessqlite.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notessqlite.user_passwords.Password

class PasswordDatabase(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null, DATABASE_VERSION
){
    companion object{
        private const val DATABASE_NAME = "tester.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "xxx"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PASSWORD_DEFINED = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_PASSWORD_DEFINED TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun createPassword(pass: Password){
        val db = writableDatabase
        val value = ContentValues().apply {
            put(COLUMN_PASSWORD_DEFINED, pass.password)
        }
        db.insert(TABLE_NAME,null,value)
        db.close()
    }
    fun updatePassword(pass: Password){
        val db = writableDatabase
        val value = ContentValues().apply {
            put(COLUMN_PASSWORD_DEFINED,pass.password)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(pass.id.toString())
        db.update(TABLE_NAME,value,whereClause,whereArgs)
        db.close()
    }

}