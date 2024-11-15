<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <%@ include file="common/head.jsp" %>
            <link rel="stylesheet" href="public/css/screens/manager.css">
            <script src="public/js/screens/manager.js" defer></script>
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
                    <div class="col-2">
                      <input type="text" class="form-control" id="productName" placeholder="Nome do produto" maxlength="255">
                    </div>
                    <div class="col-2">
                      <input type="number" class="form-control" id="productId" placeholder="Id do produto" min="1">
                    </div>
                    <div class="col-1">
                      <button class="btn btn-secondary" onclick="cleanProductNameAndId()">Limpar</button>
                    </div>
                  </div>
                  <div class="row justify-content-start card-line">
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <div class="row window-title">
                    <h1>Pedidos</h1>
                  </div>
                  <div class="row menu-bar">
                    <div class="col-1">
                      <button class="btn btn-primary" onclick="loadOrders()">Pesquisar</button>
                    </div>
                  </div>
                  <div class="row">
                    <table class="table table-striped" id="orderTable">
                      <thead>
                        <tr>
                          <th class="text-center">Id</th>
                          <th class="text-center">Id do Cliente</th>
                          <th class="text-center">Data</th>
                          <th class="text-center">Status</th>
                          <th class="text-center">Produto</th>
                          <th class="text-center">Preço</th>
                          <th class="text-center">Quantidade</th>
                          <th class="text-center">Subtotal</th>
                          <th class="text-center">Total</th>
                        </tr>
                      </thead>
                      <tbody></tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
        </body>

    </html>