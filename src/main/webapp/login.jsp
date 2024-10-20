<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Página de Login</title>
        <link rel="stylesheet" href="public/css/bootstrap.min.css">
        <script src="public/js/bootstrap.min.js" defer></script>
        <script src="public/js/util/util.js" defer></script>
        <script src="public/js/util/modal.js" defer></script>
        <script src="public/js/service/authService.js" defer></script>
        <script src="public/js/service/requestService.js" defer></script>
        <script src="public/js/service/userService.js" defer></script>
    </head>

    <body>
        <div class="container vh-100 d-flex justify-content-center align-items-center">
            <div class="card" style="width: 100%; max-width: 400px;">
                <div class="card-body">
                    <h5 class="card-title text-center">Login</h5>
                    <form>
                        <div class="mb-3">
                            <label for="email" class="form-label">E-mail</label>
                            <input type="email" class="form-control" id="email" name="email"
                                placeholder="Digite seu e-mail" required maxlength="100">
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Senha</label>
                            <input type="password" class="form-control" id="password" name="password"
                                placeholder="Digite sua senha" required maxlength="100">
                        </div>
                        <button type="button" class="btn btn-primary w-100" onclick="authenticate()">Entrar</button>
                    </form>
                    <button class="btn btn-link w-100" onclick="getUserModal()">Criar novo
                        usuário</button>
                </div>
            </div>
        </div>
    </body>

    </html>