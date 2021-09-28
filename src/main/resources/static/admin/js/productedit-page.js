const BTN_CHANGE_FILE = document.querySelector("#form-file button");
const AVATAR_PREVIEW = document.querySelector("#form-file img");
const BASIC_FORM = document.getElementById("basic-form");
const NAME_INPUT = BASIC_FORM.querySelector("input[name='name']");
const PRICE_INPUT = BASIC_FORM.querySelector("input[name='price']");
const PRICEOFF_INPUT = BASIC_FORM.querySelector("input[name='priceOff']");
const QUANLITY_INPUT = BASIC_FORM.querySelector("input[name='quanlity']");
const NAME_ERROR = BASIC_FORM.querySelector(".name-error");
const PRICE_ERROR = BASIC_FORM.querySelector(".price-error");
const PRICEOFF_ERROR = BASIC_FORM.querySelector(".priceoff-error");
const QUANLITY_ERROR = BASIC_FORM.querySelector(".quanlity-error");

NAME_INPUT.addEventListener("focus", () => NAME_ERROR.innerText = '');
PRICE_INPUT.addEventListener("focus", () => PRICE_ERROR.innerText = '');
PRICEOFF_INPUT.addEventListener("focus", () => PRICEOFF_ERROR.innerText = '');
QUANLITY_INPUT.addEventListener("focus", () => QUANLITY_ERROR.innerText = '');
BTN_CHANGE_FILE.addEventListener("click", handleChangeFileAvatar);
BASIC_FORM.addEventListener("submit", handleSaveProduct);

numberInput();

generateSelectOptions("category", g_iCategoryId);
generateSelectOptions("brand", g_iBrandId);


function handleChangeFileAvatar(evt) {
    evt.preventDefault();
    const id = BTN_CHANGE_FILE.dataset.id;
    const modal = new uploadModal();
    modal.onUpload(async url => {
        const resp = await httpClient("/api/product/avatar", "PATCH", {
            id: id,
            avatar: url
        });
        if (resp.isSuccess) {
            AVATAR_PREVIEW.src = url;
        } else {
            showAlert("failed", "Thất bại", "Đã xảy ra lỗi, vui lòng thử lại!");
        }
    })
}

async function generateSelectOptions(selectName, selectedId) {
    const resp = await httpClient("/api/" + selectName);
    if (resp.isSuccess) {
        let namePro = selectName + "Id";
        const selectElm = BASIC_FORM.querySelector(`select[name='${namePro}']`);
        resp.data.forEach(val => {
            const optionElm = document.createElement("option");
            optionElm.value = val.id;
            optionElm.text = val.name;
            if (val.id == selectedId) {
                optionElm.selected = true;
            }
            selectElm.appendChild(optionElm);
        })
    }
}

async function handleSaveProduct(evt) {
    evt.preventDefault();
    const formData = getFormData(BASIC_FORM);
    const isValid = validateForm(formData);
    if (isValid) {
        const body = {
            id: formData.id,
            brandId: Number(formData.brandId),
            categoryId: Number(formData.categoryId),
            name: formData.name,
            status: JSON.parse(formData.status),
            quanlity: Number(formData.quanlity.replace(/,/g, '')),
            price: Number(formData.price.replace(/,/g, '')),
            priceOff: Number(formData.priceOff.replace(/,/g, ''))
        }
        const resp = await httpClient("/api/product/" + formData.id, "PUT", body);
        if (resp.isSuccess) {
            showAlert('success', 'Thành công', 'Cập nhật sản phẩm thành công!');
        } else {
            showAlert('failed', 'Lỗi', 'Thêm sản phẩm thất bại!');
        }
    }
}


function validateForm(formData) {
    let { name, price, quanlity, priceOff } = formData;
    name = name.trim();
    price = price.replace(/,/g, '');
    priceOff = priceOff.replace(/,/g, '');
    quanlity = quanlity.replace(/,/g, '');
    const nameRegex = /^[a-z][a-z\d\s\\)(/-]{2,}$/gi;
    let rs = true;
    if (name == '') {
        NAME_ERROR.innerText = "Nhập tên sản phẩm!";
        rs = false;
    } else if (!nameRegex.test(removeAccent(name))) {
        NAME_ERROR.innerText = "Tên sản phẩm không hợp lệ!";
        rs = false;
    }

    if (price == '') {
        PRICE_ERROR.innerText = "Nhập giá sản phẩm!";
        rs = false;
    }
    if (quanlity == '') {
        QUANLITY_ERROR.innerText = "Nhập số lượng sản phẩm!";
        rs = false;
    }
    if (Number(priceOff) > Number(price)) {
        PRICEOFF_ERROR.innerText = "Giá khuyến mãi không hợp lệ!";
        rs = false;
    }
    return rs;
}