function postData() {
    const xhr = new XMLHttpRequest();
    const url = '/connect';
    const host = document.getElementById('host').value;
    const port = document.getElementById('port').value;
    const sid = document.getElementById('sid').value;
    const userNm = document.getElementById('userNm').value;
    const userPW = document.getElementById('userPW').value;
    const dbType = document.getElementById('dbType').value;
    const params = `host=${host}&port=${port}&sid=${sid}&userNm=${userNm}&userPW=${userPW}&dbType=${dbType}`;
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            const result = JSON.parse(this.responseText);
            if (result.status === 'success') {
                alert('Connection successful');
                getUrl('/success');
//                location.replace('/success');

            } else {
                alert('Connection failed');
                getUrl('/');
//                location.replace('/');

            }
        }
    };
    xhr.send(params);
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

function postSchema() {
    const xhr = new XMLHttpRequest();
    const url = '/show';
    const schema = document.getElementById('schema').value;
    const params = `schema=${schema}`;
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            // Show table list under the button
            const tableListDiv = document.getElementById('table-list');
            tableListDiv.innerHTML = this.responseText;
        }
    };
    xhr.send(params);
}

function selectTableRow(button) {
    // Get the table row that contains the clicked button
    var row = button.parentNode.parentNode;

    // Get the data from the row
    var tableNm = row.cells[0].textContent;
    var params = "tableNm=" + encodeURIComponent(tableNm);
    // Send an AJAX request to the server to get the column information for the selected table
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/showCol', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const columnListDiv = document.getElementById('column-list');
            columnListDiv.innerHTML = this.responseText;
        }
    };
    xhr.send(params);
}