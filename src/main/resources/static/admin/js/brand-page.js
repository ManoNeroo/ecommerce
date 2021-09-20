var pagi = new Pagination({ rowCount: g_iTotalItem, pageSize: g_iPageSize, defaultPage: g_iCurrentPage },
	"#brand-pagination");

pagi.onChangePage(currentPage => {
	let origin = window.location.origin;
	let path = window.location.pathname;
	let urlParams = new URLSearchParams(window.location.search);
	let rqUrl = origin + path;
	let name = urlParams.get('name');
	let status = urlParams.get("status");
	let query = "?page=" + currentPage;
	if (name != null) {
		if (name.trim() != "") {
			query += "&name=" + name;
		}
	}
	if (status != null) {
		status = status.trim().toLowerCase();
		if (status == "true" || status == "false") {
			query += "&status=" + status;
		}
	}
	rqUrl += query;
	window.location.href = rqUrl;
});

const BRAND_MODAL_JQ = $("#brand-modal");
const BRAND_MODAL = document.getElementById("brand-modal");
const BTN_ADD = document.getElementById("btn-add-brand");
const BRAND_MODAL_SAVE_BTN = BRAND_MODAL.querySelector("#save");
const BRAND_FORM = document.getElementById("brand-form");
const ID_INPUT = BRAND_FORM.querySelector("input[name='id']");
const ENABLE_RADIO = document.getElementById("enable-status");
const DISABLE_RADIO = document.getElementById("disable-status");
const NAME_INPUT = BRAND_FORM.querySelector("input[name='name']");
const BTN_EDITS = document.querySelectorAll(".table-list .btn-edit");
const BTN_TOGGLE_STATUSES = document.querySelectorAll(".table .btn-toggle-status");
const BTN_CHANGE_FILE = document.querySelector("#form-file button");
const FILE_LOGO_INPUT = document.querySelector("#form-file input");
const LOGO_PREVIEW = document.querySelector("#form-file img");
let CREATEDAT = null;

BTN_CHANGE_FILE.addEventListener("click", evt => { evt.preventDefault(); FILE_LOGO_INPUT.click() });
FILE_LOGO_INPUT.addEventListener("change", handleChangeFileLogo);
BRAND_MODAL_JQ.on('hidden.bs.modal', handleModalHidden);
BTN_ADD.addEventListener("click", handleOpenModalAdd);
BRAND_MODAL_SAVE_BTN.addEventListener("click", handleSaveBrand);
NAME_INPUT.addEventListener('focus', () => handleFocusInput(BRAND_FORM.querySelector(".name-error")));
BTN_EDITS.forEach(btn => {
	btn.addEventListener("click", handleOpenModalEdit);
});
BTN_TOGGLE_STATUSES.forEach(btn => {
	btn.addEventListener("click", handleToggleStatus);
});

function handleChangeFileLogo(evt) {
    var input = evt.target;
    var reader = new FileReader();
    reader.onload = function(){
      var dataURL = reader.result;
      LOGO_PREVIEW.src = dataURL;
    };
    reader.readAsDataURL(input.files[0]);
}

async function handleOpenModalAdd() {
	await generateSelectOptions();
	showBrandModal("Thêm thương hiệu");
}

async function generateSelectOptions(selectedList = []) {
	const resp = await httpClient("/api/category?isSortByName=true");
	if(resp.isSuccess) {
		let data = [];
		resp.data.forEach(category => {
			const isContain = selectedList.some(c => c.id == category.id);
			if(isContain) {
				data.push({id: category.id, text: category.name, selected: true});
			} else {
				data.push({id: category.id, text: category.name});
			}
		});
		$('#brand-category select').select2({
			placeholder: "Select category...",
			allowClear: true,
			data: data
		});
	} else {
		showAlert('failed', 'Lỗi', 'Xảy ra lỗi khi tải các danh mục!');
	}
}

function showBrandModal(title) {
	const modalTitle = BRAND_MODAL.querySelector(".modal-title");
	modalTitle.innerText = title;
	BRAND_MODAL_JQ.modal('show');
}

async function handleOpenModalEdit(evt) {
	evt.preventDefault();
	const {target} = evt;
	const id = target.getAttribute("data-id");
	const resp = await httpClient("/api/brand/" + id);
	if(resp.isSuccess) {
		showEditModal(resp.data);
	}
}

