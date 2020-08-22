package com.example.plan_it.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.plan_it.Class.CompanyM
import com.example.plan_it.R
import com.example.plan_it.Users.Adapter.CompanyAdapter
import com.google.gson.Gson
import org.json.JSONArray

class Programari : AppCompatActivity() {

    private var list_out:TextView?=null
    private var recyclerView:RecyclerView?=null
    private lateinit var URL:String
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programari)
        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(this)

        var intent: Intent = getIntent()
        val for_search: String = intent.getStringExtra("for_search")
        val locality: String = intent.getStringExtra("locality")
        val token: String = intent.getStringExtra("token")
        val type_search:Int=intent.getIntExtra("type_search",1)
        var serviceName=""
        var companyName=""
        var city=""
        var country=""
        if (type_search==1){
            companyName=for_search
        }else{
            serviceName=for_search
        }

        if (locality.contains(",")){
            city=locality.split(",")[0].trim()
            country=locality.split(",")[1].trim()
        }else{
            city=locality
        }

        list_out = findViewById(R.id.list_companies)
        recyclerView = findViewById(R.id.recicler_view)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(baseContext))


        var list_companies = ArrayList<CompanyM.CompanyJ>()
        val url1="$URL/search?" +
                "page=0" +
                "&serviceName=$companyName" +
                "&companyName=$serviceName" +
                "&city=$city" +
                "&country=$country"

        val putRequest = object : StringRequest(
            Method.GET, url1,
            Response.Listener<String> { response ->
                // response
                var res = JSONArray(response)
                for (i in 0..(res.length()-1)){
                    var mCompanyEntity = Gson()?.fromJson(res[i].toString(), CompanyM.CompanyJ::class.java)
                    list_companies.add(mCompanyEntity)
                    var companyAdapter = CompanyAdapter(baseContext, list_companies,token)
                    recyclerView!!.adapter=companyAdapter
                }
            },
            Response.ErrorListener { error->
                Toast.makeText(baseContext,error!!.message, Toast.LENGTH_LONG)
            }){}

        queue.add(putRequest)
    }
}
