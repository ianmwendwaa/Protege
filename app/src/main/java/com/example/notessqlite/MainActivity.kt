package com.example.notessqlite

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.notessqlite.archives.ArchivesFragment
import com.example.notessqlite.archives.ui.birthday.BirthdayFragment
import com.example.notessqlite.archives.ui.trash.TrashFragment
import com.example.notessqlite.folders.CategoriesFragment
import com.example.notessqlite.databinding.ActivityMainBinding
import com.example.notessqlite.notes.NotesFragment
import com.example.notessqlite.relationships.RelationshipsFragment
import com.example.notessqlite.todo.ToDoFragment
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this,R.color.blue)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(NotesFragment())

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener(this)

//        val menu = navigationView.menu
//        val indieTypeface = ResourcesCompat.getFont(this,R.font.indie_flower)
//
//        for (i in 0 until menu.size()){
//            val menuItem = menu.getItem(i)
//            val navigationMenuItemView = menuItem.actionView as NavigationMenuItemView
//            val textView = navigationMenuItemView.findViewById<TextView>(R.id.)
//            textView.setTypeface(indieTypeface,Typeface.NORMAL)
//        }

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
                }
                R.id.navigation_relationships->{
                    replaceFragment(RelationshipsFragment())
                    true
                }else -> false
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_password->{
                replaceFragment(com.example.notessqlite.archives.ui.password.PasswordFragment())
            }
            R.id.nav_trash->{
                replaceFragment(TrashFragment())
            }
            R.id.nav_slideshow->{
                replaceFragment(BirthdayFragment())
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
}




