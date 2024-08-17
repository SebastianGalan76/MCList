export function loadRatings(playerRatings, categories) {
    const averageRatings = calculateAverageRatings(playerRatings, categories);
    const overallRatign = calculateOverallAverage(averageRatings);

    const categoryTemplate = document.getElementById('rating-category-template');


    const overallRatingsContainers = document.querySelectorAll('.overall-ratings-container');
    overallRatingsContainers.forEach(overallRatignsContainer => {
        const categoriesContainer = overallRatignsContainer.querySelector('.categories-container');

        averageRatings.forEach(ratingObj => {
            const ratingView = categoryTemplate.content.cloneNode(true);
            ratingView.querySelector('.title').innerHTML = ratingObj.categoryName;
            ratingView.querySelector('.rating-value').innerHTML = ratingObj.averageRate.toFixed(1);;

            const percent = (ratingObj.averageRate / 5.0) * 100;
            ratingView.querySelector('.progress').style.width = percent + "%";
            categoriesContainer.appendChild(ratingView);
        });

        overallRatignsContainer.querySelector('.overall-rating').innerHTML = overallRatign.toFixed(1);
    });

    const ratingsCountByCategory = playerRatings.reduce((acc, playerRating) => {
        const categoryName = playerRating.category.name;
        const rate = playerRating.rate;

        if (!acc[categoryName]) {
            acc[categoryName] = { categoryName, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };
        }

        acc[categoryName][rate] += 1;

        return acc;
    }, {});

    categories.forEach(category => {
        const categoryName = category.name;
        if (!ratingsCountByCategory[categoryName]) {
            ratingsCountByCategory[categoryName] = { categoryName, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };
        }
    })

    const ratingsCategoryContainer = document.getElementById('ratings-categories');
    const ratingTemplate = document.getElementById('ratings-template');
    const ratingTemplateInverse = document.getElementById('ratings-template-inverse');

    const ratingsPerCategory = Object.values(ratingsCountByCategory);

    var index = 1;
    ratingsPerCategory.forEach(ratings => {
        var template;
        if (index % 2 == 0) {
            template = ratingTemplate.content.cloneNode(true);
        }
        else {
            template = ratingTemplateInverse.content.cloneNode(true);
        }
        index++;

        const ratingsElement = template.querySelector('.ratings-main');
        const totalRatings = ratings[5] + ratings[4] + ratings[3] + ratings[2] + ratings[1];

        ratingsElement.querySelector('.title').innerHTML = ratings.categoryName;
        for (var i = 1; i <= 5; i++) {
            var percent = (ratings[i] / totalRatings) * 100;

            var ratingsView = ratingsElement.querySelector('.rating-' + i);
            ratingsView.querySelector('.rating-value').innerHTML = ratings[i];
            ratingsView.querySelector('.progress').style.width = percent + "%";

            var average;
            if (totalRatings != 0) {
                average = ((ratings[5] * 5 + ratings[4] * 4 + ratings[3] * 3 + ratings[2] * 2 + ratings[1] * 1) / totalRatings);
            }
            else {
                average = 0;
            }

            ratingsElement.querySelector('.overall-rating').innerHTML = average.toFixed(1);
        }

        ratingsCategoryContainer.appendChild(ratingsElement);
    });
}

function calculateAverageRatings(ratings, categories) {
    const categoryRatings = {};

    categories.forEach(category => {
        const categoryId = category.id;
        if (!categoryRatings[categoryId]) {
            categoryRatings[categoryId] = {
                name: category.name,
                totalRate: 0,
                count: 0
            };
        }
    });

    ratings.forEach(rate => {
        const categoryId = rate.category.id;
        categoryRatings[categoryId].totalRate += rate.rate;
        categoryRatings[categoryId].count += 1;
    });

    const averageRatings = [];
    for (const categoryId in categoryRatings) {
        const category = categoryRatings[categoryId];
        var averageRate;

        if (category.count != 0) {
            averageRate = category.totalRate / category.count;
        }
        else {
            averageRate = 0;
        }

        averageRatings.push({
            categoryId: parseInt(categoryId),
            categoryName: category.name,
            averageRate: averageRate
        });
    }

    // Sortowanie averageRatings po categoryId
    averageRatings.sort((a, b) => a.categoryId - b.categoryId);

    return averageRatings;
}
function calculateOverallAverage(ratings) {
    if (ratings == null || ratings.length == 0) {
        return 0;
    }

    const totalRate = ratings.reduce((sum, rating) => sum + rating.averageRate, 0);
    const overallAverage = totalRate / ratings.length;
    return overallAverage;
}