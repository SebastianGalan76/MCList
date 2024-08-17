const passwordInput = document.querySelector('input[id="input-password"]');
const repeatedPasswordInput = document.querySelector('input[id="input-password-confirm"]');

const errorMessage = document.getElementById("error-message");

function resetPassword() {
    if (!checkPassword()) {
        return;
    }
    errorMessage.innerHTML = "";

    resetPasswordDto = { token: getTokenFromUrl(), newPassword: passwordInput.value };

    fetch('/auth/resetPasswordHandle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(resetPasswordDto)
    })
        .then(function (response) {
            return response.json();
        }).then(function (response) {
            if (!response) {
                return;
            }

            if (response.status !== "OK") {
                errorMessage.innerHTML = response.message;
                return;
            }

            showPopup();
        })
}

function checkPassword() {
    if (passwordInput.value != repeatedPasswordInput.value) {
        errorMessage.innerHTML = "Wprowadzone hasła nie są identyczne";

        passwordInput.classList.add("incorrect-value");
        repeatedPasswordInput.classList.add("incorrect-value");

        return false;
    }

    if (passwordInput.value.length < 4) {
        passwordInput.classList.add("incorrect-value");
        repeatedPasswordInput.classList.add("incorrect-value");

        errorMessage.innerHTML = "Hasło jest zbyt krótkie";
        return false;
    }

    passwordInput.classList.remove("incorrect-value");
    repeatedPasswordInput.classList.remove("incorrect-value");
    return true;
}

function getTokenFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('token');
}
