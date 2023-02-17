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

function insertData() {

    var result = confirm("Excel Data를 Table에 넣으시겠습니까?");
    if (result == true) {
        const fileInput = document.getElementById('fileInput');
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);

        const xhr = new XMLHttpRequest();
        xhr.open('Post', '/inputData', true);
        xhr.onreadystatechange = function() {

            if (xhr.readyState === 4 && xhr.status === 200) {
                const result = JSON.parse(this.responseText);
                if (result.status === 'success') {
                    alert('InputData successful');

                } else {
                    alert('InputData failed');
                }
                getUrl('/showData');
            }
        };
        xhr.send(formData);
    } else if (result == false) {}
}

function getUrl(newUrl) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', newUrl, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const newContent = xhr.responseText;
            document.body.innerHTML = newContent;
        }
    };
    xhr.send();
}