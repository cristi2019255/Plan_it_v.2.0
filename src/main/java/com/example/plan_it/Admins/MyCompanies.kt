package com.example.plan_it.Admins

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.plan_it.Admins.Adapter.CompanyAdapter
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.User
import com.example.plan_it.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

class MyCompanies : AppCompatActivity() {

    private var Companies:RecyclerView?=null
    private lateinit var URL:String
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_companies)

        Companies=findViewById(R.id.Companies_recyclerView_activity_my_companies)
        Companies!!.setHasFixedSize(true)
        Companies!!.setLayoutManager(LinearLayoutManager(baseContext))

        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(this)
        val intent=getIntent()
        val token=intent.getStringExtra("token")

        val url1="$URL/c/findByOwner"
        val getcountryRequest = object : StringRequest(
            Method.GET, url1,
            Response.Listener { response ->
                // response
                var companies=ArrayList<String>()
                var res = JSONArray(response)
                for (i in 0..(res.length()-1)){
                    var companyJ= JSONObject(res[i].toString())
                    var details="${companyJ.get("city")}," +
                            "${companyJ.get("country")}|" +
                            "${companyJ.get("category")}|" +
                            "${companyJ.get("name")}|"+
                            "${companyJ.get("address")}|"+
                            "${res[i].toString()}"
                    companies.add(details)
                }

                var companyAdapter = CompanyAdapter(baseContext,companies,token)
                Companies!!.setAdapter(companyAdapter)
            },
            Response.ErrorListener { error->
                // error
                Toast.makeText(baseContext,error!!.message.toString(), Toast.LENGTH_LONG)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val map=HashMap<String,String>()
                map.put("token",token)
                return map
            }
        }
        queue.add(getcountryRequest)
    }
}