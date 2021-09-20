(function () {
    const PRODUCT_SLIDES = document.querySelectorAll("#product-slider .swiper-slide");
    let slideLoop = false;
    let autoplay = {};
    if(PRODUCT_SLIDES.length > 1) {
        slideLoop = true;
        autoplay = {
            delay: 6e3,
            disableOnInteraction: false,
        };
    }
    new Swiper('#product-slider', {
        loop: slideLoop,
        autoplay
    });

    const TECHNICAL_DATA_TABLE = new TechnicalDataTable("#product-technical-data");
    TECHNICAL_DATA_TABLE.setData(JSON.parse(g_oProduct.technicalData));
    const STAR_POINT = document.getElementById("star-point");
    const STAR_LIST1 = generateStarList(g_oProduct.avgStar, 'size-2');
    const STAR_LIST2 = generateStarList(g_oProduct.avgStar, 'size-2');
    const AVG_STAR = STAR_LIST1.dataset.star;
    const PROMO_PRICE_ELM = document.querySelector(".price-main");
    const PRICE_ELM = document.querySelector(".price-sub del");
    const STAR_LIST_WRAPPER1= document.getElementById("star-list-1");
    const STAR_LIST_WRAPPER2 = document.getElementById("star-list-2");
    const BTN_EXPAND_ARTICLE = document.getElementById("btn-expand-article");
    const PRODUCT_ARTICLE = document.getElementById("product-article");

    STAR_LIST_WRAPPER1.insertAdjacentElement("afterbegin", STAR_LIST1);
    STAR_LIST_WRAPPER2.appendChild(STAR_LIST2);
    STAR_POINT.innerText = AVG_STAR;
    PROMO_PRICE_ELM.innerText = formatNumber(g_oProduct.promoPrice) + "₫";
    PRICE_ELM.innerText = formatNumber(g_oProduct.price) + "₫";

    BTN_EXPAND_ARTICLE.addEventListener("click", handleExpandArticle);


    function handleExpandArticle(evt) {
        evt.preventDefault();
        const OVERLAY = BTN_EXPAND_ARTICLE.parentElement;
        PRODUCT_ARTICLE.style.height = "auto";
        PRODUCT_ARTICLE.style.position = "relative";
        OVERLAY.remove();
    }
})();