(function () {
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

    PHONENUMBER_INPUT && PHONENUMBER_INPUT.addEventListener("keyup", (evt) => {
        const { target } = evt;
        let { value } = target;
        value = value.replace(/\D/g, '');
        target.value = value;
    })

    BACK_BTN && BACK_BTN.addEventListener("click", () => window.history.back());
    BTN_EDIT_CUSINFO && BTN_EDIT_CUSINFO.addEventListener("click", handleOpenModalEdit);
    CUSINFO_FORM && CUSINFO_FORM.addEventListener("submit", handleSubmitForm)

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
})()