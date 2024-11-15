//products
const products = [
];

async function loadProducts() {
    let productContainer = document.querySelector(".card-line");
    productContainer.innerHTML = '';

    let result = await findProducts();

    if (!result) {
        return;
    }

    products.length = 0;
    result.forEach(product => products.push(product));

    products.forEach(product => {
        let productCardHTML = `
            <div class="col-3 custom-card">
                <div class="row">
                    <div class="col text-center">
                        <h1 class="fs-5 all-texts">${truncateText(product.name, 28)}</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col text-center">
                        <img src="${product.imgUrl}" class="card-img-top custom-img" alt="...">
                    </div>
                </div>
                <div class="row">
                    <div class="col-6">
                        <p class="text-center fw-bold">
                        Categoria:
                        </p>
                    </div>
                    <div class="col-6">
                        <p class="text-center all-texts">${truncateText(product.categoryName, 15)}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-6">
                        <p class="text-center fw-bold">
                        Preço:
                        </p>
                    </div>
                    <div class="col-6">
                        <p class="text-center">${formatPrice(product.price)}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <p class="card-description">${truncateText(product.description, 107)}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-4 offset-2">
                        <button type="button" class="btn btn-primary" onclick="updateProduct(${product.id})">Alterar</button>
                    </div>
                    <div class="col-4">
                        <button type="button" class="btn btn-danger" onclick="deleteProduct(${product.id})">Excluir</button>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <p class="text-center all-texts fw-bold">Id: ${product.id}</p>
                    </div>
                </div>
            </div>
        `;

        productContainer.innerHTML += productCardHTML;
    });
}

function updateProduct() {
}

function deleteProduct() {
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