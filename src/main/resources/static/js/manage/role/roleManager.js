const roleTemplate = document.getElementById('role-template');
const roleContainer = document.getElementById('created-role-container');

const emailInput = document.getElementById("user-email-input");
const roleSelect = document.getElementById("role-select");

const createLinkError = document.getElementById("create-role-error");

const createButton = document.getElementById("create-role-button");
createButton.addEventListener('click', () => {
    createRole();
});

var roleId = -1000;
var roles = [];

async function createRole() {
    emailInput.value = emailInput.value.trim();
    if (emailInput.value.length == 0) {
        createLinkError.innerHTML = "Pole e-mail lub login nie może być puste!";
        return;
    }
    else {
        createLinkError.innerHTML = null;
    }

    try {
        const response = await fetch('/user/find?identifier=' + emailInput.value, {
            method: 'GET',
        });

        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`Error: ${errorResponse.message}`);
        }
        else {
            const responseJson = await response.json();

            if (responseJson.status == "BAD_REQUEST") {
                createLinkError.innerHTML = responseJson.message;
            }
            else if (responseJson.status == "OK") {
                const role = {
                    id: roleId,
                    user: {
                        login: emailInput.value,
                    },
                    role: roleSelect.value
                }
                addRoleView(role);

                clearPanel();
                roleId++;
            }
        }
    } catch (error) {
        createLinkError.innerHTML = error.message;
    }
}
function removeRole(idToRemove) {
    const roleToRemove = roles.find(role => role.id === idToRemove);

    if (roleToRemove) {
        roleToRemove.view.remove();

        const indexToRemove = roles.findIndex(role => role.id === idToRemove);

        if (indexToRemove !== -1) {
            roles.splice(indexToRemove, 1);
        }
    }
}

export function loadRoles(loadedRolesJson) {
    loadedRolesJson.forEach(loadedRole => {
        const transformedObject = {
            id: loadedRole.id,
            user: loadedRole.user,
            role: loadedRole.role
        };
        addRoleView(transformedObject);
    });
}

export function getRoles() {
    return roles;
}

function addRoleView(role) {
    const template = roleTemplate.content.cloneNode(true);
    const roleElement = template.querySelector('.role');

    const email = roleElement.querySelector('.user-login');
    email.innerHTML = role.user.login;

    const roleName = roleElement.querySelector('.role-name');
    roleName.innerHTML = role.role;

    roleContainer.appendChild(roleElement);

    if(role.role == 'OWNER'){
        roleElement.querySelector('.remove-button').remove();
    }
    else{
        roleElement.querySelector('.remove-button').addEventListener('click', () => {
            removeRole(role.id);
        });
    }

    role.view = roleElement;
    roles.push(role);
}

function clearPanel() {
    emailInput.value = null;
    //urlInput.value = null;
}