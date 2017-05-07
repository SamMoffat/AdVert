var x = document.getElementById("geolocation_show_id_lat");
var y = document.getElementById("geolocation_show_id_lon");
var z = document.getElementById("geolocation_show_id");
var geoLat;
var geoLon;
getGeolocation();
//https://www.w3schools.com/html/html5_geolocation.asp
function getGeolocation () {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(storePositions);
    } else { 
    	if (x != null) {
    		x.innerHTML = "Geolocation must be allowed to get your location!";	
    	}
    }
}
function storePositions(position) {
	geoLat = position.coords.latitude;
	geoLon = position.coords.longitude;
	if (x != null) { x.innerHTML = position.coords.latitude; }
	if (y != null) { y.innerHTML = position.coords.latitude; }
	if (z != null) { z.innerHTML += "{ \"latitude\" : \"" + position.coords.latitude +
					"\", \"longitude\" : \"" + position.coords.longitude + "\" }"; }	
   
}