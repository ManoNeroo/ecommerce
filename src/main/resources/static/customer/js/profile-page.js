!function(){const e=document.getElementById("change-avatar-btn"),t=document.getElementById("user-basic-info-form"),n=document.getElementById("user-password-form"),r=t&&t.querySelector("input[name='phoneNumber']");e&&e.addEventListener("click",function(e){e.preventDefault(),(new uploadModal).onUpload(async e=>{const t=await httpClient("/api/account/avatar","PATCH",{userId:g_iUserId,avatar:e});t.isSuccess?window.location.href=window.location.origin+"/user/account/resetusersession?returnUrl=/user/account/profile":showAlert("failed","Thất bại","Đã xảy ra lỗi, vui lòng thử lại!")})}),t&&t.addEventListener("submit",async function(e){if(e.preventDefault(),function(e){const t=getFormData(e),{firstName:n,lastName:r,phoneNumber:i}=t;let s=!0;const o=e.querySelector(".firstName-error"),a=e.querySelector(".lastName-error"),c=e.querySelector(".phoneNumber-error");return""==n.trim()?(s=!1,o.innerText="Nhập tên!"):/^[a-z][a-z\d\s]{2,}$/i.test(removeAccent(n.trim()))||(s=!1,o.innerText="Tên không hợp lệ"),""==r.trim()?(s=!1,a.innerText="Nhập tên họ!"):/^[a-z][a-z\d\s]{2,}$/i.test(removeAccent(r.trim()))||(s=!1,a.innerText="Tên họ không hợp lệ!"),""==i.trim()?(s=!1,c.innerText="Nhập số điện thoại!"):/^(0|\+84)(\s|\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\d)(\s|\.)?(\d{3})(\s|\.)?(\d{3})$/.test(i.trim())||(s=!1,c.innerText="Số điện thoại không hợp lệ!"),s}(t)){const e=getFormData(t),n=await httpClient("/api/account/basicinfo/"+e.userId,"PUT",e);n.isSuccess?window.location.href=window.location.origin+"/user/account/resetusersession?returnUrl=/user/account/profile":showAlert("failed","Thất bại","Cập nhật thông tin thất bại, vui lòng thử lại!")}}),n&&n.addEventListener("submit",async function(e){if(e.preventDefault(),function(e){const t=getFormData(e),{currentPassword:n,newPassword:r,matchingPassword:i}=t,s=e.querySelector(".currentPassword-error"),o=e.querySelector(".newPassword-error"),a=e.querySelector(".matchingPassword-error");let c=!0;return""==n.trim()?(c=!1,s.innerText="Nhập mật khẩu hiện tại!"):""==r.trim()?(c=!1,o.innerText="Nhập mật khẩu mới!"):r.trim().length<6?(c=!1,o.innerText="Mật khẩu phải từ 6 ký tự trở lên!"):""==i.trim()?(c=!1,a.innerText="Nhập lại mật khẩu!"):i.trim()!=r.trim()&&(c=!1,a.innerText="Mật khẩu không khớp!"),c}(n)){const e=getFormData(n),t=await httpClient("/api/account/password","PUT",{password:e.currentPassword,matchingPassword:e.matchingPassword,newPassword:e.newPassword,userId:e.userId});t.isSuccess?(n.reset(),showAlert("success","Thành công","Đã cập nhật mật khẩu thành công")):showAlert("failed","Thất bại","Mật khẩu hiện tại không đúng, vui lòng thử lại!")}}),r&&r.addEventListener("keyup",e=>{const{target:t}=e;let{value:n}=t;n=n.replace(/\D/g,""),t.value=n}),t&&t.querySelectorAll("input[type='text']").forEach(e=>{e.addEventListener("focus",e=>{const{target:t}=e,n=t.nextElementSibling;n&&(n.innerText="")})}),n&&n.querySelectorAll("input[type='password']").forEach(e=>{e.addEventListener("focus",e=>{const{target:t}=e,n=t.nextElementSibling;n&&(n.innerText="")})})}();