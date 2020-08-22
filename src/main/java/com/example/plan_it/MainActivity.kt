package com.example.plan_it

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.snackbar.Snackbar

import java.util.HashMap
import com.example.plan_it.Class.User
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    private lateinit var Inregistrare:Button
    private lateinit var Autorizare:Button
    private lateinit var Animation:Button

    private lateinit var imageView1:ImageView
    private lateinit var imageView2:ImageView
    private lateinit var URL:String
    private lateinit var queue: RequestQueue
    private var animationfllow:Boolean=true
    lateinit var root:RelativeLayout
    private lateinit var activity_main:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main=findViewById(R.id.root_element)
        root=findViewById(R.id.root_element)
        queue= Volley.newRequestQueue(this)
        URL=resources.getString(R.string.Server_Url)
        addOnActionListener()

    }

    fun addOnActionListener(){
        Inregistrare=findViewById(R.id.buttonSignIn)
        Autorizare=findViewById(R.id.buttonRegister)
        Animation=findViewById(R.id.animation_activity_main)
        imageView1=findViewById(R.id.imageView1)
        imageView2=findViewById(R.id.imageView2)

        Inregistrare.setOnClickListener {
            ShowSignInWindow()
        }
        Autorizare.setOnClickListener {
            ShowAuthorizationWindow()
        }
        Animation.setOnClickListener {
            var anim: Animation= AnimationUtils.loadAnimation(this, R.anim.alpha_reverse)
            if (animationfllow) {
                anim = AnimationUtils.loadAnimation(this, R.anim.myalfa)
            }else{
                anim= AnimationUtils.loadAnimation(this, R.anim.alpha_reverse)

            }
            anim.fillAfter=true
            imageView1.startAnimation(anim)
            imageView2.startAnimation(anim)
            animationfllow=!animationfllow
        }

    }

    private fun ShowAuthorizationWindow() {
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle("Înregistrare")
        dialog.setMessage("Completați toate câmpurile pentru a vă putea Înregistra")
        val inflater = LayoutInflater.from(this@MainActivity)
        val signinview = inflater.inflate(R.layout.form_inregistrare, null)
        dialog.setView(signinview)

        dialog.setNegativeButton(
            "Termină"
        ) { dialog, which -> dialog.dismiss() }


        val email = signinview.findViewById<EditText>(R.id.emailfield)
        val pass = signinview.findViewById<EditText>(R.id.passwordfield)
        val phone = signinview.findViewById<EditText>(R.id.phonefield)
        val name = signinview.findViewById<EditText>(R.id.namefield)


        dialog.setPositiveButton("Înregistrare", object:DialogInterface.OnClickListener {

            override fun onClick(dialog:DialogInterface, which:Int) {

                if (TextUtils.isEmpty(email.text)) {
                    Snackbar.make(root, "nu ați introdus email", Snackbar.LENGTH_SHORT).show()
                    return
                }
                if (pass.text.length < 5) {
                    Snackbar.make(root, "parola e prea scurtă", Snackbar.LENGTH_SHORT).show()
                    return
                }
                if (phone.text.length < 9) {
                    Snackbar.make(root, "numărul de telefon invalid", Snackbar.LENGTH_SHORT).show()
                    return
                }
                if (TextUtils.isEmpty(name.text)) {
                    Snackbar.make(root, "Numele e invalid", Snackbar.LENGTH_SHORT).show()
                    return
                }

                val namedb = name.text.toString()
                val phonedb = phone.text.toString()
                val emaildb = email.text.toString()
                val passdb = pass.text.toString()
                var role=1
                SignUp(emaildb,namedb,passdb,role,phonedb,"Ungheni","R.Moldova")
            }
        })

        dialog.show()
    }

    fun ShowSignInWindow(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Autentificare")
        dialog.setMessage("Completați câmpurile")
        val inflater = LayoutInflater.from(this)
        val authorization = inflater.inflate(R.layout.form_autorizare, null)
        dialog.setView(authorization)

        val email = authorization.findViewById<EditText>(R.id.emailFieldA)
        val pass = authorization.findViewById<EditText>(R.id.passwordFieldA)

        dialog.setNegativeButton("Termină") { dialog, which -> dialog.dismiss() }

        dialog.setPositiveButton("Autentificare", object:DialogInterface.OnClickListener {

            override fun onClick(dialog:DialogInterface, which:Int) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Vă rugăm să introduceți email-ul", Snackbar.LENGTH_SHORT).show()
                    return
                }
                if (pass.getText().length < 5) {
                    Snackbar.make(root, "Parola prea scurtă", Snackbar.LENGTH_SHORT).show()
                    return
                }
                val emaildb = email.getText().toString()
                val passdb = pass.getText().toString()

                Auth(emaildb,passdb)
            }
        })

        dialog.show()
    }


    fun SignUp(email:String,name:String,pass:String,role:Int,phone:String,city:String,country:String){
        val url1="$URL/u/signup"
        var obiect= JSONObject()
        try {
            obiect.put("email", email)
            obiect.put("name", name)
            obiect.put("phone", phone)
            obiect.put("role", role)
            obiect.put("city", city)
            obiect.put("country", country)
            obiect.put("password", pass)
            obiect.put("id", null)
        }catch (e:JSONException){
            e.printStackTrace()
        }

        val postRequest =object: StringRequest(Method.POST, url1,
            Response.Listener<String> { response ->
                // response
                Toast.makeText(baseContext,response.toString(),Toast.LENGTH_LONG).show()
            },Response.ErrorListener{
                    error: VolleyError? ->
                //error
                Toast.makeText(this@MainActivity,error!!.message,Toast.LENGTH_LONG).show()
            }){
            override fun getBody(): ByteArray {
                return obiect.toString().toByteArray(charset("UTF-8"))
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        queue.add(postRequest)

    }

    fun Auth(email:String,pass:String){
        val url1="${URL}/u/auth"
        val postRequest =object: StringRequest(
            Method.POST, url1,
            Response.Listener<String> { response ->
                // response
                var res = JSONObject(response)
                var intent = Intent(this@MainActivity, MainPage_For_Users::class.java)
                println(res.get("token").toString())
                intent.putExtra("token",res.get("token").toString())
                intent.putExtra("authTime",res.get("authTime").toString())
                intent.putExtra("email",res.get("email").toString())
                intent.putExtra("id",res.get("id").toString())
                intent.putExtra("name",res.get("name").toString())
                intent.putExtra("phone",res.get("phone").toString())
                intent.putExtra("city",res.get("city").toString())
                intent.putExtra("country",res.get("country").toString())
                startActivity(intent)
            }, Response.ErrorListener{
                    error: VolleyError? ->
                //error
                Toast.makeText(this@MainActivity, "Autorizare eșuată!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val map=HashMap<String,String>()
                map.put("email",email)
                map.put("password",pass)
                return map
            }
        }
        queue.add(postRequest)

    }
}