function findCategories() {
    /*let productNameInput = document.getElementById("productName");*/

    if (productNameInput) {
        return findCategoryById(productNameInput.value);
    }
}

async function findAllCategories() {
    try {
        let response = await makeApiRequest('categories', 'GET', null);
        
        if (response.status === 200) {
            let body = await response.json();

            return body;
        } else if (response.status === 400) {
            showCustomToast("Requisição inadequada.", "orange");
        } else if (response.status === 500) {
            showCustomToast("Ocorreu um erro inesperado no servidor.", "red");
        } else {
            showCustomToast("Ocorreu um erro inesperado.", "red");
        }
    } catch (error) {
        showCustomToast("Ocorreu um erro inesperado.", "red");
    }
}

async function findCategoryById(id) {
    try {
        let response = await makeApiRequest('Categories/' + id, 'GET', null);
        
        if (response.status === 200) {
            showCustomToast("Sucesso ao buscar categoria pelo id.", "green");

            let body = await response.json();

            return body;
        } else if (response.status === 400) {
            showCustomToast("Requisição inadequada.", "orange");
        } else if (response.status === 500) {
            showCustomToast("Ocorreu um erro inesperado no servidor.", "red");
        } else {
            showCustomToast("Ocorreu um erro inesperado.", "red");
        }
    } catch (error) {
        showCustomToast("Ocorreu um erro inesperado.", "red");
    }
}