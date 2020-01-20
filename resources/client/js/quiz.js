function pageLoad() {
    viewQuiz();
}
function viewQuiz(){
    let tableHTML = '<table>' +
        '<tr>' +
        '<th>Name</th>' +
        '</tr>';
debugger;
    fetch("/Tools/listAll/",{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {

            for (let quizNames of responseData){
                tableHTML += `<tr>` +
                    `<td>${quizNames.name}</td>` +
                    `</tr>`;
            }
           tableHTML += '</table>'
        }

        document.getElementById("tableHTML").innerHTML = tableHTML;

    })
}