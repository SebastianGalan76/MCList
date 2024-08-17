import { selectedRank, removeRank } from "./staffManager.js";

const rankNamePreview = document.getElementById('rank-name-preview');
const rankEditorPopup = document.getElementById('rank-editor-popup');

const rankNameInput = document.getElementById('rank-name-input');
rankNameInput.addEventListener('input', () => {
    rankNamePreview.innerHTML = rankNameInput.value;
    selectedRank.name = rankNameInput.value;
});

const rankColorInput = document.getElementById('rank-color-input');
rankColorInput.addEventListener('input', () => {
    rankNamePreview.style.color = rankColorInput.value;
    selectedRank.color = rankColorInput.value;
});

const nickInput = document.getElementById('nick-input');
const discordInput = document.getElementById('discord-input');
const tiktokInput = document.getElementById('tiktok-input');
const youtubeInput = document.getElementById('youtube-input');
const instagramInput = document.getElementById('instagram-input');

const playerTemplate = document.getElementById('player-template');
const playersContainer = document.querySelector('.players-container');
const selectedPlayerPreview = document.querySelector('.selected-player');

const editButton = document.querySelector('.edit-button');
const removeButton = document.getElementById('remove-player-button');

const socialLinkError = document.getElementById('social-link-error');

const addPlayerError = document.querySelector('.add-player-error');

var selectedPlayer = null;

document.getElementById("remove-rank-button").addEventListener('click', () => {
    removeRank();
})

document.getElementById("create-new-player").addEventListener('click', () => {
    addPlayer();
});
document.getElementById("save-rank-changes-button").addEventListener('click', () => {
    saveChanges();
})

removeButton.addEventListener('click', function () {
    removePlayer();
});
editButton.addEventListener('click', function () {
    editPlayer();
});

document.addEventListener("click", (event) => {
    if (!event.target.closest('input') && !event.target.closest('.player')) {
        if (selectedPlayer != null) {
            unselectPlayer();
        }
    }
});

function addPlayer() {
    if (nickInput.value.length == 0) {
        addPlayerError.innerHTML = "Nick gracza nie może być pusty!";
        return;
    }
    else {
        addPlayerError.innerHTML = null;
    }
    if(!checkSocialLinks()){
        return;
    }

    const player = {
        nick: nickInput.value,
        discord: discordInput.value,
        tiktok: tiktokInput.value,
        youtube: youtubeInput.value,
        instagram: instagramInput.value,
    };

    addPlayerView(player, true);

    clearPanel();
    unselectPlayer();
}

function selectPlayer(player) {
    if (selectedPlayer != null) {
        selectedPlayer.view.classList.remove("selected");
    }
    selectedPlayer = player;
    player.view.classList.add("selected");

    nickInput.value = player.nick;
    discordInput.value = player.discord;
    tiktokInput.value = player.tiktok;
    youtubeInput.value = player.youtube;
    instagramInput.value = player.instagram;

    selectedPlayerPreview.style.visibility = "visible";
    selectedPlayerPreview.querySelector('.head-src').src = selectedPlayer.view.querySelector('.head-src').src;
    selectedPlayerPreview.querySelector('.nick').innerHTML = selectedPlayer.nick;
}

function unselectPlayer() {
    if (selectedPlayer != null) {
        selectedPlayer.view.classList.remove("selected");
    }
    selectedPlayer = null;
    clearPanel();

    selectedPlayerPreview.style.visibility = "hidden";
}

function removePlayer() {
    if (selectedPlayer != null) {
        selectedPlayer.view.remove();

        const index = selectedRank.players.findIndex(p => p.nick === selectedPlayer.nick);
        if (index !== -1) {
            selectedRank.players.splice(index, 1);
        }
    }
}
function editPlayer() {
    if (selectedPlayer != null) {
        if(!checkSocialLinks()){
            return;
        }

        const index = selectedRank.players.findIndex(p => p.nick === selectedPlayer.nick);
        selectedRank.players[index].nick = nickInput.value;
        selectedRank.players[index].discord = discordInput.value;
        selectedRank.players[index].tiktok = tiktokInput.value;
        selectedRank.players[index].youtube = youtubeInput.value;
        selectedRank.players[index].instagram = instagramInput.value;
        
        const view = selectedRank.players[index].view;
        const links = view.querySelector('.links');

        if (selectedRank.players[index].discord.length > 0) {
            links.querySelector('.fa-discord').style.display = "block";
        }
        else{
            links.querySelector('.fa-discord').style.display = "none";
        }
        if(selectedRank.players[index].tiktok.length > 0){
            links.querySelector('.fa-tiktok').style.display = "block";
        }
        else{
            links.querySelector('.fa-tiktok').style.display = "none";
        }
        if(selectedRank.players[index].youtube.length > 0){
            links.querySelector('.fa-youtube').style.display = "block";
        }
        else{
            links.querySelector('.fa-youtube').style.display = "none";
        }
        if(selectedRank.players[index].instagram.length > 0){
            links.querySelector('.fa-instagram').style.display = "block";
        }
        else{
            links.querySelector('.fa-instagram').style.display = "none";
        }

        view.querySelector('.nick').innerHTML = nickInput.value;
        view.querySelector('.head-src').src = "https://minotar.net/helm/" + nickInput.value;

        clearPanel();
    }
}
function clearPanel() {
    nickInput.value = null;
    discordInput.value = null;
    tiktokInput.value = null;
    youtubeInput.value = null;
    instagramInput.value = null;
}

