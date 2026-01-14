document.addEventListener("DOMContentLoaded", () => {
    const carousel = document.querySelector(".carousel");
    const slides = document.querySelectorAll(".carousel_img");
    const prevBtn = document.querySelector(".prev_btn");
    const nextBtn = document.querySelector(".next_btn");
    const dots = document.querySelectorAll(".dot");

    if (!carousel || slides.length === 0) return;

    let currentIndex = 0;
    const total = slides.length;

    function showSlide(index) {
        if (index < 0) index = total - 1;
        if (index >= total) index = 0;

        currentIndex = index;
        const offset = -index * 100;
        carousel.style.transform = `translateX(${offset}%)`;

        dots.forEach((dot, i) => {
            dot.classList.toggle("active", i === index);
        });
    }

    prevBtn?.addEventListener("click", () => showSlide(currentIndex - 1));
    nextBtn?.addEventListener("click", () => showSlide(currentIndex + 1));

    dots.forEach((dot, i) => {
        dot.addEventListener("click", () => showSlide(i));
    });

    let autoTimer = setInterval(() => {
        showSlide(currentIndex + 1);
    }, 3000);

    carousel.addEventListener("mouseenter", () => clearInterval(autoTimer));
    carousel.addEventListener("mouseleave", () => {
        autoTimer = setInterval(() => {
            showSlide(currentIndex + 1);
        }, 3000);
    });

    showSlide(0);
});
