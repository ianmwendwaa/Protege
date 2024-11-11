package com.example.notessqlite.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notessqlite.categories.Category

class CategoriesDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "y.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "sthe"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FOLDER_NAME = "x_name"
        private const val COLUMN_DATE_OF_MODIFICATION = "date_of_modification"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_FOLDER_NAME TEXT,$COLUMN_DATE_OF_MODIFICATION TEXT)"
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
            put(COLUMN_DATE_OF_MODIFICATION,category.dateModified)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }
//    it's literally in the function lil Timmy
    fun retrieveFolders(): MutableList<Category>{
        val categoryList = mutableListOf<Category>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_NAME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_MODIFICATION))
            val category = Category(id,name,date)
            categoryList.add(category)
        }
        cursor.close()
        db.close()
        return categoryList
    }
    fun deleteFolder(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getFolderById(folderId: Int): Category {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $folderId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_NAME))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_MODIFICATION))

        cursor.close()
        db.close()
        return Category(id, name, date)
    }
}