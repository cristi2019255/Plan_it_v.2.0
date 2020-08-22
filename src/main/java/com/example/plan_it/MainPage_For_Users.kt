package com.example.plan_it

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.plan_it.Admins.AddCompany
import com.example.plan_it.Class.CompanyM
import com.example.plan_it.Class.User
import com.example.plan_it.Users.Adapter.CompanyAdapter
import com.example.plan_it.Users.MyProgramari
import com.example.plan_it.Users.Programari
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainPage_For_Users : AppCompatActivity() {

    private lateinit var queue: RequestQueue
    private var user: FirebaseUser? = null
    private val PICK_IMAGE = 1
    internal var imageUri: Uri? = null

    internal var uploadTask: UploadTask? = null
    internal lateinit var storageReference: StorageReference
    internal lateinit var reference: DatabaseReference

    private lateinit var URL:String
    private var profileImage: ImageView? = null
    private var ChangeNameButton:Button?=null
    private var Nume_user:EditText?=null
    private var Telefon_user:TextView?=null
    private var Mail_user:TextView?=null
    private var Search:Button?=null
    private var myProgramari:Button?=null
    private var Search_value:AutoCompleteTextView?=null
    private lateinit var email_intent:String
    private lateinit var token_intent:String
    var userS: User =
        User("DEFAULT", "", "", "", "", "",null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page__for__users)

        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(this)

        Nume_user=findViewById(R.id.Nume)
        Telefon_user=findViewById(R.id.Phone_activity_main_page_for_users)
        Mail_user=findViewById(R.id.Mail_user)
        myProgramari=findViewById(R.id.Programari_user_activity_main_page_for_users)
        profileImage = findViewById(R.id.profileImage_activity_mainPage_for_admins)
        ChangeNameButton=findViewById(R.id.ChangeName)
        Search=findViewById(R.id.Cauta)
        Search_value=findViewById(R.id.Cauta_edit_text) as AutoCompleteTextView


        Nume_user!!.setText(intent.getStringExtra("name"))
        Telefon_user!!.setText(intent.getStringExtra("phone"))
        email_intent=intent.getStringExtra("email")
        token_intent=intent.getStringExtra("token")
        Mail_user!!.setText(email_intent)
        //uploading image
        if (userS.imageURL.equals("DEFAULT")) {
            profileImage!!.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(baseContext).load(userS.imageURL).into(profileImage!!)
        }

        addOnClickListener()
    }


    fun addOnClickListener() {
        //
        profileImage!!.setOnClickListener(View.OnClickListener {
            //val gallery = Intent()
            //gallery.type = "image/*"
            //gallery.action = Intent.ACTION_GET_CONTENT
           // startActivityForResult(Intent.createChooser(gallery, "Sellect picture"), PICK_IMAGE)
        })
        //

        val LocalitatiValue = findViewById(R.id.Localitate)as AutoCompleteTextView
        val TypeValue = findViewById(R.id.type_activity_main_page_for_users) as Spinner
        val types = ArrayAdapter(
            this@MainPage_For_Users,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.type_search)
        )
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        TypeValue.setAdapter(types)
        TypeValue.setGravity(View.TEXT_ALIGNMENT_CENTER)

        LocalitatiValue!!.threshold=1
        LocalitatiValue!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

                val url1="${URL}/map/sugestion"
                val putRequest = object : StringRequest(Method.POST, url1,
                    Response.Listener<String> { response ->
                        // response
                        var localities=ArrayList<String>()
                        var res = JSONArray(response)
                        for (i in 0..(res.length()-1)){
                            var mCompanyEntity = Gson()?.fromJson(res[i].toString(), CompanyM.CompanyJ::class.java)
                            localities.add(mCompanyEntity.company.city+","+mCompanyEntity.company.country)
                        }
                        var localitiesAdapter = ArrayAdapter<String>(
                            this@MainPage_For_Users,
                            android.R.layout.simple_dropdown_item_1line,
                            localities)
                        LocalitatiValue!!.setAdapter(localitiesAdapter)
                    },
                    Response.ErrorListener {error->
                        // error
                        Toast.makeText(baseContext,error!!.message.toString(),Toast.LENGTH_LONG)
                    }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params.put("search_input", Search_value!!.text.toString().trim())
                        params.put("type", TypeValue!!.selectedItemPosition.toString())
                        params.put("second_box", "")
                        return params
                    }
                }
                queue.add(putRequest)

            }
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        Search_value!!.threshold=1
        Search_value!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {

                val url1="${URL}/map/sugestion"
                val putRequest = object : StringRequest(Method.POST, url1,
                    Response.Listener<String> { response ->
                        // response
                        var companies=ArrayList<String>()
                        var res = JSONArray(response)
                        for (i in 0..(res.length()-1)){
                            var mCompanyEntity = Gson()?.fromJson(res[i].toString(), CompanyM.CompanyJ::class.java)
                            if (TypeValue!!.selectedItemPosition==0) {
                                companies.add(mCompanyEntity.company.name)
                            }else {
                                for (i in 0..(mCompanyEntity.company.services.size - 1)) {
                                    companies.add(mCompanyEntity.company.services[i].name)
                                }
                            }
                        }
                        var companiesAdapter = ArrayAdapter<String>(
                            this@MainPage_For_Users,
                            android.R.layout.simple_dropdown_item_1line,
                            companies)
                        Search_value!!.setAdapter(companiesAdapter)
                    },
                    Response.ErrorListener {error->
                        // error
                        Toast.makeText(baseContext,"error",Toast.LENGTH_LONG)
                    }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params.put("search_input", text.toString().trim())
                        params.put("type", TypeValue!!.selectedItemPosition.toString())
                        params.put("second_box", LocalitatiValue!!.text.toString().trim())
                        return params
                    }
                }
                queue.add(putRequest)

            }
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        ChangeNameButton!!.setOnClickListener{
            val name=Nume_user!!.text.toString().trim()
            val url1="${URL}/u/changeName?" +
                    "email=$email_intent" +
                    "&name=$name"

            val putRequest = object : StringRequest(Method.PUT, url1,
                Response.Listener<String> { response ->
                    // response
                    Toast.makeText(baseContext,response.toString(),Toast.LENGTH_LONG)
                },
                Response.ErrorListener {error->
                    // error
                    Toast.makeText(baseContext,"error",Toast.LENGTH_LONG)
                }
            ) {
               override fun getParams(): Map<String, String> {
                        //not working suppose trouble with server
                        val params = HashMap<String, String>()
                        params.put("email", email_intent)
                        params.put("name", name)
                        return params
               }
            }
            queue.add(putRequest)
        }

        Search!!.setOnClickListener {
            var intent=Intent(this@MainPage_For_Users, Programari::class.java)
            intent.putExtra("email",email_intent)
            intent.putExtra("token",token_intent)
            intent.putExtra("for_search" , Search_value!!.text.toString().trim())
            intent.putExtra("locality" , LocalitatiValue!!.text.toString().trim())
            intent.putExtra("type_search",TypeValue.selectedItemPosition)
            startActivity(intent)
        }

        myProgramari!!.setOnClickListener {
            var intent=Intent(this@MainPage_For_Users, MyProgramari::class.java)
            intent.putExtra("email",getIntent().getStringExtra("email"))
            startActivity(intent)
        }
    }

    fun uploadImage() {
        val pd = ProgressDialog(this)
        pd.setMessage("Uploading...")
        pd.show()
        if (imageUri != null) {
            val fileReferece = storageReference.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)
            )
            //uploading the imageUri
            fileReferece.putFile(imageUri!!).addOnSuccessListener {
                fileReferece.downloadUrl.addOnCompleteListener { task ->
                    val mUri = task.result!!.toString()
                    reference =
                        FirebaseDatabase.getInstance().getReference("Users").child(user!!.getUid())
                    val map = HashMap<String, Any>()
                    map["imageURL"] = mUri

                    reference.updateChildren(map)
                    pd.dismiss()
                }
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                pd.setMessage("Uploaded " + progress.toInt() + "%")
            }
        } else {
            Toast.makeText(baseContext, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    fun getFileExtension(uri: Uri): String? {
        val contentResolver = baseContext.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data

            if (uploadTask != null && uploadTask!!.isInProgress()) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                profileImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
