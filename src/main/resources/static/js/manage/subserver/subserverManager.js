const subserverTemplate = document.getElementById('subserver-template');
const linkContainer = document.getElementById('subserver-list-container');

const nameInput = document.getElementById("subserver-name-input");
const colorInput = document.getElementById("subserver-color-input");

const createSubserverError = document.getElementById("create-subserver-error");

const createButton = document.getElementById("create-subserver-button");
createButton.addEventListener('click', () => {
    createSubserver();
});

import { getSelectedVersions, loadSelectedVersions, removeAllSelectedVersions } from '../../versionManager.js';
import { getSelectedModes, removeAllSelectedModes, onlySingleSelectionMode, loadSelectedModes } from '../../modeManager.js';
onlySingleSelectionMode();

var subserverId = -1000;
export var subservers = [];
export var selectedSubserver;

export function getSortedSubservers() {
    const subserverViews = linkContainer.querySelectorAll('.item');

    var index = 0;
    subserverViews.forEach(subserverView => {
        subservers.forEach(subserver => {
            if (subserver.view === subserverView) {
                subserver.index = index;
                return;
            }
        });
        index++;
    });

    return subservers;
}

function createSubserver() {
    nameInput.value = nameInput.value.trim();
    if (nameInput.value.length == 0) {
        createSubserverError.innerHTML = "Nazwa trybu nie może być pusta!";
        return;
    }
    else {
        createSubserverError.innerHTML = null;
    }
    const selectedMode = getSelectedModes();
    if (selectedMode == null || selectedMode.length == 0) {
        createSubserverError.innerHTML = "Musisz wybrać tryb serwera. Jeśli brakuje Twojego trybu to napisz do nas";
        return;
    }
    const selectedVersions = getSelectedVersions();
    if (selectedVersions == null || selectedVersions.length == 0) {
        createSubserverError.innerHTML = "Musisz wybrać wersje serwera";
        return;
    }

    if (selectedSubserver != null) {
        selectedSubserver.view.classList.remove("selected");

        createButton.innerHTML = "Stwórz";

        selectedSubserver.name = nameInput.value;
        selectedSubserver.color = colorInput.value;
        selectedSubserver.versions = selectedVersions;
        selectedSubserver.mode = selectedMode[0];

        const subserverElement = selectedSubserver.view;
        const namePreview = subserverElement.querySelector('.subserver-name-preview');

        namePreview.innerHTML = selectedSubserver.name;
        namePreview.style.color = selectedSubserver.color;

        if (selectedSubserver.mode != null) {
            const modeTag = subserverElement.querySelector('.mode-tag ');
            modeTag.style.display = "flex";

            modeTag.querySelector('.content').innerHTML = selectedSubserver.mode.name;
        }
        if (selectedSubserver.versions != null && selectedSubserver.versions.length > 0) {
            const versionTag = subserverElement.querySelector('.version-tag');
            versionTag.style.display = "flex";

            const sortedArray = selectedSubserver.versions.sort((a, b) => parseFloat(a.id) - parseFloat(b.id));

            const minValue = sortedArray[0];
            const maxValue = sortedArray[sortedArray.length - 1];

            if (minValue.id == maxValue.id) {
                versionTag.querySelector('.content').innerHTML = minValue.name;
            }
            else {
                versionTag.querySelector('.content').innerHTML = minValue.name + " - " + maxValue.name;
            }
        }

        selectedSubserver = null;
        clearPanel();
        return;
    }

    const subserver = {
        id: subserverId,
        name: nameInput.value,
        color: colorInput.value,
        versions: selectedVersions,
        mode: selectedMode[0],
        index: subservers.length
    }
    addSubserverView(subserver);

    clearPanel();
    subserverId++;
}
function removeSubserver(idToRemove) {
    const linkToRemove = subservers.find(subserver => subserver.id === idToRemove);

    if (linkToRemove) {
        linkToRemove.view.remove();

        const indexToRemove = subservers.findIndex(subserver => subserver.id === idToRemove);

        if (indexToRemove !== -1) {
            subservers.splice(indexToRemove, 1);
        }
    }
}

export function loadSubservers(loadedSubserversJson) {
    loadedSubserversJson.forEach(loadedSubserver => {
        const transformedObject = {
            id: loadedSubserver.id,
            name: loadedSubserver.name.name,
            color: loadedSubserver.name.color,
            mode: loadedSubserver.mode,
            versions: loadedSubserver.versions,
            index: loadedSubserver.index
        };
        addSubserverView(transformedObject);
    });
}

function addSubserverView(subserver) {
    const template = subserverTemplate.content.cloneNode(true);
    const subserverElement = template.querySelector('.subserver');

    const namePreview = subserverElement.querySelector('.subserver-name-preview');

    namePreview.innerHTML = subserver.name;
    namePreview.style.color = subserver.color;
    linkContainer.appendChild(subserverElement);

    subserverElement.querySelector('.remove-subserver-button').addEventListener('click', () => {
        removeSubserver(subserver.id);
    });

    if (subserver.mode != null) {
        const modeTag = subserverElement.querySelector('.mode-tag ');
        modeTag.style.display = "flex";

        modeTag.querySelector('.content').innerHTML = subserver.mode.name;
    }
    if (subserver.versions != null && subserver.versions.length > 0) {
        const versionTag = subserverElement.querySelector('.version-tag');
        versionTag.style.display = "flex";

        const sortedArray = subserver.versions.sort((a, b) => parseFloat(a.id) - parseFloat(b.id));

        const minValue = sortedArray[0];
        const maxValue = sortedArray[sortedArray.length - 1];

        if (minValue.id == maxValue.id) {
            versionTag.querySelector('.content').innerHTML = minValue.name;
        }
        else {
            versionTag.querySelector('.content').innerHTML = minValue.name + " - " + maxValue.name;
        }
    }

    subserver.view = subserverElement;
    subservers.push(subserver);

    //Dragging system
    subserverElement.addEventListener("dragstart", () => {
        setTimeout(() => subserverElement.classList.add("dragging"), 0);
        if (selectedSubserver != null) {
            createButton.innerHTML = "Stwórz";
            selectedSubserver.view.classList.remove("selected");
            clearPanel();
        }

        selectedSubserver = null;
    });
    subserverElement.addEventListener("dragend", () => subserverElement.classList.remove("dragging"));

    //Selecting system
    subserverElement.addEventListener('click', () => {
        clearPanel();

        if (selectedSubserver != null) {
            selectedSubserver.view.classList.remove("selected");

            if (selectedSubserver.view == subserverElement) {
                selectedSubserver = null;
                createButton.innerHTML = "Stwórz";

                return;
            }
        }

        selectedSubserver = subserver;
        selectedSubserver.view.classList.add("selected");

        nameInput.value = selectedSubserver.name;
        colorInput.value = selectedSubserver.color;
        loadSelectedModes(selectedSubserver.mode);
        loadSelectedVersions(selectedSubserver.versions);

        createButton.innerHTML = "Zapisz";
    });
}

function clearPanel() {
    nameInput.value = null;
    colorInput.value = "#ffffff";

    removeAllSelectedVersions();
    removeAllSelectedModes();
}