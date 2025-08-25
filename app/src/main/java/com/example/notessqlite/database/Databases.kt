//This file contains all databases in the app

package com.example.notessqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notessqlite.Birthday
import com.example.notessqlite.database.NoteDatabase.Companion.COLUMN_CONTENT
import com.example.notessqlite.database.NoteDatabase.Companion.COLUMN_DATE
import com.example.notessqlite.database.NoteDatabase.Companion.COLUMN_TITLE
import com.example.notessqlite.folders.Category
import com.example.notessqlite.notes.Note
import com.example.notessqlite.relationships.Relationship
import com.example.notessqlite.todo.ToDo
import com.example.notessqlite.user_passwords.Password
import java.util.concurrent.TimeUnit

class NoteDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "all_my_notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?){
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DATE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_TITLE TEXT DEFAULT 'General'")
            db?.execSQL("ALTER TABLE all_my_notes RENAME TO all_of_my_notes");
            // Create the new table.
            db?.execSQL("CREATE TABLE all_my_notes (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, date DATE)")
            // Copy data to the new table.
            db?.execSQL("INSERT INTO all_my_notes(id, title, content, date) SELECT id, title, content, date FROM all_of_my_notes")
            // Drop the temporary table.
            db?.execSQL("DROP TABLE all_of_my_notes")
        }
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.time)
        }
        db.insert(TABLE_NAME, null,values)
        db.close()
    }


    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.time)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
    fun getAllNotes(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val note = Note(id, title, content, time)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun getNoteById(noteId: Int) : Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

        cursor.close()
        db.close()
        return Note(id, title, content, time)
    }

    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun searchNote(searchTerm:String):MutableList<Note>{
        val noteEntities = mutableListOf<Note>()
        val searchQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE LIKE ?"
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

class TrashDatabase(context:Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "trash.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "trash"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
        private val EXPIRATION_TIMELAPSE = TimeUnit.DAYS.toMillis(7)
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_NAME TEXT,$COLUMN_CONTENT TEXT,$COLUMN_DATE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME "
        db?.execSQL(dropTableQuery)
    }
    fun addNoteIntoTrash(note: Note){
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME,note.title)
            put(COLUMN_CONTENT,note.content)
            put(COLUMN_DATE,note.time)
        }
        db.insert(TABLE_NAME,null,contentValues)
        db.close()
    }
    fun getAllDisposedTrash():MutableList<Note>{
        val db = readableDatabase
        val disposedList = mutableListOf<Note>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val disposedNote = Note(id,title, content,date)
            disposedList.add(disposedNote)
        }
        cursor.close()
        db.close()
        return disposedList
    }
    fun restoreNoteFromTrash(noteId:Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause, whereArgs)
        db.close()
    }
    fun destroyNotes(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
}

class EntryDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "entry.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "all_my_entries"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?){
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DATE TEXT, UNIQUE($COLUMN_TITLE))"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values1 = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.time)
        }
        db.insert(TABLE_NAME, null, values1)
    }

    fun updateNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.time)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getAllEntries(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val uniqueNotes = HashSet<Pair<String,String>>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val note = Note(id, title, content, time)
            val titleContentPair = Pair(title,content)
            if (!uniqueNotes.contains(titleContentPair)){
                notesList.add(note)
                uniqueNotes.add(titleContentPair)
            }
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun getNoteById(noteId: Int) : Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

        cursor.close()
        db.close()
        return Note(id, title, content, time)
    }

    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}

class ToDoDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "todo.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "todoTable"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DATE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertToDo(todo: ToDo){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, todo.taskName)
            put(COLUMN_CONTENT, todo.taskDescription)
            put(COLUMN_DATE, todo.time)
        }
        db.insert(TABLE_NAME, null,values)
        db.close()
    }

    fun updateToDo(toDo: ToDo){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE,toDo.taskName)
            put(COLUMN_CONTENT,toDo.taskDescription)
            put(COLUMN_DATE,toDo.time)
        }
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(toDo.id.toString())
        db.update(TABLE_NAME,values,whereClause, whereArgs)
        db.close()
    }
    fun getAllToDos(): MutableList<ToDo> {
        val todoList = mutableListOf<ToDo>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        try{
            while (cursor.moveToNext()){
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

                val todo = ToDo(id, title, content, date)
                todoList.add(todo)
            }
        }finally {
            if(cursor!=null){
                cursor.close()
            }
            db?.close()
        }
        return todoList
    }
    fun deleteToDo(todoId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(todoId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}

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

class PasswordDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){
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
    fun fetchPasswordRequest(): MutableList<Password> {
        val passwordDef = mutableListOf<Password>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val pass = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_DEFINED))


            val password = Password(id, pass)
            passwordDef.add(password)
        }
        cursor.close()
        db.close()
        return passwordDef
    }
}

class CategoriesDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "folder.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "all_folders_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FOLDER_NAME = "name"
        private const val COLUMN_FOLDER_DESCRIPTION = "description"
        private const val COLUMN_DATE_OF_MODIFICATION = "date_of_modification"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_FOLDER_NAME TEXT,$COLUMN_FOLDER_DESCRIPTION TEXT,$COLUMN_DATE_OF_MODIFICATION TEXT)"
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
            put(COLUMN_FOLDER_DESCRIPTION,category.folderDescription)
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
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_DESCRIPTION))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_MODIFICATION))
            val category = Category(id,name,date,description)
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
        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_DESCRIPTION))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_MODIFICATION))

        cursor.close()
        db.close()
        return Category(id, name, date, description)
    }
    fun updateNote(category: Category){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FOLDER_NAME, category.folderName)
            put(COLUMN_FOLDER_DESCRIPTION, category.folderDescription)
            put(COLUMN_DATE_OF_MODIFICATION, category.dateModified)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(category.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
}

class ArchivesDatabase(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "archives.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "all_archived"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_DATE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertArchivedNote(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.time)
        }
        db.insert(TABLE_NAME, null,values)
        db.close()
    }

    fun getArchivedNotes(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val note = Note(id, title, content, time)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }
    fun getNoteById(noteId: Int) : Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

        cursor.close()
        db.close()
        return Note(id, title, content, time)
    }
    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}

class InsertNoteIntoFolderDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "b.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "seth"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT,$COLUMN_CONTENT TEXT,$COLUMN_DATE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
    }

    fun insertIntoFolder(note: Note){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_DATE, note.time)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }
    fun getFolders(): MutableList<Category> {
        val notesList = mutableListOf<Category>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val note = Category(id, title, content, time)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }
    fun getFoldersNotes(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val note = Note(id, title, content, time)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }
    fun deleteFolder(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
    fun updateNoteFolderId(noteId: Int, folderId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("folderId", folderId)
        }
        db.update("Notes", values, "id = ?", arrayOf(noteId.toString()))
        db.close()
    }
    fun deleteNote(noteId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}

class BirthDayDatabase(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "birthdays.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "birthdays"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BIRTHDATE = "birthdate"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_NAME TEXT,$COLUMN_BIRTHDATE TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
    }
    fun createBirthday(birthday: Birthday){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME,birthday.name)
            put(COLUMN_BIRTHDATE,birthday.dob)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }
    fun getBirthdays():MutableList<Birthday>{
        val birthdayList = mutableListOf<Birthday>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val dob = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDATE))

            val birthday = Birthday(id, name, dob)
            birthdayList.add(birthday)
        }
        cursor.close()
        db.close()
        return birthdayList
    }
    fun getBirthdayById(birthdayId: Int){
        val db = readableDatabase
        
    }
    fun updateBirthday(birthday: Birthday){
        val db = writableDatabase
        val newValues = ContentValues().apply {
            put(COLUMN_NAME,birthday.name)
            put(COLUMN_BIRTHDATE,birthday.dob)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(birthday.id.toString())
        db.update(TABLE_NAME,newValues,whereClause, whereArgs)
        db.close()
    }
    fun deleteBirthday(birthdayId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(birthdayId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
}