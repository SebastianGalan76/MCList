const addressInput = document.getElementById('address-input');
const portInput = document.getElementById('port-input');

const premiumCheckbox = document.getElementById('server-premium-checkbox');
const modsCheckbox = document.getElementById('server-mods-checkbox');

import { getSelectedVersions } from './versionManager.js';
import { getSelectedModes } from './modeManager.js';
import { isAddressValid, isPortValid } from './utils.js';

addressInput.addEventListener('input', function () {
    const currentValue = addressInput.value;
    const filteredValue = currentValue.replace(/:/g, '');
    if (currentValue !== filteredValue) {
        addressInput.value = filteredValue;
    }
});

const serverStatusResponseError = document.getElementById('server-status-response-error');
const loadingStatusAnimation = document.getElementById("loading-animation");

const errorMessage = document.getElementById('error-message');

var isFunctionLocked = false;

const addServerButton = document.getElementById('add-server-button');
if(addServerButton){
    addServerButton.addEventListener('click', addServer);
}

async function addServer() {
    if (!isAddressValid(addressInput.value)) {
        errorMessage.innerHTML = "Wpisz poprawny adres serwera!";
        return;
    }
    if (!isPortValid(portInput.value)) {
        errorMessage.innerHTML = "Wpisz poprawny port serwera!";
        return;
    }
    errorMessage.innerHTML = null;

    serverStatusResponseError.style.display = "none";
    loadingStatusAnimation.style.display = "block";

    if (isFunctionLocked) {
        setTimeout(() => {
            addServer();
        }, 5000);

        return;
    }

    isFunctionLocked = true;
    setTimeout(() => {
        isFunctionLocked = false;
    }, 5000);

    const serverDto = {
        ip: addressInput.value,
        port: portInput.value,

        versions: getSelectedVersions(),
        modes: getSelectedModes()
    }

    try {
        const response = await fetch('/add-new-server/post', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(serverDto)
        });


        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`Error: ${errorResponse.message}`);
        }
        else {
            const responseJson = await response.json();
            loadingStatusAnimation.style.display = "none";

            if (responseJson.status == "BAD_REQUEST") {
                errorMessage.innerHTML = responseJson.message;
            }
            if (responseJson.status == "NOT_FOUND") {
                serverStatusResponseError.style.display = "block";
            }
            if (responseJson.status == "PERMANENT_REDIRECT") {
                window.location.href = responseJson.redirect;
            }
        }
    } catch (error) {
        errorMessage.innerHTML = error.message;
    }
}

export async function editServer(serverId) {
    if (!isAddressValid(addressInput.value)) {
        errorMessage.innerHTML = "Wpisz poprawny adres serwera!";
        return;
    }
    if (!isPortValid(portInput.value)) {
        errorMessage.innerHTML = "Wpisz poprawny port serwera!";
        return;
    }
    errorMessage.innerHTML = null;

    serverStatusResponseError.style.display = "none";
    loadingStatusAnimation.style.display = "block";

    if (isFunctionLocked) {
        setTimeout(() => {
            addServer();
        }, 5000);

        return;
    }

    isFunctionLocked = true;
    setTimeout(() => {
        isFunctionLocked = false;
    }, 5000);

    const serverDto = {
        ip: addressInput.value,
        port: portInput.value,

        versions: getSelectedVersions(),
        modes: getSelectedModes(),
        premium: premiumCheckbox.checked,
        mods: modsCheckbox.checked
    }

    try {
        const response = await fetch('/server/' + serverId+ '/manage/info/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(serverDto)
        });


        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`Error: ${errorResponse.message}`);
        }
        else {
            const responseJson = await response.json();
            loadingStatusAnimation.style.display = "none";

            if (responseJson.status == "BAD_REQUEST" || responseJson.status == "OK") {
                errorMessage.innerHTML = responseJson.message;
            }
            if (responseJson.status == "NOT_FOUND") {
                serverStatusResponseError.style.display = "block";
            }
            if (responseJson.status == "PERMANENT_REDIRECT") {
                window.location.href = responseJson.redirect;
            }
        }
    } catch (error) {
        errorMessage.innerHTML = error.message;
    }
}