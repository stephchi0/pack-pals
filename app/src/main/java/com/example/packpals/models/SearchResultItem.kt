package com.example.packpals.models

class SearchResultItem(
    var mainText: String? = null,
    var secondaryText: String? = null
){
    fun addMainText(s:String){
       this.mainText = s
    }
    fun addSecondaryText(s:String){
        this.secondaryText = s
    }
}