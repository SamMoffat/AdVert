function httpGET(theUrl) {
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.open( "GET", theUrl, false );
  xmlHttp.send( null );
  return xmlHttp.responseText;
}

function httpPOST(theUrl) {
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.open( "POST", theUrl, false );
  xmlHttp.send( null );
  return xmlHttp.responseText;
}

function httpGETAsync(theUrl, callback) {
  var httpRequest = new XMLHttpRequest();
  httpRequest.onreadystatechange = function() {
    if (httpRequest.readyState == 4 && httpRequest.status == 200) {
      var data = httpRequest.responseText; 
      if (callback) {
        callback(data);
      }                   
    }
  };
  httpRequest.open('GET', theUrl, true); 
  httpRequest.send(null);
}

function httpPOSTAsync(theUrl, callback) {
  var httpRequest = new XMLHttpRequest();
  httpRequest.onreadystatechange = function() {
    if (httpRequest.readyState == 4 && httpRequest.status == 200) {
      var data = httpRequest.responseText; 
      if (callback) {
        callback(data);
      }                   
    }
  };
  httpRequest.open('POST', theUrl, true); 
  httpRequest.send(null);
}

function httpPOSTAsyncWToken(theUrl, token, callback) {
	var httpRequest = new XMLHttpRequest();
	httpRequest.onreadystatechange = function () {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var data = httpRequest.responseText;
			if (callback) {
				callback(data);
			}
 		}
	};
	httpRequest.open('POST', theUrl, true);
	httpRequest.setRequestHeader("Authorization", token);
	httpRequest.send(null);
}

function callbackToConsole(theData) {
  console.log(theData);
}

