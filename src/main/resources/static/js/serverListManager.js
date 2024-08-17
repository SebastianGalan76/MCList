const serverTemplate = document.getElementById("server-template");
const serverListContainer = document.getElementById("server-container");

import { getCurrentPage, refreshPageContainer } from "./pageManager.js";
import { getBanner } from "./smallBannerManager.js";
import { copyValueToClipboard } from "./copyToClipboard.js";

export async function loadServers() {
    const response = await fetch('/server/list/' + getCurrentPage(), {
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
        var responseJson = await response.json();

        populateList(responseJson.content);
        refreshPageContainer(responseJson.page.totalPages);
    }
}

export function populateList(listArray) {
    serverListContainer.innerHTML = null;
    let serversHeader = 0;
    let serverIndex = 1;

    listArray.forEach(serverJson => {
        const template = serverTemplate.content.cloneNode(true);

        const link = template.querySelector('.link');
        link.href = "/server/" + serverJson.ip;

        const addressValue = template.querySelector('.address-value');
        addressValue.innerHTML = serverJson.name.name;

        const motd = template.querySelector('.motd');
        const formattedMotd = serverJson.detail.motdHtml.replace(/\n/g, "<br>");
        motd.innerHTML = formattedMotd;

        const logo = template.querySelector('.logo-src');
        if (serverJson.detail.icon != null) {
            logo.src = serverJson.detail.icon;
        }
        else {
            logo.style.display = "none";
        }

        if (serverJson.mode != null) {
            const modeTag = template.querySelector('.mode-tag ');
            modeTag.style.display = "flex";

            modeTag.querySelector('.content').innerHTML = serverJson.mode.name;
        }
        if (serverJson.versions != null && serverJson.versions.length > 0) {
            const versionTag = template.querySelector('.version-tag');
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
        if (serverJson.mods) {
            template.querySelector('.mods-tag ').style.display = "flex";
        }
        if (serverJson.premium) {
            template.querySelector('.premium-tag ').style.display = "flex";
        }

        if (serverJson.promotionPoints > 0) {
            const promotionPoints = template.querySelector('.promotion-value');
            promotionPoints.innerHTML = serverJson.promotionPoints;

            template.querySelector('.server').classList.add('promoted');

            if (serversHeader == 0 || serversHeader == 2) {
                serversHeader = 1;
                serverListContainer.appendChild(createServerHeader('promoted', '👑', 'Promowane serwery'));
            }
        }
        else {
            template.querySelector('.promotion').style.display = "none";

            if (serversHeader == 0 || serversHeader == 1) {
                serversHeader = 2;
                serverListContainer.appendChild(createServerHeader('', '', 'Serwery'));
            }
        }
        
        template.querySelector('.fa-copy').addEventListener('click', (event) => {
            event.preventDefault();
            copyValueToClipboard(serverJson.ip);
        });

        const online = template.querySelector('.online-value');
        online.innerHTML = serverJson.onlinePlayers;

        const vote = template.querySelector('.vote-value');
        vote.innerHTML = serverJson.votes.length;

        if(serverIndex % 8 == 0){
            var banner = getBanner();
            if(banner != null){
                serverListContainer.appendChild(banner);
            }
        }

        serverListContainer.appendChild(template);
        serverIndex++;
    });
}

export function showLoadingPanel(){
    serverListContainer.innerHTML = null;
    serverListContainer.appendChild(createLoadedView());
}

function createServerHeader(className, icon, text) {
    const header = document.createElement('div');
    header.className = 'servers-header';
    if (className) header.classList.add(className);

    const title = document.createElement('div');
    title.className = 'title';

    if (icon) {
        const iconElement = document.createElement('div');
        iconElement.className = 'icon';
        iconElement.textContent = icon;
        title.appendChild(iconElement);
    }

    title.appendChild(document.createTextNode(text));
    header.appendChild(title);

    const divider = document.createElement('div');
    divider.className = className ? 'divider-line-horizontal' : 'divider-line';
    header.appendChild(divider);

    return header;
}

function createLoadedView() {
    const lottiePlayer = document.createElement('lottie-player');
    lottiePlayer.classList.add('center');
    lottiePlayer.id = 'loading-animation';
    lottiePlayer.src = '/animation/loading.json';
    lottiePlayer.background = 'transparent';
    lottiePlayer.speed = '1';
    lottiePlayer.style.width = '100px';
    lottiePlayer.style.height = '100px';
    lottiePlayer.loop = true;
    lottiePlayer.autoplay = true;
    return lottiePlayer;
}