function createToast() {
    let existingToast = document.querySelector("#liveToast");

    if (existingToast) {
        return;
    }

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

    document.body.insertAdjacentHTML('beforeend', toastHTML);
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

function showCustomEntityFormModal() {
    let modalElement = document.getElementById('registerModal');
    let modal = bootstrap.Modal.getOrCreateInstance(modalElement);

    modal.show();
}

function getConfirmationModalHTML(message, action) {
        let userModalHtml =  `
            <div class="modal fade" id="confirmationModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="staticBackdropLabel">Tela de confirmação</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>${message}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="${action}">Confirmar</button>
                    </div>
                    </div>
                </div>
            </div>
        `;
    
    return userModalHtml;
}

function showCustomConfirmationModal() {
    let modalElement = document.getElementById('confirmationModal');
    let modal = bootstrap.Modal.getOrCreateInstance(modalElement);

    modal.show();
}

function truncateText(text, maxLength) {
    if (text.length <= maxLength) {
        return text;
    }
    
    return text.substring(0, maxLength - 3) + '...';
}
