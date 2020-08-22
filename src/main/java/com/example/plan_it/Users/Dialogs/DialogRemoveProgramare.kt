package com.example.plan_it.Users.Dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class DialogRemoveProgramare : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_programare_remove)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this@DialogRemoveProgramare)
        dialog.setTitle("Ștergere programare")
        dialog.setMessage("Sunteți sigur că doriți să ștergeți acesta programare din lista de programari?")
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
                var UserId=intent.getStringExtra("UserId")
                var Key=intent.getStringExtra("UserKey")

                var refProgUser= FirebaseDatabase.getInstance().getReference("Users")
                    .child(UserId).child("Programari").child(Key)

                var reference=FirebaseDatabase.getInstance().getReference("companies")
                    .child(locality).child(category).child(companyName).child("Specialists")
                    .child(specialistName).child("ProgramariSpecialist")
                    .child(Key)

                refProgUser.setValue(null)
                reference.setValue(null)

                finish()
            }
        })

        dialog.show()
    }
}