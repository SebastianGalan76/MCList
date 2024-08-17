const nameInput = document.getElementById('name-input');
const premiumCheckbox = document.getElementById('server-premium-checkbox');
const modsCheckbox = document.getElementById('server-mods-checkbox');


import { getCurrentPage, refreshPageContainer } from "./pageManager.js";
import { getSelectedModes, onlySingleSelectionMode } from "./modeManager.js";
import { getSelectedVersions, onlySingleSelectionVersion } from "./versionManager.js";
import { populateList, showLoadingPanel } from "./serverListManager.js";

onlySingleSelectionMode();
onlySingleSelectionVersion();

document.getElementById('search-server-button').addEventListener('click', searchServers);
var isFunctionLocked = false;

export async function searchServers() {
    showLoadingPanel();

    if (isFunctionLocked) {
        setTimeout(() => {
            searchServers();
        }, 5000);

        return;
    }

    isFunctionLocked = true;
    setTimeout(() => {
        isFunctionLocked = false;
    }, 5000);

    let version = null;
    let selectedVersions = getSelectedVersions();
    if(selectedVersions.length>0){
        version = selectedVersions[0];
    }

    let mode = null;
    let selectedModes = getSelectedModes();
    if(selectedModes.length>0){
        mode = selectedModes[0];
    }

    let searchDto = {
        name: nameInput.value,
        mode: mode,
        version: version,
        premium: premiumCheckbox.checked,
        mods: modsCheckbox.checked
    }

    const response = await fetch('/server/search/' + getCurrentPage(), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(searchDto)
    });


    if (!response.ok) {
        const errorResponse = await response.json();
        throw new Error(`Error: ${errorResponse.message}`);
    }
    else {
        var responseJson = await response.json();

        populateList(responseJson.content);
        refreshPageContainer(responseJson.page.totalPages);
    }
}