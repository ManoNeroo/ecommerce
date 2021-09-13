class Pagination {
    constructor(configs, selector) {
        if (selector.nodeType !== 1) {
            selector = document.querySelector(selector);
        }
        this.configs = validateConfigs(configs);
        this.element = initElement(this.configs, selector);
    }

    onChangePage(callBack) {
        this.element.addEventListener("changePage", evt => {
            callBack(evt.detail.currentPage);
        });
    }

    setPage(page) {
        if (page <= this.configs.rowCount) {
            initPagination({ ...this.configs, defaultPage: page }, this.element);
        }
    }
}

const PAGINATION_BTN_ACTIVE_CLASS_NAME = "active";
const PAGINATION_WRAPPER_CLASS_NAME = "mn-pagination";
const PAGINATION_CLASS_NAME = "mn-pagination-elm";
const PAGINATION_ITEM_CLASS_NAME = "mn-pagination-item";
const DEFAULT_HREF = "javascript:void(0)";
const PREV_SVG = `<svg class="mn-pagination-icprev" focusable="false" viewBox="0 0 24 24" aria-hidden="true"><path d="M15.41 16.09l-4.58-4.59 4.58-4.59L14 5.5l-6 6 6 6z"></path></svg>`;
const NEXT_SVG = `<svg class="mn-pagination-icnext" focusable="false" viewBox="0 0 24 24" aria-hidden="true"><path d="M8.59 16.34l4.58-4.59-4.58-4.59L10 5.75l6 6-6 6z"></path></svg>`;

function getTotalPages(rowCount, pageSize) {
    if (rowCount % pageSize === 0) {
        return Math.round(rowCount / pageSize);
    } else {
        return (Math.floor(rowCount / pageSize) + 1);
    }
}

function validateConfigs(configs) {
    if (typeof (configs.pageSize) !== "number" || configs.pageSize < 1) {
        configs.pageSize = 1;
    }
    if (typeof (configs.rowCount) !== "number" || configs.rowCount < 1) {
        configs.rowCount = 1;
    }
    if (typeof (configs.defaultPage) !== "number" || configs.defaultPage < 1) {
        configs.defaultPage = 1;
    }
    if (!configs.href) {
        configs.href = DEFAULT_HREF;
    }
    return configs;
}

function createActionButton(content, href) {
    const paginationButton = document.createElement("li");
    const paginationButtonA = document.createElement("a");
    paginationButton.classList.add(PAGINATION_ITEM_CLASS_NAME);
    paginationButtonA.innerHTML = content;
    paginationButtonA.href = href;
    paginationButton.appendChild(paginationButtonA);
    return paginationButton;
}

function initElement(configs, selector) {
    const paginationWrapper = document.createElement("div");
    paginationWrapper.classList.add(PAGINATION_WRAPPER_CLASS_NAME);
    initPagination(configs, paginationWrapper);
    selector.appendChild(paginationWrapper);
    return paginationWrapper;
}

function initPagination({ defaultPage, pageSize, rowCount, href }, paginationWrapper) {
    let active;
    let beforePage = defaultPage - 1;
    let afterPage = defaultPage + 1;
    const totalPages = getTotalPages(rowCount, pageSize);
    if (totalPages > 1) {
        const pagination = document.createElement("ul");
        const paginationPrev = createActionButton(PREV_SVG, href);
        const paginationNext = createActionButton(NEXT_SVG, href);
        pagination.appendChild(paginationPrev);
        if (defaultPage > 1) {
            paginationPrev.addEventListener("click", () => {
                initPagination({ defaultPage: defaultPage - 1, pageSize, rowCount, href }, paginationWrapper);
            });
            paginationPrev.classList.remove("disabled");
        } else {
            paginationPrev.classList.add("disabled");
        }

        if (defaultPage == totalPages) {
            beforePage = beforePage - 2;
        } else if (defaultPage == totalPages - 1) {
            beforePage = beforePage - 1;
        }
        if (defaultPage == 1) {
            afterPage = afterPage + 2;
        } else if (defaultPage == 2) {
            afterPage = afterPage + 1;
        }

        if (defaultPage > 2 && beforePage > 1) {
            const paginationBtn = createActionButton("1", href);
            paginationBtn.addEventListener("click", () => {
                initPagination({ defaultPage: 1, pageSize, rowCount, href }, paginationWrapper);
            });
            pagination.appendChild(paginationBtn);
            if (defaultPage > 3 && beforePage > 2) {
                const paginationBtn = createActionButton("...", href);
                pagination.appendChild(paginationBtn);
            }
        }

        for (let plength = beforePage; plength <= afterPage; plength++) {
            if (plength > totalPages || plength < 0) {
                continue;
            }
            if (plength == 0) {
                plength = plength + 1;
            }
            if (defaultPage == plength) {
                active = true;
            } else {
                active = false;
            }
            const paginationBtn = createActionButton(plength, href);
            paginationBtn.addEventListener("click", () => {
                initPagination({ defaultPage: plength, pageSize, rowCount, href }, paginationWrapper);
            });
            if (active) {
                paginationBtn.classList.add(PAGINATION_BTN_ACTIVE_CLASS_NAME);
                const event = new CustomEvent("changePage", {
                    detail: {
                        currentPage: defaultPage
                    }
                });
                paginationWrapper.dispatchEvent(event);
            }
            pagination.appendChild(paginationBtn);
        }

        if (defaultPage < totalPages - 1 && afterPage < totalPages) {
            if (defaultPage < totalPages - 2 && afterPage < totalPages - 1) {
                const paginationBtn = createActionButton("...", href);
                pagination.appendChild(paginationBtn);
            }
            const paginationBtn = createActionButton(totalPages, href);
            paginationBtn.addEventListener("click", () => {
                initPagination({ defaultPage: totalPages, pageSize, rowCount, href }, paginationWrapper);
            });
            pagination.appendChild(paginationBtn);
        }

        if (defaultPage < totalPages) {
            paginationNext.addEventListener("click", () => {
                initPagination({ defaultPage: defaultPage + 1, pageSize, rowCount, href }, paginationWrapper);
            });
            paginationNext.classList.remove("disabled");
        } else {
            paginationNext.classList.add("disabled")
        }
        pagination.classList.add(PAGINATION_CLASS_NAME);
        pagination.appendChild(paginationNext);

        paginationWrapper.innerHTML = "";
        paginationWrapper.appendChild(pagination);
    }
}