package com.example.plan_it.Users.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Users.Adapter.ProgramarePassedAdapter
import com.example.plan_it.R
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class  ProgramariPassedFragment(var UserId:String): Fragment() {
    private var programariPassedRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_programari_passed, container, false)
        programariPassedRecyclerView = view.findViewById(R.id.programari_fragment_programari_passed)
        programariPassedRecyclerView!!.setHasFixedSize(true)
        programariPassedRecyclerView!!.layoutManager = LinearLayoutManager(context)

        var refProgramari = FirebaseDatabase.getInstance().getReference("Users")
            .child(UserId).child("Programari")

        refProgramari.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<HashMap<String, Any>>() {}
                if (snapshot.getValue(t) != null) {
                    var hashmap = snapshot.getValue(t)
                    var map = hashmap!!.keys.toList()

                    //set current time
                    val currentTime =
                        SimpleDateFormat("dd|MM|yyyy HH:mm", Locale.getDefault()).format(
                            Date()
                        )
                    val calCurrent = Calendar.getInstance()
                    var dateCurrent = currentTime.split(" ")[0]
                    var hourCurrent = currentTime.split(" ")[1]
                    calCurrent.set(Calendar.DAY_OF_MONTH, dateCurrent.split("|")[0].toInt())
                    calCurrent.set(Calendar.MONTH, dateCurrent.split("|")[1].toInt())
                    calCurrent.set(Calendar.YEAR, dateCurrent.split("|")[2].toInt())
                    calCurrent.set(Calendar.HOUR_OF_DAY, hourCurrent.split(":")[0].toInt())
                    calCurrent.set(Calendar.MINUTE, hourCurrent.split(":")[1].toInt())
                    var cal = Calendar.getInstance()

                    var listPassed = ArrayList<String>()
                    var listFuture = ArrayList<String>()

                    for (i in 0..(map.size - 1)) {
                        var date = map.get(i).split(" ")[0]
                        var hour = map.get(i).split(" ")[1]
                        cal.set(Calendar.DAY_OF_MONTH, date.split("|")[0].toInt())
                        cal.set(Calendar.MONTH, date.split("|")[1].toInt())
                        cal.set(Calendar.YEAR, date.split("|")[2].toInt())
                        cal.set(Calendar.HOUR_OF_DAY, hour.split(":")[0].toInt())
                        cal.set(Calendar.MINUTE, hour.split(":")[1].toInt())

                        if (cal.before(calCurrent)) {
                            listPassed.add("${map.get(i)} ${hashmap.get(map.get(i))}")
                        }
                    }
                    var programariPassedAdapter = ProgramarePassedAdapter(context!!, listPassed)
                    programariPassedRecyclerView!!.setAdapter(programariPassedAdapter)
                }
            }
        })

        return view
    }

}
