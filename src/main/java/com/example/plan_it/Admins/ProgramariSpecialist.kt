package com.example.plan_it.Admins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Adapter.ProgramariAdapter
import com.example.plan_it.R
import com.google.firebase.database.*
import java.util.HashMap

class ProgramariSpecialist : AppCompatActivity() {

    private var programari:RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programari_specialist)

        programari=findViewById(R.id.programari_activity_programari_specialist)
        programari!!.setHasFixedSize(true)
        programari!!.setLayoutManager(LinearLayoutManager(baseContext))

        var intent=getIntent()
        var locality:String=intent.getStringExtra("company locality")
        var category:String=intent.getStringExtra("company category")
        var companyName:String=intent.getStringExtra("company name")
        var specialistName:String=intent.getStringExtra("specialist name")

        var reference=FirebaseDatabase.getInstance().getReference("companies")
            .child(locality).child(category).child(companyName).child("Specialists").
                child(specialistName).child("ProgramariSpecialist")

        var map=HashMap<String,Any>()
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<HashMap<String, Any>>() {}
                if (snapshot.getValue(t)!=null){
                    map=snapshot.getValue(t)!!
                    var list=ArrayList<String>()
                    for (i in map.keys){
                        list.add("$i ${map[i]}")
                    }
                    var programariAdapter= ProgramariAdapter(baseContext,list,locality,category,companyName,specialistName)
                    programari!!.setAdapter(programariAdapter)
                }
            }
        })



    }
}
