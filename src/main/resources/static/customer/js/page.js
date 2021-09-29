(function () {
	const FORM_HEADER_SEARCH = document.querySelector(".header-search form");
	const FORM_HEADER_SEARCH_INPUT = FORM_HEADER_SEARCH.querySelector("input");
	const HEADER_TOP = $("#header .header-top");

	loadCategories();
	loadCartNumberItem();
	FORM_HEADER_SEARCH.addEventListener("submit", handleSubmitHeaderSearch);
	FORM_HEADER_SEARCH_INPUT.addEventListener("focus", handleFocusHeaderSearch);
	FORM_HEADER_SEARCH_INPUT.addEventListener("keyup", handleKeyupHeaderSearch);
	HEADER_TOP.click(handleClickOutSearchInput);

	function handleSubmitHeaderSearch(evt) {
		evt.preventDefault();
		const searchValue = FORM_HEADER_SEARCH_INPUT.value.trim();
		if (searchValue != '') {
			window.location.href = window.location.origin + "/search?keyword=" + searchValue;
		}
	}

	function handleClickOutSearchInput(e) {
		var t = $(e.target);
		t.is(".header-search form") || t.is(".header-search form *") || t.is(".header-search-suggest")
			|| t.is(".header-search-suggest *") || handleHideSuggest();
	}

	function handleHideSuggest() {
		const overlay = document.querySelector(".overlay-suggest");
		overlay && overlay.remove();
		clearSuggestItems();
	}

	async function handleKeyupHeaderSearch() {
		const data = await searchProductByName();
		generateSuggestItems(data);
	}

	async function handleFocusHeaderSearch() {
		const data = await searchProductByName();
		handleHideSuggest();
		const overlay = document.createElement("div");
		overlay.className = "overlay-suggest";
		overlay.addEventListener("click", handleHideSuggest);
		document.body.appendChild(overlay);
		generateSuggestItems(data);
	}

	async function searchProductByName() {
		const searchValue = FORM_HEADER_SEARCH_INPUT.value.trim();
		if (searchValue != '') {
			const resp = await httpClient("/api/product/topbyname?top=5&status=true&name=" + searchValue);
			if (resp.isSuccess) {
				return resp.data;
			}
		}
		return null;
	}

	async function loadCategories() {
		const resp = await httpClient("/api/category?isEnable=true");
		if (resp.isSuccess) {
			const categoryMenu = document.querySelector("#header-menu-container .swiper-wrapper");
			categoryMenu.innerHTML = '';
			let html = '';
			resp.data.forEach(category => {
				html += `<li class="swiper-slide">
						<a href="/product?category=${category.name}">
							<i>
								<svg viewBox="0 0 24 24">
									<path
										d="${category.logo}">
									</path>
								</svg>
							</i>
							<span>${category.name}</span>
						</a>
					</li>`;
			});
			categoryMenu.innerHTML = html;
			new Swiper('#header-menu-container', {
				paginationClickable: true,
				preventClicks: false,
				freeMode: true,
				spaceBetween: 0,
				slidesPerView: 10,
				breakpoints: {
					768: {
						slidesPerView: 5
					},
					1200: {
						slidesPerView: 8
					}
				}
			});
		}
	}

	function clearSuggestItems() {
		const SUGGEST_DIV = document.querySelector(".header-search-suggest");
		const SUGGEST_TITLE = SUGGEST_DIV.querySelector(".suggest-title");
		const SUGGEST_UL = SUGGEST_DIV.querySelector("ul");
		SUGGEST_DIV.classList.remove("active");
		SUGGEST_TITLE.innerText = '';
		SUGGEST_UL.innerHTML = '';
	}

	function generateSuggestItems(items) {
		const SUGGEST_DIV = document.querySelector(".header-search-suggest");
		const SUGGEST_TITLE = SUGGEST_DIV.querySelector(".suggest-title");
		const SUGGEST_UL = SUGGEST_DIV.querySelector("ul");
		const SEARCH_TRENDS = [{ link: "/search?keyword=iPhone", text: "iPhone" },
		{ link: "/search?keyword=ipad", text: "iPad" },
		{ link: "/search?keyword=macbook", text: "Macbook" },
		{ link: "/search?keyword=samsung galaxy", text: "Samsung Galaxy" },
		{ link: "/search?keyword=oppo", text: "OPPO" },
		{ link: "/search?keyword=xiaomi", text: "Xiaomi" }];
		SUGGEST_DIV.classList.add("active");
		SUGGEST_TITLE.classList.remove("text-danger");
		SUGGEST_UL.innerHTML = '';
		if (items != null) {
			if (items.length > 0) {
				SUGGEST_TITLE.innerText = "Kết quả gợi ý";
				items.forEach(val => {
					const li = document.createElement("li");
					const itemAElm = generateSuggestItem(val);
					li.className = "hits-item";
					li.appendChild(itemAElm);
					SUGGEST_UL.appendChild(li);
				});
			} else {
				SUGGEST_TITLE.innerText = "Không có kết quả nào!";
				SUGGEST_TITLE.classList.add("text-danger");
			}
		} else {
			SUGGEST_TITLE.innerText = "Xu hướng tìm kiếm";
			SEARCH_TRENDS.forEach(val => {
				const li = document.createElement("li");
				li.innerHTML = `<a href="${val.link}">${val.text}</a>`;
				SUGGEST_UL.appendChild(li);
			})
		}
	}
	function generateSuggestItem(product) {
		const a = document.createElement("a");
		a.href = "/product/" + product.id;
		a.innerHTML = `<div class="suggest-item">
							<div class="suggest-item-img"><img src="${product.avatar}" alt="${product.name}"></div>
							<div class="suggest-item-info">
								<h3 class="suggest-item-name">${product.name}</h3>
								<div class="suggest-item-promo-price">${formatNumber(product.promoPrice)}₫
									<del class="suggest-item-price">${product.promoPrice != product.price ? formatNumber(product.price) + '₫' : ''}</del>
								</div>
							</div>
						</div>`;
		return a;
	}
})();

