const BTN_CHANGE_FILE = document.querySelector("#form-file button");
const FILE_AVATAR_INPUT = document.querySelector("#form-file input");
const AVATAR_PREVIEW = document.querySelector("#form-file img");
const FORM_ADD = document.getElementById("form-add");
const NAME_INPUT = FORM_ADD.querySelector("input[name='name']");
const PRICE_INPUT = FORM_ADD.querySelector("input[name='price']");
const PRICEOFF_INPUT = FORM_ADD.querySelector("input[name='priceOff']");
const QUANLITY_INPUT = FORM_ADD.querySelector("input[name='quanlity']");
const NAME_ERROR = FORM_ADD.querySelector(".name-error");
const PRICE_ERROR = FORM_ADD.querySelector(".price-error");
const PRICEOFF_ERROR = FORM_ADD.querySelector(".priceoff-error");
const QUANLITY_ERROR = FORM_ADD.querySelector(".quanlity-error");

NAME_INPUT.addEventListener("focus", () => NAME_ERROR.innerText = '');
PRICE_INPUT.addEventListener("focus", () => PRICE_ERROR.innerText = '');
PRICEOFF_INPUT.addEventListener("focus", () => PRICEOFF_ERROR.innerText = '');
QUANLITY_INPUT.addEventListener("focus", () => QUANLITY_ERROR.innerText = '');

BTN_CHANGE_FILE.addEventListener("click", evt => { evt.preventDefault(); FILE_AVATAR_INPUT.click() });
FILE_AVATAR_INPUT.addEventListener("change", handleChangeFileAvatar);
FORM_ADD.addEventListener("submit", handleSaveProduct);

numberInput();

function handleChangeFileAvatar(evt) {
    var input = evt.target;
    var reader = new FileReader();
    reader.onload = function(){
      var dataURL = reader.result;
      AVATAR_PREVIEW.src = dataURL;
    };
    reader.readAsDataURL(input.files[0]);
}

async function handleSaveProduct(evt) {
    evt.preventDefault();
    const formData = getFormData(FORM_ADD);
    const isValid = validateForm(formData);
    if(isValid) {
        let fileUrl;
		if(FILE_AVATAR_INPUT.files.length > 0) {
			const fData = new FormData();
			fData.append("file", FILE_AVATAR_INPUT.files[0]);
			const uploadResp = await httpClient("/api/upload", "POST", fData);
			if(uploadResp.isSuccess) {
				fileUrl = uploadResp.data;
			} else {
				showAlert('failed', 'Lỗi', 'Có lỗi xảy ra trong quá trình upload file!');
			}
		} else {
			fileUrl = AVATAR_PREVIEW.src;
		}
		if(fileUrl) {
			const body = {
                brandId: Number(formData.brandId),
                categoryId: Number(formData.categoryId),
                name: formData.name,
                status: JSON.parse(formData.status),
                avatar: fileUrl,
                quanlity: Number(formData.quanlity.replace(/,/g, '')),
                price: Number(formData.price.replace(/,/g, '')),
                priceOff: Number(formData.priceOff.replace(/,/g, ''))
            }
            const resp = await httpClient("/api/product", "POST", body);
            if(resp.isSuccess) {
                let origin = window.location.origin;
                window.location.href = origin + "/admin/product/edit/" + resp.data.id;
            } else {
                showAlert('failed', 'Lỗi', 'Thêm sản phẩm thất bại!');
            }
		}
    }
}

function validateForm(formData) {
	let { name, price, quanlity, priceOff } = formData;
	name = name.trim();
    price = price.replace(/,/g, '');
    priceOff = priceOff.replace(/,/g, '');
    quanlity = quanlity.replace(/,/g, '');
	const nameRegex = /^[a-z][a-z\d\s)(]{2,}$/gi;
	let rs = true;
    if(name == '') {
        NAME_ERROR.innerText = "Nhập tên sản phẩm!";
        rs = false;
    }else if (!nameRegex.test(removeAccent(name))) {
		NAME_ERROR.innerText = "Tên sản phẩm không hợp lệ!";
		rs = false;
	}

    if(price == '') {
        PRICE_ERROR.innerText = "Nhập giá sản phẩm!";
        rs = false;
    }
    if(quanlity == '') {
        QUANLITY_ERROR.innerText = "Nhập số lượng sản phẩm!";
        rs = false;
    }
    if(Number(priceOff) > Number(price)) {
        PRICEOFF_ERROR.innerText = "Giá khuyến mãi không hợp lệ!";
        rs = false;
    }
	return rs;
}