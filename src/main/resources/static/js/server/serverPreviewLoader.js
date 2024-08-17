export function loadServerPreview(serverJson) {
    if (serverJson == null) {
        return;
    }

    const serverPreview = document.getElementById('server-preview');

    const addressValue = serverPreview.querySelector('.address-value');
    addressValue.innerHTML = serverJson.name.name;

    const motd = serverPreview.querySelector('.motd');
    const formattedMotd = serverJson.detail.motdHtml.replace(/\n/g, "<br>");
    motd.innerHTML = formattedMotd;

    const logo = serverPreview.querySelector('.logo-src');
    if (serverJson.detail.icon != null) {
        logo.src = serverJson.detail.icon;
    }
    else {
        logo.style.display = "none";
    }

    if (serverJson.mode != null) {
        const modeTag = serverPreview.querySelector('.mode-tag ');
        modeTag.style.display = "flex";

        modeTag.querySelector('.content').innerHTML = serverJson.mode.name;
    }
    if (serverJson.versions != null && serverJson.versions.length > 0) {
        const versionTag = serverPreview.querySelector('.version-tag');
        versionTag.style.display = "flex";

        const sortedArray = serverJson.versions.sort((a, b) => parseFloat(a.id) - parseFloat(b.id));

        const minValue = sortedArray[0];
        const maxValue = sortedArray[sortedArray.length - 1];

        if (minValue.id == maxValue.id) {
            versionTag.querySelector('.content').innerHTML = minValue.name;
        }
        else {
            versionTag.querySelector('.content').innerHTML = minValue.name + " - " + maxValue.name;
        }
    }
    if (serverJson.mods){
        serverPreview.querySelector('.mods-tag ').style.display = "flex";
    }
    if (serverJson.premium){
        serverPreview.querySelector('.premium-tag ').style.display = "flex";
    }

    const online = serverPreview.querySelector('.online-value');
    online.innerHTML = serverJson.onlinePlayers;

    const vote = serverPreview.querySelector('.vote-value');
    vote.innerHTML = serverJson.votes.length;
}