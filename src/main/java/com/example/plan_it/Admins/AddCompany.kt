package com.example.plan_it.Admins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.plan_it.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class AddCompany : AppCompatActivity() {
    private var locality:AutoCompleteTextView?=null
    private var category:AutoCompleteTextView?=null
    private var country:AutoCompleteTextView?=null
    private var name:EditText?=null
    private var adress:EditText?=null
    private var add: Button?=null
    private lateinit var URL:String
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_company)

        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(this)
        val intent=getIntent()
        val token=intent.getStringExtra("token")

        locality=findViewById(R.id.locality_activity_add_company)
        category=findViewById(R.id.category_activity_add_company)
        country=findViewById(R.id.country_add_company)

        country!!.threshold=1
        country!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

                val url1="${URL}/fetch/countrylist?" +
                        "page=0" +
                        "&city=${locality!!.text.toString().trim()}" +
                        "&country=$text"
                val getcountryRequest = object : StringRequest(Method.GET, url1,
                    Response.Listener { response ->
                        // response
                        var countries=ArrayList<String>()
                        var res = JSONArray(response)
                        for (i in 0..(res.length()-1)){
                            var countryJ=JSONObject(res[i].toString())
                            countries.add(countryJ.get("country").toString())
                        }
                        var countriesAdapter = ArrayAdapter<String>(
                            this@AddCompany,
                            android.R.layout.simple_dropdown_item_1line,
                            countries)
                        country!!.setAdapter(countriesAdapter)
                    },
                    Response.ErrorListener {error->
                        // error
                        Toast.makeText(baseContext,error!!.message.toString(),Toast.LENGTH_LONG)
                    }
                ) {}
                queue.add(getcountryRequest)
            }
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        locality!!.threshold=1
        locality!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

                val url1="${URL}/fetch/citylist?" +
                        "page=0" +
                        "&city=$text" +
                        "&country=${country!!.text.toString().trim()}"
                val getcityRequest = object : StringRequest(Method.GET, url1,
                    Response.Listener { response ->
                        // response
                        var cities=ArrayList<String>()
                        var res = JSONArray(response)
                        for (i in 0..(res.length()-1)){
                            var countryJ=JSONObject(res[i].toString())
                            cities.add(countryJ.get("city").toString())
                        }
                        var citiesAdapter = ArrayAdapter<String>(
                            this@AddCompany,
                            android.R.layout.simple_dropdown_item_1line,
                            cities)
                        locality!!.setAdapter(citiesAdapter)
                    },
                    Response.ErrorListener {error->
                        // error
                        Toast.makeText(baseContext,error!!.message.toString(),Toast.LENGTH_LONG)
                    }
                ) {}
                queue.add(getcityRequest)
            }
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        category!!.threshold=1
        category!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

                val url1="${URL}/fetch/animelist?" +
                        "page=0" +
                        "&name=$text"
                val getcategoryRequest = object : StringRequest(Method.GET, url1,
                    Response.Listener { response ->
                        // response
                        var categories=ArrayList<String>()
                        var res = JSONArray(response)
                        for (i in 0..(res.length()-1)){
                            var countryJ=JSONObject(res[i].toString())
                            categories.add(countryJ.get("list").toString())
                        }
                        var categoryAdapter = ArrayAdapter<String>(
                            this@AddCompany,
                            android.R.layout.simple_dropdown_item_1line,
                            categories)
                        category!!.setAdapter(categoryAdapter)
                    },
                    Response.ErrorListener {error->
                        // error
                        Toast.makeText(baseContext,error!!.message.toString(),Toast.LENGTH_LONG)
                    }
                ) {}
                queue.add(getcategoryRequest)
            }
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        name=findViewById(R.id.name_activity_add_company)
        adress=findViewById(R.id.adress_activity_add_company)
        add=findViewById(R.id.add_activity_add_company)

        add!!.setOnClickListener {
            val url1="${URL}/c/android/create"
            val postRequest =object: StringRequest(
                Method.POST, url1,
                Response.Listener<String> { response ->
                    // response
                    Toast.makeText(this@AddCompany, response.toString(), Toast.LENGTH_SHORT).show()
                    finish()
                }, Response.ErrorListener{
                        error: VolleyError? ->
                    //error
                    Toast.makeText(this@AddCompany, error!!.message.toString(), Toast.LENGTH_SHORT).show()
                    finish()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val map=HashMap<String,String>()
                    map.put("token",token)
                    return map
                }
                override fun getParams(): MutableMap<String, String> {
                    val map= HashMap<String,String>()
                    map.put("name",name!!.text.toString().trim())
                    map.put("city",locality!!.text.toString().trim())
                    map.put("address",adress!!.text.toString().trim())
                    map.put("country",country!!.text.toString().trim())
                    map.put("category",category!!.text.toString().trim())
                    return map
                }
            }
            queue.add(postRequest)
        }

    }
}
