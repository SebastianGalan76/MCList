const loginInput = document.getElementById("login-input");
const passwordInput = document.getElementById("password-input");

const errorMessage = document.getElementById("error-message");

async function signIn() {
    if (!checkValues()) {
        return;
    }

    const signInDto = {
        identifier: loginInput.value,
        password: passwordInput.value,
    }

    try {
        const response = await fetch('/auth/signInPost', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(signInDto)
        });


        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`Error: ${errorResponse.message}`);
        }
        else {
            const authenticationResponse = await response.json();

            if (authenticationResponse.status != "OK") {
                errorMessage.innerHTML = authenticationResponse.message;
            }
            else {
                errorMessage.innerHTML = null;

                setCookie('jwt_token', authenticationResponse.token, 21);
                window.location.href = '/';
            }
        }
    } catch (error) {
        errorMessage.innerHTML = error.message;
    }
}

function resetPassword() {
    const emailResetPasswordInput = document.querySelector('input[id="input-email-reset-password"]');

    document.getElementById("reset-password-info").style.display = "block";
    document.getElementById("reset-password-submit").style.display = "none";

    resetPasswordDto = {
        email: emailResetPasswordInput.value,
    };

    fetch('/auth/resetPasswordRequire', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(resetPasswordDto)
    });
}


function checkValues() {
    if (!checkLogin()) {
        return false;
    }
    if (!checkPassword()) {
        return false;
    }

    errorMessage.innerHTML = "";
    return true;
}

function checkLogin() {
    const login = loginInput.value;

    if (login.length < 4) {
        errorMessage.innerHTML = "Login jest zbyt krótki!"
        loginInput.classList.add("incorrect");
        return false;
    }

    if (login.length > 30) {
        errorMessage.innerHTML = "Login jest zbyt długi!"
        loginInput.classList.add("incorrect");
        return false;
    }

    loginInput.classList.remove("incorrect");
    return true;
}

function checkPassword() {
    const password = passwordInput.value;

    if (password.length < 4) {
        errorMessage.innerHTML = "Hasło jest zbyt krótkie!"
        passwordInput.classList.add("incorrect");
        return false;
    }

    if (password.length > 30) {
        errorMessage.innerHTML = "Hasło jest zbyt długie!"
        passwordInput.classList.add("incorrect");
        return false;
    }

    passwordInput.classList.remove("incorrect");
    return true;
}