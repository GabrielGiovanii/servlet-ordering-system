const priceFormat = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

function formatPrice(price) {
    return priceFormat.format(price);
}

function unformatPrice(formattedPrice) {
    return parseFloat(formattedPrice.replace(/[\R$\s.]/g, '').replace(',', '.'));
}

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
                        <p class="text-center all-texts">${truncateText(product.categoryName, 17)}</p>
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
        alert("Quantidade não pode ser 0");
        return;
    }

    let product = products.find(p => p.id === productId);
    let cartItem = cart.find(item => item.id === productId);

    if (!cartItem) {
        addProductToCartTable(product, quantity);
    } else {
        alert("Produto já existente no carrinho, atualize o carrinho para mudanças.");
    }
}

function addProductToCartTable(product, quantity) {
    cart.push({ ...product, quantity });

    let subtotal = product.price * quantity;

    let cartLine = `
        <tr>
            <td>${product.name}</td>
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

    let total = unformatPrice(totalTd.textContent);
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

function finalizeOrder() {
    if (cart.length === 0) {
        alert("Carrinho vazio. Adicione itens antes de finalizar o pedido.");
        return;
    }

    /*let cartTableContainer = document.querySelector("#cartTable tbody");
    cartTableContainer.innerHTML = "";*/
}

//orders
const orders = [];