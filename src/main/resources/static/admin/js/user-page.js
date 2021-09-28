(function () {

    const BTN_OPEN_MODAL_ADD = document.getElementById("btn-open-modal-add");
    const FORM_ADD = document.getElementById("user-form-add");
    const MODAL_ADD_JQ = $('#user-modal-add');
    const FORM_EDIT = document.getElementById("user-form-edit");
    const MODAL_EDIT_JQ = $('#user-modal-edit');
    const BTN_TOGGLE_STATUSES = document.querySelectorAll(".table .btn-toggle-status");
    const BTN_EDIT_USERS = document.querySelectorAll(".table .btn-edit");
    const FILTER_FORM = document.querySelector(".filter-form");
    const BTN_SAVE_ADD = document.getElementById("saveAdd");
    const BTN_SAVE_EDIT = document.getElementById("saveEdit");
    const FORM_ADD_PHONEINPUT = FORM_ADD && FORM_ADD.querySelector("input[name='phoneNumber']");
    const FORM_EDIT_PHONEINPUT = FORM_EDIT && FORM_EDIT.querySelector("input[name='phoneNumber']");

    FORM_ADD_PHONEINPUT && FORM_ADD_PHONEINPUT.addEventListener("keyup", (evt) => {
        const { target } = evt;
        let { value } = target;
        value = value.replace(/\D/g, '');
        target.value = value;
    })

    FORM_EDIT_PHONEINPUT && FORM_EDIT_PHONEINPUT.addEventListener("keyup", (evt) => {
        const { target } = evt;
        let { value } = target;
        value = value.replace(/\D/g, '');
        target.value = value;
    })

    MODAL_ADD_JQ.on('hidden.bs.modal', handleModalHidden);
    MODAL_EDIT_JQ.on('hidden.bs.modal', handleModalHidden);
    FILTER_FORM && FILTER_FORM.addEventListener("submit", handleSubmitFilter);
    BTN_OPEN_MODAL_ADD && BTN_OPEN_MODAL_ADD.addEventListener("click", () => {
        const USERROLE1_TEXTAREA = document.querySelector('#form-user-roles1 ~ .select2-container textarea');
        USERROLE1_TEXTAREA && USERROLE1_TEXTAREA.addEventListener("focus",
            () => document.querySelector('#form-user-roles1 ~ .userRoles-error').innerText = '');
        $("#user-modal-add").modal('show')
    });
    BTN_SAVE_ADD && BTN_SAVE_ADD.addEventListener("click", handleAddUser);

    BTN_SAVE_EDIT && BTN_SAVE_EDIT.addEventListener("click", handleEditUser);

    BTN_TOGGLE_STATUSES && BTN_TOGGLE_STATUSES.forEach(btn => {
        btn.addEventListener("click", handleToggleStatus);
    });

    BTN_EDIT_USERS && BTN_EDIT_USERS.forEach(btn => {
        btn.addEventListener("click", handleOpenModalEdit);
    })

    FORM_ADD && FORM_ADD.querySelectorAll("input[type='text']").forEach(input => {
        input.addEventListener("focus", evt => {
            const { target } = evt;
            const errorSpan = target.nextElementSibling;
            errorSpan && (errorSpan.innerText = '');
        })
    })

    FORM_EDIT && FORM_EDIT.querySelectorAll("input[type='text']").forEach(input => {
        input.addEventListener("focus", evt => {
            const { target } = evt;
            const errorSpan = target.nextElementSibling;
            errorSpan && (errorSpan.innerText = '');
        })
    })

    var pagi = new Pagination({ rowCount: g_iTotalItem, pageSize: g_iPageSize, defaultPage: g_iCurrentPage },
        "#page-pagination");

    pagi.onChangePage(currentPage => {
        let origin = window.location.origin;
        let path = window.location.pathname;
        let urlParams = new URLSearchParams(window.location.search);
        let rqUrl = origin + path;
        let query = urlParams.toString();
        if (query != '') {
            query = query.replace(/page=\d+/ig, "page=" + currentPage);
        } else {
            query = "page=" + currentPage;
        }
        rqUrl += "?" + query;
        window.location.href = rqUrl;
    });

    generateUserRolesOptions(g_aRoles);
    generateFormUserRolesOptions(null, '#form-user-roles1');

    function handleModalHidden() {
        window.location.reload();
    }

    async function handleOpenModalEdit(evt) {
        evt.preventDefault();
        const { target } = evt;
        const id = target.getAttribute("data-id");
        const resp = await httpClient("/api/account/" + id);
        if (resp.isSuccess) {
            showEditModal(resp.data);
        }
    }

    function showEditModal(data) {
        const roleIds = data.roles.map(r => r.id);
        generateFormUserRolesOptions(roleIds, '#form-user-roles2');
        const userId = FORM_EDIT.querySelector("input[name='userId']");
        const firstName = FORM_EDIT.querySelector("input[name='firstName']");
        const lastName = FORM_EDIT.querySelector("input[name='lastName']");
        const phoneNumber = FORM_EDIT.querySelector("input[name='phoneNumber']");
        const maleRadio = FORM_EDIT.querySelector("#radioMale2");
        const feMaleRadio = FORM_EDIT.querySelector("#radioFemale2");
        userId.value = data.id;
        firstName.value = data.firstName;
        lastName.value = data.lastName;
        phoneNumber.value = data.phoneNumber;
        if (data.gender) {
            feMaleRadio.checked = true;
        } else {
            maleRadio.checked = true;
        }
        const USERROLE2_TEXTAREA = document.querySelector('#form-user-roles2 ~ .select2-container textarea');
        USERROLE2_TEXTAREA && USERROLE2_TEXTAREA.addEventListener("focus",
            () => document.querySelector('#form-user-roles2 ~ .userRoles-error').innerText = '');
        $("#user-modal-edit").modal('show');
    }

    async function handleEditUser(evt) {
        evt.preventDefault();
        if (validateForm(FORM_EDIT)) {
            const formData = getFormData(FORM_EDIT);
            delete formData["userRoles[]"];
            const roleOptions = $("#form-user-roles2").select2("data");
            if (roleOptions.length == 0) {
                const userRolesError = FORM_EDIT.querySelector(".userRoles-error");
                userRolesError.innerText = "Chọn quyền người dùng!";
            } else {
                formData.roleIxs = [];
                roleOptions.forEach(val => formData.roleIxs.push(Number(val.id)));
                toggleLoading(true);
                const resp1 = await httpClient("/api/account/basicinfo/" + formData.userId, "PUT", formData);
                if(resp1.isSuccess) {
                    const resp2 = await httpClient("/api/account/roles/" + formData.userId, "PUT", formData);
                    if(resp2.isSuccess) {
                        showAlert("success", "Thành công", "Chỉnh sửa tài khoản thành công!");
                    } else {
                        showAlert("failed", "Thất bại", "Chỉnh sửa thất bại, vui lòng thử lại!");
                    }
                } else {
                    showAlert("failed", "Thất bại", "Chỉnh sửa thất bại, vui lòng thử lại!");
                }
                toggleLoading(false);
            }
        }
    }

    async function handleAddUser(evt) {
        evt.preventDefault();
        if (validateForm(FORM_ADD)) {
            const formData = getFormData(FORM_ADD);
            delete formData["userRoles[]"];
            const roleOptions = $("#form-user-roles1").select2("data");
            if (roleOptions.length == 0) {
                const userRolesError = FORM_ADD.querySelector(".userRoles-error");
                userRolesError.innerText = "Chọn quyền người dùng!";
            } else {
                formData.roleIxs = [];
                roleOptions.forEach(val => formData.roleIxs.push(Number(val.id)));
                toggleLoading(true);
                const resp = await httpClient("/api/account", "POST", formData);
                toggleLoading(false);
                if (resp.isSuccess) {
                    showAlert("success", "Thành công", "Đã thêm tài khoản thành công.");
                } else {
                    showAlert("failed", "Thất bại", "Tài khoản đã tồn tại, vui lòng thử lại!");
                }
            }
        }
    }

    function validateForm(formElm) {
        const formData = getFormData(formElm);
        const { userName, firstName, lastName, phoneNumber, password, matchingPassword } = formData;
        const userNameRegex = /^[a-z][a-z\d]{4,}$/
        const nameRegex = /^[a-z][a-z\d\s]{2,}$/i;
        const nameRegex2 = /^[a-z][a-z\d\s]{2,}$/i;
        const phoneNumberRegex = /^(0|\+84)(\s|\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\d)(\s|\.)?(\d{3})(\s|\.)?(\d{3})$/;
        let rs = true;
        const userNameError = formElm.querySelector(".userName-error");
        const firstNameError = formElm.querySelector(".firstName-error");
        const lastNameError = formElm.querySelector(".lastName-error");
        const phoneNumberError = formElm.querySelector(".phoneNumber-error");
        const passwordError = formElm.querySelector(".password-error");
        const matchingPasswordError = formElm.querySelector(".matchingPassword-error");
        if (userName != undefined) {
            if (userName.trim() == '') {
                rs = false;
                userNameError.innerText = "Nhập tên đăng nhập!"
            } else if (!userNameRegex.test(userName.trim())) {
                rs = false;
                userNameError.innerText = "Tên đăng nhập không hợp lệ!"
            }
        }
        if (firstName.trim() == '') {
            rs = false;
            firstNameError.innerText = "Nhập tên họ!";
        } else if (!nameRegex.test(removeAccent(firstName.trim()))) {
            rs = false;
            firstNameError.innerText = "Tên họ không hợp lệ"
        }

        if (lastName.trim() == '') {
            rs = false;
            lastNameError.innerText = "Nhập tên!";
        } else if (!nameRegex2.test(removeAccent(lastName.trim()))) {
            rs = false;
            lastNameError.innerText = "Tên không hợp lệ!";
        }

        if (phoneNumber.trim() == '') {
            rs = false;
            phoneNumberError.innerText = "Nhập số điện thoại!";
        } else if (!phoneNumberRegex.test(phoneNumber.trim())) {
            rs = false;
            phoneNumberError.innerText = "Số điện thoại không hợp lệ!";
        }
        if (password != undefined) {
            if (password.trim() == '') {
                rs = false;
                passwordError.innerText = "Nhập mật khẩu!";
            } else if (password.trim().length < 6) {
                rs = false;
                passwordError.innerText = "Mật khẩu phải từ 6 ký tự trở lên!";
            } else if (matchingPassword.trim() == '') {
                rs = false;
                matchingPasswordError.innerText = "Nhập lại mật khẩu!";
            } else if (matchingPassword.trim() != password.trim()) {
                rs = false;
                matchingPasswordError.innerText = "Mật khẩu không khớp!"
            }
        }
        return rs;
    }

    function handleSubmitFilter(evt) {
        evt.preventDefault();
        const formData = getFormData(evt.target);
        let origin = window.location.origin;
        let path = window.location.pathname;
        let rqUrl = origin + path;
        const search = formData.search;
        const status = formData.status;
        const userRoles = $("#user-roles").select2("data");
        let query = "?page=1";
        search != '' && (query += "&search=" + search);
        status != '' && (query += "&status=" + status);
        userRoles.forEach((val, ix) => {
            if (ix == 0) {
                query += "&roles=" + val.id;
            } else {
                query += "," + val.id;
            }
        })
        rqUrl += query;
        window.location.href = rqUrl;
    }

    async function handleToggleStatus(evt) {
        evt.preventDefault();
        const { target } = evt;
        const id = target.getAttribute("data-id");
        const isEnable = target.classList.contains("btn-success");
        const body = {
            userId: id
        };
        if (isEnable) {
            body.status = true;
            const resp = await httpClient("/api/account/togglestatus", "POST", body);
            if (resp.isSuccess) {
                const badge = target.parentElement.previousElementSibling.querySelector("span");
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
            body.status = false;
            const resp = await httpClient("/api/account/togglestatus", "POST", body);
            if (resp.isSuccess) {
                const badge = target.parentElement.previousElementSibling.querySelector("span");
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

    function generateUserRolesOptions(selectedList) {
        let data = [
            {
                id: 1,
                text: "Admin"
            },
            {
                id: 2,
                text: "Manager"
            },
            {
                id: 3,
                text: "Employee"
            },
            {
                id: 4,
                text: "Customer"
            }
        ];
        let arr = [];
        if (selectedList != null) {
            arr = data.map(v => {
                const isContain = selectedList.some(i => i == v.id);
                if (isContain) {
                    return { id: v.id, text: v.text, selected: true };
                } else {
                    return { id: v.id, text: v.text };
                }
            });
        } else {
            arr = [...data];
        }
        $('#user-roles').select2({
            placeholder: "Chọn quyền",
            allowClear: true,
            data: arr
        });
    }

    function generateFormUserRolesOptions(selectedList, cssSelector) {
        let data = [
            {
                id: 2,
                text: "Manager"
            },
            {
                id: 3,
                text: "Employee",
                selected: true
            },
            {
                id: 4,
                text: "Customer"
            }
        ];
        let arr = [];
        if (selectedList != null) {
            arr = data.map(v => {
                const isContain = selectedList.some(i => i == v.id);
                if (isContain) {
                    return { id: v.id, text: v.text, selected: true };
                } else {
                    return { id: v.id, text: v.text };
                }
            });
        } else {
            arr = [...data];
        }
        $(cssSelector).select2({
            placeholder: "Chọn quyền",
            allowClear: true,
            data: arr
        });
    }
})();