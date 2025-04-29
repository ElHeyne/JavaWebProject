window.onload = function () {
    const params = new URLSearchParams(window.location.search);
    const id = parseInt(params.get("id"));

    if (id) {
        fetchParkingDetails(id);
        fetchParkingSpots(id);
    }
};

function fetchParkingDetails(id) {
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("parkingDetailsTbody").innerHTML = xhttp.responseText;
        };
    };

    xhttp.open("GET", `http://localhost:8080/JavaWebProject/ParkingDetails?id=${id}`, true);
    xhttp.send();
}

function fetchParkingSpots(id) {
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("parkingSpotsTbody").innerHTML = xhttp.responseText;
        };
    };

    xhttp.open("GET", `http://localhost:8080/JavaWebProject/ParkingSpots?id=${id}`, true);
    xhttp.send();
}

function delParking(id) {
    let dhttp = new XMLHttpRequest();

    dhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            window.location.href = "index.html";
        };
    };

    dhttp.open("DELETE", "http://localhost:8080/JavaWebProject/Index?id=" + id, true);
    dhttp.send();
}

function postParkingSpots() {
    let phttp = new XMLHttpRequest();

    phttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("formTotalSpots").value = "";
            window.location.reload();
        };
    };

    phttp.open("POST", "http://localhost:8080/JavaWebProject/ParkingSpots", true);
    phttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    phttp.send("id=" + document.getElementById("detailsParkingId").innerText + "&total_spots=" + document.getElementById("formTotalSpots").value);
}
