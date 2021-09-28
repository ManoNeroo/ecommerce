class ImageList {
    constructor(selector) {
        if (selector.nodeType !== 1) {
            selector = document.querySelector(selector);
        }
        this.element = initImageList(selector);
        this.element.querySelector(".image-item-upload").addEventListener("click", handleClickAddImage.bind(this))
    }

    onAddImage(callBack) {
        this.element.addEventListener("addImage", () => {
            callBack();
        });
    }

    onEditImage(callBack) {
        this.element.addEventListener("editImage", evt => {
            callBack(evt.detail.itemId);
        });
    }

    onRemoveImage(callBack) {
        this.element.addEventListener("removeImage", evt => {
            callBack(evt.detail.itemId);
        });
    }

    clearImages() {
        this.element.innerHTML = '';
        const divAdd = document.createElement("div");
        const divAddWrapper = document.createElement("div");
        const spanUpload = document.createElement("span");
        divAdd.className = "image-item col-md-3 col-sm-4";
        divAddWrapper.className = "image-item-wrapper";
        spanUpload.className = "image-item-upload";
        spanUpload.innerHTML = `<svg viewBox="0 0 24 24">
                            <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"></path>
                            </svg>`;
        divAddWrapper.appendChild(spanUpload);
        divAdd.appendChild(divAddWrapper);
        this.element.appendChild(divAdd);
        spanUpload.addEventListener("click", handleClickAddImage.bind(this))
    }

    setImages(data) {
        if (Array.isArray(data)) {
            data.forEach(dt => {
                this.addImage(dt);
            })
        }
    }

    removeImage(id) {
        const item = this.element.querySelector(`.image-item[data-id='${id}']`);
        if (item) {
            item.remove();
        }
    }

    addImage(data) {
        if (data) {
            if (data.hasOwnProperty("id") && data.hasOwnProperty("name")) {
                const item = initImageItem(data);
                this.element.appendChild(item);
                const btnEdit = item.querySelector(".btn-edit");
                const btnDelete = item.querySelector(".btn-delete");
                btnEdit.addEventListener("click", handleClickEditImage.bind(this));
                btnDelete.addEventListener("click", handleClickRemoveImage.bind(this));
            }
        }
    }

    editImage(data) {
        if (data) {
            if (data.hasOwnProperty("id") && data.hasOwnProperty("name")) {
                const item = this.element.querySelector(`.image-item[data-id='${data.id}']`);
                if (item) {
                    const image = item.querySelector("img");
                    image.src = data.name;
                }
            }
        }
    }
}

function handleClickAddImage() {
    const event = new Event("addImage");
    this.element.dispatchEvent(event);
}

function handleClickEditImage(evt) {
    const { target } = evt;
    let parentElm = target.parentElement.parentElement;
    if (target.nodeName == 'svg') {
        parentElm = parentElm.parentElement;
    } else if (target.nodeName == 'path') {
        parentElm = parentElm.parentElement.parentElement;
    }
    const event = new CustomEvent("editImage", {
        detail: {
            itemId: parentElm.dataset.id
        }
    });
    this.element.dispatchEvent(event);
}

function handleClickRemoveImage(evt) {
    const { target } = evt;
    let parentElm = target.parentElement.parentElement;
    if (target.nodeName == 'svg') {
        parentElm = parentElm.parentElement;
    } else if (target.nodeName == 'path') {
        parentElm = parentElm.parentElement.parentElement;
    }
    const event = new CustomEvent("removeImage", {
        detail: {
            itemId: parentElm.dataset.id
        }
    });
    this.element.dispatchEvent(event);
}

function initImageItem(data) {
    const divItem = document.createElement("div");
    const divItemWrapper = document.createElement("div");
    const divOverlay = document.createElement("div");
    const image = document.createElement("img");
    const buttonEdit = document.createElement("button");
    const buttonDelete = document.createElement("button");
    divOverlay.className = "overlay";
    buttonEdit.className = "btn-edit";
    buttonEdit.type = "button";
    buttonEdit.innerHTML = `<svg viewBox="0 0 24 24">
                            <path
                                d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 00-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z">
                            </path>
                            </svg>`;
    buttonDelete.className = "btn-delete";
    buttonDelete.type = "button";
    buttonDelete.innerHTML = `<svg viewBox="0 0 24 24">
                            <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"></path>
                            </svg>`
    divItem.className = "image-item col-md-3 col-sm-4";
    divItemWrapper.className = "image-item-wrapper";
    image.src = data.name;
    divItem.setAttribute("data-id", data.id);
    divItemWrapper.append(image, divOverlay, buttonEdit, buttonDelete);
    divItem.appendChild(divItemWrapper);
    const imageHeight = image.height;
    const imageWidth = image.width;
    if (imageWidth > imageHeight) {
        image.style.width = "96%"
    } else {
        image.style.height = "16em";
    }
    return divItem;
}

function initImageList(selector) {
    const divRow = document.createElement("div");
    const divAdd = document.createElement("div");
    const divAddWrapper = document.createElement("div");
    const spanUpload = document.createElement("span");
    divRow.className = "row";
    divAdd.className = "image-item col-md-3 col-sm-4";
    divAddWrapper.className = "image-item-wrapper";
    spanUpload.className = "image-item-upload";
    spanUpload.innerHTML = `<svg viewBox="0 0 24 24">
                            <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"></path>
                            </svg>`;
    divAddWrapper.appendChild(spanUpload);
    divAdd.appendChild(divAddWrapper);
    divRow.appendChild(divAdd);
    selector.appendChild(divRow);
    return divRow;
}

const PICTURE_TAB = document.getElementById("picture-nav");

PICTURE_TAB.addEventListener("click", handleClickPictureTab);
const imageList = new ImageList("#image-list");
imageList.onEditImage(handleSaveProductPicture);
imageList.onRemoveImage(handleDeleteProductPicture)
imageList.onAddImage(handleSaveProductPicture)

async function handleClickPictureTab() {
    const id = BASIC_FORM.querySelector("input[name='id']").value;
    const resp = await httpClient("/api/productpicture/product/" + id);
    imageList.clearImages();
    if (resp.isSuccess) {
        if (resp.data != null) {
            imageList.setImages(resp.data);
        }
    } else {
        showAlert('failed', 'Lỗi', 'Có lỗi xảy ra khi tải ảnh của sản phẩm!')
    }
}

function handleSaveProductPicture(picId) {
    const modal = new uploadModal();
    modal.onUpload(async url => {
        const productId = BASIC_FORM.querySelector("input[name='id']").value;
        const body = {
            name: url,
            productId
        };
        if (picId) {
            body.id = picId;
        }
        const picResp = await httpClient("/api/productpicture", "POST", body);
        if (picResp.isSuccess) {
            if (picId) {
                imageList.editImage(picResp.data);
            } else {
                imageList.addImage(picResp.data);
            }
        } else {
            showAlert('failed', 'Lỗi', 'Có lỗi xảy ra, vui lòng thử lại!');
        }
    })
}

async function handleDeleteProductPicture(picId) {
    const confirm = window.confirm("Xác nhận xóa ảnh!");
    if (confirm) {
        const resp = await httpClient("/api/productpicture/" + picId, "DELETE");
        if (resp.isSuccess) {
            imageList.removeImage(picId);
        } else {
            showAlert('failed', 'Lỗi', 'Có lỗi xảy ra, vui lòng thử lại!');
        }
    }
}