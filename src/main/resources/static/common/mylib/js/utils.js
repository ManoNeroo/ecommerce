const PAGE_ALERT_CLOSE_BTN = document.querySelector("#page-alert .close");

PAGE_ALERT_CLOSE_BTN.addEventListener("click", handleClickCloseAlert);

function handleClickCloseAlert(evt) {
	evt.preventDefault();
	hideAlert();
}

function showAlert(status, title, message) {
	const pageAlert = document.getElementById("page-alert");
	if (pageAlert) {
		const alertTitle = pageAlert.querySelector(".title");
		const alertIcon = pageAlert.querySelector(".fas");
		const alertMessage = pageAlert.querySelector(".message");
		hideAlert();
		switch (status) {
			case 'success':
				pageAlert.classList.add("alert-success");
				alertIcon.classList.add("fa-check");
				break;
			case 'warning':
				pageAlert.classList.add("alert-warning");
				alertIcon.classList.add("fa-exclamation-triangle");
				break;
			case 'failed':
				pageAlert.classList.add("alert-danger");
				alertIcon.classList.add("fa-ban");
				break;
		}
		alertTitle.innerText = title;
		alertMessage.innerText = message;
		pageAlert.classList.add("active");
		setTimeout(hideAlert, 2500);
	}
}

function hideAlert() {
	const pageAlert = document.getElementById("page-alert");
	if (pageAlert) {
		const alertTitle = pageAlert.querySelector(".title");
		const alertIcon = pageAlert.querySelector(".fas");
		const alertMessage = pageAlert.querySelector(".message");
		pageAlert.classList.remove("alert-success");
		pageAlert.classList.remove("alert-warning");
		pageAlert.classList.remove("alert-danger");
		alertIcon.classList.remove("fa-check");
		alertIcon.classList.remove("fa-exclamation-triangle");
		alertIcon.classList.remove("fa-ban");
		alertTitle.innerText = '';
		alertMessage.innerText = '';
		pageAlert.classList.remove("active");
	}
}

function getFormData(formDataElm) {
	const formData = new FormData(formDataElm);
	let obj = {};
	formData.forEach(function (value, key) {
		obj[key] = value;
	});
	return obj;
}

async function httpClient(endpoint, method = "GET", body = null) {
	const url = window.location.origin + endpoint;
	let fetchOptions = {
		method
	};

	if (body instanceof FormData) {
		fetchOptions.body = body
	} else {
		fetchOptions.body = body ? JSON.stringify(body) : null;
		fetchOptions.headers = {
			"Content-Type": "application/json"
		};
	}

	const resp = await fetch(url, fetchOptions);

	if (resp.status == 200 || resp.status == 201) {
		return resp.json();
	} else {
		return {
			isSuccess: false,
			status: resp.status
		};
	}
}

async function uploadFile(file) {
	const fData = new FormData();
	fData.append("file", file);
	return await httpClient("/api/upload", "POST", fData);
}

function removeAccent(str) {
	var AccentsMap = [
		"aàảãáạăằẳẵắặâầẩẫấậ",
		"AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬ",
		"dđ", "DĐ",
		"eèẻẽéẹêềểễếệ",
		"EÈẺẼÉẸÊỀỂỄẾỆ",
		"iìỉĩíị",
		"IÌỈĨÍỊ",
		"oòỏõóọôồổỗốộơờởỡớợ",
		"OÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢ",
		"uùủũúụưừửữứự",
		"UÙỦŨÚỤƯỪỬỮỨỰ",
		"yỳỷỹýỵ",
		"YỲỶỸÝỴ"
	];
	for (var i = 0; i < AccentsMap.length; i++) {
		var re = new RegExp('[' + AccentsMap[i].substr(1) + ']', 'g');
		var char = AccentsMap[i][0];
		str = str.replace(re, char);
	}
	return str;
}

function numberInput() {
	const listElm = document.querySelectorAll(".is-number");
	listElm.forEach(elm => {
		elm.addEventListener("keyup", handleNumberInput)
	})
}

function handleNumberInput(evt) {
	const { target } = evt;
	let { value } = target;
	value = value.replace(/\D/g, '')
		.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
	target.value = value;
}

function formatNumber(num, regex = /(\d)(?=(\d\d\d)+(?!\d))/g, replaceValue = "$1,") {
    if (typeof (num) === "number") {
        let arr = num.toString().split('.');
        arr[0] = arr[0].replace(regex, replaceValue);
        return arr.join('.');
    }
    return num;
}