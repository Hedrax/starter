# Location Reminder
It's a TODO list application with a location feature that reminders the user 
with actions associated with a location of his choice when he's in the surrounding area.\
_This project was made within the Android Nanodegree_
## Project's KeyPoints
* Firebase Authentication
* Google Maps API
* Geofence API
* MVVM Architecture
* Testing
  * Unit Tests: ViewModel/LiveData, Test Double (Fake), Coroutines, and Room testing.
  * Integration Tests: Repository-Database, and UI Controller-ViewModel.
  * Instrumentation:  Fragment Test, Activity Test, and Application Navigation.
## Libraries
* [RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView)
* [Navigation Component](https://developer.android.com/guide/navigation)
* [Coroutines](https://developer.android.com/kotlin/coroutines)
* [Firebase Auth](https://firebase.google.com/docs/auth/android/google-signin)
* [Mockito](https://github.com/mockito/mockito)
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Room](https://developer.android.com/jetpack/androidx/releases/room)
* [Maps SDK](https://developers.google.com/maps/documentation/android-sdk/overview)
* [Material Component](https://developer.android.com/jetpack/compose/layouts/material)
* [Databinding](https://developer.android.com/topic/libraries/data-binding)
* [Android Jetpack WorkManager library](https://developer.android.com/jetpack/androidx/releases/work) ([JobIntentService](https://developer.android.com/reference/androidx/core/app/JobIntentService))

## ScreenShots
<table>
  <tr>
    <td align="center">Welcoming Page</td>
     <td align="center">Selecting Login method</td>
     <td align="center">Selection Map</td>
  </tr>
  <tr>
    <td><img src="/screenshots/Screenshot_2023-02-04-03-41-21-025_com.udacity.project4.jpg" width=300 height=540></td>
   <td><img src="/screenshots/Screenshot_2023-02-04-03-41-27-782_com.udacity.project4.jpg" width=300 height=540></td>
   <td><img src="/screenshots/Screenshot_2023-02-04-03-43-05-671_com.udacity.project4.jpg" width=300 height=540></td>
  </tr>
  <tr>
     <td align="center">Selecting Location</td>
      <td align="center">Details Fragment</td>
      <td align="center">Listing Fragment</td>
   </tr>
   <tr>
     <td><img src="/screenshots/Screenshot_2023-02-04-03-43-43-694_com.udacity.project4.jpg" width=300 height=540></td>
    <td><img src="/screenshots/Screenshot_2023-02-04-03-43-47-656_com.udacity.project4.jpg" width=300 height=540></td>
    <td><img src="/screenshots/Screenshot_2023-02-04-03-44-01-546_com.udacity.project4.jpg" width=300 height=540></td>
   </tr>
 </table>

 ## How to use
1. Register with your account or link with your google account to the appliction.
2. Click on the floating red (save icon) button in the listing screen after the authentication process.
3. Click on Add reminder button
4. Select location on the map
5. Add title of the place you want to save.
6. Add describtion of the location which will be selected (**optional**)
7. Click on **save reminder**
8. Make sure to give Background permission
9. It's all set and you're geofence is active
