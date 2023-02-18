# Location Reminder
It is a TO-DO list application with a location feature that reminds the user 
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
## Built with
* [Kotlin](https://kotlinlang.org/): Default language used to build this project
* [RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView): A ViewGroup that contains the views corresponding to your data. Each individual element in the list is defined by a view holder object.
* [Navigation Component](https://developer.android.com/guide/navigation): Android Jetpack's Navigation component, used for Fragment-based navigation
* [Coroutines](https://developer.android.com/kotlin/coroutines): A concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
* [Firebase Auth](https://firebase.google.com/docs/auth/android/google-signin): backend services for use the SDKs and in-built UI libraries to authenticate the user in the application
* [Mockito](https://github.com/mockito/mockito): a Java-based framework used for unit testing of Java applications.
* [Maps SDK](https://developers.google.com/maps/documentation/android-sdk/overview): a library for embedding highly customized maps within Android applications
* [Material Component](https://developer.android.com/jetpack/compose/layouts/material): Interactive building blocks for creating a user interface.
* [Android Architecture Components](): a collection of libraries that help design robust, testable, and maintainable apps: Room (a SQLite object mapping library), LiveData (builds data objects that notify views when the underlying database changes), ViewModel (stores UI-related data that isn't destroyed on app rotations)
* [Databinding](https://developer.android.com/topic/libraries/data-binding): A Jetpack support library that allows to bind UI components in your layouts to data sources in your app using a declarative format rather than programmatically
* [Android Jetpack WorkManager library](https://developer.android.com/jetpack/androidx/releases/work): allows to schedule work to run one-time or repeatedly using flexible scheduling windows.

## ScreenShots
<table>
  <tr>
    <td align="center">Welcoming Page</td>
     <td align="center">Selecting Login method</td>
     <td align="center">Selection Map</td>
  </tr>
  <tr>
    <td><img src="/screenshots/Screenshot_2023-02-04-03-41-21-025_com.udacity.project4.jpg"></td>
   <td><img src="/screenshots/Screenshot_2023-02-04-03-41-27-782_com.udacity.project4.jpg"></td>
   <td><img src="/screenshots/Screenshot_2023-02-04-03-43-05-671_com.udacity.project4.jpg"></td>
  </tr>
  <tr>
     <td align="center">Selecting Location</td>
      <td align="center">Details Fragment</td>
      <td align="center">Listing Fragment</td>
   </tr>
   <tr>
     <td><img src="/screenshots/Screenshot_2023-02-04-03-43-43-694_com.udacity.project4.jpg"></td>
    <td><img src="/screenshots/Screenshot_2023-02-04-03-43-47-656_com.udacity.project4.jpg"></td>
    <td><img src="/screenshots/Screenshot_2023-02-04-03-44-01-546_com.udacity.project4.jpg"></td>
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
