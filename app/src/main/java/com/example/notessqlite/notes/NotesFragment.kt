package com.example.notessqlite.notes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notessqlite.R
import com.example.notessqlite.databases.InsertNoteIntoFolderDatabase
import com.example.notessqlite.databases.NoteDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class NotesFragment : Fragment(),AnimationTrigger {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }
    private lateinit var videoView: VideoView
    override fun triggerAnimation() {
        videoView = requireView().findViewById(R.id.videoAnim)
        videoView.visibility = View.VISIBLE

        val animationSrc = Uri.parse("android.resource://"+
                requireActivity().packageName+"/"+ R.raw.lisref)
        videoView.setVideoURI(animationSrc)

        val animationController = MediaController(requireContext())
        videoView.setMediaController(animationController)
        animationController.setAnchorView(videoView)
        Log.d("VideoUri","VideoUri:$animationSrc")

        videoView.start()

        Handler(Looper.getMainLooper()).postDelayed({
            dismissAnimation()
        },3000)
    }

    override fun dismissAnimation() {
        videoView = requireView().findViewById(R.id.videoAnim)
        videoView.visibility = View.GONE
        videoView.stopPlayback()
    }

    private lateinit var db: NoteDatabase
    private lateinit var db2:InsertNoteIntoFolderDatabase
    private lateinit var notesAdapter: NotesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetings: TextView = view.findViewById(R.id.greetings)
        val recyclerView: RecyclerView = view.findViewById(R.id.notesRView)
        val addBtn: FloatingActionButton = view.findViewById(R.id.addBtn)
        val searchView: SearchView = view.findViewById(R.id.searchView)
        val noDataView: LinearLayout = view.findViewById(R.id.noDataView)
        val noNotes: LinearLayout = view.findViewById(R.id.noNotes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        db = context?.let { NoteDatabase(it) }!!
        db2 = context?.let { InsertNoteIntoFolderDatabase(it) }!!
        notesAdapter = NotesAdapter(db.getAllNotes(), childFragmentManager,requireContext())
        recyclerView.adapter = notesAdapter
        arguments?.getInt("newNotePosition")?.let { newNotePosition->
            recyclerView.scrollToPosition(newNotePosition)
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(newNotePosition)
            viewHolder?.itemView?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
//        sort.setOnClickListener {
//            db.getAllNotesByTitle()
//        }

        //             Setting up a view that returns if there are no notes
        val notesQueryList = db.getAllNotes()
        if (notesQueryList.isEmpty()){
            noNotes.visibility = View.VISIBLE
        }else{
            noNotes.visibility = View.GONE
        }

        //              Launching a new note activity
        addBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            startActivity(intent)
        }
        searchView.queryHint = getString(R.string.queryHint)

        //               Greeting logic
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
        fun getCelebration():String{
            val month = Calendar.getInstance().get(Calendar.MONTH)+1
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            return when {
                month == 1 && day == 12 -> getString(R.string.odriya)
                month == 1 && day == 31 -> getString(R.string.beryl)
                month == 2 && day == 7 -> getString(R.string.christine)
                month == 2 && day == 19 -> getString(R.string.stephanie)
                month == 3 && day == 1 -> getString(R.string.ann)
                month == 4 && day == 7 -> getString(R.string.mine)
                month == 5 && day == 1 -> getString(R.string.clarissa)
                month == 5 && day == 6 -> getString(R.string.aiyana)
                month == 5 && day == 17 -> getString(R.string.kimberly)
                month == 5 && day == 19 -> getString(R.string.megan)
                month == 8 && day == 2 -> getString(R.string.seanice)
                month == 8 && day == 22 -> getString(R.string.amandine)
                month == 9 && day == 29 -> getString(R.string.lashley)
                month == 10 && day == 29 -> getString(R.string.simone)
                month == 11 && day == 23 -> getString(R.string.mum)
                month == 12 && day == 5 -> getString(R.string.kailetu)
                else -> getGreeting()
            }
        }
        val celebration = getCelebration()
        greetings.typeWriteMessage(lifecycleScope,celebration,100)

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
    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}