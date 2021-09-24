(function () {
    const MAX_ITEM = 5;
    const SELECTALL = document.querySelector("#select-all-cart-item input");
    const DELETEALL = document.getElementById("delete-all-cart-item");
    const OPENCARTMODALBTN = document.getElementById("open-cart-modal");
    const CARTFORM = document.getElementById("cart-form");
    const FULLNAME_ERROR = document.getElementById("fullName-error");
    const PHONENUMBER_ERROR = document.getElementById("phoneNumber-error");
    const ADDRESS_ERROR = document.getElementById("address-error");
    const FULLNAME_INPUT = CARTFORM.querySelector("input[name='fullName']");
    const PHONENUMBER_INPUT = CARTFORM.querySelector("input[name='phoneNumber']");
    const ADDRESS_INPUT = CARTFORM.querySelector("input[name='address']");

    FULLNAME_INPUT.addEventListener("focus", () => FULLNAME_ERROR.innerText = '');
    PHONENUMBER_INPUT.addEventListener("focus", () => PHONENUMBER_ERROR.innerText = '');
    ADDRESS_INPUT.addEventListener("focus", () => ADDRESS_ERROR.innerText = '');

    PHONENUMBER_INPUT.addEventListener("keyup", (evt) => {
        const { target } = evt;
        let { value } = target;
        value = value.replace(/\D/g, '');
        target.value = value;
    })

    class CartList {
        constructor(cssSelector) {
            this.element = document.querySelector(cssSelector);
            this.data = [];
            this.numberChecked = 0;
        }

        onEditItem(callBack) {
            this.element.addEventListener("editItem", evt => {
                callBack(evt.detail.itemList);
            });
        }

        onRemoveItem(callBack) {
            this.element.addEventListener("removeItem", evt => {
                callBack(evt.detail.itemList);
            });
        }

        setData(data) {
            if (Array.isArray(data) && this.element) {
                let checked = true;
                data.forEach(item => {
                    if (!item.checked) {
                        checked = false;
                    } else {
                        this.numberChecked++;
                    }
                    this.data.push(item);
                    const cartItem = generateCartItem(item);
                    const btnPlus = cartItem.querySelector(".btn-plus-quanlity");
                    const btnMinus = cartItem.querySelector(".btn-minus-quanlity");
                    const btnDelete = cartItem.querySelector(".cart-delete");
                    const checkbox = cartItem.querySelector(".checkbox input");
                    const bindParam = { cartListObj: this, itemData: item };
                    btnPlus.addEventListener("click", handleIncreaseQuanlity.bind(bindParam));
                    btnMinus.addEventListener("click", handleDecreaseQuanlity.bind(bindParam));
                    btnDelete.addEventListener("click", handleRemoveItem.bind(bindParam));
                    checkbox.addEventListener("change", handleItemCheckBox.bind(bindParam));
                    this.element.appendChild(cartItem);
                });
                if (checked) {
                    SELECTALL.checked = true;
                }
            }
        }

        editItem(itemData) {
            let ix = this.data.findIndex(val => val.product.id == itemData.product.id);
            if (ix != -1) {
                const obj = this.data[ix];
                if (itemData.quanlity <= obj.product.quanlity && itemData.quanlity <= MAX_ITEM) {
                    const elem = this.element.querySelector(`.cart-item[data-id='${itemData.product.id}']`);
                    const btnPlus = elem.querySelector('.btn-plus-quanlity');
                    const btnMinus = elem.querySelector('.btn-minus-quanlity');
                    const span = elem.querySelector('.quanlity-input');
                    const total = elem.querySelector('.item-total');
                    const checkbox = elem.querySelector(".checkbox input");
                    checkbox.checked = itemData.checked;
                    btnMinus.classList.remove("disable");
                    btnPlus.classList.remove("disable");
                    span.innerText = itemData.quanlity;
                    total.innerHTML = formatNumber(itemData.quanlity * obj.product.promoPrice) + '₫';
                    if (itemData.quanlity == MAX_ITEM) {
                        btnPlus.classList.add("disable");
                    }
                    if (itemData.quanlity == 1) {
                        btnMinus.classList.add("disable");
                    }
                    this.data.splice(ix, 1, itemData);
                    const event = new CustomEvent("editItem", {
                        detail: {
                            itemList: this.data
                        }
                    });
                    this.element.dispatchEvent(event);
                }
            }
        }

        removeItem(itemId) {
            const ix = this.data.findIndex(val => val.product.id == itemId);
            if (ix != -1) {
                const elem = this.element.querySelector(`.cart-item[data-id='${itemId}']`);
                elem.remove();
                this.data.splice(ix, 1);
                const event = new CustomEvent("removeItem", {
                    detail: {
                        itemList: this.data
                    }
                });
                this.element.dispatchEvent(event);
            }
        }
    }

    const cartList = new CartList("#item-list");
    cartList.setData(g_oCart.cartItems);
    generateSummary(g_oCart.cartItems);
    cartList.onEditItem(itemList => {
        generateSummary(itemList);
    })
    cartList.onRemoveItem(itemList => {
        const numberItem = document.querySelector(".header-top-menu ul .cart .number-item");
        const nItem = itemList.length;
        if (nItem >= 1) {
            numberItem.innerText = nItem;
            generateSummary(itemList);
        } else {
            numberItem.remove();
            const cartItemList = document.getElementById("cart-item-list");
            const cartNull = document.getElementById("cart-null");
            cartItemList && cartItemList.classList.add("d-none");
            cartNull && cartNull.classList.remove("d-none");
        }
    });

    SELECTALL.addEventListener("change", handleSelectAll);
    DELETEALL.addEventListener("click", handleDeleteAll);
    OPENCARTMODALBTN.addEventListener("click", handleOpenCartModal);
    CARTFORM.addEventListener("submit", handleSubmitCart);

    function handleSubmitCart(evt) {
        evt.preventDefault();
        const formData = getFormData(CARTFORM);
        if (validateForm(formData)) {
            evt.target.submit();
        }
    }

    function validateForm(formData) {
        let { fullName, phoneNumber, address } = formData;
        fullName = fullName.trim();
        phoneNumber = phoneNumber.trim();
        address = address.trim();
        const nameRegex = /^[a-z][a-z\d\s]{2,}$/gi;
        const addressRegex = /^[\w\s\.,\\)(/-]{6,}$/i;
        const phoneRegex = /^\d{10,11}$/;
        let rs = true;
        if (fullName == '') {
            FULLNAME_ERROR.innerText = "Nhập tên khách hàng!";
            rs = false;
        } else if (!nameRegex.test(removeAccent(fullName))) {
            FULLNAME_ERROR.innerText = "Tên khách hàng không hợp lệ!";
            rs = false;
        }
        if (phoneNumber == '') {
            PHONENUMBER_ERROR.innerText = "Nhập số điện thoại!";
            rs = false;
        } else if (!phoneRegex.test(phoneNumber)) {
            PHONENUMBER_ERROR.innerText = "Số điện thoại không hợp lệ!";
            rs = false;
        }
        if (address == '') {
            ADDRESS_ERROR.innerText = "Nhập địa chỉ giao hàng!";
            rs = false;
        } else if (!addressRegex.test(removeAccent(address))) {
            ADDRESS_ERROR.innerText = "Địa chỉ không hợp lệ!";
            rs = false;
        }
        return rs;
    }

    function handleOpenCartModal() {
        if (cartList.numberChecked > 0) {
            const tbody = document.querySelector("#cart-modal tbody");
            tbody.innerHTML = '';
            let total = 0;
            cartList.data.forEach((item, ix) => {
                if (item.checked) {
                    const tr = document.createElement("tr");
                    const totalPromoPrice = item.quanlity * item.product.promoPrice;
                    const totalPrice = item.quanlity * item.product.price;
                    total += totalPromoPrice;
                    tr.innerHTML = `<td>${ix + 1}</td>
                    <td class="d-flex">
                        <div class="cart-item-picture">
                            <img src="${item.product.avatar}"
                                alt="${item.product.name}">
                        </div>
                        <div class="cart-item-info ml-2">
                            <div class="item-name">
                                <a href="/product/${item.product.id}">${item.product.name}</a>
                            </div>
                            <div class="list-star"></div>
                        </div>
                    </td>
                    <td>${item.quanlity}</td>
                    <td>
                        <div class="cart-item-price">
                            ${formatNumber(item.quanlity * item.product.promoPrice)}₫
                        </div>
                        <div class="cart-item-price root-price">
                            <del>${totalPrice != totalPromoPrice
                            ? formatNumber(item.quanlity * item.product.price) + '₫'
                            : ''}</del>
                        </div>
                    </td>`;
                    tbody.appendChild(tr);
                }
            });
            const trTotal = document.createElement("tr");
            trTotal.className = "total";
            trTotal.innerHTML = `<td></td>
            <td class="d-flex"></td>
            <td >Tổng cộng</td>
            <td>
                <div class="cart-item-price">
                    ${formatNumber(total)}₫
                </div>
            </td>`;
            tbody.appendChild(trTotal);
            $("#cart-modal").modal("show");
        }
    }


    function handleSelectAll(evt) {
        evt.preventDefault();
        const { target } = evt;
        if (target.checked) {
            cartList.numberChecked = cartList.data.length;
        } else {
            cartList.numberChecked = 0;
        }
        cartList.data.forEach(async itemData => {
            if (itemData.checked != target.checked) {
                toggleLoading(true);
                const resp = await httpClient("/api/cartitem", "PUT", {
                    cartId: g_sUserName,
                    productId: itemData.product.id,
                    quanlity: itemData.quanlity,
                    checked: target.checked
                });
                if (resp.isSuccess) {
                    itemData.checked = target.checked;
                    cartList.editItem(itemData);
                }
                toggleLoading(false);
            }
        });
    }

    function handleDeleteAll(evt) {
        evt.preventDefault();
        const confirm = window.confirm("Xác nhận xóa tất cả sản phẩm khỏi giỏ hàng!");
        if (confirm) {
            cartList.data.forEach(async itemData => {
                toggleLoading(true);
                const resp = await httpClient("/api/cartitem", "DELETE", {
                    cartId: g_sUserName,
                    productId: itemData.product.id
                });
                if (resp.isSuccess) {
                    cartList.removeItem(itemData.product.id);
                }
                toggleLoading(false);
            })
        }
    }

    function handleIncreaseQuanlity(evt) {
        evt.preventDefault();
        let { cartListObj, itemData } = this;
        if (itemData.quanlity < itemData.product.quanlity && itemData.quanlity < MAX_ITEM) {
            let quanlity = itemData.quanlity + 1;
            toggleLoading(true);
            httpClient("/api/cartitem", "PUT", {
                cartId: g_sUserName,
                productId: itemData.product.id,
                quanlity: quanlity,
                checked: itemData.checked
            }).then(resp => {
                toggleLoading(false);
                if (resp.isSuccess) {
                    itemData.quanlity = quanlity;
                    cartListObj.editItem(itemData);
                }
            }).catch(err => {
                console.log(err);
                toggleLoading(false);
            })
        }
    }

    function handleDecreaseQuanlity(evt) {
        evt.preventDefault();
        let { cartListObj, itemData } = this;
        if (itemData.quanlity > 1) {
            let quanlity = itemData.quanlity - 1;
            toggleLoading(true);
            httpClient("/api/cartitem", "PUT", {
                cartId: g_sUserName,
                productId: itemData.product.id,
                quanlity: quanlity,
                checked: itemData.checked
            }).then(resp => {
                toggleLoading(false);
                if (resp.isSuccess) {
                    itemData.quanlity = quanlity;
                    cartListObj.editItem(itemData);
                }
            }).catch(err => {
                console.log(err);
                toggleLoading(false);
            })
        }
    }

    function handleRemoveItem(evt) {
        evt.preventDefault();
        let { cartListObj, itemData } = this;
        const confirm = window.confirm("Xác nhận xóa sản phẩm khỏi giỏ hàng!");
        if (confirm) {
            toggleLoading(true);
            httpClient("/api/cartitem", "DELETE", {
                cartId: g_sUserName,
                productId: itemData.product.id
            }).then(resp => {
                toggleLoading(false);
                if (resp.isSuccess) {
                    cartListObj.removeItem(itemData.product.id);
                }
            }).catch(err => {
                console.log(err);
                toggleLoading(false);
            })
        }
    }

    function handleItemCheckBox(evt) {
        evt.preventDefault();
        let { cartListObj, itemData } = this;
        const { checked } = evt.target;
        toggleLoading(true);
        httpClient("/api/cartitem", "PUT", {
            cartId: g_sUserName,
            productId: itemData.product.id,
            quanlity: itemData.quanlity,
            checked
        }).then(resp => {
            toggleLoading(false);
            if (resp.isSuccess) {
                itemData.checked = checked;
                cartListObj.editItem(itemData);
                if (checked) {
                    cartListObj.numberChecked++;
                } else {
                    cartListObj.numberChecked--;
                }
                if (cartListObj.numberChecked == cartListObj.data.length) {
                    SELECTALL.checked = true;
                } else {
                    SELECTALL.checked = false;
                }
            }
        }).catch(err => {
            console.log(err);
            toggleLoading(false);
        })
    }

    function generateSummary(data) {
        if (Array.isArray(data)) {
            const tbody = document.getElementById("cart-summary-body");
            const spanTotal = document.getElementById("cart-summary-total");
            if (tbody && spanTotal) {
                let totalPrice = 0;
                tbody.innerHTML = '';
                data.forEach(item => {
                    if (item.checked) {
                        const tr = document.createElement("tr");
                        const total = item.quanlity * item.product.promoPrice;
                        tr.innerHTML = `<td class="item-name">${item.product.name}</td>
                                <td>${item.quanlity}</td>
                                <td>${formatNumber(total)}₫</td>`;
                        totalPrice += total;
                        tbody.appendChild(tr);
                    }
                });
                if (totalPrice == 0) {
                    tbody.innerHTML = `<tr id="summary-null">
                                        <td class="item-name">Chưa có sản phẩm nào!</td>
                                        <td></td>
                                        <td></td>
                                    </tr>`;
                }
                spanTotal.innerText = formatNumber(totalPrice) + '₫';
            }
        }
    }

    function generateCartItem(data) {
        const div = document.createElement("div");
        div.className = "cart-item";
        div.setAttribute("data-id", data.product.id);
        div.innerHTML = `<label class="checkbox cart-col">
                            <input type="checkbox" ${data.checked ? 'checked' : ''}>
                            <i></i>
                        </label>
                        <div class="cart-item-picture cart-col">
                            <img src="${data.product.avatar}" alt="${data.product.name}">
                        </div>
                        <div class="cart-item-info cart-col">
                            <div class="item-name">
                                <a href="${'/product/' + data.product.id}">${data.product.name}</a>
                            </div>
                            <div class="list-star"></div>
                        </div>
                        <div class="cart-item-price cart-col pl-2 pr-2">
                        ${formatNumber(data.product.promoPrice)}₫
                        </div>
                        <div class="cart-item-quanlity cart-col pl-2 pr-2">
                            <div class="quanlity-btn">
                                <button type="button" class="element btn-minus-quanlity${data.quanlity > 1 ? '' : ' disable'}">
                                    <svg viewBox="0 0 10 10">
                                        <polygon
                                            points="4.5 4.5 3.5 4.5 0 4.5 0 5.5 3.5 5.5 4.5 5.5 10 5.5 10 4.5">
                                        </polygon>
                                    </svg>
                                </button>
                                <span class="quanlity-input element">${data.quanlity}</span>
                                <button type="button" class="element btn-plus-quanlity${data.quanlity < MAX_ITEM ? '' : ' disable'}">
                                    <svg viewBox="0 0 10 10">
                                        <polygon
                                            points="10 4.5 5.5 4.5 5.5 0 4.5 0 4.5 4.5 0 4.5 0 5.5 4.5 5.5 4.5 10 5.5 10 5.5 5.5 10 5.5">
                                        </polygon>
                                    </svg>
                                </button>
                            </div>
                        </div>
                        <div class="cart-item-price cart-col pl-2 pr-2 item-total">
                        ${formatNumber(data.product.promoPrice * data.quanlity)}₫
                        </div>
                        <div class="cart-item-action cart-col pl-2 pr-2">
                            <span class="cart-delete">
                                <span class="icon">
                                    <svg viewBox="0 0 24 24">
                                        <path
                                            d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z">
                                        </path>
                                    </svg>
                                </span>
                            </span>
                        </div>`;
        return div;
    }

})();