function checkSocialLinks(){
    if(tiktokInput.value.length > 0){
        if(!tiktokInput.value.startsWith("https://www.tiktok.com/")){
            socialLinkError.innerHTML = "Link do TikToka powinien zaczynać się od https://www.tiktok.com/";
            return false;
        }
    }
    if(instagramInput.value.length > 0){
        if(!instagramInput.value.startsWith("https://www.instagram.com/")){
            socialLinkError.innerHTML = "Link do Instagrama powinien zaczynać się od https://www.instagram.com/";
            return false;
        }
    }
    if(youtubeInput.value.length > 0){
        if(!youtubeInput.value.startsWith("https://www.youtube.com/")){
            socialLinkError.innerHTML = "Link do YouTube powinien zaczynać się od https://www.youtube.com/";
            return false;
        }
    }
    socialLinkError.innerHTML = null;
    return true;
}

export function hideEditPanel() {
    rankEditorPopup.classList.remove("active");
}
export function showEditPanel(staffRank) {
    rankEditorPopup.classList.add("active");

    rankNameInput.value = staffRank.name;
    rankColorInput.value = staffRank.color;
    rankNamePreview.innerHTML = staffRank.name;
    rankNamePreview.style.color = staffRank.color;

    loadPlayers(staffRank.players);
}

function saveChanges(){
    const rankNameView = selectedRank.view.querySelector('.rank-name');

    rankNameView.innerHTML = selectedRank.name;
    rankNameView.style.color = selectedRank.color;

    hideEditPanel();
}

function loadPlayers(players) {
    playersContainer.innerHTML = null;

    if (players == null) {
        return;
    }

    players.forEach(player => {
        addPlayerView(player, false);
    })
}

function addPlayerView(player, addToArray) {
    const template = playerTemplate.content.cloneNode(true);
    const playerElement = template.querySelector('.player');

    playerElement.querySelector('.nick').innerHTML = player.nick;
    playerElement.querySelector('.head-src').src = "https://minotar.net/helm/" + player.nick;
    playersContainer.appendChild(playerElement);
    player.view = playerElement;

    const links = playerElement.querySelector('.links');
    if (player.discord.length > 0) {
        links.querySelector('.fa-discord').style.display = "block";
    }
    if(player.tiktok.length > 0){
        links.querySelector('.fa-tiktok').style.display = "block";
    }
    if(player.youtube.length > 0){
        links.querySelector('.fa-youtube').style.display = "block";
    }
    if(player.instagram.length > 0){
        links.querySelector('.fa-instagram').style.display = "block";
    }

    if(addToArray){
        if (selectedRank.players == null) {
            selectedRank.players = [];
        }
        selectedRank.players.push(player);
    }

    playerElement.addEventListener("click", () => {
        selectPlayer(player);
    });

    //Dragging system
    playerElement.addEventListener("dragstart", () => {
        setTimeout(() => playerElement.classList.add("dragging"), 0);
    });
    playerElement.addEventListener("dragend", () => {
        playerElement.classList.remove("dragging");
        changePlayerIndex();
    });

}

function changePlayerIndex(){
    if (selectedRank.players != null && selectedRank.players.length > 1) {
        const playerContainer = selectedRank.players[0].view.parentElement;
        const playerViews = playerContainer.querySelectorAll('.item');

        var indexPlayer = 0;
        playerViews.forEach(playerView => {
            selectedRank.players.forEach(playerRank => {
                if (playerRank.view === playerView) {
                    playerRank.index = indexPlayer;
                }
            });
            indexPlayer++;
        });
    }
}