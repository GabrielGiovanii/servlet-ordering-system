async function insertOrder(body) {
    try {
        let response = await makeApiRequest('orders', 'POST', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na inserção de usuário.", "red");
    }
}

async function updateOrder(id) {
    try {
        let body = {
            id: id
        };

        let response = await makeApiRequest('orders', 'PUT', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na atualização de usuário.", "red");
    }
}

async function findAllOrders() {
    try {
        let response = await makeApiRequest('orders', 'GET', null, true);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de categorias.", "red");
    }
}

async function findOrderById(id) {
    try {
        let response = await makeApiRequest('orders/' + id, 'GET', null, false);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de usuário pelo id.", "red");
     }
}