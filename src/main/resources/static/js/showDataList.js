function deleteData() {

    var result = confirm("DATA를 삭제하시겠습니까?");

    if(result == true){
        const xhr = new XMLHttpRequest();
        xhr.open('GET', '/deleteData', true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const newContent = xhr.responseText;
                document.body.innerHTML = newContent;
            }
        };
        xhr.send();
    }else if(result == false){

    }
}

function createExcel() {
    var result = confirm("Excel파일을 만드시겠습니까?");

        if(result == true){
            const xhr = new XMLHttpRequest();
            xhr.open('GET', '/createExcel', true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                }
            };
            xhr.send();
        }else if(result == false){

        }
}

