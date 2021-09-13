var pagi = new Pagination({ rowCount: g_iTotalItem, pageSize: g_iPageSize, defaultPage: g_iCurrentPage },
	"#category-pagination");

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

const CATEGORY_MODAL_JQ = $("#category-modal");
const CATEGORY_MODAL = document.getElementById("category-modal");
const BTN_ADD = document.getElementById("btn-add-category");
const CATEGORY_MODAL_SAVE_BTN = CATEGORY_MODAL.querySelector("#save");
const CATEGORY_FORM = document.getElementById("category-form");
const ID_INPUT = CATEGORY_FORM.querySelector("input[name='id']");
const ENABLE_RADIO = document.getElementById("enable-status");
const DISABLE_RADIO = document.getElementById("disable-status");
const NAME_INPUT = CATEGORY_FORM.querySelector("input[name='name']");
const LOGO_INPUT = CATEGORY_FORM.querySelector("textarea[name='logo']");
const BTN_EDITS = document.querySelectorAll(".table-list .btn-edit");
const BTN_TOGGLE_STATUSES = document.querySelectorAll(".table .btn-toggle-status");

CATEGORY_MODAL_JQ.on('hidden.bs.modal', handleModalHidden);
BTN_ADD.addEventListener("click", handleOpenModalAdd);
CATEGORY_MODAL_SAVE_BTN.addEventListener("click", handleSaveCategory);
NAME_INPUT.addEventListener('focus', () => handleFocusInput(CATEGORY_FORM.querySelector(".name-error")));
LOGO_INPUT.addEventListener('focus', () => handleFocusInput(CATEGORY_FORM.querySelector(".logo-error")));
BTN_EDITS.forEach(btn => {
	btn.addEventListener("click", handleOpenModalEdit);
});
BTN_TOGGLE_STATUSES.forEach(btn => {
	btn.addEventListener("click", handleToggleStatus);
});


function handleOpenModalAdd() {
	showCategoryModal("Thêm danh mục");
}

function showCategoryModal(title) {
	const modalTitle = CATEGORY_MODAL.querySelector(".modal-title");
	modalTitle.innerText = title;
	CATEGORY_MODAL_JQ.modal('show');
}

async function handleOpenModalEdit(evt) {
	evt.preventDefault();
	const {target} = evt;
	const id = target.getAttribute("data-id");
	const resp = await httpClient("/api/category/" + id);
	if(resp.isSuccess) {
		showEditModal(resp.data);
	}
}

function showEditModal(data) {
	if(data.status) {
		ENABLE_RADIO.checked = true;
		DISABLE_RADIO.checked = false;
	}else {
		ENABLE_RADIO.checked = false;
		DISABLE_RADIO.checked = true;
	}
	NAME_INPUT.value = data.name;
	LOGO_INPUT.value = data.logo;
	ID_INPUT.value = data.id;
	showCategoryModal("Sửa danh mục");
}


function handleModalHidden() {
	window.location.reload();
}

async function handleSaveCategory() {
	const formData = getFormData(CATEGORY_FORM);
	const isValid = validateForm(formData);
	if (isValid) {
		if (formData.id != "") {
			const resp = await httpClient("/api/category/" + formData.id, "PUT", formData);
			if(resp.isSuccess) {
				showAlert('success', 'Thành công', 'Sửa danh mục thành công!');
			}else {
				showAlert('failed', 'Thất bại', 'Sửa danh mục thất bại!');
			}
		} else {
			const resp = await httpClient("/api/category", "POST", formData);
			if(resp.isSuccess) {
				showAlert('success', 'Thành công', 'Thêm danh mục thành công!');
			}else {
				showAlert('failed', 'Thất bại', 'Thêm danh mục thất bại!');
			}
		}
	}
}

function validateForm(formData) {
	let { name, logo } = formData;
	name = name.trim();
	logo = logo.trim();
	const nameRegex = /^[a-z][a-z\d\s)(]{2,}$/gi;
	const logoRegex = /[a-z][^a-z]*/gi;
	const nameErrorSpan = CATEGORY_FORM.querySelector(".name-error");
	const logoErrorSpan = CATEGORY_FORM.querySelector(".logo-error");
	let rs = true;
	if (!nameRegex.test(removeAccent(name))) {
		nameErrorSpan.innerText = "Tên danh mục không hợp lệ!";
		rs = false;
	}
	if (!logoRegex.test(logo)) {
		logoErrorSpan.innerText = "Svg path không hợp lệ!";
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
		const resp = await httpClient("/api/category/togglestatus?id=" + id + "&status=true");
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
		const resp = await httpClient("/api/category/togglestatus?id=" + id + "&status=false");
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







