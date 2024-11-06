package com.example.notessqlite.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notessqlite.categories.Category

class CategoriesDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = ""
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "categories.db"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FOLDER_NAME = "folder_name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_FOLDER_NAME TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
    }
    fun createFolder(category: Category){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FOLDER_NAME,category.folderName)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }
//    it's literally in the function lil Timmy
    fun retrieveCategories(): List<Category>{

        val categoryList = mutableListOf<Category>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
    while (cursor.moveToNext()){
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_NAME))
        val category = Category(id,name)
        categoryList.add(category)
    }
    cursor.close()
    db.close()
    return categoryList
    }

}