package com.example.plan_it.Admins.Dialogs

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class DialogRemoveHourInProgram : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_remove_hour_in_program)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this@DialogRemoveHourInProgram)
        dialog.setTitle("Ștergere ore în programul de lucru")
        dialog.setMessage("Sunteți sigur că doriți să ștergeți acest interval orar din programul de lucru?")
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
                var Day = intent.getStringExtra("day")
                var key = intent.getStringExtra("key")

                var reference =
                    FirebaseDatabase.getInstance().getReference("companies").child(locality)
                        .child(category).child(companyName).child("Specialists")
                        .child(specialistName).child("program").child(Day).child(key)

                reference.setValue(null)
                finish()
            }
        })

        dialog.show()
    }
}
