const FILTER_FORM = document.querySelector(".filter-form");
const BTN_TOGGLE_STATUSES = document.querySelectorAll(".table .btn-toggle-status");

FILTER_FORM.addEventListener("submit", handleFilterProduct);
BTN_TOGGLE_STATUSES.forEach(btn => {
    btn.addEventListener("click", handleToggleStatus);
});

var pagi = new Pagination({ rowCount: g_iTotalItem, pageSize: g_iPageSize, defaultPage: g_iCurrentPage },
    "#page-pagination");

pagi.onChangePage(currentPage => {
    let origin = window.location.origin;
    let path = window.location.pathname;
    let urlParams = new URLSearchParams(window.location.search);
    let rqUrl = origin + path;
    let query = urlParams.toString();
    if (query != '') {
        query = query.replace(/page=\d+/ig, "page=" + currentPage);
    } else {
        query = "page=" + currentPage;
    }
    rqUrl += "?" + query;
    window.location.href = rqUrl;
});


generateSelectPriceRangeOptions(g_iPriceRangeIxs);
generateSelectBrandOptions(g_aBrands, g_aSearchBrandIds);
generateSelectCategoryOptions(g_aCategories, g_aSearchCategoryIds);

function handleFilterProduct(evt) {
    evt.preventDefault();
    let origin = window.location.origin;
    let path = window.location.pathname;
    let rqUrl = origin + path;
    let status = FILTER_FORM.querySelector("select[name='status']").value.trim();
    let isSort = FILTER_FORM.querySelector("select[name='isSort']").value.trim();
    let name = FILTER_FORM.querySelector("input[name='name']").value.trim();
    let categories = $("#category").select2("data");
    let brands = $("#brand").select2("data");
    let priceRanges = $("#priceRange").select2("data");
    let query = "?page=1";
    name != '' && (query += "&name=" + name)
    status != '' && (query += "&status=" + status)
    isSort != '' && (query += "&isSort=" + isSort);
    categories.forEach((val, ix) => {
        if (ix == 0) {
            query += "&category=" + val.id;
        } else {
            query += "," + val.id;
        }
    });
    brands.forEach((val, ix) => {
        if (ix == 0) {
            query += "&brand=" + val.id;
        } else {
            query += "," + val.id;
        }
    });
    priceRanges.forEach((val, ix) => {
        if (ix == 0) {
            query += "&priceRange=" + val.id;
        } else {
            query += "," + val.id;
        }
    });
    rqUrl += query;
    window.location.href = rqUrl;
}

function generateSelectPriceRangeOptions(selectedList) {
    let data = [
        {
            id: 0,
            text: "Dưới 10,000,000"
        },
        {
            id: 1,
            text: "10,000,000 - 15,000,000"
        },
        {
            id: 2,
            text: "15,000,000 - 20,000,000"
        },
        {
            id: 3,
            text: "20,000,000 - 25,000,000"
        },
        {
            id: 4,
            text: "Trên 25,000,000"
        }
    ];
    let arr = [];
    if (selectedList != null) {
        arr = data.map(v => {
            const isContain = selectedList.some(i => i == v.id);
            if (isContain) {
                return { id: v.id, text: v.text, selected: true };
            } else {
                return { id: v.id, text: v.text };
            }
        });
    } else {
        arr = [...data];
    }
    $('#priceRange').select2({
        placeholder: "Chọn mức giá",
        allowClear: true,
        data: arr
    });
}

async function handleToggleStatus(evt) {
    evt.preventDefault();
    const { target } = evt;
    const id = target.getAttribute("data-id");
    const isEnable = target.classList.contains("btn-success");
    if (isEnable) {
        const resp = await httpClient("/api/product/togglestatus", "PATCH", { productId: id, status: true });
        if (resp.isSuccess) {
            const badge = target.parentElement.previousElementSibling.querySelector("span");
            badge.classList.remove("badge-warning");
            badge.classList.add("badge-success");
            badge.innerText = "Enable";
            target.classList.remove("btn-success");
            target.classList.add("btn-warning");
            target.innerText = "Disable";
        } else {
            showAlert('failed', 'Thất bại', 'Có lỗi xảy ra!');
        }
    } else {
        const resp = await httpClient("/api/product/togglestatus", "PATCH", { productId: id, status: false });
        if (resp.isSuccess) {
            const badge = target.parentElement.previousElementSibling.querySelector("span");
            badge.classList.remove("badge-success");
            badge.classList.add("badge-warning");
            badge.innerText = "Disable";
            target.classList.add("btn-success");
            target.classList.remove("btn-warning");
            target.innerText = "Enable";
        } else {
            showAlert('failed', 'Thất bại', 'Có lỗi xảy ra!');
        }
    }
}

function generateSelectBrandOptions(data, selectedList) {
    data = data.map(b => { return { id: b.id, text: b.name } });
    let arr = [];
    if (selectedList != null) {
        arr = data.map(v => {
            const isContain = selectedList.some(i => i == v.id);
            if (isContain) {
                return { id: v.id, text: v.text, selected: true };
            } else {
                return { id: v.id, text: v.text };
            }
        });
    } else {
        arr = [...data];
    }
    $('#brand').select2({
        placeholder: "Chọn thương hiệu",
        allowClear: true,
        data: arr
    });
}

function generateSelectCategoryOptions(data, selectedList) {
    data = data.map(b => { return { id: b.id, text: b.name } });
    let arr = [];
    if (selectedList != null) {
        arr = data.map(v => {
            const isContain = selectedList.some(i => i == v.id);
            if (isContain) {
                return { id: v.id, text: v.text, selected: true };
            } else {
                return { id: v.id, text: v.text };
            }
        });
    } else {
        arr = [...data];
    }
    $('#category').select2({
        placeholder: "Chọn danh mục",
        allowClear: true,
        data: arr
    });
}
