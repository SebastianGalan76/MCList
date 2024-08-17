const bannerTemplate = document.getElementById("banner-small-template");

var banners = [];
var bannerIndex = 0;

export function loadBanners(bannersJson) {
    banners = bannersJson;
}

export function getBanner() {
    if(banners == null || banners.length == 0){
        return null;
    }

    var bannerJson = banners[bannerIndex];
    const template = bannerTemplate.content.cloneNode(true);

    const banner = template.querySelector('.banner-small-container');

    const bannerLink = banner.querySelector('.banner');
    bannerLink.href = bannerJson.link;

    const bannerSrc = banner.querySelector('.banner-img-scr');
    bannerSrc.src = bannerJson.filePath;

    bannerIndex = (bannerIndex + 1) % banners.length;

    return banner;
}
