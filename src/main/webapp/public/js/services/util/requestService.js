async function makeApiRequest(resource, method, body, showSuccessMessage) {
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
        let httpStatus = response.status;

        if (httpStatus === 200 || httpStatus === 201 || httpStatus === 204) {
            if (showSuccessMessage) {
                showCustomToast("Sucesso ao realizar ação.", "green");
            }

            return response;
        } else if (httpStatus === 400) {
            showCustomToast(`Requisição inadequada para o recurso: '${resource}' e o método: '${method}'.`, "orange");
        } else if (httpStatus === 401) {
            if (resource === 'auth') {
                showCustomToast("E-mail ou senha incorretos ou usuário inexistente.", "orange");
            } else {
                showCustomToast(`Requisição não autorizada para o recurso: '${resource}' e o método: '${method}'.`, "orange");
            }
        } else if (httpStatus === 409) {
            showCustomToast(`Requisição com conflito de dados para o recurso: '${resource}' e o método: '${method}'.`, "red");
        } else if (httpStatus === 500) {
            showCustomToast(`Erro inesperado no servidor para o recurso: '${resource}' e o método: '${method}'.`, "red");
        } else {
            showCustomToast(`Ocorreu algo inesperado no recurso: '${resource}' e o método: '${method}'.`, "red");
        }
    } catch (error) {
        showCustomToast(`Erro inesperado ao fazer requisição no recurso: '${resource}' e o método: '${method}'.`, "red");
    }
}