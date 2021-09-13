const TECHNICAL_TAB = document.getElementById("technical-nav");
const TECHNICAL_FORM = document.getElementById("technical-form");
const table = new TechnicalDataTable("#technical-data-preview");
const form = new TechnicalDataForm("#technical-add-fields");
form.onAddRow(rowData => table.addRow(rowData))
form.onEditRow(rowData => table.editRow(rowData));
form.onRemoveRow(rowId => table.removeRow(rowId))

TECHNICAL_TAB.addEventListener("click", handleClickTechnicalTab);
TECHNICAL_FORM.addEventListener("submit", handleSaveTechnical);

async function handleSaveTechnical(evt) {
    evt.preventDefault();
    const productId = BASIC_FORM.querySelector("input[name='id']").value;
    let body = {
        content: '',
        productId: productId
    }
    body.content = JSON.stringify(form.data);
    const idInput = TECHNICAL_FORM.querySelector("input[name='id']");
    const id = idInput.value;
    if(id != '' && !isNaN(Number(id))) {
        body.id = Number(id);
    }
    const resp = await httpClient("/api/technicaldata", "POST", body);
    if(resp.isSuccess) {
        idInput.value = resp.data.id;
        showAlert('success', 'Thành công', 'Thông số kỹ thuật đã được lưu lại!');
    } else {
        showAlert('failed', 'Thất bại', 'Có lỗi xảy ra, vui lòng thử lại!');
    }
}

async function handleClickTechnicalTab() {
    const id = BASIC_FORM.querySelector("input[name='id']").value;
    const resp = await httpClient("/api/technicaldata/product/" + id);
    form.clearData();
    table.clearData();
    if (resp.isSuccess) {
        if (resp.data != null) {
            form.setData(JSON.parse(resp.data.content));
            const idInputElm = TECHNICAL_FORM.querySelector("input[name='id']");
            idInputElm.value = resp.data.id;
        } else {
            form.setData(DEFAULT_DATA);
        }
    } else {
        form.setData(DEFAULT_DATA);
    }
}

const DEFAULT_DATA = [
    {
        id: '1',
        name: "Tên thông số 1",
        value: "Giá trị thông số 1"
    },
    {
        id: '2',
        name: "Tên thông số 2",
        value: "Giá trị thông số 2"
    },
    {
        id: '3',
        name: "Tên thông số 3",
        value: "Giá trị thông số 3"
    },
    {
        id: '4',
        name: "Tên thông số 4",
        value: "Giá trị thông số 4"
    },
    {
        id: '5',
        name: "Tên thông số 5",
        value: "Giá trị thông số 5"
    },
    {
        id: '6',
        name: "Tên thông số 6",
        value: "Giá trị thông số 6"
    }
];
