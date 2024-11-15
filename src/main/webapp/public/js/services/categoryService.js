async function saveCategory(categoryId, insertOrUpdate) {
    let name = document.querySelector('.modal-dialog #name').value;

    if (!name) {
        showCustomToast("Nome é obrigatório, deve ser preenchido.", "red");
        return;
    }

    let body = {
        id: categoryId,
        name: name
    };

    let responseBody;
    if (insertOrUpdate === 'insert') {
        responseBody = await insertCategory(body);

        document.getElementById('id').value = responseBody.id;
    } else if (insertOrUpdate === 'update') {
        responseBody = updateCategory(body);
    }

    return responseBody;
}

async function findCategories() {
    let categoryIdElement = document.getElementById("categoryId");

    let categoryId = parseInt(categoryIdElement.value);

    let body;
    if (categoryId) {
        body = await findCategoryById(categoryId);
        body = body ? [body] : [];
    } else {
        body = await findAllCategories();
    }

    return body;
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
        let response = await makeApiRequest('categories/' + id, 'GET', null, false);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de categoria pelo id.", "red");
     }
}

async function insertCategory(body) {
    try {
        let response = await makeApiRequest('categories', 'POST', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na inserção de categoria.", "red");
    }
}

async function updateCategory(body) {
    try {
        let response = await makeApiRequest('categories', 'PUT', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na atualização de categoria.", "red");
    }
}

async function deleteCategoryById(id) {
    try {
        let response = await makeApiRequest('categories/' + id, 'DELETE', null, true);

        return response;
    } catch (error) {
        showCustomToast("Erro inesperado na deleção de usuário pelo id.", "red");
     }
}