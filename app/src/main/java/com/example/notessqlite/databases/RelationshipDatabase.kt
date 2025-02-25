package com.example.notessqlite.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notessqlite.relationships.Relationship
import com.example.notessqlite.notes.Note

class RelationshipDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "relationshi.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "all_rel"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_RELATIONSHIP_STATUS = "relationship_status"
        private const val COLUMN_MORE_INFORMATION = "more_information"
    }

    override fun onCreate(db: SQLiteDatabase?){
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_RELATIONSHIP_STATUS TEXT, $COLUMN_MORE_INFORMATION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun createRelationship(relationship: Relationship){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, relationship.name)
            put(COLUMN_RELATIONSHIP_STATUS, relationship.relationshipStatus)
            put(COLUMN_MORE_INFORMATION, relationship.moreInfo)
        }
        db.insert(TABLE_NAME, null,values)
        db.close()
    }

    fun updateRelationship(relationship: Relationship){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, relationship.name)
            put(COLUMN_RELATIONSHIP_STATUS, relationship.relationshipStatus)
            put(COLUMN_MORE_INFORMATION, relationship.moreInfo)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(relationship.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
    fun getAllRelationships(): MutableList<Relationship> {
        val notesList = mutableListOf<Relationship>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val relationshipStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELATIONSHIP_STATUS))
            val moreInfo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MORE_INFORMATION))

            val note = Relationship(id,name,relationshipStatus, moreInfo)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun getRelationshipById(noteId: Int) : Relationship {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val relationshipStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELATIONSHIP_STATUS))
        val moreInfo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MORE_INFORMATION))

        cursor.close();
        db.close()
        return Relationship(id, name, relationshipStatus, moreInfo)
    }

    fun deleteRelationship(relationshipId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(relationshipId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun searchNote(searchTerm:String):MutableList<Note>{
        val noteEntities = mutableListOf<Note>()
        val searchQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME LIKE ?"
        val selectionArgs = arrayOf("%$searchTerm%")
        val cursor = readableDatabase.rawQuery(searchQuery,selectionArgs)
        while (cursor.moveToNext()){
            val note = Note(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                content = cursor.getString(cursor.getColumnIndexOrThrow("content")),
                time = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            )
            noteEntities.add(note)
        }
        cursor.close()
        return noteEntities
    }
}
