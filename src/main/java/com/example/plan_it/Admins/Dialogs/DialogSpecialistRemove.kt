package com.example.plan_it.Admins.Dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class DialogSpecialistRemove : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_specialist_remove)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this@DialogSpecialistRemove)
        dialog.setTitle("Ștergere specialist")
        dialog.setMessage("Sunteți sigur că doriți să ștergeți acest specialist din lista de specialisti?")
        dialog.setNegativeButton(
            "Termină"
        ) { dialog, which ->
            dialog.dismiss()
            finish()
        }

        dialog.setPositiveButton("Șterge", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                var intent = getIntent()
                var locality = intent.getStringExtra("company locality")
                var category = intent.getStringExtra("company category")
                var companyName = intent.getStringExtra("company name")
                var specialistName = intent.getStringExtra("specialist name")
                var reference = FirebaseDatabase.getInstance().getReference("companies")
                    .child(locality).child(category).child(companyName).child("Specialists")
                    .child(specialistName)
                reference.setValue(null)
                finish()
            }
        })

        dialog.show()
    }
}