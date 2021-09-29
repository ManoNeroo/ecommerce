(async function () {
    const LIST_PRODUCT_DIV = document.getElementById("product-list");
    const PRODUCT_LIST_WRAPPER = document.getElementById("product-list-wrapper");
    const PRODUCT_LIST_NULL = document.getElementById("product-list-null");
    const PRODUCT_LIST_SECTION = document.getElementById("product-list-section");
    const BRAND_LIST = document.getElementById("list-brand");
    const CATEGORY_LIST = document.getElementById("list-category");
    const PRICERANGE_LIST = document.getElementById("list-pricerange");
    const FILTER_SORT_LIS = document.querySelectorAll("#filter-sort li");
    const PRICE_RANGE_LIST = [{ id: 0, name: "Dưới 10 triệu" }, { id: 1, name: "Từ 10 - 15 triệu" }, { id: 2, name: "Từ 15 - 20 triệu" }, { id: 3, name: "Từ 20 - 25 triệu" }, { id: 4, name: "Trên 25 triệu" }];

    toggleLoading(true);
    let URL_OBJ = getUrlSearchObj();
    const brands = await getBrands();
    const categories = await getCategories();
    generateFilterList(brands, 'brand', BRAND_LIST);
    generateFilterList(categories, 'category', CATEGORY_LIST);
    generateFilterList(PRICE_RANGE_LIST, 'pricerange', PRICERANGE_LIST);
    let URLPARAMSTR = '&' + getProductApiRequestQuery(URL_OBJ, brands, categories, PRICE_RANGE_LIST, null);
    const resp = await loadItems(URLPARAMSTR, 1);
    toggleLoading(false);
    FILTER_SORT_LIS.forEach(li => {
        li.addEventListener("click", () => handleSortProduct(li))
    })
    let pagi = null;
    if (resp.isSuccess) {
        if (resp.data.length > 0) {
            pagi = new Pagination({ rowCount: resp.totalItem, pageSize: resp.limit, defaultPage: 1 },
                "#page-pagination");
            pagi.onChangePage(async currentPage => {
                await loadItems(URLPARAMSTR, currentPage);
                window.scrollTo({ top: 400, left: 0 });
            });
        }
    }

    function generateFilterList(data, key, LIST_ELM) {
        let names = [];
        if (URL_OBJ.hasOwnProperty(key)) {
            names = URL_OBJ[key].split(',');
        }
        LIST_ELM.innerHTML = '';
        let isChecked = false;
        data.forEach(price => {
            let isC = false;
            if (queryParamContain(names, price.name)) {
                isChecked = true;
                isC = true;
            }
            const checkboxItem = generateCheckboxItem(isC, price.name, price.name);
            checkboxItem.querySelector("input[type='checkbox']")
                .addEventListener("change", evt => handleChangeCheckBox(evt, key));
            LIST_ELM.appendChild(checkboxItem);
        });
        const checkboxItem = generateCheckboxItem(!isChecked, "Tất cả", '');
        checkboxItem.classList.add("all");
        checkboxItem.addEventListener("change", evt => handleChangeCheckBox(evt, key));
        LIST_ELM.insertAdjacentElement("afterbegin", checkboxItem);
    }

    async function handleSortProduct(li) {
        let dataSort = li.dataset.sort;
        const liActive = document.querySelector("#filter-sort li.active");
        if (dataSort == '') {
            dataSort = null;
        }
        URLPARAMSTR = '&' + getProductApiRequestQuery(URL_OBJ, brands, categories, PRICE_RANGE_LIST, dataSort);
        if(pagi) {
            pagi.setPage(1);
            if(pagi.configs.rowCount <= pagi.configs.pageSize) {
                await loadItems(URLPARAMSTR, 1);
            }
        }
        liActive.classList.remove("active");
        li.classList.add("active");
    }

    function handleChangeCheckBox(evt, key) {
        evt.preventDefault();
        const { target } = evt;
        const { value } = target;
        const orgin = window.location.origin;
        const path = window.location.pathname;
        let url = orgin + path;
        let query = '';
        let isRedirect = true;
        if (target.checked) {
            let isReplace = false;
            if (value == '') {
                isReplace = true;
            }
            const urlObj = insertToUrlSearchObj({ key: key, value: value }, isReplace);
            query = getSearchParamString(urlObj);
        } else {
            const urlObj = deleteAwayUrlSearchObj({ key: key, value: value })
            query = getSearchParamString(urlObj);
            if (value == '') {
                isRedirect = false;
                target.checked = true;
            }
        }
        url += query != '' ? '?' + query : query;
        isRedirect && (window.location.href = url)
    }

    async function loadItems(query, page) {
        toggleLoading(true);
        let urlRequest = `/api/product/filter?page=${page}&onlyEnable=true&limit=12${query != '&' ? query : ''}`;
        const resp = await httpClient(urlRequest);
        if (resp.isSuccess) {
            if (resp.data.length > 0) {
                toggleResultList(true);
                let noneRadiusBottom = resp.totalItem > 12 ? true : false;
                const sectionDiv = generateProductList(resp.data, noneRadiusBottom);
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

    function getSearchParamString(urlObj) {
        let query = '';
        if (urlObj != {}) {
            for (let key in urlObj) {
                query += `&${key}=${urlObj[key]}`;
            }
        }
        if (query != '') {
            query = query.slice(1);
        }
        return query;
    }

    function generateCheckboxItem(isChecked, label, value) {
        const wrapperDiv = document.createElement("div");
        wrapperDiv.className = "checkbox-item";
        wrapperDiv.innerHTML = `<label class="checkbox"><input type="checkbox" ${isChecked
            ? 'checked="checked"' : ''} value="${value}"><i></i>${label}</label>`;
        return wrapperDiv;
    }

    function generateProductList(data, noneRadiusBottom = false) {
        const rowDiv = document.createElement("div");
        rowDiv.className = "row";
        if (noneRadiusBottom) {
            PRODUCT_LIST_SECTION.classList.add("none-radius-bottom");
        }
        data.forEach(product => {
            const productItem = generateProductItem(product, "col-md-4");
            rowDiv.appendChild(productItem);
        });
        return rowDiv;
    }

    function toggleResultList(isShow) {
        if (isShow) {
            PRODUCT_LIST_WRAPPER.style.display = "block";
            PRODUCT_LIST_NULL.style.display = "none";
        } else {
            PRODUCT_LIST_WRAPPER.style.display = "none";
            PRODUCT_LIST_NULL.style.display = "block";
        }
    }

    function getUrlSearchObj() {
        var urlSearchParams = new URLSearchParams(window.location.search);
        let obj = {};
        urlSearchParams.forEach((val, key) => {
            obj[key.toLocaleLowerCase()] = val;
        });
        return obj;
    }

    function insertToUrlSearchObj(keyValue, isReplace = false) {
        let { key, value } = keyValue;
        key = removeAccent(key.trim()).toLocaleLowerCase();
        value = removeAccent(value.trim()).toLocaleLowerCase();
        if (URL_OBJ.hasOwnProperty(key) && !isReplace) {
            const arr = URL_OBJ[key].split(',');
            if (!queryParamContain(arr, value)) {
                URL_OBJ[key] += "," + value;
            }
        } else {
            URL_OBJ[key] = value;
        }
        if (URL_OBJ[key] == '') {
            delete URL_OBJ[key];
        }
        return URL_OBJ;
    }

    function deleteAwayUrlSearchObj(keyValue) {
        const { key, value } = keyValue;
        if (URL_OBJ.hasOwnProperty(key)) {
            const arrValue = URL_OBJ[key].split(',');
            let newValue = '';
            arrValue.forEach(val => {
                if (removeAccent(val).toLocaleLowerCase() != removeAccent(value.trim()).toLocaleLowerCase()) {
                    newValue += "," + removeAccent(val).toLocaleLowerCase();
                }
            });
            if (newValue == '') {
                delete URL_OBJ[key];
            } else {
                URL_OBJ[key] = newValue.slice(1);
            }
        }
        return URL_OBJ;
    }

    async function getBrands() {
        let urlRequest = `/api/brand?isEnable=true&isSortByName=true`;
        const resp = await httpClient(urlRequest);
        if (resp.isSuccess) {
            return resp.data;
        }
        return [];
    }

    async function getCategories() {
        let urlRequest = `/api/category?isEnable=true&isSortByName=true`;
        const resp = await httpClient(urlRequest);
        if (resp.isSuccess) {
            return resp.data;
        }
        return [];
    }

    function getProductApiRequestQuery(queryObj, brandList, categoryList, priceRangeList, isSort) {
        let query = '';
        if (queryObj.hasOwnProperty("brand")) {
            const brandNames = queryObj["brand"].split(',');
            let isBegin = true;
            brandList.forEach(brand => {
                if (queryParamContain(brandNames, brand.name)) {
                    if (isBegin) {
                        isBegin = false;
                        query += `&brand=${brand.id}`
                    } else {
                        query += `,${brand.id}`;
                    }
                }
            })
        }
        if (queryObj.hasOwnProperty("category")) {
            const categoryNames = queryObj["category"].split(',');
            let isBegin = true;
            categoryList.forEach(category => {
                if (queryParamContain(categoryNames, category.name)) {
                    if (isBegin) {
                        isBegin = false
                        query += `&category=${category.id}`
                    } else {
                        query += `,${category.id}`;
                    }
                }
            })
        }
        if (queryObj.hasOwnProperty("pricerange")) {
            const pricerangeNames = queryObj["pricerange"].split(',');
            let isBegin = true;
            priceRangeList.forEach(price => {
                if (queryParamContain(pricerangeNames, price.name)) {
                    if (isBegin) {
                        isBegin = false;
                        query += `&priceRange=${price.id}`
                    } else {
                        query += `,${price.id}`;
                    }
                }
            })
        }
        if (isSort != null) {
            query += '&isSort=' + isSort;
        }
        if (query != '') {
            query = query.slice(1);
        }
        return query;
    }

    function queryParamContain(list, item) {
        let isContain = false;
        for (let i = 0; i < list.length; i++) {
            const name = list[i].trim();
            if (removeAccent(name).toLowerCase() == removeAccent(item.trim()).toLowerCase()) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

})();