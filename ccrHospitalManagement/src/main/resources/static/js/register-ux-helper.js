document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const username = document.getElementById("username");
    const email = document.getElementById("email");
    const password = document.getElementById("password");

    function showError(input, message) {
        input.classList.add("is-invalid");
        let error = input.nextElementSibling;
        if (!error || !error.classList.contains("invalid-feedback")) {
            error = document.createElement("div");
            error.className = "invalid-feedback";
            input.parentNode.appendChild(error);
        }
        error.textContent = message;
    }

    function clearError(input) {
        input.classList.remove("is-invalid");
        const error = input.nextElementSibling;
        if (error && error.classList.contains("invalid-feedback")) {
            error.remove();
        }
    }

    function validateEmail(emailValue) {
        const re = /^\S+@\S+\.\S+$/;
        return re.test(String(emailValue).toLowerCase());
    }

    form.addEventListener("submit", function (e) {
        let valid = true;

        // Validar username
        if (username.value.trim().length < 4) {
            showError(username, "El nombre de usuario debe tener al menos 4 caracteres.");
            valid = false;
        } else {
            clearError(username);
        }


        if (!validateEmail(email.value)) {
            showError(email, "Ingresa un correo electrónico válido.");
            valid = false;
        } else {
            clearError(email);
        }


        if (password.value.length < 6) {
            showError(password, "La contraseña debe tener al menos 6 caracteres.");
            valid = false;
        } else {
            clearError(password);
        }

        if (!valid) {
            e.preventDefault();
        }
    });
});
