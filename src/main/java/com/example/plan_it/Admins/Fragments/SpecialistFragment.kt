package com.example.plan_it.Admins.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.plan_it.Admins.Adapter.CompanyAdapter
import com.example.plan_it.Admins.Adapter.SpecialistAdapter
import com.example.plan_it.Admins.AddSpecialist
import com.example.plan_it.Class.Company
import com.example.plan_it.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

class SpecialistFragment(var CompanyUsername:String,var token:String): Fragment() {
    private var specialistsRecyclerView: RecyclerView? = null
    private var addSpecialist: Button?=null
    private lateinit var URL:String
    private lateinit var queue: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_specialist_for_admin, container, false)
        specialistsRecyclerView = view.findViewById(R.id.specialists_fragment_specialist_for_admin)
        specialistsRecyclerView!!.setHasFixedSize(false)
        specialistsRecyclerView!!.layoutManager = LinearLayoutManager(context)

        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(context)

        val url1="$URL/s/search/by/company/" +
                "SPA-SRL-Iasi"
        val getSpecialistsRequest = object : StringRequest(
            Method.GET, url1,
            Response.Listener { response ->
                // response
                var specialists=ArrayList<String>()
                var res = JSONArray(response)
                for (i in 0..(res.length()-1)){
                    var SpecialistDetailed= JSONObject(res[i].toString())
                    var Specialist=JSONObject(SpecialistDetailed.get("user").toString())
                    specialists.add(Specialist.get("name").toString())
                }
                var specialistAdapter = SpecialistAdapter(
                    context!!,
                    specialists
                )
                specialistsRecyclerView!!.setAdapter(specialistAdapter)
            },
            Response.ErrorListener { error->
                // error
                Toast.makeText(context,error!!.message.toString(), Toast.LENGTH_LONG)
            }
        ) {}
        queue.add(getSpecialistsRequest)

        addSpecialist=view.findViewById(R.id.add_specialist_fragment_specialist_for_admin)
        addSpecialist!!.setOnClickListener {
  /*          var intent= Intent(context, AddSpecialist::class.java)
            intent.putExtra("company locality",locality)
            intent.putExtra("company category",category)
            intent.putExtra("company name",companyName)
            startActivity(intent)
    */    }
        return view
    }
}