const linkTemplate = document.getElementById('link-template');
const linkContainer = document.getElementById('link-list-container');

const nameInput = document.getElementById("create-link-name-input");
const urlInput = document.getElementById("create-link-url-input");

const createLinkError = document.getElementById("create-link-error");

const createButton = document.getElementById("create-link-button");
createButton.addEventListener('click', () => {
    createLink();
});

var linkId = -1000;
export var links = [];
export var selectedLink;

export function getSortedLinks() {
    const rankViews = linkContainer.querySelectorAll('.item');

    var index = 0;
    rankViews.forEach(rankView => {
        links.forEach(staffRank => {
            if (staffRank.view === rankView) {
                staffRank.index = index;
                return;
            }
        });
        index++;
    });

    return links;
}

function createLink() {
    nameInput.value = nameInput.value.trim();
    if (nameInput.value.length == 0) {
        createLinkError.innerHTML = "Nazwa linku nie może być pusta!";
        return;
    }
    if (urlInput.value.length == 0) {
        createLinkError.innerHTML = "Link nie może być pusty!";
        return;
    }
    if (!isValidURL(urlInput.value)) {
        createLinkError.innerHTML = "Podany link nie jest prawidłowy!";
        return;
    }
    else {
        createLinkError.innerHTML = null;
    }
    
    if (selectedLink != null) {
        selectedLink.view.classList.remove("selected");

        createButton.innerHTML = "Stwórz";

        selectedLink.name = nameInput.value;
        selectedLink.url = urlInput.value;

        selectedLink = null;
        clearPanel();
        return;
    }

    const link = {
        id: linkId,
        name: nameInput.value,
        url: urlInput.value,
        index: links.length,
    }
    addLinkView(link);

    clearPanel();
    linkId++;
}
function removeLink(idToRemove) {
    const linkToRemove = links.find(link => link.id === idToRemove);

    if (linkToRemove) {
        linkToRemove.view.remove();

        const indexToRemove = links.findIndex(link => link.id === idToRemove);

        if (indexToRemove !== -1) {
            links.splice(indexToRemove, 1);
        }
    }
}

export function loadLinks(loadedLinksJson) {
    loadedLinksJson.forEach(loadedLink => {
        const transformedObject = {
            id: loadedLink.id,
            name: loadedLink.name,
            url: loadedLink.url
        };
        addLinkView(transformedObject);
    });
}

function addLinkView(link) {
    const template = linkTemplate.content.cloneNode(true);
    const linkElement = template.querySelector('.link');

    const linkPreview = linkElement.querySelector('.link-name-preview');

    linkPreview.innerHTML = link.name;
    linkContainer.appendChild(linkElement);

    linkElement.querySelector('.remove-link-button').addEventListener('click', () => {
        removeLink(link.id);
    });

    link.view = linkElement;
    links.push(link);


    //Dragging system
    linkElement.addEventListener("dragstart", () => {
        setTimeout(() => linkElement.classList.add("dragging"), 0);
        if(selectedLink != null){
            createButton.innerHTML = "Stwórz";
            selectedLink.view.classList.remove("selected");
            clearPanel();
        }

        selectedLink = null;
    });
    linkElement.addEventListener("dragend", () => linkElement.classList.remove("dragging"));

    //Selecting system
    linkElement.addEventListener('click', () => {
        if (selectedLink != null) {
            selectedLink.view.classList.remove("selected");

            if (selectedLink.view == linkElement) {
                selectedLink = null;
                createButton.innerHTML = "Stwórz";
                clearPanel();

                return;
            }
        }

        selectedLink = link;
        selectedLink.view.classList.add("selected");

        nameInput.value = selectedLink.name;
        urlInput.value = selectedLink.url;
        createButton.innerHTML = "Zapisz";
    });
}

function clearPanel() {
    nameInput.value = null;
    urlInput.value = null;
}

function isValidURL(str) {
    try {
        new URL(str);
        return true;
    } catch (e) {
        return false;
    }
}