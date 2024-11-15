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
        let responseBody = await insertUser(body);

        document.getElementById('id').value = responseBody.id;
    } else if (insertOrUpdate === 'update') {
        updateUser(body);
    }
}

async function insertUser(body) {
    try {
        let response = await makeApiRequest('users', 'POST', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na inserção de usuário.", "red");
    }
}

async function updateUser(body) {
    try {
        let response = await makeApiRequest('users', 'PUT', body, true);

        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na atualização de usuário.", "red");
    }
}

async function findUserById(id) {
    try {
        let response = await makeApiRequest('users/' + id, 'GET', null, false);
        
        if (response) {
            let body = await response.json();

            return body;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na consulta de usuário pelo id.", "red");
     }
}

async function deleteUserById(id) {
    try {
        let response = await makeApiRequest('users/' + id, 'DELETE', null, false);

        if (response) {
            var destinationPath = window.location.href;

            let slicedUrl = destinationPath.split('/').slice(0, -1).join('/');
    
            destinationPath = slicedUrl + "/logout";
            window.location.href = destinationPath;
        }
    } catch (error) {
        showCustomToast("Erro inesperado na deleção de usuário pelo id.", "red");
     }
}