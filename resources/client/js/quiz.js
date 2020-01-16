function pageLoad() {

    let now = new Date();

    let myHTML = '<div style="text-align:center;">'
        + '<h1></h1>'
        + '<img  class = "image" src="/client/img/190107_abcnl_davis_hpMain_16x9_992.jpg" height="500"; width = "6000";   alt="Logo"/>'
        + '<div style="font-style: bold;">'
        + 'Generated at ' + now.toLocaleTimeString()
        + '</div>'
        + '</div>';

    document.getElementById("testDiv").innerHTML = myHTML;
    checkLogin()
}