async function showEditModal(data) {
	if(data.status) {
		ENABLE_RADIO.checked = true;
		DISABLE_RADIO.checked = false;
	}else {
		ENABLE_RADIO.checked = false;
		DISABLE_RADIO.checked = true;
	}
	LOGO_PREVIEW.src = data.logo;
	NAME_INPUT.value = data.name;
	ID_INPUT.value = data.id;
	CREATEDAT = data.createdAt;
	await generateSelectOptions(data.categories);
	showBrandModal("Sửa thương hiệu");
}


function handleModalHidden() {
	window.location.reload();
}

async function handleSaveBrand() {
	const formData = getFormData(BRAND_FORM);
	const isValid = validateForm(formData);
	if (isValid) {
		let fileUrl;
		if(FILE_LOGO_INPUT.files.length > 0) {
			const fData = new FormData();
			fData.append("file", FILE_LOGO_INPUT.files[0]);
			const uploadResp = await httpClient("/api/upload", "POST", fData);
			if(uploadResp.isSuccess) {
				fileUrl = uploadResp.data;
			} else {
				showAlert('failed', 'Lỗi', 'Có lỗi xảy ra trong quá trình upload file!');
			}
		} else {
			fileUrl = LOGO_PREVIEW.src;
		}
		if(fileUrl) {
			const categories = $('#brand-category select').select2('data');
			const categoryIds = categories.map(c => Number(c.id));
			if (formData.id != "") {
				const data = {
					id: formData.id,
					name: formData.name,
					logo: fileUrl,
					status: formData.status,
					categoryIds,
					createdAt: CREATEDAT
				}
				const resp = await httpClient("/api/brand/" + formData.id, "PUT", data);
				if(resp.isSuccess) {
					showAlert('success', 'Thành công', 'Sửa thương hiệu thành công!');
				}else {
					showAlert('failed', 'Thất bại', 'Sửa thương hiệu thất bại!');
				}
			} else {
				const data = {
					name: formData.name,
					logo: fileUrl,
					status: formData.status,
					categoryIds
				}
				const resp = await httpClient("/api/brand", "POST", data);
				if(resp.isSuccess) {
					showAlert('success', 'Thành công', 'Đã thêm thương hiệu thành công!');
				}else {
					showAlert('failed', 'Thất bại', 'Thêm thương hiệu thất bại!');
				}
			}
		}
	}
}

function validateForm(formData) {
	let { name } = formData;
	name = name.trim();
	const nameRegex = /^[a-z][a-z\d\s)(]{2,}$/gi;
	const nameErrorSpan = BRAND_FORM.querySelector(".name-error");
	let rs = true;
	if (!nameRegex.test(removeAccent(name))) {
		nameErrorSpan.innerText = "Tên thương hiệu không hợp lệ!";
		rs = false;
	}
	return rs;
}

async function handleToggleStatus(evt) {
	evt.preventDefault();
	const {target} = evt;
	const id = target.getAttribute("data-id");
	const isEnable = target.classList.contains("btn-success");
	if(isEnable) {
		const resp = await httpClient("/api/brand/togglestatus?id=" + id + "&status=true");
		if(resp.isSuccess) {
			const badge = target.parentElement.parentElement.previousElementSibling.querySelector("span");
			badge.classList.remove("badge-warning");
			badge.classList.add("badge-success");
			badge.innerText = "Enable";
			target.classList.remove("btn-success");
			target.classList.add("btn-warning");
			target.innerText = "Disable";
		} else {
			showAlert('failed', 'Thất bại', 'Có lỗi xảy ra!');
		}
	} else {
		const resp = await httpClient("/api/brand/togglestatus?id=" + id + "&status=false");
		if(resp.isSuccess) {
			const badge = target.parentElement.parentElement.previousElementSibling.querySelector("span");
			badge.classList.remove("badge-success");
			badge.classList.add("badge-warning");
			badge.innerText = "Disable";
			target.classList.add("btn-success");
			target.classList.remove("btn-warning");
			target.innerText = "Enable";
		} else {
			showAlert('failed', 'Thất bại', 'Có lỗi xảy ra!');
		}
	}
}

function handleFocusInput(errorSpan) {
	errorSpan.innerText = '';
}
