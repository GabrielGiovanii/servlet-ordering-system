async function insertPayment(orderId, paymentMethodCode) {
    try {
        let body = {
            orderId: orderId,
            paymentMethodCode: paymentMethodCode
        };

        let response = await makeApiRequest('payments', 'POST', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na atualização de usuário.", "red");
    }
}

async function findAllPayments() {
    try {
        let response = await makeApiRequest('payments', 'GET', null, true);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de categorias.", "red");
    }
}

async function findPaymentById(id) {
    try {
        let response = await makeApiRequest('payments/' + id, 'GET', null, false);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de usuário pelo id.", "red");
     }
}