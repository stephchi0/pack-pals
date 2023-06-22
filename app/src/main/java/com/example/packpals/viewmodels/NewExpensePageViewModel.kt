package com.example.packpals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.packpals.models.Pal

class NewExpensePageViewModel : ViewModel() {
    val palsList: LiveData<List<Pal>> = MutableLiveData(
        listOf(
            Pal(1, "username123", "stooge", ""),
            Pal(2, "mooge246", "mooge", ""),
            Pal(3, "test998", "looge", ""),
            Pal(4, "bruh999", "jooge", "")
        )
    )
}