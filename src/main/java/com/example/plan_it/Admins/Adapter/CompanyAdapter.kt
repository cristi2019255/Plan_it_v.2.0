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
import com.example.plan_it.Admins.AdministrateCompany
import com.example.plan_it.Class.Company
import com.example.plan_it.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CompanyAdapter(var mContext: Context,var mDetails:List<String>,var token:String) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_admin_company, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDetails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.profile_Image!!.setImageResource(R.mipmap.ic_launcher)
        holder.locality!!.setText(mDetails.get(position).split("|")[0])
        holder.category!!.setText(mDetails.get(position).split("|")[1])
        holder.name!!.setText(mDetails.get(position).split("|")[2])
        holder.adress!!.setText(mDetails.get(position).split("|")[3])
        holder.planifica!!.setOnClickListener {
            var intent= Intent(mContext, AdministrateCompany::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("token",token)
            intent.putExtra("company json",mDetails.get(position).split("|")[4])
            mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView?=null
        var adress: TextView?=null
        var locality:TextView?=null
        var category:TextView?=null
        var planifica: Button?=null
        var profile_Image: ImageView?=null
        init {
            name=itemView.findViewById(R.id.CompanyName_company_admin_item)
            adress=itemView.findViewById(R.id.Adress_company_admin_item)
            locality=itemView.findViewById(R.id.locality_company_admin_item)
            category=itemView.findViewById(R.id.category_company_admin_item)
            planifica=itemView.findViewById(R.id.Planifica_company_admin_item)
            profile_Image=itemView.findViewById(R.id.company_image_in_company_admin_item)
        }
    }
}