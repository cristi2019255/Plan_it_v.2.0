package com.example.plan_it.Admins.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Dialogs.DialogRemoveHourInProgram
import com.example.plan_it.Admins.Dialogs.DialogRemoveSpecialistForService
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase


class SpecialistsForServicesAdaper(var mContext: Context, var mSpecialists:List<String?>, var CompanyLocality:String
                  , var CompanyCategory:String, var CompanyName:String, var ServiceName:String)
    : RecyclerView.Adapter<SpecialistsForServicesAdaper.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_admin_specialist_for_services, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mSpecialists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
           holder.Name!!.setText(mSpecialists[position])
           holder.stergeSpecialist!!.setOnClickListener {
               var intent= Intent(mContext, DialogRemoveSpecialistForService::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.putExtra("company locality",CompanyLocality)
               intent.putExtra("company category",CompanyCategory)
               intent.putExtra("company name",CompanyName)
               intent.putExtra("service name",ServiceName)
               intent.putExtra("key",mSpecialists[position]!!)
               mContext.startActivity(intent)
           }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Name: TextView?=null
        var stergeSpecialist: Button?=null
        init {
            Name=itemView.findViewById(R.id.Specialist_specialist_for_services_item_admin)
            stergeSpecialist=itemView.findViewById(R.id.Remove_specialist_for_services_item_admin)
        }
    }
}