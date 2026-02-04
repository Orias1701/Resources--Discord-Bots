// ====================== TIỆN ÍCH ======================
function measureTextWidth(text, font = "13px Arial") {
    const canvas = document.createElement("canvas");
    const ctx = canvas.getContext("2d");
    ctx.font = font;
    return ctx.measureText(text).width;
}

function mapValue(rawText) {
    const valueMap = {
        "DA_THANH_TOAN": "Đã T.Toán",
        "CHUA_THANH_TOAN": "Chưa T.Toán",
        "DAT_ONLINE": "Online",
        "DAT_TRUC_TIEP": "Trực tiếp",
        "DA_TRA": "Đã Trả",
        "DANG_SU_DUNG": "Đang SD",
        "QUA_HAN": "Quá hạn",
        "TOT": "Tốt",
        "HONG": "Hỏng",
        "CAN_DON": "Cần dọn",
        "DA_ROI": "Đã rời",
        "DANG_O": "Đang ở",
        "DA_DAT": "Đã đặt",
        "TRONG": "Trống"
    };
    return valueMap[rawText] || rawText;
}

function normalizeDate(str) {
    if (/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}/.test(str)) {
        return str.split("T")[0];
    }
    return str;
}

/**
 * cell: DOM element
 * font: font string for measuring
 * maxWidths: array (optional) to update measurement per column
 * i: column index
 * doMeasure: nếu true thì cập nhật maxWidths[i]
 */
function processCell(cell, font, maxWidths, i, doMeasure) {
    let rawText = (cell.textContent || "").trim();

    if (cell.classList.contains("data-cell")) {
        let display = normalizeDate(mapValue(rawText));
        cell.setAttribute("data-raw", rawText);

        const firstTextNode = Array.from(cell.childNodes).find(n => n.nodeType === Node.TEXT_NODE);
        if (firstTextNode) {
            firstTextNode.nodeValue = " " + display + " ";
        } else {
            cell.appendChild(document.createTextNode(display));
        }

        if (doMeasure && Array.isArray(maxWidths)) {
            maxWidths[i] = Math.max(maxWidths[i] || 0, measureTextWidth(display, font) + 20);
        }
    } else if (cell.classList.contains("item-actions")) {
        // giữ raw
        cell.setAttribute("data-raw", rawText);
        if (doMeasure && Array.isArray(maxWidths)) {
            // đo theo rawText (fallback)
            maxWidths[i] = Math.max(maxWidths[i] || 0, measureTextWidth(rawText, font) + 20);
        }
    }
}

// ====================== GRID APPLY (chia đều) ======================
function applyGrid(container, header, rows, colCount) {
    const gridTemplate = `repeat(${colCount}, 1fr)`;

    header.style.display = "grid";
    header.style.gridTemplateColumns = gridTemplate;

    rows.forEach(row => {
        row.style.display = "grid";
        row.style.gridTemplateColumns = gridTemplate;
    });
}

// ====================== ENHANCERS ======================
function enhanceTable(container) {
    const rows = container.querySelectorAll(".data-item");
    if (rows.length === 0) return;

    const firstItem = rows[0];
    const firstCells = firstItem.querySelectorAll(".data-cell");
    if (firstCells.length === 0) return;

    // ===== TẠO HEADER CHUNG =====
    const header = document.createElement("div");
    header.className = "data-header";
    firstCells.forEach(cell => {
        const headCell = document.createElement("div");
        headCell.textContent = cell.getAttribute("data-label") || "";
        header.appendChild(headCell);
    });
    // thêm cột action
    header.appendChild(document.createElement("div"));
    container.insertBefore(header, container.firstChild);

    // ===== CHUẨN BỊ ĐO WIDTH =====
    const colCount = header.querySelectorAll("div").length;
    const maxWidths = new Array(colCount).fill(0);

    const sampleStyle = window.getComputedStyle(firstItem);
    const font = `${sampleStyle.fontSize} ${sampleStyle.fontFamily}`;

    // đo header labels
    header.querySelectorAll("div").forEach((h, i) => {
        const text = (h.textContent || "").trim();
        maxWidths[i] = Math.max(maxWidths[i], measureTextWidth(text, font) + 20);
    });

    // xử lý cell + đo width
    rows.forEach(row => {
        const rowCells = row.querySelectorAll(".data-cell, .item-actions");
        rowCells.forEach((cell, i) => processCell(cell, font, maxWidths, i, true));
    });

    // đảm bảo cột action >= 50px
    const actionIndex = maxWidths.length - 1;
    if (maxWidths[actionIndex] < 50) {
        maxWidths[actionIndex] = 50;
    }

    // ===== TÍNH MIN-WIDTH-MAIN =====
    const maxColWidth = Math.max(...maxWidths);
    const totalMinWidth = maxColWidth * colCount;
    document.documentElement.style.setProperty("--min-width-main", totalMinWidth + "px");

    // ===== ÁP GRID CHIA ĐỀU =====
    applyGrid(container, header, rows, colCount);
}

function enhanceGrid(container) {
    const rows = container.querySelectorAll(".data-item");
    if (rows.length === 0) return;

    const sampleStyle = window.getComputedStyle(rows[0]);
    const font = `${sampleStyle.fontSize} ${sampleStyle.fontFamily}`;

    rows.forEach(row => {
        const cells = row.querySelectorAll(".data-cell, .item-actions");
        if (cells.length === 0) return;

        const header = document.createElement("div");
        header.className = "data-header";
        cells.forEach(cell => {
            const headCell = document.createElement("div");
            headCell.textContent = cell.getAttribute("data-label") || "";
            header.appendChild(headCell);
        });
        row.insertBefore(header, row.firstChild);

        const dataWrapper = document.createElement("div");
        dataWrapper.className = "data-data";

        cells.forEach((cell, i) => {
            // không đo để tính min-width-main ở chế độ card (tránh xung đột)
            processCell(cell, font, null, i, false);
            dataWrapper.appendChild(cell);
        });

        row.appendChild(dataWrapper);
    });
}

function enhanceDefault(container) {
    const rows = container.querySelectorAll(".data-item");
    if (rows.length === 0) return;

    const sampleStyle = window.getComputedStyle(rows[0]);
    const font = `${sampleStyle.fontSize} ${sampleStyle.fontFamily}`;

    rows.forEach(row => {
        const rowCells = row.querySelectorAll(".data-cell, .item-actions");
        rowCells.forEach((cell, i) => processCell(cell, font, null, i, false));
    });
}

// ====================== KHỞI ĐỘNG ======================
function enhanceContainers() {
    document.querySelectorAll(".data-container.table-lyo").forEach(enhanceTable);
    document.querySelectorAll(".data-container.grid-lyo").forEach(enhanceGrid);
    document.querySelectorAll(".data-container:not(.table-lyo):not(.grid-lyo)").forEach(enhanceDefault);
}

document.addEventListener("DOMContentLoaded", enhanceContainers);
