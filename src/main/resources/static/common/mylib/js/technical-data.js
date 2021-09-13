class TechnicalDataTable {
    constructor(selector) {
        if (selector.nodeType !== 1) {
            selector = document.querySelector(selector);
        }
        this.table = initTechnicalDataTable(selector);
        this.data = [];
    }

    setData(data) {
        if (Array.isArray(data)) {
            data.forEach(rowData => {
                this.addRow(rowData);
            });
        }
    }

    clearData() {
        this.data = [];
        this.table.tBodies[0].innerHTML = '';
    }

    addRow(rowData) {
        if (rowData) {
            if (rowData.hasOwnProperty("id") && rowData.hasOwnProperty("name") && rowData.hasOwnProperty("value")) {
                this.data.push(rowData);
                const tr = addTableRow(rowData);
                this.table.tBodies[0].appendChild(tr);
                return tr;
            }
        }
        return null;
    }

    editRow(rowData) {
        if (rowData) {
            if (rowData.hasOwnProperty("id") && rowData.hasOwnProperty("name") && rowData.hasOwnProperty("value")) {
                const tr = this.table.tBodies[0].querySelector(`tr[data-id='${rowData.id}']`);
                if (tr) {
                    const ix = this.data.findIndex(dt => dt.id == rowData.id);
                    this.data.splice(ix, 1, rowData);
                    tr.cells[0].innerText = rowData.name;
                    tr.cells[1].innerText = rowData.value;
                    return tr;
                }
            }
        }
        return null;
    }

    removeRow(rowId) {
        const tr = this.table.tBodies[0].querySelector(`tr[data-id='${rowId}']`);
        if (tr) {
            const ix = this.data.findIndex(rowData => rowData.id == rowId);
            this.data.splice(ix, 1);
            this.table.tBodies[0].removeChild(tr);
            return tr;
        }
        return null;
    }

}

class TechnicalDataForm {
    constructor(selector) {
        if (selector.nodeType !== 1) {
            selector = document.querySelector(selector);
        }
        this.element = initTechnicalDataForm(selector);
        this.data = [];
        this.element.querySelector(".technical-row-add button")
            .addEventListener("click", handleAddRowField.bind(this))
    }

    clearData() {
        this.data = [];
        const divAdd = document.createElement("div");
        const buttonAdd = document.createElement("button");
        buttonAdd.type = "button";
        buttonAdd.className = "add-row-btn";
        buttonAdd.innerHTML = `<svg viewBox="0 0 24 24">
        <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"></path>
        </svg>`;
        divAdd.className = "technical-row-add";
        divAdd.appendChild(buttonAdd);
        this.element.innerHTML = '';
        this.element.appendChild(divAdd);
        buttonAdd.addEventListener("click", handleAddRowField.bind(this))
    }

    onAddRow(callBack) {
        this.element.addEventListener("addRow", evt => {
            callBack(evt.detail.rowData);
        });
    }

    onEditRow(callBack) {
        this.element.addEventListener("editRow", evt => {
            callBack(evt.detail.rowData);
        });
    }

    onRemoveRow(callBack) {
        this.element.addEventListener("removeRow", evt => {
            callBack(evt.detail.rowId);
        });
    }

    setData(data) {
        if (Array.isArray(data)) {
            data.forEach(rowData => {
                this.addRow(rowData);
            });
        }
    }

    addRow(rowData) {
        if (rowData) {
            if (rowData.hasOwnProperty("name") && rowData.hasOwnProperty("value")) {
                if (!rowData.id) {
                    rowData.id = generateId(10);
                }
                this.data.push(rowData);
                const rowField = addFieldRow(rowData);
                const input1 = rowField.querySelector("input[name='name']");
                const input2 = rowField.querySelector("input[name='value']");
                const removeBtn = rowField.querySelector("button");
                const divAdd = this.element.querySelector(".technical-row-add");
                input1.addEventListener('keyup', handleEditRow.bind(this));
                input2.addEventListener('keyup', handleEditRow.bind(this));
                removeBtn.addEventListener("click", handleRemoveRow.bind(this));
                this.element.insertBefore(rowField, divAdd);
                const event = new CustomEvent("addRow", {
                    detail: {
                        rowData: rowData
                    }
                });
                this.element.dispatchEvent(event);
                return rowField;
            }
        }
        return null;
    }

