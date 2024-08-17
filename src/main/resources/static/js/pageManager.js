var pageContainer;
var currentPage;
var changePageAction;

export function initializePageManager(container, action){
    pageContainer = container;
    changePageAction = action;

    currentPage = 1;
}

export function refreshPageContainer(totalPages) {
    pageContainer.innerHTML = "";

    if (totalPages < 2) {
        return;
    }

    // Jeśli ilość stron jest mniejsza od 5, to tworzymy tylko elementy dla wszystkich stron
    if (totalPages <= 5) {
        for (var i = 1; i <= totalPages; i++) {
            createPageElement(i);
        }
    } else {
        if (currentPage >= 4) {
            createPageElement(1);
            createIntervalElement();
        }

        for (var i = currentPage - 2; i <= currentPage + 2; i++) {
            if (i > 0 && i <= totalPages) {
                createPageElement(i);
            }
        }

        if (currentPage <= totalPages - 3) {
            createIntervalElement();
            createPageElement(totalPages);
        }
    }
}

export function changePage(page) {
    currentPage = page;

    changePageAction();
}

export function getCurrentPage(){
    return currentPage;
}

function createPageElement(pageNumber) {
    var pageDiv = document.createElement("div");
    pageDiv.className = "page";

    if (pageNumber === currentPage) {
        pageDiv.classList.add("selected");
    }

    pageDiv.addEventListener('click', function () {
        changePage(pageNumber);
    });

    pageDiv.id = pageNumber;
    pageDiv.innerText = pageNumber;
    pageContainer.appendChild(pageDiv);
}

function createIntervalElement() {
    var intervalSpan = document.createElement("span");
    intervalSpan.className = "interval";
    intervalSpan.innerText = "...";
    pageContainer.appendChild(intervalSpan);
}

function resetPage() {
    currentPage = 1;
}
