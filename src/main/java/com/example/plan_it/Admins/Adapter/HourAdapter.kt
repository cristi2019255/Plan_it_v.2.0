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
import com.example.plan_it.R
import com.example.plan_it.Users.Dialogs.DialogRemoveProgramare
import com.google.firebase.database.FirebaseDatabase



class HourAdapter(var mContext: Context, var mHours:List<String?>, var CompanyLocality:String
                     , var CompanyCategory:String, var CompanyName:String,var SpecialistName:String,var Day:String) : RecyclerView.Adapter<HourAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_admin_hour, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mHours.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mHours!=null) {
            holder.hours!!.setText(mHours[position])

            holder.stergeOra!!.setOnClickListener {
                var intent= Intent(mContext, DialogRemoveHourInProgram::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("company locality",CompanyLocality)
                intent.putExtra("company category",CompanyCategory)
                intent.putExtra("company name",CompanyName)
                intent.putExtra("specialist name",SpecialistName)
                intent.putExtra("day",Day)
                intent.putExtra("key",mHours[position]!!)
                mContext.startActivity(intent)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hours: TextView?=null
        var stergeOra: Button?=null
        init {
            hours=itemView.findViewById(R.id.Hours_hour_item_admin)
            stergeOra=itemView.findViewById(R.id.Remove_hour_item_admin)
        }
    }
}