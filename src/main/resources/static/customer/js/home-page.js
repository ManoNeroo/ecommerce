(function () {
    const TOP_PRODUCT = 12;
    const TOP_PRODUCT_CATEGORYIDS = "1,2,3"
    const TOP_PRODUCT_TITLE = [{ title: "Điện thoại nổi bật", link: "/product?category=dien thoai" },
    { title: "Laptop bán chạy", link: "/product?category=laptop" },
    { title: "Tablet hot", link: "/product?category=tablet" }];

    loadTopProduct();

    async function loadTopProduct() {
        const resp = await httpClient(`/api/product/top?top=${TOP_PRODUCT}&categoryIds=${TOP_PRODUCT_CATEGORYIDS}`);
        if (resp.isSuccess) {
            const topProductSections = document.getElementById("top-product-section");
            topProductSections.innerHTML = '';
            resp.data.forEach((dt, ix) => {
                const sectionProduct = generateTopProductSection(TOP_PRODUCT_TITLE[ix], dt);
                topProductSections.appendChild(sectionProduct);
            })
        }
    }

    function generateTopProductSection(titleObj, data) {
        const sectionDiv = document.createElement("div");
        const sectionHeaderDiv = document.createElement("div");
        const sectionBodyDiv = document.createElement("div");
        const sectionBodyRowDiv = document.createElement("div");
        sectionDiv.className = "section";
        sectionHeaderDiv.className = "section-header";
        sectionBodyDiv.className = "section-body";
        sectionBodyRowDiv.className = "row";
        sectionBodyDiv.appendChild(sectionBodyRowDiv);
        sectionDiv.append(sectionHeaderDiv, sectionBodyDiv);
        sectionHeaderDiv.innerHTML = `<h3 class="section-title">${titleObj.title}</h3>
        <a href="${titleObj.link}" class="section-link">Xem tất cả</a>`;
        data.forEach(product => {
            const productItem = generateProductItem(product, "col-md-3");
            sectionBodyRowDiv.appendChild(productItem);
        });
        return sectionDiv;
    }


})();

