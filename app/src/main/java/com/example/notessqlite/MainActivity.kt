package com.example.notessqlite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.notessqlite.archives.ArchivesFragment
import com.example.notessqlite.archives.ui.birthday.BirthdayFragment
import com.example.notessqlite.databinding.ActivityMainBinding
import com.example.notessqlite.email.EmailHandler
import com.example.notessqlite.folders.FolderFragment
import com.example.notessqlite.notes.NotesFragment
import com.example.notessqlite.toasts.CodeBase
import com.example.notessqlite.todo.ToDoFragment

@Suppress("DEPRECATION")
class MainActivity: AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this,R.color.blue)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(NotesFragment())

        //calling the email engine to detect any events
//        EmailHandler().sendEmail("Goodnight Ian!","Go sleep rn!")

        dailyEventWatcher()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.navigation_notes ->{
                    replaceFragment(NotesFragment())
                    true
                }
                R.id.navigation_todo->{
                    replaceFragment(ToDoFragment())
                    true
                }
                R.id.navigation_archive->{
                    replaceFragment(ArchivesFragment())
                    true
                }
                R.id.navigation_folders->{
                    replaceFragment(FolderFragment())
                    true
                }
                R.id.navigation_birthday->{
                    replaceFragment(BirthdayFragment())
                    true
                }else -> false
            }
        }
    }

    private fun dailyEventWatcher() {
        val worker = "Event watcher and handler"

        //schedule the worker to run after every 24 hours
        val dailyWorkRequest = PeriodicWorkRequest.Builder(
            EmailHandler.EmailWorker::class.java,
            24,
            java.util.concurrent.TimeUnit.HOURS).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            worker,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }

    private fun replaceFragment(fragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }catch (e: Exception){
            CodeBase.showToast(this, e.localizedMessage, R.drawable.ic_info)
        }
    }
}