function getUserModalHtml() {
    return `
        <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">Registrar Usuário</h5>
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
                                    placeholder="Insira sua senha" required maxlength="100">
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="confirmPassword">Confirmação de Senha*</label>
                                <input class="form-control" id="confirmPassword" name="confirmPassword" type="password"
                                    placeholder="Confirme sua senha" required maxlength="100">
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
}

function getUserModal(insertOrUpdate) {
    let registerModalElement = document.getElementById("registerModal");

    if (registerModalElement) {
        registerModalElement.remove();
    }

    document.body.insertAdjacentHTML('beforeend', getUserModalHtml());

    if (insertOrUpdate === "insert") {
        document.querySelector(".modal-dialog #modalSaveButton").setAttribute("onclick", "saveUser(2, 'insert')");
    } else if (insertOrUpdate === "update") {
        document.querySelector(".modal-dialog #modalSaveButton").setAttribute("onclick", "saveUser(2, 'update')");
    }

    showCustomModal();
}