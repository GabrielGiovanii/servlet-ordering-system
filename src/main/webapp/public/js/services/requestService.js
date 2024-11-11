async function makeApiRequest(resource, method, body) {
    const BASE_URL = 'http://localhost:8082/servlet-ordering-system/api/';
    let endpoint = BASE_URL + resource;

    try {
        let requestOptions = {
            method: method,
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        };

        if (body) {
            requestOptions.body = JSON.stringify(body);
        }

        let response = await fetch(endpoint, requestOptions);

        return response;
    } catch (error) {
        showCustomToast("Houve um erro ao solicitar recursos ao servidor.", "red");
    }
}