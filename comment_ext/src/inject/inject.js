function sendURL(response) {
var url = document.URL;
if (url.substring(0, 25) == "http://imgur.com/gallery/") {
	$.post("http://127.0.0.1:5000/thing", {url: url});
}
}

chrome.extension.sendMessage({}, sendURL);
