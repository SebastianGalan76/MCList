import { showEditPanel, hideEditPanel } from "./rankEditor.js";

const rankTemplate = document.getElementById('rank-template');
const rankContainer = document.getElementById('rank-container');

const nameInput = document.getElementById("create-rank-name-input");
const colorInput = document.getElementById("create-rank-color-input");

const createRankError = document.getElementById("create-rank-error");

const removeRankConfirmationPopup = document.getElementById('remove-rank-confirmation-popup');

const createButton = document.getElementById("create-rank-button");
createButton.addEventListener('click', () => {
    createRank();
});

var staffRankId = -1000;
export var staffRanks = [];
export var selectedRank;

export function getSortedStaffRanks() {
    const rankViews = rankContainer.querySelectorAll('.item');

    var index = 0;
    rankViews.forEach(rankView => {
        staffRanks.forEach(staffRank => {
            if (staffRank.view === rankView) {
                staffRank.index = index;
                return;
            }
        });
        index++;
    });

    return staffRanks;
}

function createRank() {
    nameInput.value = nameInput.value.trim();
    if (nameInput.value.length == 0) {
        createRankError.innerHTML = "Nazwa rangi nie może być pusta!";
        return;
    }
    else {
        createRankError.innerHTML = null;
    }

    const staffRank = {
        id: staffRankId,
        name: nameInput.value,
        color: colorInput.value,
        index: staffRanks.length,
    }
    addStaffView(staffRank);

    clearPanel();
    staffRankId++;

    selectedRank = staffRank;
    showEditPanel(staffRank);
}
export function removeRank() {
    removeRankConfirmationPopup.classList.add("active");
    removeRankConfirmationPopup.querySelector('#remove-rank-confirmation-name').innerHTML = selectedRank.name;

    const yesButton = removeRankConfirmationPopup.querySelector('.yes-button');
    yesButton.addEventListener('click', () => {
        confirmRemoveRank();
        removeRankConfirmationPopup.classList.remove("active");
    });

    const noButton = removeRankConfirmationPopup.querySelector('.no-button');
    const closeButton = removeRankConfirmationPopup.querySelector('.close-btn');

    noButton.addEventListener('click', () => {
        removeRankConfirmationPopup.classList.remove("active");
    });
    closeButton.addEventListener('click', () => {
        removeRankConfirmationPopup.classList.remove("active");
    });
}

export function loadStaff(loadedStaffJson) {
    loadedStaffJson.forEach(loadedStaff => {
        addStaffView(loadedStaff);
    });
}

function confirmRemoveRank(){
    if(selectedRank == null){
        return;
    }

    const rankToRemove = staffRanks.find(staffRank => staffRank.id === selectedRank.id);

    if (rankToRemove) {
        rankToRemove.view.remove();

        const indexToRemove = staffRanks.findIndex(staffRank => staffRank.id === selectedRank.id);

        if (indexToRemove !== -1) {
            staffRanks.splice(indexToRemove, 1);
            selectedRank = null;
        }
    }

    hideEditPanel();
}

function addStaffView(staffRank) {
    const template = rankTemplate.content.cloneNode(true);
    const rankElement = template.querySelector('.rank');

    const rankPreview = rankElement.querySelector('.rank-name');

    rankPreview.innerHTML = staffRank.name;
    rankPreview.style.color = staffRank.color;
    rankContainer.appendChild(rankElement);

    staffRank.view = rankElement;
    staffRanks.push(staffRank);

    //Dragging system
    rankElement.addEventListener("dragstart", () => {
        setTimeout(() => rankElement.classList.add("dragging"), 0);
        hideEditPanel();
        selectedRank = null;
    });
    rankElement.addEventListener("dragend", () => rankElement.classList.remove("dragging"));

    //Selecting system
    rankElement.addEventListener('click', () => {
        selectedRank = staffRank;
        showEditPanel(staffRank);
    });
}

function clearPanel() {
    nameInput.value = null;
    colorInput.value = "#ffffff";
}