const contentContainers = document.querySelectorAll('.main-container-content');
const contentButtons = document.querySelectorAll("#main-container #button-container .button");

function showContent(button, contentId, scrollTo) {
    contentContainers.forEach(content => {
        content.style.display = "none";
    });
    contentButtons.forEach(button => {
        button.classList.remove("selected");
    });

    const content = document.getElementById(contentId);
    content.style.display = "block";
    button.classList.add("selected");

    if (scrollTo) {
        content.scrollIntoView({
            behavior: 'smooth'
        });
    }
}