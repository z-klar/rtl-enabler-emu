
function start() {
    setInterval(ask_data, 300);
}

function ask_data() {
    //location.reload(true);

    var xhttp = new XMLHttpRequest();
    var url2 = document.baseURI;   // http://localhost:9999/enabler
    npos = url2.lastIndexOf("/");
    url2 = url2.substring(0, npos) + '/enabler/abtbuttons';
    //console.log('URL2: ' + url2);

    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //console.log('Data:' + this.responseText);
            const obj = JSON.parse(this.responseText);
            element = document.getElementById("btn_abt_home");
            if(obj.abtButtonHome) element.className="btn btn-success  btn-block";
            else element.className="btn btn-danger btn-block";
            element = document.getElementById("btn_abt_menu");
            if(obj.abtButtonMenu) element.className="btn btn-success  btn-block";
            else element.className="btn btn-danger btn-block";
            element = document.getElementById("btn_abt_power");
            if(obj.abtButtonPower) element.className="btn btn-success  btn-block";
            else element.className="btn btn-danger btn-block";
            element = document.getElementById("abt_buttons_log");
            element.value = obj.abtButtonsMessage1;
        }
    };
    xhttp.open("GET", url2, true);
    xhttp.setRequestHeader("Content-type", "application/xml");
    xhttp.send();
}

