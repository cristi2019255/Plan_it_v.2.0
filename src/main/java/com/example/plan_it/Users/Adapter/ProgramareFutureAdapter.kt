package com.example.plan_it.Users.Adapter

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
import com.example.plan_it.Class.Company
import com.example.plan_it.R
import com.example.plan_it.Users.Dialogs.DialogRemoveProgramare
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProgramareFutureAdapter(var mContext: Context, var mProgramari:ArrayList<String>,var UserId:String)
    : RecyclerView.Adapter<ProgramareFutureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_programari_future, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mProgramari.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path=mProgramari.get(position).split(" ")[3]
        val nameService=path.split("|")[3]
        val date=mProgramari.get(position).split(" ")[0]
        val hour=mProgramari.get(position).split(" ")[1]
        val duration=mProgramari.get(position).split(" ")[2]
        val locality=path.split("|")[0]
        val category=path.split("|")[1]
        val companyName=path.split("|")[2]

        var reference= FirebaseDatabase.getInstance().getReference("companies")
            .child(locality).child(category).child(companyName)

        holder.dateTextView!!.setText(date)
        holder.hourTextView!!.setText("ora $hour")
        holder.durationTextView!!.setText("$duration minute")
        holder.localityTextView!!.setText(locality)
        holder.categoryTextView!!.setText(category)
        holder.companyNameTextView!!.setText(companyName)
        holder.nameTextView!!.setText(nameService)

        //image
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Company::class.java)!=null) {
                    var company = snapshot.getValue(Company::class.java)
                    if (company!!.imageUrl.equals("Default")) {
                        holder.image!!.setImageResource(R.mipmap.ic_launcher)
                    } else {
                        Glide.with(mContext).load(company.imageUrl).into(holder.image!!)
                    }
                }
            }
        })

        holder.remove!!.setOnClickListener {
            var key="$date $hour $duration"

            var intent= Intent(mContext, DialogRemoveProgramare::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("UserId",UserId)
            intent.putExtra("UserKey",key)
            intent.putExtra("company locality",locality)
            intent.putExtra("company category",category)
            intent.putExtra("company name",companyName)
            intent.putExtra("specialist name",nameService)
            mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameTextView: TextView?=null
        var localityTextView: TextView?=null
        var categoryTextView: TextView?=null
        var companyNameTextView: TextView?=null
        var dateTextView: TextView?=null
        var hourTextView: TextView?=null
        var durationTextView: TextView?=null
        var image:ImageView?=null
        var remove:Button?=null

        init {
            nameTextView=itemView.findViewById(R.id.Name_programari_future_item)
            localityTextView=itemView.findViewById(R.id.locality_programari_future_item)
            categoryTextView=itemView.findViewById(R.id.category_programari_future_item)
            companyNameTextView=itemView.findViewById(R.id.companyName_programari_future_item)
            dateTextView=itemView.findViewById(R.id.Data_programari_future_item)
            hourTextView=itemView.findViewById(R.id.ora_programari_future_item)
            durationTextView=itemView.findViewById(R.id.durata_programari_future_item)
            image=itemView.findViewById(R.id.company_image_item_programari_future)
            remove=itemView.findViewById(R.id.remove_item_programari_future)
        }
    }


}