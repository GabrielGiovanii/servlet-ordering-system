async function saveProduct(productId, insertOrUpdate) {
    let id = document.querySelector('.modal-dialog #id').value;
    let name = document.querySelector('.modal-dialog #name').value;
    let description = document.querySelector('.modal-dialog #description').value;
    let price = document.querySelector('.modal-dialog #price').value;
    let imgUrl = document.querySelector('.modal-dialog #imgUrl').value;
    let categoryId = document.querySelector('#categorySelect').value;

    if (!id) {
        id = productId;
    }

    price = unformatPrice(price);

    if (!name || !price || !imgUrl) {
        showCustomToast("Todos os campos obrigatórios devem ser preenchidos.", "red");
        return;
    }

    let body = {
        id: id,
        name: name,
        description: description,
        price: price,
        imgUrl: imgUrl,
        categoryId: categoryId
    };

    let category = await findCategoryById(categoryId);

    let responseBody;
    if (insertOrUpdate === 'insert') {
        responseBody = await insertProduct(body);

        document.getElementById('id').value = responseBody.id;
    } else if (insertOrUpdate === 'update') {
        responseBody = await updateProduct(body);
    }

    responseBody.categoryName = category.name;

    return responseBody;
}

async function findProducts() {
    let productNameInput = document.getElementById("productName");
    let productIdElement = document.getElementById("productId");

    let productName = productNameInput.value;
    let productId = parseInt(productIdElement.value);

    let body;
    if (productName && productId) {
        showCustomToast("Não é possível pesquisar por nome e id do produto de maneira simultânea", "orange");
        return;
    } else if (productName) {
        body = await findAllProductsByName(productName);
    } else if (productId) {
        body = await findProductById(productId);
        body = body ? [body] : [];
    } else {
        body = await findAllProducts();
    }

    let categories = await findAllCategories();

    for (let product of body) {
        let categoryId = product.categoryId;
    
        let category = categories.find(cat => cat.id === categoryId);

        if (category) {
            product.categoryName = category.name;
        }
    
        delete product.categoryId;
    }

    return body;
}

async function findAllProductsByName(name) {
    try {
        let response = await makeApiRequest('products?name=' + name, 'GET', null, true);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de produtos pelo nome.", "red");
    }
}

async function findProductById(id) {
    try {
        let response = await makeApiRequest('products/' + id, 'GET', null, true);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de produtos pelo nome.", "red");
    }
}

async function findAllProducts() {
    try {
        let response = await makeApiRequest('products', 'GET', null, true);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de produtos.", "red");
    }
}

async function insertProduct(body) {
    try {
        let response = await makeApiRequest('products', 'POST', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na inserção de produto.", "red");
    }
}

async function updateProduct(body) {
    try {
        let response = await makeApiRequest('products', 'PUT', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na atualização de produto.", "red");
    }
}

async function deleteProductById(id) {
    try {
        let response = await makeApiRequest('products/' + id, 'DELETE', null, true);

        return response;
    } catch (error) {
        showCustomToast("Erro inesperado na deleção de produto pelo id.", "red");
     }
}