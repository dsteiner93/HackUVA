chrome.webNavigation.onHistoryStateUpdated.addListener(function(details){
	chrome.tabs.getSelected(null,function(tab) {
		var vurl = tab.url;
    	sendURL(vurl);
	});
});

function sendURL(turl) {
	if (turl.substring(0, 25) == "http://imgur.com/gallery/") {
		$.post("http://127.0.0.1:5000/thing", {url: turl});
	}
}