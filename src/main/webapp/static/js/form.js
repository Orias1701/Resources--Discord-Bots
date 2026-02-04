document.addEventListener("DOMContentLoaded", function () {
    // --- mapping dùng chung ---
    const valueMap = {
        "DA_THANH_TOAN": "Đã T.Toán",
        "CHUA_THANH_TOAN": "Chưa T.Toán",
        "DAT_ONLINE": "Online",
        "DAT_TRUC_TIEP": "Trực tiếp",
        "DA_TRA": "Đã Trả",
        "DANG_SU_DUNG": "Đang sử dụng",
        "QUA_HAN": "Quá hạn",
        "TOT": "Tốt",
        "HONG": "Hỏng",
        "CAN_DON": "Cần dọn",
        "DA_ROI": "Đã rời",
        "DANG_O": "Đang ở",
        "DA_DAT": "Đã đặt",
        "TRONG": "Trống"
    };

    function mapValue(rawText) {
        return valueMap[rawText] || rawText;
    }

    // --- áp dụng mapping cho form-container ---
    function enhanceForm(container) {
        // mapping cho label
        container.querySelectorAll("label").forEach(label => {
            const raw = label.textContent.trim();
            const mapped = mapValue(raw);
            if (mapped !== raw) label.textContent = mapped;
        });

        // mapping cho select -> option
        container.querySelectorAll("select option").forEach(opt => {
            const raw = opt.textContent.trim();
            const mapped = mapValue(raw);
            if (mapped !== raw) opt.textContent = mapped;
        });

        // mapping cho input có value
        container.querySelectorAll("input[type=text], input[type=hidden]").forEach(inp => {
            const raw = inp.value.trim();
            const mapped = mapValue(raw);
            if (mapped !== raw) inp.value = mapped;
        });
    }

    document.querySelectorAll(".form-container").forEach(enhanceForm);
});

function loadForm(url) {
    // Ghi tạm chữ đang tải (nếu muốn)
    $("#formContent").html("Đang tải...");

    // Chỉ load dữ liệu, chưa show overlay
    $("#formContent").load(url, function(response, status, xhr) {
        if (status === "success") {
            $("#formOverlay").css("display", "flex");

            setTimeout(() => {
                $("#formOverlay").addClass("active");
            }, 10);
        } else {
            $("#formContent").html("Lỗi tải dữ liệu!");
        }
    });
}

function closeForm() {
    $("#formOverlay").removeClass("active");

    setTimeout(() => {
        $("#formOverlay").css("display", "none");
        $("#formContent").html("");
    }, 300);
}