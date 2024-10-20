async function makeApiRequest(resource, method, body) {
    const BASE_URL = 'http://localhost:8082/servlet-ordering-system/';
    let endpoint = BASE_URL + resource;

    try {
        let response = await fetch(endpoint, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'origin-of-request': 'api-servlet'
            },
            body: JSON.stringify(body)
        });

        return response;
    } catch (error) {
        showCustomToast("Houve um erro ao solicitar recursos ao servidor.", "red");
    }
}