package com.github.skrox.travelally.ui.mainscreen.home

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.github.skrox.travelally.data.db.entities.Trip
import com.github.skrox.travelally.data.repositories.TripsRepository
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.join
import java.lang.Exception


class HomeViewModel( private val userRepository: UserRepository,
                     private val mGoogleSignInClient: GoogleSignInClient,
                     private val tripsRepository: TripsRepository) : ViewModel() {

    var homeListener:HomeListener? = null

    fun getuser(context:Context)=userRepository.getUser(context)

    fun logout(view:View){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(view.context as MainActivity, OnCompleteListener<Void?> {
                // ...
                userRepository.logout()
            })
    }
//    val popularTrips:LiveData<List<Trip>> = liveData {
//
//        try {
//            val data = tripsRepository.getPopularTrips()
//            emit(data)
//        }catch (e:Exception){
//            Log.e("error homevm", e.message)
//            homeListener?.onFailure(e.message ?: "Unknown cause")
//        }
//    }


    private val popularTrips = MutableLiveData<List<Trip>>()
    val _popularTrips : LiveData<List<Trip>> = popularTrips

    private val tripsNearMe = MutableLiveData<List<Trip>>()
    val _tripsNearMe : LiveData<List<Trip>> = tripsNearMe

    fun loadPopularTrips()=viewModelScope.launch(Dispatchers.IO){
        try {
            val trips=tripsRepository.getPopularTrips()
            popularTrips.postValue(trips)
        }catch (e:Exception){
            homeListener?.onFailure(e.message ?: "Unknown cause")
            e.printStackTrace()
        }

    }

    fun loadTripsNearMe()=viewModelScope.launch(Dispatchers.IO) {
        try {
            val trips = tripsRepository.getTripsNearMe()
            tripsNearMe.postValue(trips)
        }catch (e:Exception){
            homeListener?.onFailure(e.message ?: "Unknown cause")
            e.printStackTrace()
        }
    }








}