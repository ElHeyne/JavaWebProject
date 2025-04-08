function send(){
    var ehttp = new XMLHttpRequest();

    ehttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("name").value = "";  
        }
    };

    ehttp.open("POST", "http://localhost:8080/JavaWebProject/Index", true);
    ehttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    ehttp.send("name="+document.getElementById("name").value);
}

function get(){
    var http = new XMLHttpRequest();

    http.open("GET", "http://localhost:8080/JavaWebProject/Index", true);
    http.send();

    http.onreadystatechange = function () {
        if (http.readyState == 4 && http.status == 200) {
            document.getElementById("content").innerHTML = http.responseText;
        }
    }
}