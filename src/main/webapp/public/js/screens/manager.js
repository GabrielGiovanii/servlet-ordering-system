//categories
const categories = [];

async function loadCategories() {
    let orderTableContainer = document.querySelector("#categoryTable tbody");
    orderTableContainer.innerHTML = '';

    let result = await findCategories();

    if (!result) {
        return;
    }

    categories.length = 0;
    result.forEach(category => categories.push(category));

    addCategoryToCategoryTable(result);
}

function addCategoryToCategoryTable(responseBody) {
    let orderTableContainer = document.querySelector("#categoryTable tbody");

    responseBody.forEach(item => {
        let categoryId = item.id;

        let orderLine = `
        <tr>
            <td id="categoryId" class="text-center">${categoryId}</td>
            <td id="categoryName" class="text-center">${truncateText(item.name, 28)}</td>
            <td class="text-center">
                <button class="btn btn-success btn-sm" onclick="getCategoryModal('update', ${categoryId})">Alterar</button>
                <button class="btn btn-danger btn-sm" 
                    onclick="getConfirmationModal('Tem certeza que deseja deletar a categoria com id ${categoryId}?', 'deleteCategoryInTable(${categoryId})')"
                    >Deletar
                </button>
            </td>
        </tr>
    `;

    orderTableContainer.innerHTML += orderLine;
    });
}

async function insertCategoryInTable() {
    let responseBody = await saveCategory(null, 'insert');

    if (responseBody) {
        categories.push(responseBody);

        responseBody = [responseBody];
        addCategoryToCategoryTable(responseBody);
    }
}

function updateOrDeleteCategoryRow(updateOrDelete, responseBody, categoryId) {
    let categoryTableContainer = document.querySelector("#categoryTable tbody");

    if (!categoryId) {
        categoryId = responseBody.id;
    }

    let rowElementToDelete;

    categoryTableContainer.querySelectorAll("tr").forEach(row => {
        let categoryIdElement = row.querySelector("#categoryId");

        if (parseInt(categoryIdElement.textContent) === categoryId) {
            if (updateOrDelete === 'update') {
                let nameElement = row.querySelector("#categoryName");

                nameElement.innerHTML = responseBody.name;
            } else if (updateOrDelete === 'delete') {
                rowElementToDelete = row;
            }
        }
    });

    if (rowElementToDelete) {
        rowElementToDelete.remove();
    }
}

async function updateCategoryInTable(categoryId) {
    let responseBody = await saveCategory(categoryId, 'update');

    if (responseBody) {
        let itemIndex = categories.findIndex(item => item.id === categoryId);

        let item = categories[itemIndex];

        if (item) {
            updateOrDeleteCategoryRow('update', responseBody, null);
        }
    }
}

async function deleteCategoryInTable(categoryId) {
    let responseBody = await deleteCategoryById(categoryId);

    if (responseBody) {
        let itemIndex = categories.findIndex(item => item.id === categoryId);

        let item = categories[itemIndex];

        if (item) {
            updateOrDeleteCategoryRow('delete', responseBody, categoryId);
        }
    }
}

function cleaningCategoryNameAndId() {
    document.getElementById("categoryName").value = "";
    document.getElementById("categoryId").value = null;
}

//products
const products = [];

async function loadProducts() {
    let result = await findProducts();

    if (!result) {
        return;
    }

    products.length = 0;
    result.forEach(product => products.push(product));

    let productContainer = document.querySelector(".card-line");
    productContainer.innerHTML = '';

    addProductIntoCardLine(result);
}

function addProductIntoCardLine(responseBody) {
    let productContainer = document.querySelector(".card-line");

    responseBody.forEach(product => {
        let productCardHTML = `
            <div class="col-3 custom-card">
                <div class="row">
                    <div class="col text-center">
                        <h1 class="fs-5 all-texts" id ="productNameId">${truncateText(product.name, 28)}</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col text-center">
                        <img src="${product.imgUrl}" class="card-img-top custom-img" id ="imgUrlId">
                    </div>
                </div>
                <div class="row">
                    <div class="col-6">
                        <p class="text-center fw-bold">
                        Categoria:
                        </p>
                    </div>
                    <div class="col-6">
                        <p class="text-center all-texts" id="categoryNameId">${truncateText(product.categoryName, 15)}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-6">
                        <p class="text-center fw-bold">
                        Preço:
                        </p>
                    </div>
                    <div class="col-6">
                        <p class="text-center" id="priceProductId">${formatPrice(product.price)}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <p class="card-description" id="descriptionProductId">${truncateText(product.description, 107)}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4 offset-2">
                        <button type="button" class="btn btn-primary" onclick="getProductModal('update', ${product.id})">Alterar</button>
                    </div>
                    <div class="col-4">
                        <button type="button" class="btn btn-danger" onclick="getConfirmationModal('Tem certeza que deseja apagar o produto com id ${product.id}?', 'deleteProductInCardLine(${product.id})')">Excluir</button>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <p class="text-center all-texts fw-bold">Id: ${product.id}</p>
                        <input type="hidden" id="productId" value="${product.id}">
                    </div>
                </div>
            </div>
        `;

        productContainer.innerHTML += productCardHTML;
    });
}

async function insertProductInCardLine() {
    let responseBody = await saveProduct(null, 'insert');

    if (responseBody) {
        products.push(responseBody);

        responseBody = [responseBody];
        addProductIntoCardLine(responseBody);
    }
}

