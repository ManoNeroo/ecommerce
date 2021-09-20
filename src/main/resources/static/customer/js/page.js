(function () {
	loadCategories();
	loadCartNumberItem();

	async function loadCartNumberItem() {
		if (g_sUserName) {
			const resp = await httpClient(`/api/cart/${g_sUserName}/numberitem`);
			if (resp.isSuccess) {
				let numberItem = resp.data;
				if (numberItem > 0) {
					const span = document.createElement("span");
					const cartElm = document.querySelector(".header-top-menu ul .cart");
					span.className = "number-item";
					span.innerText = numberItem;
					cartElm.appendChild(span);
				}
			}
		}
	}

	async function loadCategories() {
		const resp = await httpClient("/api/category?isEnable=true");
		if (resp.isSuccess) {
			const categoryMenu = document.querySelector("#header-menu-container .swiper-wrapper");
			categoryMenu.innerHTML = '';
			let html = '';
			resp.data.forEach(category => {
				html += `<li class="swiper-slide">
						<a href="/product?category=${category.id}">
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
})();

function toggleLoading(isShow) {
    const loading = document.getElementById("page-loading");
    if (isShow) {
        loading.classList.add("active");
    } else {
        loading.classList.remove("active");
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