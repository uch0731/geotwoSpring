// const fileInput = document.getElementById('fileInput');
//  const formData = new FormData();
//  formData.append('file', fileInput.files[0]);
//
//  fetch('/upload', {
//    method: 'POST',
//    body: formData
//  })
//  .then(response => {
//    // handle the response from the server
//  })
//  .catch(error => {
//    // handle any errors
//  });
//}

// json 방식으로 넘기기
//function inputData() {
//  const fileInput = document.getElementById('fileInput');
//  const reader = new FileReader();
//  reader.onload = () => {
//    const data = reader.result;
//    const jsonData = { excelData: data };
//
//    fetch('/inputData', {
//      method: 'POST',
//      headers: {
//        'Content-Type': 'application/json'
//      },
//      body: JSON.stringify(jsonData)
//    })
//    .then(response => {
//      // handle the response from the server
//    })
//    .catch(error => {
//      // handle any errors
//    });
//  };
//  reader.readAsBinaryString(fileInput.files[0]);
//}