async function loadCartNumberItem() {
	if (g_sUserName) {
		const resp = await httpClient(`/api/cart/${g_sUserName}/numberitem`);
		if (resp.isSuccess) {
			let numberItem = resp.data;
			if (numberItem > 0) {
				const span = document.querySelector(".header-top-menu ul .cart number-item") || document.createElement("span");
				const cartElm = document.querySelector(".header-top-menu ul .cart");
				span.className = "number-item";
				span.innerText = numberItem;
				cartElm.appendChild(span);
			}
		}
	}
}

function generateProductItem(product, className) {
	const itemDiv = document.createElement("div");
	const itemWrapperA = document.createElement("a");
	const productPictureP = document.createElement("p");
	const productNameH3 = document.createElement("h3");
	const productPromoPriceP = document.createElement("p");
	const productVoteDiv = document.createElement("div");
	const productVoteCountP = document.createElement("p");
	const productPromoLabel = document.createElement("label");
	const productStarList = generateStarList(product.avgStar, "size-1");
	const classNameItem = "product-item " + className;
	itemDiv.className = classNameItem;
	productVoteDiv.append(productStarList, productVoteCountP);
	productVoteDiv.className = "product-vote";
	itemWrapperA.append(productPictureP, productNameH3, productPromoPriceP, productVoteDiv, productPromoLabel);
	itemDiv.appendChild(itemWrapperA);
	itemWrapperA.href = "/product/" + product.id;
	itemWrapperA.title = product.name;
	productPictureP.innerHTML = `<img src="${product.avatar}" alt="${product.name}">`;
	productPictureP.className = "product-picture";
	productNameH3.className = "product-name";
	productNameH3.innerText = product.name;
	productPromoPriceP.className = "product-promo-price";
	let priceHtml = `${formatNumber(product.promoPrice)}<span>đ</span>`;
	if (product.promoPrice != product.price) {
		priceHtml += `<del class="product-price">${formatNumber(product.price)}<span>đ</span></del>`;
	}
	productPromoPriceP.innerHTML = priceHtml;
	productVoteCountP.innerText = `(${formatNumber(product.numberVote)} đánh giá)`;
	productPromoLabel.className = "product-promo";
	if (product.priceOff > 0) {
		productPromoLabel.innerText = `Giảm ${formatNumber(product.priceOff)} ₫`;
	}
	return itemDiv;
}

function generateStarList(avgStar, sizeClass) {
	const starSvgPath = "M12 17.27 18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z";
	const haftStarSvgPath = "m22 9.24-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4V6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
	const borderStarSvgPath = "m22 9.24-7.19-.62L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27 18.18 21l-1.63-7.03L22 9.24zM12 15.4l-3.76 2.27 1-4.28-3.32-2.88 4.38-.38L12 6.1l1.71 4.04 4.38.38-3.32 2.88 1 4.28L12 15.4z";
	const ul = document.createElement("ul");
	const className = "star-list " + sizeClass;
	ul.className = className;
	let numberStar = 0;
	for (let i = 1; i <= 5; i++) {
		const li = document.createElement("li");
		if (avgStar < i) {
			const b = i - avgStar;
			if (b < 0.2 && b >= 0) {
				li.innerHTML = `<svg viewBox="0 0 24 24"><path d="${starSvgPath}"></path></svg>`;
				numberStar += 1;
			} else if (b >= 0.2 && b < 0.65) {
				li.innerHTML = `<svg viewBox="0 0 24 24"><path d="${haftStarSvgPath}"></path></svg>`;
				numberStar += 0.5;
			} else {
				li.innerHTML = `<svg viewBox="0 0 24 24"><path d="${borderStarSvgPath}"></path></svg>`;
			}
		} else {
			li.innerHTML = `<svg viewBox="0 0 24 24"><path d="${starSvgPath}"></path></svg>`;
			numberStar += 1;
		}
		ul.appendChild(li);
	}
	ul.setAttribute("data-star", numberStar);
	return ul;
}