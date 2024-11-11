async function findProducts() {
    let productNameInput = document.getElementById("productName");
    let productName = productNameInput.value;

    let body;

    if (productName) {
        body = await findAllProductsByName(productName);
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
        let response = await makeApiRequest('products?name=' + name, 'GET', null);
        
        if (response.status === 200) {
            showCustomToast("Sucesso ao buscar produtos pelo nome.", "green");

            return await response.json();
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

async function findAllProducts() {
    try {
        let response = await makeApiRequest('products', 'GET', null);
        
        if (response.status === 200) {
            showCustomToast("Sucesso ao buscar todos produtos.", "green");

            return await response.json();
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