    editRow(rowData) {
        if (rowData) {
            if (rowData.hasOwnProperty("id") && rowData.hasOwnProperty("name") && rowData.hasOwnProperty("value")) {
                const rowField = this.element.querySelector(`.technical-row[data-id='${rowData.id}']`);
                if (rowField) {
                    const input1 = rowField.querySelector("input[name='name']");
                    const input2 = rowField.querySelector("input[name='value']");
                    const ix = this.data.findIndex(dt => dt.id == rowData.id);
                    this.data.splice(ix, 1, rowData);
                    input1.value = rowData.name;
                    input2.value = rowData.value;
                    const event = new CustomEvent("editRow", {
                        detail: {
                            rowData: rowData
                        }
                    });
                    this.element.dispatchEvent(event);
                    return rowField;
                }
            }
        }
    }

    removeRow(rowId) {
        const rowField = this.element.querySelector(`.technical-row[data-id='${rowId}']`);
        if (rowField) {
            const ix = this.data.findIndex(dt => dt.id == rowId);
            this.data.splice(ix, 1);
            const event = new CustomEvent("removeRow", {
                detail: {
                    rowId: rowId
                }
            });
            this.element.dispatchEvent(event);
            this.element.removeChild(rowField);
            return rowField;
        }
    }
}

function handleEditRow(evt) {
    var { target } = evt;
    var { value } = target;
    var parentElm = target.parentElement;
    var { id } = parentElm.dataset;
    var obj = { id: id };
    if (target.name == 'name') {
        obj.name = value;
        const nextElm = target.nextElementSibling;
        obj.value = nextElm.value;
    } else {
        obj.value = value;
        const prevElm = target.previousElementSibling;
        obj.name = prevElm.value;
    }
    this.editRow(obj);
}

function generateId(length) {
    var result = '';
    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for (var i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() *
            charactersLength));
    }
    return result;
}

function handleAddRowField() {
    this.addRow({ name: '', value: '' });
}

function handleRemoveRow(evt) {
    var { target } = evt;
    var parentELm = target.parentElement;
    if (target.nodeName == 'svg') {
        parentELm = parentELm.parentElement;
    }else if(target.nodeName == 'path') {
        parentELm = parentELm.parentElement.parentElement;
    }
    var { id } = parentELm.dataset;
    this.removeRow(id);
}

function addTableRow(rowData) {
    const tr = document.createElement("tr");
    const td1 = document.createElement("td");
    const td2 = document.createElement("td");
    tr.setAttribute("data-id", rowData.id);
    td1.innerText = rowData.name;
    td2.innerText = rowData.value;
    tr.append(td1, td2);
    return tr;
}

function addFieldRow(rowData) {
    const div = document.createElement("div");
    const input1 = document.createElement("input");
    const input2 = document.createElement("input");
    const button = document.createElement("button");
    div.className = "technical-row";
    div.setAttribute("data-id", rowData.id);
    input1.type = "text";
    input1.name = "name";
    input1.placeholder = "Tên thông số";
    input1.value = rowData.name;
    input2.type = "text";
    input2.name = "value";
    input2.placeholder = "Giá trị thông số";
    input2.value = rowData.value;
    button.type = "button";
    button.innerHTML = `<svg viewBox="0 0 24 24">
    <path d="M19 13H5v-2h14v2z"></path>
</svg>`;
    div.append(input1, input2, button);
    return div;
}


function initTechnicalDataTable(selector) {
    const table = document.createElement("table");
    const tbody = document.createElement("tbody");
    table.className = "technical-data-table";
    table.appendChild(tbody);
    selector.innerHTML = '';
    selector.appendChild(table);
    return table;
}

function initTechnicalDataForm(selector) {
    const wrapper = document.createElement("div");
    const divAdd = document.createElement("div");
    const buttonAdd = document.createElement("button");
    buttonAdd.type = "button";
    buttonAdd.className = "add-row-btn";
    buttonAdd.innerHTML = `<svg viewBox="0 0 24 24">
    <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"></path>
</svg>`;
    divAdd.className = "technical-row-add";
    wrapper.className = "technical-fields";
    divAdd.appendChild(buttonAdd);
    wrapper.appendChild(divAdd);
    selector.innerHTML = '';
    selector.appendChild(wrapper);
    return wrapper;
}