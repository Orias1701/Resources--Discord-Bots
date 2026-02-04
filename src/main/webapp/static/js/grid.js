function updateMediaFlag() {
    const screenW = parseFloat(getComputedStyle(document.documentElement).getPropertyValue("--screen-w"));

    const th3 = screenW * 0.8; // 3/4
    const th2 = screenW * 0.6; // 2/4
    const th1 = screenW * 0.4; // 1/4

    const root = document.documentElement;

    // reset trước
    root.classList.remove("narrow-3", "narrow-2", "narrow-1");

    if (window.innerWidth < th1) {
        root.classList.add("narrow-1");
    } else if (window.innerWidth < th2) {
        root.classList.add("narrow-2");
    } else if (window.innerWidth < th3) {
        root.classList.add("narrow-3");
    }
}

window.addEventListener("resize", updateMediaFlag);
updateMediaFlag();

