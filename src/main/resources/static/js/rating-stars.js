document.addEventListener("DOMContentLoaded", function () {
  const ratingElements = document.querySelectorAll(".rating-stars");

  ratingElements.forEach((el) => {
    const rating = parseFloat(el.dataset.rating);
    const fullStars = Math.floor(rating / 2);
    const halfStar = rating % 2 >= 1;
    const totalStars = 5;

    el.innerHTML = "";

    for (let i = 0; i < totalStars; i++) {
      const star = document.createElement("span");
      star.classList.add("star");

      if (i < fullStars) {
        star.classList.add("full");
      } else if (i === fullStars && halfStar) {
        star.classList.add("half");
      }

      el.appendChild(star);
    }
  });
});
