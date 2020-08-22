package com.example.plan_it.Users.Adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.CompanyM
import com.example.plan_it.Users.Inscriere
import com.example.plan_it.R


class CompanyAdapter(var mContext:Context,var mCompanies:ArrayList<CompanyM.CompanyJ>,var token:String) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_company, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mCompanies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //image
        var company: CompanyM.CompanyJ =mCompanies.get(position)
        holder.profile_Image!!.setImageResource(R.mipmap.ic_launcher)
        //

        holder.name!!.setText(company.company.name)
        holder.adress!!.setText(company.company.address)
        holder.locality!!.setText(company.company.city+","+company.company.country)
        holder.category!!.setText(company.company.category)

        holder.planifica!!.setOnClickListener {
            var intent=Intent(mContext, Inscriere::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("company name",company.company.name)
            intent.putExtra("company location",company.company.city+","+company.company.country+","+company.company.address)
            intent.putExtra("company id",company.company.id)
            intent.putExtra("token",token)
            val ServiceNames=ArrayList<String>()
            val ServicePrices=ArrayList<String>()
            val ServiceTimes=ArrayList<String>()
            for (i in 0..(company.company.services.size-1)){
                ServiceNames.add(company.company.services[i].name)
                ServicePrices.add(company.company.services[i].name)
                ServiceTimes.add(company.company.services[i].name)
            }
            intent.putExtra("company services names",ServiceNames)
            intent.putExtra("company services prices",ServicePrices)
            intent.putExtra("company services times",ServiceTimes)
            mContext.startActivity(intent)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView?=null
        var adress:TextView?=null
        var locality:TextView?=null
        var category:TextView?=null
        var planifica:Button?=null
        var profile_Image:ImageView?=null
        init {
          name=itemView.findViewById(R.id.CompanyName_company_item)
          adress=itemView.findViewById(R.id.Adress_company_item)
          locality=itemView.findViewById(R.id.locality_company_item)
          category=itemView.findViewById(R.id.category_company_item)
          planifica=itemView.findViewById(R.id.Planifica_company_item)
          profile_Image=itemView.findViewById(R.id.company_image_in_company_item)
        }
    }


}