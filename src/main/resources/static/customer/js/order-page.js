(function () {
    const SEARCH_INPUT = document.getElementById("order-search-input");
    const PAGE_PAGI = document.getElementById("page-pagination");
    const BACK_BTN = document.querySelector(".back-btn");
    const BTN_EDIT_CUSINFO = document.getElementById("btn-edit-cusinfo");
    const CUSINFO_FORM = document.querySelector("#cusinfo-modal form");
    const FULLNAME_ERROR = CUSINFO_FORM && CUSINFO_FORM.querySelector(".fullName-error");
    const PHONENUMBER_ERROR = CUSINFO_FORM && CUSINFO_FORM.querySelector(".phoneNumber-error");
    const ADDRESS_ERROR = CUSINFO_FORM && CUSINFO_FORM.querySelector(".address-error");
    const FULLNAME_INPUT = CUSINFO_FORM && CUSINFO_FORM.querySelector("input[name='fullName']");
    const PHONENUMBER_INPUT = CUSINFO_FORM && CUSINFO_FORM.querySelector("input[name='phoneNumber']");
    const ADDRESS_INPUT = CUSINFO_FORM && CUSINFO_FORM.querySelector("input[name='address']");

    FULLNAME_INPUT && FULLNAME_INPUT.addEventListener("focus", () => FULLNAME_ERROR.innerText = '');
    PHONENUMBER_INPUT && PHONENUMBER_INPUT.addEventListener("focus", () => PHONENUMBER_ERROR.innerText = '');
    ADDRESS_INPUT && ADDRESS_INPUT.addEventListener("focus", () => ADDRESS_ERROR.innerText = '');
    BTN_EDIT_CUSINFO && BTN_EDIT_CUSINFO.addEventListener("click", handleOpenModalEdit);
    CUSINFO_FORM && CUSINFO_FORM.addEventListener("submit", handleSubmitForm)

    PHONENUMBER_INPUT && PHONENUMBER_INPUT.addEventListener("keyup", (evt) => {
        const { target } = evt;
        let { value } = target;
        value = value.replace(/\D/g, '');
        target.value = value;
    })

    BACK_BTN && BACK_BTN.addEventListener("click", () => window.history.back());

    if (PAGE_PAGI) {
        var pagi = new Pagination({ rowCount: g_iTotalItem, pageSize: g_iPageSize, defaultPage: g_iCurrentPage },
            PAGE_PAGI);
        pagi.onChangePage(currentPage => {
            let origin = window.location.origin;
            let path = window.location.pathname;
            let urlParams = new URLSearchParams(window.location.search);
            let rqUrl = origin + path;
            let query = "";
            let type = urlParams.get("type");
            let search = urlParams.get("search");
            if (type != null) {
                query += "type=" + type;
            }
            if (search != null) {
                query += (query == "" ? "search=" : "&search=") + search;
            }
            query += (query == "" ? "page=" : "&page=") + currentPage;
            rqUrl += "?" + query;
            window.location.href = rqUrl;
        });
    }

    SEARCH_INPUT && SEARCH_INPUT.addEventListener("keyup", e => {
        const { key } = e;
        if (key) {
            if (key.toUpperCase() == 'ENTER') {
                const { value } = e.target;
                let origin = window.location.origin;
                let path = window.location.pathname;
                let urlParams = new URLSearchParams(window.location.search);
                let rqUrl = origin + path;
                let query = "";
                let type = urlParams.get("type");
                if (type != null) {
                    query += "type=" + type;
                }
                query += (query == "" ? "search=" : "&search=") + value;
                rqUrl += "?" + query;
                window.location.href = rqUrl;
            }
        }
    });

    function handleOpenModalEdit(evt) {
        evt.preventDefault();
        $("#cusinfo-modal").modal("show");
    }

    function handleSubmitForm(evt) {
        evt.preventDefault();
        const formData = getFormData(CUSINFO_FORM);
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

})();