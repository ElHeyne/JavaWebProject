function post() {
    let shttp = new XMLHttpRequest();

    shttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("name").value = "";
            document.getElementById("total_spots").value = "";
            window.location.reload();
        }
    };

    shttp.open("POST", "http://localhost:8080/JavaWebProject/Index", true);
    shttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    shttp.send("name=" + document.getElementById("name").value + "&total_spots=" + document.getElementById("total_spots").value);
}

function get() {
    let ghttp = new XMLHttpRequest();

    ghttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("parkingTbody").innerHTML = ghttp.responseText;
        };
    };

    ghttp.open("GET", "http://localhost:8080/JavaWebProject/Index", true);
    ghttp.send();
}

function del(id) {
    let dhttp = new XMLHttpRequest();

    dhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            get();
        };
    };

    dhttp.open("DELETE", "http://localhost:8080/JavaWebProject/Index?id=" + id, true);
    dhttp.send();
}

function detailParking(id) {
    window.location.href = "details.html?id=" + id;
}