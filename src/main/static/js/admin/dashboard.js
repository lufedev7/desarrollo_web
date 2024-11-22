// src/main/resources/static/js/admin/dashboard.js

let currentSection = 'users'; // Para saber qué sección está activa
let searchTimeout;

$(document).ready(function() {
    // Cargar usuarios por defecto
    loadUsers();

    // Configurar el evento de búsqueda
    $('#searchInput').on('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            const query = $(this).val();
            if(currentSection === 'users') {
                searchUsers(query);
            } else {
                searchProducts(query);
            }
        }, 300); // Esperar 300ms después de que el usuario deje de escribir
    });
});

function loadUsers() {
    currentSection = 'users';
    $('.list-group-item').removeClass('active');
    $('[onclick="loadUsers()"]').addClass('active');
    $('#searchInput').attr('placeholder', 'Buscar usuarios...');
    searchUsers('');
}

function loadProducts() {
    currentSection = 'products';
    $('.list-group-item').removeClass('active');
    $('[onclick="loadProducts()"]').addClass('active');
    $('#searchInput').attr('placeholder', 'Buscar productos...');
    searchProducts('');
}

function searchUsers(query) {
    $.ajax({
        url: '/admin/dashboard/users/search',
        method: 'GET',
        data: { query: query },
        success: function(response) {
            displayUsers(response);
        },
        error: function(xhr) {
            handleError(xhr);
        }
    });
}

function searchProducts(query) {
    $.ajax({
        url: '/admin/dashboard/products/search',
        method: 'GET',
        data: {
            query: query,
            pageNumber: 0,
            pageSize: 10,
            sortBy: 'productId',
            sortDir: 'DESC'
        },
        success: function(response) {
            displayProducts(response.content);
        },
        error: function(xhr) {
            handleError(xhr);
        }
    });
}

function displayUsers(users) {
    const table = `
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${users.map(user => `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.userName}</td>
                        <td>${user.email}</td>
                        <td>${user.seller ? 'Vendedor' : 'Usuario'}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteUser(${user.id})">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;

    $('#contentTable').html(table);
}

function displayProducts(products) {
    const table = `
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Precio</th>
                    <th>Vendido</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${products.map(product => `
                    <tr>
                        <td>${product.productId}</td>
                        <td>${product.productName}</td>
                        <td>$${product.originalPrice}</td>
                        <td>${product.isSold ? 'Sí' : 'No'}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-danger" onclick="deleteProduct(${product.productId})">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;

    $('#contentTable').html(table);
}

function deleteUser(userId) {
    if(confirm('¿Está seguro de eliminar este usuario?')) {
        $.ajax({
            url: `/api/users/${userId}`,
            method: 'DELETE',
            success: function() {
                searchUsers($('#searchInput').val());
            },
            error: function(xhr) {
                handleError(xhr);
            }
        });
    }
}

function deleteProduct(productId) {
    if(confirm('¿Está seguro de eliminar este producto?')) {
        $.ajax({
            url: `/api/products/${productId}`,
            method: 'DELETE',
            success: function() {
                searchProducts($('#searchInput').val());
            },
            error: function(xhr) {
                handleError(xhr);
            }
        });
    }
}

function handleError(xhr) {
    let message = 'Ha ocurrido un error';
    if(xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
    }
    alert(message);
}