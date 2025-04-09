
document.addEventListener("DOMContentLoaded", function () {
    const passwordField = document.getElementById("password");
    const toggleLink = document.getElementById("togglePassword");

    if (passwordField && toggleLink) {
        toggleLink.addEventListener("click", function (e) {
            e.preventDefault();
            if (passwordField.type === "password") {
                passwordField.type = "text";
                toggleLink.textContent = "Ocultar contraseña";
            } else {
                passwordField.type = "password";
                toggleLink.textContent = "Mostrar contraseña";
            }
        });
    }
});
