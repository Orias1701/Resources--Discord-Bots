(function () {
  var path = window.location.pathname;
  var links = document.querySelectorAll('.app-sidebar a[href]');
  links.forEach(function (a) {
    try {
      var url = new URL(a.href, window.location.origin);
      // Active nếu path trùng hoặc là trang con (prefix)
      if (path === url.pathname || path.startsWith(url.pathname + '/')) {
        a.classList.add('active');
      }
    } catch (e) {}
  });
})();


function getDeviceScreenMetrics() {
  const screenW = window.screen.width;
  const screenH = window.screen.height;

  return {
    "screen-w": screenW,
    "screen-h": screenH,
    "spx-w": screenW / 1920,
    "spx-h": screenH / 1080,
    "rate-wh": screenW / screenH
  };
}

// Ví dụ sử dụng:
const metrics = getDeviceScreenMetrics();
console.log(metrics);

function updateScreenVars() {
  const sw = window.screen.width;
  const sh = window.screen.height;

  // tính toán
  const spxW = sw / 1920;
  const spxH = sh / 1080;
  const rate = sw / sh;

  // set vào CSS variables
  document.documentElement.style.setProperty('--screen-w', sw + "px");
  document.documentElement.style.setProperty('--screen-h', sh + "px");
  document.documentElement.style.setProperty('--spx-w', spxW);
  document.documentElement.style.setProperty('--spx-h', spxH);
  document.documentElement.style.setProperty('--rate-wh', rate);
}

// chạy khi load
updateScreenVars();

document.getElementById("sw_layout").onclick = function () {
    const parent = document.querySelector(".data-layout");
    const btn = this; // chính nút sw_layout

    if (parent.classList.contains("table-layout-sw")) {
        parent.classList.remove("table-layout-sw");
        parent.classList.add("grid-layout-sw");

        btn.classList.remove("table-active");
        btn.classList.add("grid-active");
    } else {
        parent.classList.remove("grid-layout-sw");
        parent.classList.add("table-layout-sw");

        btn.classList.remove("grid-active");
        btn.classList.add("table-active");
    }
};

const sidebar = document.querySelector(".app-sidebar");
const toggleBtn = document.getElementById("menu-toggle");
const mainContent = document.querySelector(".app-container .app-main-content");

// Hàm kiểm tra mobile
function isMobile() {
    return window.matchMedia("(max-width: 1259px)").matches;
}

// Hàm update sidebar khi resize
function updateSidebar(e) {
    if (e.matches) { 
        // Mobile
        sidebar.classList.remove("show");
        sidebar.classList.add("hide");
        toggleBtn.style.display = "inline-block"; // hiện icon
    } else {
        // Desktop
        sidebar.classList.remove("hide");
        sidebar.classList.add("show");
        toggleBtn.style.display = "none"; // ẩn icon
    }
}

// Toggle khi click icon
toggleBtn.onclick = function () {
    if (isMobile()) {
        if (sidebar.classList.contains("show")) {
            sidebar.classList.remove("show");
            sidebar.classList.add("hide");
        } else {
            sidebar.classList.remove("hide");
            sidebar.classList.add("show");
        }
    }
};

// Click vào main content => đóng sidebar
mainContent.onclick = function () {
    if (isMobile() && sidebar.classList.contains("show")) {
        sidebar.classList.remove("show");
        sidebar.classList.add("hide");
    }
};

// Lắng nghe khi thay đổi màn hình
const mediaQuery = window.matchMedia("(max-width: 1259px)");
mediaQuery.addEventListener("change", updateSidebar);

// Khởi tạo trạng thái ban đầu
updateSidebar(mediaQuery);

function enforceMinWidth() {
    if (window.innerWidth < 960) {
        document.body.style.width = "960px";
        document.body.style.overflowX = "auto"; // hiện scrollbar ngang
    } else {
        document.body.style.width = "auto";
        document.body.style.overflowX = "hidden";
    }
}

window.addEventListener("resize", enforceMinWidth);
window.addEventListener("load", enforceMinWidth);