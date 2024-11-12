async function authenticate() {
    let email = document.querySelector('.card #email').value;
    let password = document.querySelector('.card #password').value;

    try {
        let response = await makeApiRequest('auth', 'POST', { email, password }, true);

        if (response) {
            let userRole = (await response.json()).userRole;

            var destinationPath = window.location.href;
            destinationPath = destinationPath.replace("/login", "");

            if (userRole === "ADMIN") {
                destinationPath += "/manager";
            } else if (userRole === "CLIENT") {
                destinationPath += "/home";
            }
            
            window.location.href = destinationPath;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na autenticação.", "red");
    }
}