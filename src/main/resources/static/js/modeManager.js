const searchInput = document.getElementById('mode-input');

//Upper panel
const listPanel = document.getElementById('mode-list-panel');
const listContainer = document.getElementById('mode-list-container');

const selectedItemsContainer = document.getElementById('selected-mode-container');

var singleSelection = false;

var modeArray = [];
var selectedModeId = -1;

searchInput.addEventListener('focus', function () {
    listPanel.style.display = 'block';
    searchInput.classList.add("focused");
});

searchInput.addEventListener('input', function () {
    const filteredArray = modeArray.filter(mode =>
        mode.name.toLowerCase().startsWith(searchInput.value.toLowerCase())
    );
    populateList(filteredArray);
});

document.addEventListener('click', function (event) {
    if (!listPanel.contains(event.target) && !searchInput.contains(event.target)) {
        listPanel.style.display = 'none';
        searchInput.blur();
        searchInput.classList.remove("focused");
    }
    else {
        searchInput.focus();
    }
});

loadModes();

export function getSelectedModes() {
    const selectedModes = modeArray.filter(mode =>
        mode.checkbox.checked
    );

    return selectedModes;
}

export function loadSelectedModes(selectedMode) {
    if (selectedMode != null) {
        selectedModeId = selectedMode.id;

        if(modeArray.length > 0){
            if (selectedModeId > 0) {
                const mode = modeArray.find(mode => mode.id === selectedModeId);
                if (mode != null) {
                    mode.checkbox.checked = true;
                    addSelectedItem(mode);
                }
            }
        }
    }
}

export function onlySingleSelectionMode(){
    singleSelection = true;
}

async function loadModes() {
    const response = await fetch('/mode/listAll', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    });


    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(`Error: ${errorResponse.message}`);
    }
    else {
        modeArray = await response.json();

        populateList(modeArray);
    }
}

function populateList(modeArray) {
    listContainer.innerHTML = null;

    modeArray.forEach(mode => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'item';

        if (mode.checkbox != null) {
            itemDiv.appendChild(mode.checkbox);
        }
        else {
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'checkbox';
            checkbox.id = `checkbox-mode-${mode.id}`;
            mode.checkbox = checkbox;

            checkbox.addEventListener('change', () => handleCheckboxChange(mode));
            itemDiv.appendChild(checkbox);
        }

        const label = document.createElement('label');
        label.htmlFor = `checkbox-mode-${mode.id}`;
        label.textContent = mode.name;

        itemDiv.appendChild(label);
        listContainer.appendChild(itemDiv);
    });

    if (selectedModeId > 0) {
        const mode = modeArray.find(mode => mode.id === selectedModeId);
        if (mode != null) {
            mode.checkbox.checked = true;
            addSelectedItem(mode);
        }
    }
}

function handleCheckboxChange(mode) {
    if (mode.checkbox.checked) {
        addSelectedItem(mode);
    } else {
        removeSelectedItem(mode);
    }
}

function addSelectedItem(mode) {
    if(singleSelection){
        removeAllSelectedModes();
    }

    const modeDiv = document.createElement('div');
    modeDiv.className = 'item';

    const valueDiv = document.createElement('div');
    valueDiv.className = 'value';
    valueDiv.textContent = mode.name;

    const removeButtonDiv = document.createElement('div');
    removeButtonDiv.className = 'remove-button';
    removeButtonDiv.innerHTML = '<i class="fa-solid fa-trash"></i>';

    removeButtonDiv.addEventListener('click', () => {
        modeDiv.remove();
        mode.checkbox.checked = false;
    });

    modeDiv.appendChild(valueDiv);
    modeDiv.appendChild(removeButtonDiv);

    mode.view = modeDiv;
    selectedItemsContainer.appendChild(modeDiv);
}

function removeSelectedItem(mode) {
    if (mode.view != null) {
        mode.view.remove();

        mode.view = null;
    }
}

export function removeAllSelectedModes(){
    modeArray.forEach(mode => {
        if(mode.checkbox.checked && mode.view != null){
            mode.view.remove();
            mode.view = null;
            
            mode.checkbox.checked = false;
        }
    })
}