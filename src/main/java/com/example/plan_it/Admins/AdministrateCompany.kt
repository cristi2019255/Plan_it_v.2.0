package com.example.plan_it.Admins

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.plan_it.Admins.Adapter.CompanyAdapter
import com.example.plan_it.Admins.Fragments.ServiceFragment
import com.example.plan_it.Admins.Fragments.SpecialistFragment
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.CompanyM
import com.example.plan_it.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

class AdministrateCompany : AppCompatActivity() {
    private var CompanyName:TextView?=null
    private var CompanyAdress:EditText?=null
    private var ChangeCompanyAdress:Button?=null

    private var companyImage: ImageView? = null
    private val PICK_IMAGE = 1
    internal var imageUri: Uri? = null

    internal var uploadTask: UploadTask? = null
    internal lateinit var storageReference: StorageReference
    internal lateinit var referencePhoto: DatabaseReference

    var CompanyUsername:String=""
    var token:String=""

    private lateinit var URL:String
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrate_company)

        var intent=getIntent()
        var CompanyS=intent.getStringExtra("company json")
        var CompanyJson=JSONObject(CompanyS)
        token=intent.getStringExtra("token")

        CompanyName=findViewById(R.id.Company_Name_change_activity_administrate_company)
        CompanyAdress=findViewById(R.id.company_Adress_change_activity_administrate_company)
        ChangeCompanyAdress=findViewById(R.id.changeName_activity_administrate_company)
        companyImage = findViewById(R.id.CompanyImage_activity_administrate_company)


        URL=resources.getString(R.string.Server_Url)
        queue= Volley.newRequestQueue(this)


        CompanyName!!.setText(CompanyJson.get("name").toString())
        CompanyAdress!!.setText(CompanyJson.get("address").toString())

        class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
            private var fragments: java.util.ArrayList<Fragment>
            private var titles: java.util.ArrayList<String>

            init {
                this.fragments = java.util.ArrayList()
                this.titles = java.util.ArrayList()
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }

            fun addFragment(fragment: Fragment, title: String) {
                fragments.add(fragment)
                titles.add(title)
            }
            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }

        }

        val tabLayout: TabLayout = findViewById(R.id.tab_layout_activity_administrate_company)
        val viewPager: ViewPager = findViewById(R.id.view_pager_activity_administrate_company)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(SpecialistFragment(CompanyUsername,token), "Specialisti")
        viewPagerAdapter.addFragment(ServiceFragment(CompanyUsername,token), "Servicii")
        viewPager.setAdapter(viewPagerAdapter)
        tabLayout.setupWithViewPager(viewPager)

        companyImage!!.setImageResource(R.mipmap.ic_launcher)

        ChangeCompanyAdress!!.setOnClickListener {
            CompanyJson.put("address",CompanyAdress!!.text.toString().trim())
            val url1="$URL/c/android/change"
            val PutRequest=object:StringRequest(
            Method.PUT,url1,Response.Listener<String>{
                    response ->
                    CompanyJson = JSONObject(response)
                    CompanyAdress!!.setText(CompanyJson.get("address").toString())
                },Response.ErrorListener {
                    error ->
                    Toast.makeText(this,error!!.message.toString(),Toast.LENGTH_SHORT).show()
                }){
                override fun getBody(): ByteArray {
                    return CompanyJson.toString().toByteArray(charset("UTF-8"))
                }
            }
            queue.add(PutRequest)
        }


        companyImage!!.setOnClickListener {
            //val gallery = Intent()
            //gallery.type = "image/*"
            //gallery.action = Intent.ACTION_GET_CONTENT
            //startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMAGE)
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
                    /*referencePhoto =
                        FirebaseDatabase.getInstance().getReference("companies")
                            .child(CompanyLocalityS).child(CompanyCategoryS).child(CompanyNameS)*/
                    val map = HashMap<String, Any>()
                    map["imageUrl"] = mUri
                    referencePhoto.updateChildren(map)
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
                companyImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
