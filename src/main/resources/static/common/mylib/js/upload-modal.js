const E_UPLOAD_TIME_TRANSITION = 300;

class uploadModal {
    constructor() {
        this.element = initEUploadModal();
    }
    onUpload(callBack) {
        this.element.addEventListener("upload", evt => {
            callBack(evt.detail.resource);
        });
    }
}

function initEUploadModal() {
    const closeSvg = "<svg viewBox='0 0 24 24'><path d='M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z'></path></svg>";
    const uploadSvg = "<svg viewBox='0 0 24 24'><path d='M20 6h-8l-2-2H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2zm0 12H4V8h16v10zM8 13.01l1.41 1.41L11 12.84V17h2v-4.16l1.59 1.59L16 13.01 12.01 9 8 13.01z'></path></svg>";
    const eUploadDiv = document.createElement("div");
    const eUploadOverlayDiv = document.createElement("div");
    const eUploadHeaderDiv = document.createElement("div");
    const eUploadTitleH4 = document.createElement("h4");
    const eUploadCloseSpan = document.createElement("span");
    const eUploadBodyDiv = document.createElement("div");
    const eUploadLabel1 = document.createElement("label");
    const eUploadResourceDiv = document.createElement("div");
    const eUploadResourceInput = document.createElement("input")
    const eUploadResourceSpanup = document.createElement("span");
    const eUploadFooterDiv = document.createElement("div");
    const eUploadCloseBtn = document.createElement("button");
    const eUploadOkBtn = document.createElement("button");
    eUploadCloseSpan.innerHTML = closeSvg;
    eUploadResourceSpanup.innerHTML = uploadSvg;
    eUploadHeaderDiv.append(eUploadTitleH4, eUploadCloseSpan);
    eUploadResourceDiv.append(eUploadResourceInput, eUploadResourceSpanup);
    eUploadBodyDiv.append(eUploadLabel1, eUploadResourceDiv);
    eUploadFooterDiv.append(eUploadCloseBtn, eUploadOkBtn);
    eUploadDiv.append(eUploadHeaderDiv, eUploadBodyDiv, eUploadFooterDiv);
    eUploadOverlayDiv.className = "e-upload-overlay";
    eUploadDiv.className = "e-upload";
    eUploadHeaderDiv.className = "e-upload-header";
    eUploadTitleH4.className = "e-upload-title";
    eUploadTitleH4.innerText = "Upload ảnh";
    eUploadCloseSpan.className = "e-upload-close-btn e-upload-btn e-upload-icon";
    eUploadBodyDiv.className = "e-upload-body";
    eUploadLabel1.innerText = "Nguồn";
    eUploadResourceDiv.className = "e-upload-resource";
    eUploadResourceInput.type = "text";
    eUploadResourceInput.placeholder = "Đường dẫn hình ảnh";
    eUploadResourceInput.className = "e-upload-resource-input";
    eUploadResourceSpanup.className = "e-upload-btn e-upload-file-exp e-upload-icon";
    eUploadFooterDiv.className = "e-upload-footer";
    eUploadCloseBtn.type = "button";
    eUploadCloseBtn.className = "e-upload-close-btn e-upload-btn e-upload-footer-close";
    eUploadCloseBtn.innerText = "Đóng";
    eUploadOkBtn.type = "button";
    eUploadOkBtn.className = "e-upload-footer-save e-upload-btn";
    eUploadOkBtn.innerText = "Ok";
    document.body.append(eUploadDiv, eUploadOverlayDiv);
    setTimeout(() => {
        document.body.classList.add("e-upload-htmlbody");
        eUploadDiv.classList.add("active");
        eUploadOverlayDiv.classList.add("active");
    }, 0);
    setTimeout(() => {
        eUploadResourceInput.focus();
    }, E_UPLOAD_TIME_TRANSITION);
    eUploadOverlayDiv.addEventListener("click", () => handleCloseEUploadModal(eUploadDiv, eUploadOverlayDiv));
    eUploadCloseBtn.addEventListener("click", () => handleCloseEUploadModal(eUploadDiv, eUploadOverlayDiv));
    eUploadCloseSpan.addEventListener("click", () => handleCloseEUploadModal(eUploadDiv, eUploadOverlayDiv));
    eUploadResourceSpanup.addEventListener("click", () => handleEUploadUploadFile(eUploadResourceInput));
    eUploadOkBtn.addEventListener("click", () => handleEUploadClickOk(eUploadDiv, eUploadOverlayDiv, eUploadResourceInput))
    return eUploadDiv;
}

function handleCloseEUploadModal(eUploadDiv, eUploadOverlayDiv) {
    setTimeout(() => {
        document.body.classList.remove("e-upload-htmlbody");
        eUploadDiv.remove();
        eUploadOverlayDiv.remove();
    }, E_UPLOAD_TIME_TRANSITION);
    eUploadDiv.classList.remove("active");
    eUploadOverlayDiv.classList.remove("active");
}

function handleEUploadUploadFile(eUploadResourceInput) {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.onchange = async function () {
        var file = this.files[0];
        const fData = new FormData();
        fData.append("file", file);
        const uploadResp = await httpClient("/api/upload", "POST", fData);
        if (uploadResp.isSuccess) {
            eUploadResourceInput.value = uploadResp.data;
        } else {
            showAlert('failed', 'Lỗi', 'Có lỗi xảy ra trong quá trình upload file!');
        }
        eUploadResourceInput.focus();
    };
    input.click();
}

function handleEUploadClickOk(eUploadDiv, eUploadOverlayDiv, eUploadResourceInput) {
    const value = eUploadResourceInput.value.trim();
    const urlRegex = /(http(s)?:\/\/.)?(www\.)?([-a-zA-Z0-9@:%._\+~#=]{2,256})?(\.[a-z]{2,6})?([-a-zA-Z0-9@:%_\+.~#?&//=]*)/;
    const urlRegex2= /^\/+$/;
    if(urlRegex.test(value) && value != '' && !urlRegex2.test(value)) {
        const event = new CustomEvent("upload", {
            detail: {
                resource: value
            }
        });
        eUploadDiv.dispatchEvent(event);
        handleCloseEUploadModal(eUploadDiv, eUploadOverlayDiv);
    }
}