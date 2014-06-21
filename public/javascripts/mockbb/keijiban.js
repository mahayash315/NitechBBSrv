//	Link
function f_link(request_result) {
    document.form1.next_uri.value = request_result;
    document.form1.submit();
}

//	Event
function f_event(event) {
	document.form1.event_code.value = event;
    document.form1.submit();
}

//	Subwindow open
function f_open(width, height, target, next, option) {
	var	uri		= "";
	
	uri	= document.form1.action
			+	"?uri="			+ document.form1.uri.value
			+	"&next_uri="	+ next;
	if (option != "") {
		uri	= uri + "&" + option;
	}
	return window.open(uri, target, "width=" + width + ",height=" + height + ",scrollbars,resizable=yes");
}

//			
function createHttpRequest(callback) {
	var	xmlhttp = null;
	
	try {
		xmlhttp	= new XMLHttpRequest();
	} catch(e) {
		try {
			xmlhttp	= new ActiveXObject("Msxml2.XMLHTTP");
		} catch(e) {
			try {
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			} catch(e) {
				return null;
			}
		}
	}
	if (xmlhttp) {
		xmlhttp.onreadystatechange	= callback;
	}
	return xmlhttp;
}

//	Calendar
function calendar(obj_yyyy, obj_mm, obj_dd, date_yyyy, date_mm) {
    var     width       = 290;
    var     height      = 205;
	var		option		= "";
    var		sub_window	= null;
			
	option	= "owner_yyyy="	+ obj_yyyy +
			  "&owner_mm=" 	+ obj_mm +
			  "&owner_dd="	+ obj_dd +
			  "&date_yyyy="	+ date_yyyy +
			  "&date_mm="	+ date_mm;
	
	sub_window	= f_open(width, height, "KeijibanCalendar", "calendar", option);
	sub_window.focus();
	
	return sub_window;
}
	
//	Popup
var lay = new Array();
function mouseOver(comment, layX, layY, layW, layH){
	lay	= document.all("id_popup");
	lay.innerHTML 		 = comment;
	lay.style.left 		 = layX;
	lay.style.top 		 = layY;
	lay.style.width 	 = layW;
	lay.style.height 	 = layH;
	lay.style.visibility = "visible";
}

function mouseOut(n){
	lay = document.all("id_popup");
	lay.style.visibility = "hidden";
}
