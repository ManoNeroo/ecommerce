const ARTICLE_TAB = document.getElementById("article-nav");
const ARTICLE_TEXTAREA = document.getElementById("product-article-editor");
const ARTICLE_FORM = document.getElementById("article-form");

ARTICLE_TAB.addEventListener("click", handleClickArticleTab);

async function handleSaveArticle() {
    const productId = BASIC_FORM.querySelector("input[name='id']").value;
    let body = {
        content: '',
        productId: productId
    }
    body.content = tinymce.get('product-article-editor').getContent();
    const idInput = ARTICLE_FORM.querySelector("input[name='id']");
    const id = idInput.value;
    if(id != '' && !isNaN(Number(id))) {
        body.id = Number(id);
    }
    const resp = await httpClient("/api/productarticle", "POST", body);
    if(resp.isSuccess) {
        idInput.value = resp.data.id;
        showAlert('success', 'Thành công', 'Bài viết đã được lưu lại!');
    } else {
        showAlert('failed', 'Thất bại', 'Có lỗi xảy ra, vui lòng thử lại!');
    }
}

function handleFilePicker(cb, value, meta) {
    var input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.onchange = async function () {
        var file = this.files[0];
        const fData = new FormData();
        fData.append("file", file);
        const uploadResp = await httpClient("/api/upload", "POST", fData);
        if (uploadResp.isSuccess) {
            cb(uploadResp.data, { title: file.name });
        } else {
            showAlert('failed', 'Lỗi', 'Có lỗi xảy ra trong quá trình upload file!');
        }
    };
    input.click();
}

async function handleClickArticleTab() {
    const id = BASIC_FORM.querySelector("input[name='id']").value;
    const resp = await httpClient("/api/productarticle/product/" + id);
    if(resp.isSuccess) {
        if(resp.data != null) {
            ARTICLE_TEXTAREA.value = resp.data.content;
            const idInputElm = ARTICLE_FORM.querySelector("input[name='id']");
            idInputElm.value = resp.data.id;
        }
    }
    tinymce.init(TINYMCEOPTIONS);
}

const TINYMCEOPTIONS = {
    selector: '#product-article-editor',
    plugins: 'advlist anchor autolink autosave charmap code codesample directionality emoticons fullscreen help hr image imagetools insertdatetime link lists nonbreaking noneditable pagebreak preview save searchreplace table visualblocks visualchars wordcount',
    menubar: 'file edit view insert format tools table help',
    toolbar: 'undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen preview save | image template link anchor code codesample | ltr rtl',
    toolbar_sticky: true,
    autosave_ask_before_unload: true,
    autosave_interval: '30s',
    autosave_prefix: '{path}{query}-{id}-',
    autosave_restore_when_empty: false,
    autosave_retention: '2m',
    height: 550,
    image_caption: true,
    image_title: true,
    toolbar_mode: 'sliding',
    content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
    file_picker_types: 'image',
    file_picker_callback: handleFilePicker,
    save_onsavecallback: handleSaveArticle
};
