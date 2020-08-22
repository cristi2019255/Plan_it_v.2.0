package com.example.plan_it.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.plan_it.Class.Company
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.bumptech.glide.Glide
import com.example.plan_it.Class.CompanyM
import com.example.plan_it.Class.ServiceJ
import com.example.plan_it.Users.Adapter.ServiceAdapter
import com.example.plan_it.R
import com.example.plan_it.Users.Adapter.CompanyAdapter
import com.google.gson.Gson
import org.json.JSONArray


class Inscriere : AppCompatActivity() {

    private var CompanyName:TextView?=null
    private var CompanyAdress:TextView?=null
    private var Services:RecyclerView?=null
    private var CompanyImage:ImageView?=null
    private lateinit var URL:String
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscriere)
        val intent:Intent=getIntent()
        val CompanyId:Long=intent.getLongExtra("company_id",0)
        val token:String=intent.getStringExtra("token")
        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(this)

        CompanyName=findViewById(R.id.CompanyName_activity_inscriere)
        CompanyAdress=findViewById(R.id.CompanyAdress_activity_inscriere)
        Services=findViewById(R.id.Servicii_recycler_view_activity_inscriere)
        CompanyImage=findViewById(R.id.profileImage_activity_mainPage_for_admins)
        Services=findViewById(R.id.Servicii_recycler_view_activity_inscriere)
        Services!!.setHasFixedSize(true)
        Services!!.setLayoutManager(LinearLayoutManager(baseContext))

        CompanyImage!!.setImageResource(R.mipmap.ic_launcher)

        val name=intent.getStringExtra("company name")
        val location=intent.getStringExtra("company location")
        val services_names=intent.getStringArrayListExtra("company services names")
        val services_prices=intent.getStringArrayListExtra("company services prices")
        val services_times=intent.getStringArrayListExtra("company services times")
        CompanyName!!.setText(name)
        CompanyAdress!!.setText(location)

        var serviceAdapter = ServiceAdapter(baseContext,services_names,services_prices,services_times,CompanyId,token)
        Services!!.setAdapter(serviceAdapter)


    }
}
