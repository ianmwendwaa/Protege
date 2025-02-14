package com.example.notessqlite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.notessqlite.archives.ArchivesFragment
import com.example.notessqlite.categories.CategoriesFragment
import com.example.notessqlite.databinding.ActivityMainBinding
import com.example.notessqlite.notes.NotesFragment
import com.example.notessqlite.todo.ToDoFragment

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this,R.color.blue)
        binding = ActivityMainBinding.inflate(layoutInflater)
        CodeBase.showToast(this,"Testing",R.drawable.butterfly_effect)
        setContentView(binding.root)
        replaceFragment(NotesFragment())
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.navigation_notes ->{
                    replaceFragment(NotesFragment())
                    true
                }
                R.id.navigation_todo -> {
                    replaceFragment(ToDoFragment())
                    true
                }
                R.id.navigation_archive->{
                    replaceFragment(ArchivesFragment())
                    true
                }
                R.id.navigation_folders->{
                    replaceFragment(CategoriesFragment())
                    true
                }else -> false
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}




