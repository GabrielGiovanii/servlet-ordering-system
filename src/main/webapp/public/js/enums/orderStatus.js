const OrderStatus = Object.freeze({
    WAITING_PAYMENT: { name: 'Aguardando Pagamento', code: 1 },
    PAID: { name: 'Pago', code: 2 },
    SHIPPED: { name: 'Enviado', code: 3 },
    DELIVERED: { name: 'Entregue', code: 4 },
    CANCELED: { name: 'Cancelado', code: 5 }
});