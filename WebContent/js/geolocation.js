//https://www.w3schools.com/html/html5_geolocation.asp
var x = document.getElementById("geolocation_show_id");
var latitude, longitude;
getGeolocation();
function getGeolocation () {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(storePositions);
    } else { 
        x.innerHTML = "Geolocation must be allowed to get your location!";
    }
}
function storePositions(position) {
	latitude = position.coords.latitude;
	longitude = position.coords.longitude;
    x.innerHTML = "{ \"latitude\" : \"" + position.coords.latitude +
               "\", \"longitude\" : \"" + position.coords.longitude + "\" }"
}