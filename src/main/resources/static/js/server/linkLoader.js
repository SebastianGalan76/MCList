var linkTemplate;
var classNamesMap;

export function loadLinks(linkArray) {
    const linkContainerAside = document.getElementById('link-panel-aside');
    const linkContainerMain = document.getElementById('link-panel');

    linkTemplate = document.getElementById('template-link')
    classNamesMap = {
        'shop': {
            name: ['sklep', 'item shop', 'rynek'],
            iconClass: "fa-solid fa-cart-shopping"
        },
        'discord': {
            name: ['discord', 'dc'],
            iconClass: "fa-brands fa-discord"
        },
        'facebook': {
            name: ['fb', 'facebook', 'fanpage'],
            iconClass: "fa-brands fa-square-facebook"
        },
        'instagram': {
            name: ['instagram', 'insta'],
            iconClass: "fa-brands fa-square-instagram"
        },
        'youtube': {
            name: ['yt', 'youtube'],
            iconClass: "fa-brands fa-square-youtube"
        },
        'tiktok': {
            name: ['tk', 'tiktok'],
            iconClass: "fa-brands fa-tiktok"
        },
        'main-page': {
            name: ['strona', 'strona główna', 'homepage', 'stronka'],
            iconClass: "fa-solid fa-house"
        },
        'twitter': {
            name: ['x', 'twitter'],
            iconClass: "fa-brands fa-x-twitter"
        },
        'regulamin': {
            name: ['regulamin', 'rules'],
            iconClass: "fa-solid fa-scroll"
        },
    };

    linkArray.forEach(link => {
        linkContainerAside.appendChild(getLinkElement(link));
        linkContainerMain.appendChild(getLinkElement(link));
    });
}

function getLinkElement(link){
    const template = linkTemplate.content.cloneNode(true);
    const linkElement = template.querySelector('.server-link');

    const name = linkElement.querySelector('.name');
    name.innerHTML = link.name;

    template.querySelector('a').href = link.url;

    const lowerCaseName = link.name.toLowerCase();
    for (const [className, data] of Object.entries(classNamesMap)) {
        if (data.name.includes(lowerCaseName)) {
            linkElement.classList.add(className);

            const iconClass = linkElement.querySelector('.icon');
            const classArray = data.iconClass.split(' ');
            if(classArray.length > 0){
                iconClass.classList.add(...classArray);
            }

            break;
        }
    }

    return linkElement;
}