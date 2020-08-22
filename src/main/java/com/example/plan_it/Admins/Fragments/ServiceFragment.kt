package com.example.plan_it.Admins.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Adapter.ServiceAdapter
import com.example.plan_it.Admins.AddService
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.Service
import com.example.plan_it.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ServiceFragment(var CompanyUsername:String,var token:String):Fragment() {
    private var servicesRecyclerView: RecyclerView? = null
    private var addService: Button?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_for_admin, container, false)
        servicesRecyclerView = view.findViewById(R.id.services_fragment_service_for_admin)
        servicesRecyclerView!!.setHasFixedSize(true)
        servicesRecyclerView!!.layoutManager = LinearLayoutManager(context)

/*
        var servicesAdapter = ServiceAdapter(
            context!!,
            company!!.Services.values.toList(),
            locality,
            category,
            companyName
        )
        servicesRecyclerView!!.setAdapter(servicesAdapter)

*/

        addService=view.findViewById(R.id.add_service_fragment_service_for_admin)
        addService!!.setOnClickListener {
      /*      var intent= Intent(context, AddService::class.java)
            intent.putExtra("company locality",locality)
            intent.putExtra("company category",category)
            intent.putExtra("company name",companyName)
            startActivity(intent)
      */  }
        return view
    }
}