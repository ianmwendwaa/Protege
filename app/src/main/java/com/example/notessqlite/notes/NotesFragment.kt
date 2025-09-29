package com.example.notessqlite.notes

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.dialog.DialogBuilder
import com.example.notessqlite.dialog.DialogButtonEventHandler
import com.example.notessqlite.R
import com.example.notessqlite.birthdays.Birthday
import com.example.notessqlite.database.InsertNoteIntoFolderDatabase
import com.example.notessqlite.database.NoteDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

class NotesFragment : Fragment(), DialogButtonEventHandler {
    private val CHANNEL_ID = "33"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }
    private lateinit var db: NoteDatabase
    private lateinit var db2:InsertNoteIntoFolderDatabase
    private lateinit var notesAdapter: NotesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //--------Setting up elements (not good practice on my side but, gets the job done)---------
        val greetings: TextView = view.findViewById(R.id.greetings)
        val recyclerView: RecyclerView = view.findViewById(R.id.notesRView)
        val addBtn: FloatingActionButton = view.findViewById(R.id.addBtn)
        val searchView: SearchView = view.findViewById(R.id.searchView)
        val noDataView: LinearLayout = view.findViewById(R.id.noDataView)
        val noNotes: LinearLayout = view.findViewById(R.id.noNotes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { NoteDatabase(it) }!!
        db2 = context?.let { InsertNoteIntoFolderDatabase(it) }!!
        notesAdapter = NotesAdapter(db.getAllNotes(), requireContext())
        recyclerView.adapter = notesAdapter

        //-----------------Setting up a view that returns if there are no notes--------------------
        val notesQueryList = db.getAllNotes()
        noNotes.visibility = View.GONE
        if (notesQueryList.isEmpty()){
            noNotes.visibility = View.VISIBLE
        }else{
            noNotes.visibility = View.GONE
        }

        //----------------Handling the add button clicked event (launches activity to add a new note-------------
        val x = DialogBuilder(this, "ATTENTION! ATTENTION!", "This should just open the next activity")
        addBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            startActivity(intent)

        }
        searchView.queryHint = getString(R.string.queryHint)

        //-----------------Greeting type writer effect implementation------------------
        fun TextView.typeWriteMessage(lifecycleScope:LifecycleCoroutineScope, text:String, intervalMs:Long) {
            this.text = ""
            lifecycleScope.launch {
                text.forEach { char ->
                    delay(intervalMs)
                    this@typeWriteMessage.append(char.toString())
                }
            }
        }
        val lifecycleScope = lifecycleScope
        fun getGreeting():String{
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return when (currentHour) {
                in 0..11 -> getString(R.string.greeting_morning)
                in 12..16 -> getString(R.string.greeting_afternoon)
                else -> getString(R.string.greeting_evening)
            }
        }

        //----------------Birthday creation and handling logic------------------
        val odriya = Birthday(0, "Odriya", "", LocalDate.of(2005, 1, 12))
        val beryl = Birthday(1, "Beryl",  "", LocalDate.of(2003, 1, 31))
        val christine = Birthday(2, "Christine", "", LocalDate.of(2001, 2, 7))
        val stephanie = Birthday(3, "Stephanie", "", LocalDate.of(1999, 2, 19))
        val ann = Birthday(4, "Ann", "", LocalDate.of(1990, 3, 1))
        val ian = Birthday(5, "Ian", "", LocalDate.of(2005, 4, 7))
        val aiyana = Birthday(6, "Mama", "", LocalDate.of(2023, 5, 6))
        val kimberly = Birthday(7, "Kimberly", "", LocalDate.of(2006, 5, 17))
        val elizaza = Birthday(8, "Elizaza", "", LocalDate.of(2005, 6, 13))
        val seanice = Birthday(9, "Seanice", "", LocalDate.of(2007, 8, 2))
        val amandine = Birthday(10, "Amandine","", LocalDate.of(2023, 8, 22))
        val ashley = Birthday(11, "Starfire", "", LocalDate.of(2005, 9, 29))
        val mum = Birthday(12, "Mum", "", LocalDate.of(1977, 11, 23))
        val currentDate = LocalDate.now()

        // Creating a list to store the birthdays of people I care about too much
        val birthdayList = listOf(odriya, beryl, christine, stephanie, ann, ian, aiyana, kimberly,
            elizaza, seanice, amandine, ashley, mum)
        var birthdayFound = false // boolean to keep track if a birthday record has been found

        // Looping through my list to find if it's anyone's birthday
        for(amiesBirthday in birthdayList){
            if(currentDate.month == amiesBirthday.birthday?.month && currentDate.dayOfMonth == amiesBirthday.birthday?.dayOfMonth){
                greetings.typeWriteMessage(lifecycleScope, "It's ${amiesBirthday.name}'s birthday!❤️", 100)
                buildNotification(amiesBirthday.name, currentDate.year - amiesBirthday.birthday.year)
                birthdayFound = true
                break
            }
        }
        if(!birthdayFound){
            greetings.typeWriteMessage(lifecycleScope, getGreeting(), 100)
        }

        //              Search for notes Logic
        searchView.setOnClickListener {
            Toast.makeText(context,"Tap on the search icon to the left to search!",Toast.LENGTH_SHORT).show()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }

            private fun filterData(searchTerm: String?) {
                val filteredList = if (searchTerm.isNullOrEmpty()){
                    db.getAllNotes()
                }else{
                    db.searchNote(searchTerm)
                }
                notesAdapter.updateData(filteredList)//-> Updating our list to the filtered list using the function in the adapter class
            //                Updating the view if no search results are returned
                if (filteredList.isEmpty()){
                    noDataView.visibility = View.VISIBLE
                }else{
                    noDataView.visibility = View.GONE
                }
            }
        })
    }

    private fun buildNotification(birthdayAmie:String, age:Int){
        // NotificationBuilder
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle("It's $birthdayAmie's birthday!")
            .setContentText("Wish $birthdayAmie a happy birthday as she turns $age years old!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        createNotificationChannel()

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(10, builder.build())
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification channel"
            val descriptionText = "This is my notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private lateinit var noNotes: LinearLayout
    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
        noNotes = view?.findViewById<LinearLayout>(R.id.noNotes)!!
        val notesQueryList = db.getAllNotes()
        if (notesQueryList.isEmpty()){
            noNotes.visibility = View.VISIBLE
        }else{
            noNotes.visibility = View.GONE
        }
    }

    override fun okButtonOnClickListener() {
        val intent = Intent(context, AddNoteActivity::class.java)
        startActivity(intent)
    }

    override fun cancelButtonOnClickListener() {
        // It dismisses by default, so no need to define a thing here
    }
}
