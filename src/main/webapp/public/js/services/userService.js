async function saveUser(roleCode, insertOrUpdate) {
    let id = document.querySelector('.modal-dialog #id').value;
    let name = document.querySelector('.modal-dialog #name').value;
    let email = document.querySelector('.modal-dialog #email').value;
    let phone = document.querySelector('.modal-dialog #phone').value;
    let password = document.querySelector('.modal-dialog #password').value;
    let confirmPassword = document.querySelector('.modal-dialog #confirmPassword').value;

    if (!name || !email || !phone || !password || !confirmPassword) {
        showCustomToast("Todos os campos obrigatórios devem ser preenchidos.", "red");
        return;
    }

    if (password !== confirmPassword) {
        showCustomToast("Senha e Confirmação de Senha estão divergentes.", "red");
        return;
    }

    let body = {
        id: id,
        name: name,
        email: email,
        phone: phone,
        password: password,
        roleCode: roleCode
    };

    if (insertOrUpdate === 'insert') {
        let response = await insertUser(body);
        let data = await response.json();

        document.getElementById('id').value = (data.id) ? data.id : "";
    } else if (insertOrUpdate === 'update') {
        updateUser(body);
    }
}

async function insertUser(body) {
    try {
        let response = await makeApiRequest('users', 'POST', body);

        if (response.status === 201) {
            showCustomToast("Usuário inserido com sucesso.", "green");
        } else if (response.status === 400) {
            showCustomToast("Requisição inadequada.", "orange");
        } else if (response.status === 401) {
            showCustomToast("Requisição não autorizada.", "red");
        } else if (response.status === 409) {
            showCustomToast("Conflito de dados, requisição inadequada.", "red");
        } else if (response.status === 500) {
            showCustomToast("Ocorreu um erro inesperado.", "red");
        }

        return await response;
    } catch (error) {
        showCustomToast("Ocorreu um erro inesperado.", "red");
    }
}

async function updateUser(body) {
    try {
        let response = await makeApiRequest('users', 'PUT', body);

        if (response.status === 200) {
            showCustomToast("Usuário atualizado com sucesso.", "green");
        } else if (response.status === 400) {
            showCustomToast("Requisição inadequada.", "orange");
        } else if (response.status === 401) {
            showCustomToast("Requisição não autorizada.", "red");
        } else if (response.status === 409) {
            showCustomToast("Conflito de dados, requisição inadequada.", "red");
        } else if (response.status === 500) {
            showCustomToast("Ocorreu um erro inesperado.", "red");
        }
    } catch (error) {
        showCustomToast("Ocorreu um erro inesperado.", "red");
    }
}