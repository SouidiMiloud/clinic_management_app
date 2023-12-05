function fetchData(url, requestMethod, requestBody, contentType){

    const info = {
        headers:{
            "Content-Type": contentType || "application/json",
        },
        method: requestMethod
    }
    if(info.method == 'POST'){
        if(info.headers["Content-Type"] == "application/json")
            info.body = JSON.stringify(requestBody);
        else info.body = requestBody;
    }
    if(localStorage.getItem('jwt'))
        info.headers.Authorization = `Bearer ${localStorage.getItem('jwt')}`;

    return fetch(`http://localhost:8090${url}`, info)
    .then(response=>{
        if(response.status === 200){
            const dataType = response.headers.get('content-type');
            if(dataType && dataType.includes('application/json'))
                return response.json();
            return response.text();
        }
    })
};
export default fetchData;