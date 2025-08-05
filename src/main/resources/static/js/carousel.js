// carousel.js
document.addEventListener("DOMContentLoaded", function () {
    const carousel = document.querySelector(".top-picks-carousel");
    const leftBtn = document.querySelector(".scroll-left");
    const rightBtn = document.querySelector(".scroll-right");

    const scrollAmount = 300;

    leftBtn.addEventListener("click", () => {
        carousel.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
    });

    rightBtn.addEventListener("click", () => {
        carousel.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    });
});
