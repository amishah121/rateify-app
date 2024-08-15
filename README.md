# Rateify App
Rateify is an Android app that allows users to explore music albums and leave them detailed ratings. Users can discover albums based on genre, search for albums, view album details, thumbs up/down individual tracks, provide a rating out of 10, and leave a description of their thoughts. Users can save the album to their library or share their rating with others. The app uses the Last.fm API to get album information, and Google Firebase for user authentication.

### Bugs and Issues
The app is currently under development and bugs and issues may be present.

### Features
* Explore page to discover albums based on genre
* Search for albums using the Last.fm API
* View album details such as the tracklist
* Leave a rating for the album out of 10
* Leave a description including thoughts about the album
* Thumbs up/down individual tracks on the album
* Add albums to library
* Share rating with others
* Authenticate using email/password

### Technologies Used
* **Firebase**: Uses Firebase Authentication to manage user authentication
* **Last.fm API**: Uses the Last.fm API to retreive album data and search for albums
* **Retrofit**: Uses Retrofit to make API requests and receive responses from the API
* **GSON Converter**: Uses GSON Converter to convert JSON API responses into Kotlin objects
* **Kotlin Coroutines**: Uses Kotlin coroutines to manage background tasks and asynchronous operations 

### Installation
To install the app, first clone the repository and open the project in Android Studio. <br /> 
Then, follow the instructions here to generate an API Key: https://www.last.fm/api/account/create. <br /> 
Inside the local.properties file add the following line:
`LASTFM_API_KEY=<your_api_key_here>` <br /> 
Now you should be able to run the project in Android Studio on an emulator or a physical device.

### Screenshots
[alt text](https://github.com/amishah121/music-app/blob/main/images/Screenshot%202024-08-15%20124840.png "Logo Title Text 1")
