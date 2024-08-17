const searchInput = document.getElementById('version-input');

//Upper panel
const listPanel = document.getElementById('version-list-panel');
const listContainer = document.getElementById('version-list-container');

const selectedItemsContainer = document.getElementById('selected-version-container');

var versionArray = [];
var selectedVersionsIds = [];
var singleSelection = false;

searchInput.addEventListener('focus', function () {
    listPanel.style.display = 'block';
    searchInput.classList.add("focused");
});

searchInput.addEventListener('input', function () {
    const filteredArray = versionArray.filter(version =>
        version.name.toLowerCase().startsWith(searchInput.value.toLowerCase())
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

loadVersions();

export function getSelectedVersions() {
    const selectedVersions = versionArray.filter(version =>
        version.checkbox.checked
    );

    return selectedVersions;
}

export function loadSelectedVersions(selectedVersions) {
    if(selectedVersions != null){
        selectedVersionsIds = selectedVersions.map(obj => obj.id);

        if(versionArray.length > 0){
            if (selectedVersionsIds.length > 0) {
                selectedVersionsIds.forEach(selectedVersion => {
                    const version = versionArray.find(version => version.id === selectedVersion);
                    if (version != null) {
                        version.checkbox.checked = true;
                        addSelectedItem(version);
                    }
                });
            }
        }
    }
}
export function onlySingleSelectionVersion(){
    singleSelection = true;
}

async function loadVersions() {
    const response = await fetch('/version/listAll', {
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
        versionArray = await response.json();

        populateList(versionArray);
    }
}

function populateList(versionArray) {
    listContainer.innerHTML = null;

    versionArray.forEach(version => {
        const itemDiv = document.createElement('div');
        itemDiv.className = 'item';

        if (version.checkbox != null) {
            itemDiv.appendChild(version.checkbox);
        }
        else {
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'checkbox';
            checkbox.id = `checkbox-version-${version.id}`;
            version.checkbox = checkbox;

            checkbox.addEventListener('change', () => handleCheckboxChange(version));
            itemDiv.appendChild(checkbox);
        }

        const label = document.createElement('label');
        label.htmlFor = `checkbox-version-${version.id}`;
        label.textContent = version.name;

        itemDiv.appendChild(label);
        listContainer.appendChild(itemDiv);
    });

    if (selectedVersionsIds.length > 0) {
        selectedVersionsIds.forEach(selectedVersion => {
            const version = versionArray.find(version => version.id === selectedVersion);
            if (version != null) {
                version.checkbox.checked = true;
                addSelectedItem(version);
            }
        });
    }
}

function handleCheckboxChange(version) {
    if (version.checkbox.checked) {
        addSelectedItem(version);
    } else {
        removeSelectedItem(version);
    }
}

function addSelectedItem(version) {
    if(singleSelection){
        removeAllSelectedVersions();
    }

    const versionDiv = document.createElement('div');
    versionDiv.className = 'item';

    const valueDiv = document.createElement('div');
    valueDiv.className = 'value';
    valueDiv.textContent = version.name;

    const removeButtonDiv = document.createElement('div');
    removeButtonDiv.className = 'remove-button';
    removeButtonDiv.innerHTML = '<i class="fa-solid fa-trash"></i>';

    removeButtonDiv.addEventListener('click', () => {
        versionDiv.remove();
        version.checkbox.checked = false;
    });

    versionDiv.appendChild(valueDiv);
    versionDiv.appendChild(removeButtonDiv);

    version.view = versionDiv;
    selectedItemsContainer.appendChild(versionDiv);
}

function removeSelectedItem(version) {
    if (version.view != null) {
        version.view.remove();

        version.view = null;
    }
}

export function removeAllSelectedVersions(){
    versionArray.forEach(version => {
        if(version.checkbox.checked && version.view != null){
            version.view.remove();
            version.view = null;
            
            version.checkbox.checked = false;
        }
    })
}