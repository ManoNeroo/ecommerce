(function () {
    const PRODUCT_SLIDES = document.querySelectorAll("#product-slider .swiper-slide");
    const BTN_ADD_TO_CART = document.getElementById("btn-addtocart");
    const BTN_BUY_NOW = document.getElementById("btn-buynow");

    let slideLoop = false;
    let autoplay = {};
    if (PRODUCT_SLIDES.length > 1) {
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
    const STAR_LIST_WRAPPER1 = document.getElementById("star-list-1");
    const STAR_LIST_WRAPPER2 = document.getElementById("star-list-2");
    const BTN_EXPAND_ARTICLE = document.getElementById("btn-expand-article");
    const PRODUCT_ARTICLE = document.getElementById("product-article");

    STAR_LIST_WRAPPER1.insertAdjacentElement("afterbegin", STAR_LIST1);
    STAR_LIST_WRAPPER2.appendChild(STAR_LIST2);
    STAR_POINT.innerText = AVG_STAR;
    PROMO_PRICE_ELM.innerText = formatNumber(g_oProduct.promoPrice) + "₫";
    PRICE_ELM.innerText = formatNumber(g_oProduct.price) + "₫";

    BTN_EXPAND_ARTICLE.addEventListener("click", handleExpandArticle);
    BTN_ADD_TO_CART.addEventListener("click", handleAddToCart);
    BTN_BUY_NOW.addEventListener("click", handleBuyNow);

    async function handleAddToCart(evt) {
        evt.preventDefault();
        if (g_bIsAuthenticated) {
            const isSuccess = await addToCart(false)
            if (isSuccess) {
                showAlert('success', 'Thành công', 'Đã thêm sản phẩm vào giỏ hàng');
                loadCartNumberItem();
            } else {
                showAlert('failed', 'Thất bại', 'Đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng!');
            }
        } else {
            setupLoginForm();
        }
    }

    async function handleBuyNow(evt) {
        evt.preventDefault();
        evt.preventDefault();
        if (g_bIsAuthenticated) {
            const isSuccess = await addToCart(true);
            if (isSuccess) {
                const rqUrl = window.location.origin + "/user/cart";
                window.location.href = rqUrl;
            } else {
                showAlert('failed', 'Thất bại', 'Đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng!');
            }
        } else {
            setupLoginForm();
        }
    }

    async function addToCart(checked) {
        const body = {
            cartId: g_sUserName,
            productId: g_oProduct.id,
            quanlity: 1,
            checked
        };
        toggleLoading(true);
        const resp = await httpClient("/api/cartitem", "POST", body);
        toggleLoading(false);
        return resp.isSuccess;
    }

    function setupLoginForm() {
        const loginForm = document.querySelector("#sign form");
        const origin = window.location.origin;
        const path = window.location.pathname;
        const rqUrl = origin + "/login?returnUrl=" + path;
        loginForm.setAttribute("action", rqUrl);
        $("#sign").modal("show");
    }


    function handleExpandArticle(evt) {
        evt.preventDefault();
        const OVERLAY = BTN_EXPAND_ARTICLE.parentElement;
        PRODUCT_ARTICLE.style.height = "auto";
        PRODUCT_ARTICLE.style.position = "relative";
        OVERLAY.remove();
    }
})();