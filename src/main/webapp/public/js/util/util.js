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

function showCustomModal() {
    let modalElement = document.getElementById('registerModal');
    let modal = bootstrap.Modal.getOrCreateInstance(modalElement);

    modal.show();
}

function truncateText(text, maxLength) {
    if (text.length <= maxLength) {
        return text;
    }
    
    return text.substring(0, maxLength - 3) + '...';
}
