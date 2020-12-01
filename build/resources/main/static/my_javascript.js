

var timer_var;

function relay_pressed(relay_no) {
    console.log("relay [" + relay_no + "] pressed");
    //location.reload();
    var btnName = 'btn_relay' + (relay_no +1);
    element = document.getElementById(btnName);
    var command = "a"
    if(element.className.indexOf("danger") != -1) {
        console.log("   Try to set: DO[" + (relay_no+1) + "] = 1" );
        command = "s";
    }
    else {
        console.log("   Try to set: DO[" + (relay_no+1) + "] = 0" );
        command = "r";
    }

    var xhttp = new XMLHttpRequest();
    var url2 = document.baseURI;   // http://localhost:9999/emulator
    npos = url2.lastIndexOf("/");
    url2 = url2.substring(0, npos) + '/set.xml?type=' + command + "&id=" + (relay_no+1);
    console.log('URL2: ' + url2);
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
             console.log("Resp: " + this.responseText);
             update_view();
         }
    };
    xhttp.open("GET", url2, true);
    xhttp.setRequestHeader("Content-type", "application/xml");
    xhttp.send();
}

function update_view() {
    var xhttp = new XMLHttpRequest();
    var url2 = document.baseURI;   // http://localhost:9999/emulator
    npos = url2.lastIndexOf("/");
    url2 = url2.substring(0, npos) + '/fresh.xml';
    console.log('URL2: ' + url2);
    xhttp.onreadystatechange = function() {
         if (this.readyState == 4 && this.status == 200) {
            // alert(this.responseText);
             parser = new DOMParser();
             xmlDoc = parser.parseFromString(this.responseText, "text/xml");

            var relays = xmlDoc.getElementsByTagName("dout");
            for (var i = 0; i < relays.length; i++) {
                var name = relays[i];
                var ord = name.getAttribute("id");
                var value =  name.getAttribute("val");
                //console.log("  - " + i + ":  ID=" + ord + "   VAL=" + value);
                var btnName = 'btn_relay' + (ord);
                element = document.getElementById(btnName);
                if(element != null) {
                    if(value == 0) element.className="btn btn-danger  btn-block";
                    else element.className="btn btn-success btn-block";
                }
            }
         }
    };
    xhttp.open("GET", url2, true);
    xhttp.setRequestHeader("Content-type", "application/xml");
    xhttp.send();

}

function update_timer() {
    console.log(" UpdateTimer .... ");
    element = document.getElementById("btn_update_timer");
    if(element.className.indexOf("danger") != -1) {
        console.log("   Try to start timer" );
        element.className="btn btn-success btn-block";
        timer_var = setInterval(ask_data, 300);
    }
    else {
        console.log("   Try to stop timer" );
        element.className="btn btn-danger btn-block";
        clearInterval(timer_var);
    }
}

function ask_data() {
    update_view();
}

function abt_pressed() {
    var port = 0;
    console.log(" ABTPressed .... ");
    var ePort = document.getElementById("abt_port");
    port = element.value;
    var element = document.getElementById("btn_abt");
    if(element.className.indexOf("danger") != -1) {  // was stopped
        element.className="btn btn-success btn-block";
        switch_video(1, 1, port);
    }
    else {   // was running
        switch_video(1, 0, port);
        element.className="btn btn-danger btn-block";
    }
}

function fpk_pressed() {

}

function hud_pressed() {

}

/*--------------------------------------------------------
  stream_type: 1=ABT, 2=FPK, 3=HUD
  new_state:   0=Switch OFF, 1=Switch ON
---------------------------------------------------------*/
function switch_video(stream_type, new_state, port) {

    console.log("SWITCH_VIDEO: Stream=" + stream_type + ",  NewState=" + new_state + ",  Port=" + port);


}

