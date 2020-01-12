function pageLoad() {

    let now = new Date();

    let myHTML = '<div style="text-align:center;">'
        + '<h1>I once caught a fish and it was this big!</h1>'
        + '<img  class = "image" src="/client/img/bush-0710.jpg" height="200"; width = "2000";   alt="Logo"/>'
        + '<div style="font-style: bold;">'
        + 'Generated at ' + now.toLocaleTimeString()
        + '</div>'
        + '</div>';

    document.getElementById("testDiv").innerHTML = myHTML;
    checkLogin()
}
function checkLogin() {

    let username = Cookies.get("username");

    let logInHTML = '';

    if (username === undefined) {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "hidden";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "hidden";
        }

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a>";
    } else {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "visible";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "visible";
        }

        logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";
    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
