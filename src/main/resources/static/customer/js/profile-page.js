(function() {
    const CHANGE_AVATAR_BTN = document.getElementById("change-avatar-btn");
    const USER_BASIC_FORM = document.getElementById("user-basic-info-form");
    const USER_PASSWORD_FORM = document.getElementById("user-password-form");
    const PHONE_INPUT = USER_BASIC_FORM && USER_BASIC_FORM.querySelector("input[name='phoneNumber']");

    CHANGE_AVATAR_BTN && CHANGE_AVATAR_BTN.addEventListener("click", handleChangeAvatar);
    USER_BASIC_FORM && USER_BASIC_FORM.addEventListener("submit", handleUpdateUserBasicInfo);
    USER_PASSWORD_FORM && USER_PASSWORD_FORM.addEventListener("submit", handleUpdatePassword);

    PHONE_INPUT && PHONE_INPUT.addEventListener("keyup", (evt) => {
        const { target } = evt;
        let { value } = target;
        value = value.replace(/\D/g, '');
        target.value = value;
    });

    USER_BASIC_FORM && USER_BASIC_FORM.querySelectorAll("input[type='text']").forEach(input => {
        input.addEventListener("focus", evt => {
            const { target } = evt;
            const errorSpan = target.nextElementSibling;
            errorSpan && (errorSpan.innerText = '');
        })
    });

    USER_PASSWORD_FORM && USER_PASSWORD_FORM.querySelectorAll("input[type='password']").forEach(input => {
        input.addEventListener("focus", evt => {
            const { target } = evt;
            const errorSpan = target.nextElementSibling;
            errorSpan && (errorSpan.innerText = '');
        })
    });

    function handleChangeAvatar(evt) {
        evt.preventDefault();
        const modal = new uploadModal();
        modal.onUpload(async url => {
            const resp = await httpClient("/api/account/avatar", "PATCH", {
                userId: g_iUserId,
                avatar: url
            });
            if (resp.isSuccess) {
                window.location.href = window.location.origin + "/user/account/resetusersession?returnUrl=/user/account/profile";
            } else {
                showAlert("failed", "Thất bại", "Đã xảy ra lỗi, vui lòng thử lại!");
            }
        })
    }

    async function handleUpdatePassword(evt) {
        evt.preventDefault();
        if(validateForm2(USER_PASSWORD_FORM)) {
            const formData = getFormData(USER_PASSWORD_FORM);
            const resp = await httpClient("/api/account/password", "PUT", {
                password: formData.currentPassword,
                matchingPassword: formData.matchingPassword,
                newPassword: formData.newPassword,
                userId: formData.userId
            });
            if(resp.isSuccess) {
                USER_PASSWORD_FORM.reset();
                showAlert("success", "Thành công", "Đã cập nhật mật khẩu thành công");
            }else {
                showAlert("failed", "Thất bại", "Mật khẩu hiện tại không đúng, vui lòng thử lại!");
            }
        }
    }

    async function handleUpdateUserBasicInfo(evt) {
        evt.preventDefault();
        if (validateForm(USER_BASIC_FORM)) {
            const formData = getFormData(USER_BASIC_FORM);
            const resp = await httpClient("/api/account/basicinfo/" + formData.userId, "PUT", formData);
            if (resp.isSuccess) {
                window.location.href = window.location.origin + "/user/account/resetusersession?returnUrl=/user/account/profile";
            } else {
                showAlert("failed", "Thất bại", "Cập nhật thông tin thất bại, vui lòng thử lại!");
            }
        }
    }

    function validateForm2(formElm) {
        const formData = getFormData(formElm);
        const {currentPassword, newPassword, matchingPassword} = formData;
        const currentPasswordError = formElm.querySelector(".currentPassword-error");
        const newPasswordError = formElm.querySelector(".newPassword-error");
        const matchingPasswordError = formElm.querySelector(".matchingPassword-error");
        let rs = true;
        if (currentPassword.trim() == '') {
            rs = false;
            currentPasswordError.innerText = "Nhập mật khẩu hiện tại!";
        } else if (newPassword.trim() == '') {
            rs = false;
            newPasswordError.innerText = "Nhập mật khẩu mới!";
        } else if (newPassword.trim().length < 6) {
            rs = false;
            newPasswordError.innerText = "Mật khẩu phải từ 6 ký tự trở lên!";
        } else if (matchingPassword.trim() == '') {
            rs = false;
            matchingPasswordError.innerText = "Nhập lại mật khẩu!";
        } else if (matchingPassword.trim() != newPassword.trim()) {
            rs = false;
            matchingPasswordError.innerText = "Mật khẩu không khớp!"
        }
        return rs;
    }

    function validateForm(formElm) {
        const formData = getFormData(formElm);
        const { firstName, lastName, phoneNumber } = formData;
        const nameRegex = /^[a-z][a-z\d\s]{2,}$/i;
        const nameRegex2 = /^[a-z][a-z\d\s]{2,}$/i;
        const phoneNumberRegex = /^(0|\+84)(\s|\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\d)(\s|\.)?(\d{3})(\s|\.)?(\d{3})$/;
        let rs = true;
        const firstNameError = formElm.querySelector(".firstName-error");
        const lastNameError = formElm.querySelector(".lastName-error");
        const phoneNumberError = formElm.querySelector(".phoneNumber-error");
        if (firstName.trim() == '') {
            rs = false;
            firstNameError.innerText = "Nhập tên!";
        } else if (!nameRegex.test(removeAccent(firstName.trim()))) {
            rs = false;
            firstNameError.innerText = "Tên không hợp lệ"
        }

        if (lastName.trim() == '') {
            rs = false;
            lastNameError.innerText = "Nhập tên họ!";
        } else if (!nameRegex2.test(removeAccent(lastName.trim()))) {
            rs = false;
            lastNameError.innerText = "Tên họ không hợp lệ!";
        }

        if (phoneNumber.trim() == '') {
            rs = false;
            phoneNumberError.innerText = "Nhập số điện thoại!";
        } else if (!phoneNumberRegex.test(phoneNumber.trim())) {
            rs = false;
            phoneNumberError.innerText = "Số điện thoại không hợp lệ!";
        }

        return rs;
    }


})();