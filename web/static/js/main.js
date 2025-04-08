function send() {
    let shttp = new XMLHttpRequest();

    shttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("name").value = "";
        }
    };

    shttp.open("POST", "http://localhost:8080/JavaWebProject/Index", true);
    shttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    shttp.send("name=" + document.getElementById("name").value);
}

function get() {
    let ghttp = new XMLHttpRequest();

    ghttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("content").innerHTML = ghttp.responseText;
        }
    };

    ghttp.open("GET", "http://localhost:8080/JavaWebProject/Index", true);
    ghttp.send();
}