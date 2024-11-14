//products
const products = [
];

async function loadProducts() {
    let productContainer = document.querySelector(".card-line");
    productContainer.innerHTML = '';

    let result = await findProducts();
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
                    <div class="col-5 offset-2">
                        <button type="button" class="btn btn-primary" onclick="addToCart(${product.id}, this)">Comprar</button>
                    </div>
                <div class="col-3">
                    <input type="number" class="custom-input" min="0" value="0" id="quantityInput">
                </div>
                </div>
            </div>
        `;

        productContainer.innerHTML += productCardHTML;
    });
}

function cleanProductName() {
    let productNameInput = document.getElementById("productName");
    productNameInput.value = "";
}

//cart
const cart = [];

function addToCart(productId, button) {
    let parentRow = button.closest('.row');
    let quantityInput = parentRow.querySelector('#quantityInput');

    let quantity = parseInt(quantityInput.value);

    if (quantity === 0) {
        showCustomToast("Quantidade do produto a comprar precisa ser maior que 0.", "orange");
        return;
    }

    let product = products.find(p => p.id === productId);
    let cartItem = cart.find(item => item.id === productId);

    if (!cartItem) {
        addProductToCartTable(product, quantity);
    } else {
        showCustomToast("Produto já está no carrinho, caso queria modificar a quantidade, atualize o carrinho.", "orange");
    }
}

function addProductToCartTable(product, quantity) {
    cart.push({ ...product, quantity });

    let subtotal = product.price * quantity;

    let cartLine = `
        <tr>
            <td>${truncateText(product.name, 28)}</td>
            <td>
                <input type="number" class="form-control" value="${quantity}" min="1" onchange="updateQuantity(${product.id}, this)">
            </td>
            <td>${formatPrice(product.price)}</td>
            <td id="subtotalCell">${formatPrice(subtotal)}</td>
            <td>
            <button class="btn btn-danger btn-sm" onclick="removeFromCart(${product.id}, this)">Remover</button>
            </td>
        </tr>
    `;

    let cartTableContainer = document.querySelector("#cartTable tbody");
    cartTableContainer.innerHTML += cartLine;

    updateCartTotal(subtotal);
}

function updateCartTotal(subtotal) {
    let totalTd = document.getElementById("totalAmount");

    let totalContent = unformatPrice(totalTd.textContent);
    let total = isNaN(totalContent) ? 0 : totalContent;
    total += subtotal;

    totalTd.innerText = `${formatPrice(total)}`;
}

function updateQuantity(productId, input) {
    let item = cart.find(item => item.id === productId);

    if (item) {
        let parentRow = input.closest('tr');
        let subtotalCell = parentRow.querySelector('#subtotalCell');

        let newQuantity = input.value;
        let oldSubtotal = item.price * item.quantity;

        item.quantity = parseInt(newQuantity);

        let newSubtotal = item.price * item.quantity;

        subtotalCell.textContent = formatPrice(newSubtotal);
        updateCartTotal(newSubtotal - oldSubtotal);
    }
}

function removeFromCart(productId, button) {
    let itemIndex = cart.findIndex(item => item.id === productId);
    let item = cart[itemIndex];

    if (item) {
        let parentRow = button.closest('tr');
        parentRow.remove();

        cart.splice(itemIndex, 1);

        let subtotal = item.price * item.quantity;
        updateCartTotal(subtotal * -1);
    }
}

//orders
const orders = [];

function addOrderToOrderTable(responseBody) {
    let orderTableContainer = document.querySelector("#orderTable tbody");

    let buttonInsertedInOrder = false;

    responseBody.orderItems.forEach(item => {
        let statusName = Object.values(OrderStatus).find(status => status.code === responseBody.orderStatusCode)?.name;
        let orderStatusCode = responseBody.orderStatusCode;

        let actionButtons;
        if (orderStatusCode === 1) {
            let orderId = responseBody.id;

            actionButtons =  `
                <button class="btn btn-success btn-sm" onclick="getPaymentModal(${orderId})">Pagar P.</button>
                <button class="btn btn-danger btn-sm" onclick="getConfirmationModal('Tem certeza que deseja cancelar o pedido com id ${orderId}?', 'cancelOrder(${orderId})')">Cancelar P.</button>
            `;
        }

        let orderLine = `
        <tr>
            <td id="orderId">${responseBody.id}</td>
            <td>${formatDate(responseBody.moment)}</td>
            <td id="orderStatusName">${statusName}</td>
            <td>${item.productName}</td>
            <td>${formatPrice(item.price)}</td>
            <td>${item.quantity}</td>
            <td>${formatPrice(item.subtotal)}</td>
            <td>${formatPrice(responseBody.total)}</td>
            <td id="orderActions">
                ${actionButtons && !buttonInsertedInOrder ? actionButtons : ""}
            </td>
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

async function makePayment(orderId) {
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