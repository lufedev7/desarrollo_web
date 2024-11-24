let currentPage = 1;
let totalPages = 1;

console.log("Funcionando")

function switchView(view) {
    $('.view').removeClass('active').addClass('d-none');
    $(`#${view}View`).removeClass('d-none').addClass('active');
    $('#sectionTitle').text(view === 'users' ? 'User  Management' : 'Product Management');
    loadData(view);
}

function loadData(view) {
    // Simulate API call
    if (view === 'users') {
        // Load user data
        $('#usersTable').empty();
        for (let i = 1; i <= 10; i++) {
            $('#usersTable').append(`<tr><td>${i}</td><td>User ${i}</td><td>user${i}@example.com</td><td><button onclick="editUser (${i})">Edit</button><button onclick="confirmDeleteUser (${i})">Delete</button></td></tr>`);
        }
        totalPages = 5; // Simulated total pages
    } else {
        // Load product data
        $('#productsTable').empty();
        for (let i = 1; i <= 10; i++) {
            $('#productsTable').append(`<tr><td>${i}</td><td>Product ${i}</td><td>Category ${i}</td><td>User ${i}</td><td>Active</td><td><button onclick="confirmDeleteProduct(${i})">Delete</button></td></tr>`);
        }
        totalPages = 5; // Simulated total pages
    }
    updatePagination();
}

function updatePagination() {
    $('#pageInfo').text(`Page ${currentPage} of ${totalPages}`);
    $('#prevPage').prop('disabled', currentPage === 1);
    $('#nextPage').prop('disabled', currentPage === totalPages);
}

function goToPrevPage() {
    if (currentPage > 1) {
        currentPage--;
        loadData('users');
    }
}

function goToNextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        loadData('users');
    }
}

function confirmDeleteUser (id) {
    if (confirm(`Are you sure you want to delete User ${id}?`)) {
        // Delete user logic here
        alert(`User  ${id} deleted.`);
        loadData('users');
    }
}

function confirmDeleteProduct(id) {
    if (confirm(`Are you sure you want to delete Product ${id}?`)) {
        // Delete product logic here
        alert(`Product ${id} deleted.`);
        loadData('products');
    }
}

function logout() {
    // Logout logic here
    alert('Logged out');
}

// Initial load
$(document).ready(function() {
    loadData('users');
});