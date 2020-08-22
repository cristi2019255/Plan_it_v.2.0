package com.example.plan_it.Users.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Class.Service
import com.example.plan_it.Class.ServiceJ
import com.example.plan_it.Users.Inscriere_detaliata
import com.example.plan_it.R

class ServiceAdapter (var mContext: Context, var mServicesNames:ArrayList<String?>,var mServicesPrices:ArrayList<String?>,
                      var mServicesTimes: ArrayList<String?>,var CompanyId:Long,var token:String)
    : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mServicesNames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.name!!.setText(mServicesNames.get(position))
            holder.pret!!.setText(mServicesPrices.get(position))

            holder.comanda!!.setOnClickListener {
                var intent = Intent(mContext, Inscriere_detaliata::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("company id", CompanyId)
                intent.putExtra("token", token)
                intent.putExtra("service name", mServicesNames.get(position))
                intent.putExtra("service price", mServicesPrices.get(position))
                intent.putExtra("service time", mServicesTimes.get(position))

                mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var pret: TextView? = null
        var comanda: Button? = null

        init {
            name = itemView.findViewById(R.id.Name_service_item)
            pret = itemView.findViewById(R.id.Price_service_item)
            comanda = itemView.findViewById(R.id.Comanda_service_item)
        }
    }
}