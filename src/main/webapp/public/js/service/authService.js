async function authenticate() {
    let email = document.querySelector('.card #email').value;
    let password = document.querySelector('.card #password').value;

    try {
        let response = await makeApiRequest('auth', 'POST', { email, password });

        if (response.status === 201) {
            showCustomToast("Login realizado com sucesso.", "green");
        } else if (response.status === 400) {
            showCustomToast("Requisição inadequada.", "orange");
        } else if (response.status === 401) {
            showCustomToast("Credenciais para efetuar o login incorretas ou usuário não existente.", "red");
        } else if (response.status === 500) {
            showCustomToast("Ocorreu um erro inesperado no servidor.", "red");
        } else {
            showCustomToast("Ocorreu um erro inesperado.", "red");
        }
    } catch (error) {
        showCustomToast("Ocorreu um erro inesperado.", "red");
    }
}