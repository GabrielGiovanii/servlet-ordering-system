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
        let response = await makeApiRequest('products?name=' + name, 'GET', null, true);
        
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