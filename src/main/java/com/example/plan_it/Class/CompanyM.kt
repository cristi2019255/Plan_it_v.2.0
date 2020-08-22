package com.example.plan_it.Class
class CompanyM {

    data class CompanyJ(
        val company: CompanyJson
    ) {
        override fun toString(): String {
            return "CompanyJ(company=$company)"
        }
    }

    data class CompanyJson(
        val name: String,
        val city: String,
        val address: String,
        val category: String,
        val country: String,
        val specialistDTOList: List<String>,
        val services: List<ServiceJ>,
        val id: Long
    ) {
        override fun toString(): String {
            return "CompanyJson(name='$name', city='$city', address='$address', category='$category', country='$country', specialistDTOList=$specialistDTOList, services=$services, id=$id)"
        }
    }

}