function send(){
    var http = new XMLHttpRequest();

    http.open("GET", "http://localhost:8080/WebTest/Servlet", true);
    http.send();

    http.onreadystatechange = function () {
        if (http.readyState == 4 && http.status == 200) {
            document.getElementById("content").innerText = http.responseText;
        }
    }
}