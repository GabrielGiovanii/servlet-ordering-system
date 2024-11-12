function findCategories() {
    /*let productNameInput = document.getElementById("productName");*/

    if (productNameInput) {
        return findCategoryById(productNameInput.value);
    }
}

async function findAllCategories() {
    try {
        let response = await makeApiRequest('categories', 'GET', null, true);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de categorias.", "red");
    }
}

async function findCategoryById(id) {
    try {
        let response = await makeApiRequest('Categories/' + id, 'GET', null, false);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de categoria pelo id.", "red");
     }
}