function updateOrDeleteProductCol(updateOrDelete, responseBody, productId) {
    let productContainer = document.querySelector(".card-line");

    if (!productId) {
        productId = responseBody.id;
    }

    let cardElementToDelete;

    productContainer.querySelectorAll(".custom-card").forEach(card => {
        let productIdElement = card.querySelector("#productId");

        if (parseInt(productIdElement.value) === productId) {
            if (updateOrDelete === 'update') {
                let productNameIdElement = card.querySelector("#productNameId");
                let imgUrlIdElement = card.querySelector("#imgUrlId");
                let categoryNameIdElement = card.querySelector("#categoryNameId");
                let priceProductIdElement = card.querySelector("#priceProductId");
                let descriptionProductIdElement = card.querySelector("#descriptionProductId");

                productNameIdElement.innerHTML = truncateText(responseBody.name, 28);
                imgUrlIdElement.src = responseBody.imgUrl;
                categoryNameIdElement.innerHTML = truncateText(responseBody.categoryName, 15);
                priceProductIdElement.innerHTML = responseBody.price;
                descriptionProductIdElement.innerHTML = truncateText(responseBody.description, 107);
            } else if (updateOrDelete === 'delete') {
                cardElementToDelete = card;
            }
        }
    });

    if (cardElementToDelete) {
        cardElementToDelete.remove();
    }
}

async function updateProductInCardLine(productId) {
    let responseBody = await saveProduct(categoryId, 'update');

    if (responseBody) {
        let itemIndex = products.findIndex(item => item.id === productId);

        let item = products[itemIndex];

        if (item) {
            updateOrDeleteProductCol('update', responseBody, null);
        }
    }
}

async function deleteProductInCardLine(productId) {
    let responseBody = await deleteProductById(productId);

    if (responseBody) {
        let itemIndex = products.findIndex(item => item.id === productId);

        let item = products[itemIndex];

        if (item) {
            updateOrDeleteProductCol('delete', responseBody, productId);
        }
    }
}

function cleanProductNameAndId() {
    document.getElementById("productName").value = "";
    document.getElementById("productId").value = null;
}

//orders
const orders = [];

function addOrderToOrderTable(responseBody) {
    let orderTableContainer = document.querySelector("#orderTable tbody");

    let buttonInsertedInOrder = false;

    responseBody.orderItems.forEach(item => {
        let statusName = Object.values(OrderStatus).find(status => status.code === responseBody.orderStatusCode)?.name;

        let orderLine = `
        <tr>
            <td id="orderId" class="text-center">${responseBody.id}</td>
            <td class="text-center">${responseBody.clientId}</td>
            <td class="text-center">${formatDate(responseBody.moment)}</td>
            <td id="orderStatusName" class="text-center">${statusName}</td>
            <td>${item.productName}</td>
            <td>${formatPrice(item.price)}</td>
            <td class="text-center">${item.quantity}</td>
            <td>${formatPrice(item.subtotal)}</td>
            <td>${formatPrice(responseBody.total)}</td>
        </tr>
    `;

    buttonInsertedInOrder = true;

    orderTableContainer.innerHTML += orderLine;
    });
}

async function loadOrders() {
    let orderTableContainer = document.querySelector("#orderTable tbody");
    orderTableContainer.innerHTML = '';

    let result = await findAllOrders();

    orders.length = 0;

    result.forEach(order => orders.push(order));

    orders.forEach(order => {
        addOrderToOrderTable(order);
    });
}

async function finalizeOrder() {
    if (cart.length === 0) {
        showCustomToast("Carrinho vazio. Adicione itens antes de finalizar o pedido.", "orange");
        return;
    }

    let orderBody = {
        orderItems: cart.map(item => ({
            productId: item.id,
            quantity: item.quantity
        }))
    };

    let responseBody = await insertOrder(orderBody);

    orders.push(responseBody);

    addOrderToOrderTable(responseBody);

    cart.length = 0;
    document.querySelector("#cartTable tbody").innerHTML = "";
    document.getElementById("totalAmount").innerHTML = "";
}

async function cancelOrder(id) {
    let responseBody = await updateOrder(id);

    let orderTableContainer = document.querySelector("#orderTable tbody");

    let statusName = Object.values(OrderStatus).find(status => status.code === responseBody.orderStatusCode)?.name;

    orderTableContainer.querySelectorAll("tr").forEach(row => {
        let orderIdElement = row.querySelector("#orderId");
        
        if (parseInt(orderIdElement.textContent) === responseBody.id) {
            let orderStatusNameElement = row.querySelector("#orderStatusName");
            let orderActionsElement = row.querySelector("#orderActions");
            
            orderStatusNameElement.innerHTML = statusName;
            orderActionsElement.innerHTML = "";
        }
    });
}

async function makePaymentForOrder(orderId) {
    let paymentMethodSelectElement = document.getElementById("paymentMethodSelect");

    let responseBody = await insertPayment(orderId, paymentMethodSelectElement.value);

    let orderTableContainer = document.querySelector("#orderTable tbody");

    let statusName = Object.values(OrderStatus).find(status => status.code === 2)?.name;

    orderTableContainer.querySelectorAll("tr").forEach(row => {
        let orderIdElement = row.querySelector("#orderId");

        if (parseInt(orderIdElement.textContent) === responseBody.orderId) {
            let orderStatusNameElement = row.querySelector("#orderStatusName");
            let orderActionsElement = row.querySelector("#orderActions");

            orderStatusNameElement.innerHTML = statusName;
            orderActionsElement.innerHTML = "";

            document.querySelector('#registerModal .btn-secondary').click();
        }
    });
}