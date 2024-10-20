function createToast() {
    let existingToast = document.querySelector("#utilBox #liveToast");

    if (existingToast) {
        return;
    }

    let box = document.getElementById("utilBox");

    const toastHTML = `
        <div class="toast-container position-fixed bottom-0 end-0 p-3">
            <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                    <strong class="me-auto" id="toastTitle"></strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body" id="toastBody"></div>
            </div>
        </div>
    `;

    box.innerHTML += toastHTML;
}

function showCustomToast(message, color) {
    createToast();

    let toastTitle = document.getElementById('toastTitle');
    toastTitle.style.color = color;

    let title;
    if (color === 'green') {
        title = "INFO";
    } else if (color === 'red') {
        title = "ATENÇÃO";
    } else if (color === "orange") {
        title = "AVISO";
    }

    toastTitle.innerText = title;

    document.getElementById('toastBody').innerText = message;

    let toastElement = document.getElementById('liveToast');
    let toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastElement);
    toastBootstrap.show();
}

function createModal() {
    let existingToast = document.querySelector("#utilBox #registerModal");

    if (!existingToast) {
        let box = document.getElementById("utilBox");

        const modalHTML = `
            <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="modalTitle"></h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="modalForm">
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                            <button type="button" class="btn btn-primary" id="saveButton">Salvar</button>
                        </div>
                    </div>
                </div>
        `;

        box.innerHTML += modalHTML;
    }

    let modalForm = document.querySelector("#utilBox #registerModal #modalForm");
    modalForm.innerHTML = "";
}

function createInputAndLabelForModalForm(labelContent, inputName, inputType, placeholder, isRequired, isReadOnly) {
    const div = document.createElement('div');
    div.className = 'mb-3';

    const label = document.createElement('label');
    label.className = 'form-label';
    label.textContent = labelContent;

    const input = document.createElement('input');
    input.className = 'form-control';
    input.id = inputName;
    input.name = inputName;
    input.type = inputType;
    input.placeholder = placeholder ? placeholder : '';
    input.required = isRequired;
    input.readOnly = isReadOnly;

    div.appendChild(label);
    div.appendChild(input);

    const container = document.getElementById('modalForm');
    container.appendChild(div);
}

function showCustomModal() {
    let modalElement = document.getElementById('registerModal');
    let modal = bootstrap.Modal.getOrCreateInstance(modalElement);

    modal.show();
}