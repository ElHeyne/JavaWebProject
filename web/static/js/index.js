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

function delgeneral() {
    let dhttp = new XMLHttpRequest();

    let alerta = "Elimina toda la información de la app (Parkings, Plazas, Usuarios...). También reinicia todas las ID de la base de datos.\n\nAcepta o Cancela la operación.";
    if (confirm(alerta) == true) {

        dhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                get();
            };
        };
    
        dhttp.open("DELETE", "http://localhost:8080/JavaWebProject/Armagedon", true);
        dhttp.send();
    } else {
        get();
    }
}

function postgeneral() {
    let dhttp = new XMLHttpRequest();

    let alerta = "Añade varios ejemplos de parkings e información variada.";
    if (confirm(alerta) == true) {

        dhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                get();
            };
        };

        dhttp.open("POST", "http://localhost:8080/JavaWebProject/Armagedon", true);
        dhttp.send();
    } else {
        get();
    }
}

function detailParking(id) {
    window.location.href = "details.html?id=" + id;
}