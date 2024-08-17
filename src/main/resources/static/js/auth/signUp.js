const loginInput = document.getElementById("login-input");
const passwordInput = document.getElementById("password-input");
const passwordConfirmInput = document.getElementById("password-confirm-input");
const emailInput = document.getElementById("email-input");
const acceptRuleCheckbox = document.getElementById("accept-rule-checkbox");

const errorMessage = document.getElementById("error-message");

async function signUp(button) {
    if (!checkValues()) {
        return;
    }
    button.style.display = "none";
    
    const signUpDto = {
        login: loginInput.value,
        password: passwordInput.value,
        email: emailInput.value
    }
    
    try {
        const response = await fetch('/auth/signUpPost', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(signUpDto)
        });

        if (!response.ok) {
            const errorResponse = await response.json();
            button.style.display = "block";
            
            throw new Error(`Error: ${errorResponse.message}`);
        }
        else {
            const authenticationResponse = await response.json();

            if(authenticationResponse.status != "OK"){
                errorMessage.innerHTML = authenticationResponse.message;
                button.style.display = "block";
            }
            else{
                errorMessage.innerHTML = null;
                
                document.getElementById('created-successfully-popup').classList.add("active");
            }
        }
    } catch (error) {
        errorMessage.innerHTML = error.message;
    }
}

function checkValues() {
    if (!checkLogin()) {
        return false;
    }
    if (!checkPassword()) {
        return false;
    }
    if (!checkPasswordConfirm()) {
        return false;
    }
    if (!checkEmail()) {
        return false;
    }

    if(!acceptRuleCheckbox.checked){
        errorMessage.innerHTML = "Musisz zapoznać się z regulaminem i zaakceptować jego treść!";
        return;
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

function checkPasswordConfirm() {
    if (passwordInput.value != passwordConfirmInput.value) {
        errorMessage.innerHTML = "Hasła nie pasują do siebie!"
        passwordInput.classList.add("incorrect");
        passwordConfirmInput.classList.add("incorrect");
        return false;
    }

    passwordInput.classList.remove("incorrect");
    passwordConfirmInput.classList.remove("incorrect");
    return true;
}

function checkEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(emailInput.value)) {
        errorMessage.innerHTML = "Adres e-mail jest nieprawidłowy!"
        emailInput.classList.add("incorrect");
        return false;
    }

    emailInput.classList.remove("incorrect");
    return true;
}