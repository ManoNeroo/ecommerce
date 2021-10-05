!function(){const n=document.querySelector(".back-btn"),e=document.getElementById("btn-edit-cusinfo"),t=document.querySelector("#cusinfo-modal form"),i=t&&t.querySelector(".fullName-error"),r=t&&t.querySelector(".phoneNumber-error"),o=t&&t.querySelector(".address-error"),c=t&&t.querySelector("input[name='fullName']"),a=t&&t.querySelector("input[name='phoneNumber']"),d=t&&t.querySelector("input[name='address']"),s=document.getElementById("btn-cancel-order"),l=document.getElementById("btn-confirm-order"),u=document.getElementById("btn-confirm-shipping"),h=document.getElementById("btn-confirm-shipped");async function g(n){toggleLoading(!0);const e=await httpClient("/api/order/status","PUT",{status:n,orderId:g_sOrderId});if(e.isSuccess){const t=["đã được xác nhận","đang được giao","đã được giao","đã bị hủy"],i=await async function(n){const e=await httpClient("/api/account/"+n);if(e.isSuccess)return e.data;return null}(e.data.userId);null!=i&&await async function(n,e,t,i,r){return await httpClient("/api/notification","POST",{content:e,link:t,topicId:i,image:r,title:n})}("Thông báo đơn hàng",`Đơn hàng <span style="font-weight: bold">${g_sOrderId}</span> của bạn ${t[n-1]}`,`/user/order/${g_sOrderId}`,`TOPIC_USER_${i.userName.toUpperCase()}`,"/common/mylib/img/order.png"),window.scrollTo(0,0),window.location.reload()}else showAlert("failed","Thất bại","Có lỗi xảy ra, vui lòng thử lại!");return toggleLoading(!1),e}s&&s.addEventListener("click",function(n){n.preventDefault(),showDialog("confirm","warning","Cảnh báo","Bạn có chắc chắn hủy đơn hàng này không!").addEventListener("confirm",function(){g(4)})}),l&&l.addEventListener("click",function(n){n.preventDefault(),showDialog("confirm","warning","Cảnh báo","Xác nhận đơn hàng này!").addEventListener("confirm",function(){g(1)})}),u&&u.addEventListener("click",function(n){n.preventDefault(),showDialog("confirm","warning","Cảnh báo","Xác nhận đơn hàng đang được giao!").addEventListener("confirm",function(){g(2)})}),h&&h.addEventListener("click",function(n){n.preventDefault(),showDialog("confirm","warning","Cảnh báo","Xác nhận đơn hàng đã được giao!").addEventListener("confirm",function(){g(3)})}),c&&c.addEventListener("focus",()=>i.innerText=""),a&&a.addEventListener("focus",()=>r.innerText=""),d&&d.addEventListener("focus",()=>o.innerText=""),a&&a.addEventListener("keyup",n=>{const{target:e}=n;let{value:t}=e;t=t.replace(/\D/g,""),e.value=t}),n&&n.addEventListener("click",()=>window.history.back()),e&&e.addEventListener("click",function(n){n.preventDefault(),$("#cusinfo-modal").modal("show")}),t&&t.addEventListener("submit",function(n){n.preventDefault(),function(n){let{fullName:e,phoneNumber:t,address:c}=n;e=e.trim(),t=t.trim(),c=c.trim();let a=!0;return""==e?(i.innerText="Nhập tên khách hàng!",a=!1):/^[a-z][a-z\d\s]{2,}$/gi.test(removeAccent(e))||(i.innerText="Tên khách hàng không hợp lệ!",a=!1),""==t?(r.innerText="Nhập số điện thoại!",a=!1):/^\d{10,11}$/.test(t)||(r.innerText="Số điện thoại không hợp lệ!",a=!1),""==c?(o.innerText="Nhập địa chỉ giao hàng!",a=!1):/^[\w\s\.,\\)(/-]{6,}$/i.test(removeAccent(c))||(o.innerText="Địa chỉ không hợp lệ!",a=!1),a}(getFormData(t))&&n.target.submit()})}();