$(function () {
    var keyword = $('#keyword').val();
    var pageNum = $("#pageNum").val();
    if (pageNum === null || pageNum < 1) {
        pageNum = 1
    }
    var pageSize = $("#pageSize").val();
    if (pageSize === null || pageSize < 1) {
        pageSize = 10;
    }
    $("#jqGrid").jqGrid({
        url: '/admin/blogs/list',
        datatype: "json",
        postData: {keyword: keyword},
        colModel: [
            {label: 'id', name: 'blogId', index: 'blogId', width: 50, key: true, hidden: true},
            {label: '标题', name: 'blogTitle', index: 'blogTitle', width: 140},
            {label: '封面图', name: 'blogCoverImage', index: 'blogCoverImage', width: 120, formatter: coverImageFormatter},
            {label: '浏览量', name: 'blogViews', index: 'blogViews', width: 60},
            {label: '是否发布', name: 'blogStatus', index: 'blogStatus', width: 60, formatter: statusFormatter},
            {label: '是否删除', name: 'isDeleted', index: 'isDeleted', width: 60, formatter: delFormatter},
            {label: '博客分类', name: 'blogCategoryName', index: 'blogCategoryName', width: 60},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 90}
        ],
        height: 700,
        rowNum: pageSize,
        page: pageNum,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        onSelectRow: function (rowid, status, rowData) {
            if (status) {
                $('#' + rowid).find("td").addClass("SelectBG");
            } else {
                $('#' + rowid).find("td").removeClass("SelectBG");

            }
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function coverImageFormatter(cellvalue) {
        //console.log(cellvalue);
        var str;
        if (cellvalue === null || cellvalue.trim() === '' || cellvalue.trim() === 'null') {
            str = "未上传封面！";
        } else {
            str = "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
        }
        return str;
    }

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<span class=\"badge badge-secondary\">草稿</span>";
        } else if (cellvalue == 1) {
            return "<span class=\"badge badge-success\">发布</span>";
        }
    }

    function delFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<span class=\"badge badge-success\">存在</span>";
        } else if (cellvalue == 1) {
            return "<span class=\"badge badge-danger\">已删除</span>";
        }
    }

});

/**
 * 搜索功能
 */
function search() {
    //标题关键字
    var keyword = $('#keyword').val();
    if (!validLength(keyword, 20)) {
        swal("搜索字段长度过大!", {
            icon: "error",
        });
        return false;
    }
    //数据封装
    var searchData = {keyword: keyword};
    //传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    //点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    //提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", {url: '/admin/blogs/list'}).trigger("reloadGrid");
}

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function addBlog() {
    window.location.href = "/admin/blogs/edit";
}

function editBlog() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var keyword = $('#keyword').val();
    var pageNum = $("#jqGrid").getGridParam("page");
    var pageSize = $("#jqGrid").getGridParam("rowNum");
    //var params = "?keyword=" + keyword + "&pageNum=" + pageNum + "&pageSize=" + pageSize
    //window.location.href = "/admin/blogs/edit/" + id + params;
    var html = '<form action="/admin/blogs/edit/' +id + '" method="post" name="editForm" style=\'display:none\'>' +
        '<input name="keyword" hidden value="' + keyword + '">' +
        '<input name="pageNum" hidden value="' + pageNum + '">' +
        '<input name="pageSize" hidden value="' + pageSize + '">' +
        '</form>'
    document.write(html);
    document.editForm.submit();

}

function deleteBlog() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/admin/blogs/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.resultCode == 200) {
                            swal("删除成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}

function postBlog() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要发布吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/admin/blogs/post",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.resultCode == 200) {
                            swal("发布成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}

function recoverBlog() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var rowData = jQuery("#jqGrid").jqGrid("getRowData", id);
    //console.log(rowData);
    //console.log(rowData.isDeleted)
    var str = rowData.isDeleted
    if (str.indexOf("存在") != -1) {
        swal("已恢复", {
            icon: "success",
        });
        return;
    }

    swal({
        title: "确认弹框",
        text: "确认要恢复吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/admin/blogs/recover",
                    data: {blogId: id},
                    success: function (r) {
                        if (r.resultCode == 200) {
                            swal("恢复成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}