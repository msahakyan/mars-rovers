# mars-rovers

<p>
Implemented an android application which loads from NASA-API and shows a photos which were done by different mars-rovers during of their missions.
</p>

# Application description
<p>
Application provides different pictures which were done by the mars-rovers <b>spirit</b>, <b>opportunity</b> and <b>curiosity</b> during of their exploration mission on Mars (see: <a href="https://en.wikipedia.org/wiki/Mars_Exploration_Rover">mars exploration rovers</a>). Each rover has its own set of photos stored in the database, which can be queried separately. There are several possible queries that can be made against the API. Photos are organized by the sol (Martian rotation or day) on which they were taken, counting up from the rover's landing date (see also: <a href="https://api.nasa.gov/api.html#MarsPhotos">NASA public API</a>).
</p>

Used technologies
<p>
For loading purposes were used <b>Android Loaders</b> (executing multiple loaders simultaneously) with <b>google-volley</b> library. 
</p>

# Screenshots from app
<img src="https://github.com/msahakyan/mars-rovers/blob/master/app/src/main/assets/rover_photos.png" width="240px" height="426px"></img>
<br/><br/>

<img src="https://github.com/msahakyan/mars-rovers/blob/master/app/src/main/assets/rover_select_fragment.png" width="240px" height="426px"></img>
