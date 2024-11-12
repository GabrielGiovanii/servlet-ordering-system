<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <%@ include file="common/head.jsp" %>
            <link rel="stylesheet" href="public/css/screens/home.css">
            <script src="public/js/screens/home.js" defer></script>
            <script src="public/js/services/productService.js" defer></script>
            <script src="public/js/services/categoryService.js" defer></script>
        </head>

        <body>
            <%@ include file="common/navbar.jsp" %>

            <div class="container">
              <div class="row">
                <div class="col">
                  <div class="row window-title">
                    <h1>Produtos</h1>
                  </div>
                  <div class="row menu-bar">
                    <div class="col-1">
                      <button class="btn btn-primary" onclick="loadProducts()">Pesquisar</button>
                    </div>
                    <div class="col-1">
                      <button class="btn btn-primary" onclick="">Inserir</button>
                    </div>
                    <div class="col-2">
                      <input type="text" class="form-control" id="productName" placeholder="Nome do produto" maxlength="255">
                    </div>
                    <div class="col-2">
                      <input type="number" class="form-control" id="productId" placeholder="Id do produto" min="1">
                    </div>
                    <div class="col-1">
                      <button class="btn btn-secondary" onclick="cleanProductName()">Limpar</button>
                    </div>
                  </div>
                  <div class="row justify-content-start card-line">
                  </div>
                </div>
              </div>
            </div>
        </body>

    </html>