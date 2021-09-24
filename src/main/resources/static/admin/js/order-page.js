(function () {
    const FILTER_FORM = document.querySelector(".filter-form");
    FILTER_FORM && FILTER_FORM.addEventListener("submit", handleSubmitFilter);

    generateOrderStatusOptions(g_aStatuses);

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

    $('#daterange').daterangepicker({
        opens: 'center',
        maxDate: new Date(),
        autoUpdateInput: false,
        locale: {
            cancelLabel: 'Clear'
        }
    });

    $('#daterange').on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
    });

    $('#daterange').on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    if(g_dStartDate && g_dEndDate) {
        const sDate = moment(g_dStartDate, 'YYYY-MM-DD');
        const eDate = moment(g_dEndDate, 'YYYY-MM-DD');
        $('#daterange').val(sDate.format('DD/MM/YYYY') + ' - ' + eDate.format('DD/MM/YYYY'));
        $('#daterange').data('daterangepicker').setStartDate(sDate.toDate());
        $('#daterange').data('daterangepicker').setEndDate(eDate.toDate());
    }

    function handleSubmitFilter(evt) {
        evt.preventDefault();
        const formData = getFormData(evt.target);
        let origin = window.location.origin;
        let path = window.location.pathname;
        let rqUrl = origin + path;
        const drpText = formData.daterange;
        const search = formData.search;
        const statuses = $("#order-status").select2("data");
        let startDate = null;
        let endDate = null;
        let query = "?page=1";
        if (drpText.trim() != '') {
            const drp = $('#daterange').data('daterangepicker');
            startDate = drp.startDate.format("YYYY-MM-DD");
            endDate = drp.endDate.format("YYYY-MM-DD");
        }
        search != '' && (query += "&search=" + search);
        startDate != null && (query += "&startDate=" + startDate);
        endDate != null && (query += "&endDate=" + endDate);
        statuses.forEach((val,ix) => {
            if (ix == 0) {
                query += "&statuses=" + val.id;
            } else {
                query += "," + val.id;
            }
        })
        rqUrl += query;
        window.location.href = rqUrl;
    }

    function generateOrderStatusOptions(selectedList) {
        let data = [
            {
                id: 0,
                text: "Chờ xác nhận"
            },
            {
                id: 1,
                text: "Chờ lấy hàng"
            },
            {
                id: 2,
                text: "Đang giao"
            },
            {
                id: 3,
                text: "Đã giao"
            },
            {
                id: 4,
                text: "Đã hủy"
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
        $('#order-status').select2({
            placeholder: "Chọn trạng thái",
            allowClear: true,
            data: arr
        });
    }
})()