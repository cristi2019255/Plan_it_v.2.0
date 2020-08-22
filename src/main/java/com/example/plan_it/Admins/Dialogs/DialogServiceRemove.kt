package com.example.plan_it.Admins.Dialogs

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class DialogServiceRemove : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_service_remove)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this@DialogServiceRemove)
        dialog.setTitle("Ștergere serviciu")
        dialog.setMessage("Sunteți sigur că doriți să ștergeți acest serviciu din lista de servicii?")
        dialog.setNegativeButton(
            "Termină"
        ) { dialog, which -> dialog.dismiss()
        finish()
        }

        dialog.setPositiveButton("Șterge", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                var intent=getIntent()
                var locality=intent.getStringExtra("company locality")
                var category=intent.getStringExtra("company category")
                var companyName=intent.getStringExtra("company name")
                var serviceName=intent.getStringExtra("service name")
                var reference=FirebaseDatabase.getInstance().getReference("companies")
                    .child(locality).child(category).child(companyName).child("Services")
                    .child(serviceName)
                reference.setValue(null)
                finish()
            }
        })

        dialog.show()
    }
}
