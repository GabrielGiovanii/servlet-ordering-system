function getUserModalHtml(insertOrUpdate) {
    let modalTitle;
    let passwordPlaceHolder;
    let confirmPasswordPlaceHolder;

    if (insertOrUpdate === "insert") {
        modalTitle = "Registrar Usuário";
        passwordPlaceHolder = "Insira sua senha";
        confirmPasswordPlaceHolder = "Confirme sua senha";
    } else if (insertOrUpdate === "update") {
        modalTitle = "Alterar Usuário";
        passwordPlaceHolder = "Insira sua nova senha";
        confirmPasswordPlaceHolder = "Confirme sua nova senha";
    }

    let userModalHtml =  `
        <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">${modalTitle}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="modalForm">
                            <div class="mb-3">
                                <label class="form-label" for="id">Id</label>
                                <input class="form-control" id="id" name="id" type="text" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="name">Nome*</label>
                                <input class="form-control" id="name" name="name" type="text" placeholder="Insira seu nome"
                                    required maxlength="100">
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="email">E-mail*</label>
                                <input class="form-control" id="email" name="email" type="email"
                                    placeholder="Insira seu e-mail" required maxlength="100">
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="phone">Telefone*</label>
                                <input class="form-control" id="phone" name="phone" type="text"
                                    placeholder="Insira seu telefone" required maxlength="100">
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="password">Senha*</label>
                                <input class="form-control" id="password" name="password" type="password"
                                    placeholder="${passwordPlaceHolder}" required maxlength="100">
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="confirmPassword">Confirmação de Senha*</label>
                                <input class="form-control" id="confirmPassword" name="confirmPassword" type="password"
                                    placeholder="${confirmPasswordPlaceHolder}" required maxlength="100">
                            </div>
                            <div class="mb-3">
                                <input id="roleCode" name="roleCode" type="number" style="display: none;">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                        <button type="button" class="btn btn-primary" id="modalSaveButton">Salvar</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    return userModalHtml;
}

async function setUserDataInModal() {
    let userId = document.getElementById("authenticatedUserId").value;

    let body = await findUserById(userId);

    document.querySelector('.modal-dialog #id').value = body.id;
    document.querySelector('.modal-dialog #name').value = body.name;
    document.querySelector('.modal-dialog #email').value = body.email;
    document.querySelector('.modal-dialog #phone').value = body.phone;
}

function getUserModal(insertOrUpdate, roleCode) {
    let registerModalElement = document.getElementById("registerModal");

    if (registerModalElement) {
        registerModalElement.remove();
    }

    document.body.insertAdjacentHTML('beforeend', getUserModalHtml(insertOrUpdate));

    if (insertOrUpdate === "insert") {
        document.querySelector(".modal-dialog #modalSaveButton").setAttribute("onclick", `saveUser(${roleCode}, 'insert')`);
    } else if (insertOrUpdate === "update") {
        document.querySelector(".modal-dialog #modalSaveButton").setAttribute("onclick", `saveUser(${roleCode}, 'update')`);
        setUserDataInModal();
    }

    showCustomEntityFormModal();
}

function getConfirmationModal(message, action) {
    let registerModalElement = document.getElementById("confirmationModal");

    if (registerModalElement) {
        registerModalElement.remove();
    }

    document.body.insertAdjacentHTML('beforeend', getConfirmationModalHTML(message, action));

    showCustomConfirmationModal();
}

function getPaymentModalHtml(orderId) {
    let userModalHtml =  `
        <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">Pagar Pedido de id: ${orderId}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="modalForm">
                            <div class="mb-3">
                            <select class="form-select" id="paymentMethodSelect" aria-label="Método de Pagamento">
                                <option selected>Selecione um método de pagamento</option>
                                <option value="1">PIX</option>
                                <option value="2">Cartão de Crédito</option>
                                <option value="3">Cartão de Débito</option>
                            </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                        <button type="button" class="btn btn-primary" id="modalSaveButton" onclick="makePaymentForOrder(${orderId})">Salvar</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    return userModalHtml;
}

function getPaymentModal(orderId) {
    let registerModalElement = document.getElementById("registerModal");

    if (registerModalElement) {
        registerModalElement.remove();
    }

    document.body.insertAdjacentHTML('beforeend', getPaymentModalHtml(orderId));

    showCustomEntityFormModal();
}

function getCategoryModalHtml(insertOrUpdate) {
    let modalTitle;

    if (insertOrUpdate === "insert") {
        modalTitle = "Registrar Categoria";
    } else if (insertOrUpdate === "update") {
        modalTitle = "Alterar Categoria";
    }

    let categoryModalHtml =  `
        <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">${modalTitle}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="modalForm">
                            <div class="mb-3">
                                <label class="form-label" for="id">Id</label>
                                <input class="form-control" id="id" name="id" type="text" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="name">Nome*</label>
                                <input class="form-control" id="name" name="name" type="text" placeholder="Insira o nome da categoria"
                                    required maxlength="100">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                        <button type="button" class="btn btn-primary" id="modalSaveButton">Salvar</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    return categoryModalHtml;
}

async function setCategoryDataInModal(categoryId) {
    if (categoryId) {
        let body = await findCategoryById(categoryId);

        document.querySelector('.modal-dialog #id').value = body.id;
        document.querySelector('.modal-dialog #name').value = body.name;
    }
}

function getCategoryModal(insertOrUpdate, categoryId) {
    let registerModalElement = document.getElementById("registerModal");

    if (registerModalElement) {
        registerModalElement.remove();
    }

    document.body.insertAdjacentHTML('beforeend', getCategoryModalHtml(insertOrUpdate));

    if (insertOrUpdate === "insert") {
        document.querySelector(".modal-dialog #modalSaveButton").setAttribute("onclick", "insertCategoryInTable()");
    } else if (insertOrUpdate === "update") {
        document.querySelector(".modal-dialog #modalSaveButton").setAttribute("onclick", `updateCategoryInTable(${categoryId})`);
        setCategoryDataInModal(categoryId);
    }

    showCustomEntityFormModal();
}