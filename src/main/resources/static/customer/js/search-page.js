(async function () {
    const LIST_PRODUCT_DIV = document.getElementById("list-product-section");
    const SEARCH_RESULT_LIST = document.getElementById("search-result-list");
    const SEARCH_RESULT_NULL = document.getElementById("search-result-null");
    const SEARCH_RESULT_TITLE = document.getElementById("search-result-title");
    const FORM_HEADER_SEARCH_INPUT = document.querySelector(".header-search form input");

    FORM_HEADER_SEARCH_INPUT.value = g_aKeyword[0];
    toggleLoading(true);
    const resp = await loadItems(g_aKeyword[0], 1);
    toggleLoading(false);

    if (resp.isSuccess) {
        if (resp.data.length > 0) {
            var pagi = new Pagination({ rowCount: resp.totalItem, pageSize: resp.limit, defaultPage: 1 },
                "#page-pagination");
            pagi.onChangePage(async currentPage => {
                await loadItems(g_aKeyword[0], currentPage);
                window.scrollTo({top: 0,left: 0});
            });
        }
    }

    async function loadItems(keyword, page) {
        toggleLoading(true);
        const resp = await httpClient(`/api/product/filter?page=${page}&limit=12&name=${keyword}&onlyEnable=true`);
        if (resp.isSuccess) {
            if (resp.data.length > 0) {
                toggleResultList(true);
                SEARCH_RESULT_TITLE.querySelector("h1").innerHTML = `Tìm thấy <span>${resp.totalItem}</span> kết quả với từ khóa <span>"${keyword}"`;
                let noneRadiusBottom = resp.totalItem > 12 ? true : false;
                const sectionDiv = generateProductSection(resp.data, noneRadiusBottom);
                LIST_PRODUCT_DIV.innerHTML = '';
                LIST_PRODUCT_DIV.appendChild(sectionDiv);
            } else {
                toggleResultList(false);
            }
        } else {
            toggleResultList(false);
        }
        toggleLoading(false);
        return resp;
    }

    function generateProductSection(data, noneRadiusBottom = false) {
        const sectionDiv = document.createElement("div");
        const sectionBodyDiv = document.createElement("div");
        const sectionBodyRowDiv = document.createElement("div");
        sectionDiv.className = "section";
        sectionBodyDiv.className = "section-body";
        sectionBodyRowDiv.className = "row";
        if(noneRadiusBottom) {
            sectionDiv.classList.add("none-radius-bottom");
        }
        sectionBodyDiv.appendChild(sectionBodyRowDiv);
        sectionDiv.append(sectionBodyDiv);
        data.forEach(product => {
            const productItem = generateProductItem(product, "col-md-3");
            sectionBodyRowDiv.appendChild(productItem);
        });
        return sectionDiv;
    }

    function toggleResultList(isShow) {
        if (isShow) {
            SEARCH_RESULT_LIST.style.display = "block";
            SEARCH_RESULT_NULL.style.display = "none";
            SEARCH_RESULT_TITLE.style.display = "block";
        } else {
            SEARCH_RESULT_LIST.style.display = "none";
            SEARCH_RESULT_NULL.style.display = "block";
            SEARCH_RESULT_TITLE.style.display = "none";
        }
    }
})();