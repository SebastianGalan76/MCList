export function loadSubservers(subserverArray) {
    const linkContainerMain = document.getElementById('subservers-container');
    const subserverTemplate = document.getElementById('subserver-template')

    subserverArray.forEach(subserver => {
        const template = subserverTemplate.content.cloneNode(true);
        const subserverElement = template.querySelector('.subserver');

        const title = subserverElement.querySelector('.title');
        title.innerHTML = subserver.name.name;
        title.style.color = subserver.name.color;

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

        linkContainerMain.appendChild(subserverElement);
    });
}