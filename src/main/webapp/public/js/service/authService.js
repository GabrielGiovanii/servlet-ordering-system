async function authenticate() {
    let email = document.document.querySelector('.card #email').value;
    let password = document.document.querySelector('.card #password').value;

    try {
        let response = await makeApiRequest('auth', 'POST', { email, password });

        if (response.status === 201) {
            showCustomToast("Login realizado com sucesso.", "green");
        } else if (response.status === 401) {
            showCustomToast("Credenciais para efetuar o login incorretas ou usuário não existente.", "red");
        }
    } catch (error) {
        showCustomToast("Ocorreu um erro inesperado.", "red");
    }
}