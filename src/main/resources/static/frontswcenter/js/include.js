document.addEventListener("DOMContentLoaded", async () => {
  const includeTargets = document.querySelectorAll("[data-include]");

  for (let el of includeTargets) {
    const file = el.getAttribute("data-include");

    try {
      const response = await fetch(file);
      const html = await response.text();
      el.innerHTML = html;
    } catch (err) {
      console.error("Include load error:", file, err);
    }
  }
});
