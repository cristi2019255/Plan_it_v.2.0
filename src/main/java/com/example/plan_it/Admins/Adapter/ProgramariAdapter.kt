package com.example.plan_it.Admins.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plan_it.Admins.Dialogs.DialogServiceRemove
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.User
import com.example.plan_it.R
import com.example.plan_it.Users.Dialogs.DialogRemoveProgramare
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProgramariAdapter (var mContext: Context, var mProgramari:ArrayList<String>,var locality:String
                         ,var category:String,var companyName:String,var specialistName:String)
    : RecyclerView.Adapter<ProgramariAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_admin_programari, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mProgramari.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path=mProgramari.get(position).split(" ")[3]
        val dataS=mProgramari.get(position).split(" ")[0]
        val hourS=mProgramari.get(position).split(" ")[1]
        val durationS=mProgramari.get(position).split(" ")[2]

        holder.name!!.setText(dataS)
        holder.hour!!.setText(hourS)
        holder.duration!!.setText(durationS)

        var reference= FirebaseDatabase.getInstance().getReference("Users")
            .child(path)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Company::class.java)!=null) {
                    var user = snapshot.getValue(User::class.java)
                    if (user!!.imageURL.equals("Default")) {
                        holder.image!!.setImageResource(R.mipmap.ic_launcher)
                    } else {
                        Glide.with(mContext).load(user.imageURL).into(holder.image!!)
                    }
                }
            }
        })

        holder.remove!!.setOnClickListener {
            var key:String=mProgramari.get(position).split(" ")[0]+" "+
                    mProgramari.get(position).split(" ")[1]+" "+
                    mProgramari.get(position).split(" ")[2]
            var UserId=path
            var intent= Intent(mContext, DialogRemoveProgramare::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("UserId",UserId)
            intent.putExtra("UserKey",key)
            intent.putExtra("company locality",locality)
            intent.putExtra("company category",category)
            intent.putExtra("company name",companyName)
            intent.putExtra("specialist name",specialistName)
            mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView?=null
        var hour: TextView?=null
        var duration: TextView?=null
        var image: ImageView?=null
        var remove: Button?=null

        init {
            name=itemView.findViewById(R.id.Name_item_admin_programari)
            hour=itemView.findViewById(R.id.Data_item_programari_passed)
            duration=itemView.findViewById(R.id.Duration_item_admin_programari)
            image=itemView.findViewById(R.id.company_image_item_admin_programari)
            remove=itemView.findViewById(R.id.remove_item_admin_programari)
        }
    }
}