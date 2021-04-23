function fileChange() {
    let file = document.getElementById('input_img');
    let form = new FormData();
    form.append("image", file.files[0])
    let imgUrl = "https://api.imgbb.com/1/upload?key=" + imgKey;
    let settings = {
        "url": imgUrl,
        "method": "POST",
        "timeout": 0,
        "processData": false,
        "mimeType": "multipart/form-data",
        "contentType": false,
        "data": form
    };
    $.ajax(settings).done(function (response) {
            console.log(response);
            let jx = JSON.parse(response);
            let div = document.getElementById("json");
            const userImg = document.createElement("img");
            userImg.setAttribute("src", jx.data.url);
            const h2 = document.createElement("h2");
            h2.innerHTML = "Your image:";
            div.appendChild(h2);
            div.appendChild(userImg);
            const imageForm = document.getElementById("uploadForm");
            const urlInput = document.getElementById("url");
            urlInput.value = jx.data.url;
            imageForm.style.display = 'block';
            /*let imageString = jx.data.url + "," + jx.data.delete_url;
            console.log(imageString);
            div.innerHTML = jx.data.url;
            let request = new XMLHttpRequest();
            request.open("POST", "/images/upload", true);
            request.send(imageString);
            console.log(jx.data.delete_url);*/
        }
    );
}