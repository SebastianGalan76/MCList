const ratingsPanel = document.getElementById('rate-server-popup');

const errorMessage = document.getElementById("error-message");
const ratingContainer = document.getElementById('rating-container');
const ratingTemplate = document.getElementById('rating-template');

var playerRatings = [];
var serverIp;

const rateButtons = document.querySelectorAll('.rate-button');
rateButtons.forEach(rateButton => {
    rateButton.addEventListener('click', () => {
        toggleRatingsPanel();
        ratingsPanel.scrollIntoView({
            behavior: 'smooth'
        });
    });
});

document.getElementById('save-ratings-button').addEventListener('click', saveRatings)
document.getElementById('close-rate-server-popup').addEventListener('click', toggleRatingsPanel)

export function loadRatingCategories(categories, user, server) {
    if (categories == null) {
        return;
    }

    serverIp = server.ip;

    categories.forEach(category => {
        const template = ratingTemplate.content.cloneNode(true);

        const name = template.querySelector('.name');
        name.innerHTML = category.name;

        const description = template.querySelector('.description');
        description.innerHTML = category.description;

        const starContainer = template.querySelector(".stars");
        const stars = starContainer.querySelectorAll("i");

        var playerRating = {
            category: category,
            user: user,
            server: server,
            rate: 0
        }

        stars.forEach((star, index1) => {
            star.addEventListener("click", () => {
                playerRating.rate = index1 + 1;
            });
        })
        stars.forEach((star, index1) => {
            star.addEventListener("mouseover", () => {
                stars.forEach((star, index2) => {
                    index1 >= index2 ? star.classList.add("active") : star.classList.remove("active");
                })
            });
        })
        starContainer.addEventListener("mouseleave", () => {
            stars.forEach((star, index1) => {
                playerRating.rate > index1 ? star.classList.add("active") : star.classList.remove("active");
            })
        })

        playerRatings.push(playerRating);
        ratingContainer.appendChild(template);
    })
}

function toggleRatingsPanel() {
    ratingsPanel.classList.toggle('active');
}

async function saveRatings() {
    try {
        const response = await fetch('/server/' + serverIp + '/rate/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(playerRatings)
        });


        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`Error: ${errorResponse.message}`);
        }
        else {
            const responseJson = await response.json();
            if (responseJson.status == "OK") {
                document.getElementById('rated-successfully-popup').classList.add("active");
            }
            else {
                errorMessage.innerHTML = responseJson.message;
            }
        }
    } catch (error) {
        errorMessage.innerHTML = error.message;
    }
}