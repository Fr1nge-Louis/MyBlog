$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/file/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'fileId', index: 'fileId', key: true, hidden: true},
            {label: '文件类型', name: 'fileType', index: 'fileType', hidden: true},
            {label: '文件名称', name: 'fileName', index: 'fileName', width: 140,editable:true},
            {label: '文件路径', name: 'fileUrl', index: 'fileUrl', width: 120},
            {label: '文件预览', name: 'fileReqUrl', index: 'fileReqUrl', width: 160, formatter: imageFormatter},
            {label: '文件大小', name: 'fileSize', index: 'fileSize', width: 40, formatter: fileSizeFormatter},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 90}
        ],
        height: 700,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: false,
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
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
        ondblClickRow: function (id) {
            var rowData = $('#jqGrid').jqGrid('getRowData',id);
            $('#jqGrid').jqGrid('editRow',id,{
                keys : true,        //这里按[enter]保存
                url: "/admin/file/update/name",
                mtype : "POST",
                restoreAfterError: true,
                extraparam: {
                    "fileId": rowData.id
                },
                // oneditfunc: function(rowid){
                //     console.log(rowid);
                // },
                // onEnter: function (rowid){
                //     console.log("onenter"+rowid);
                //
                //     console.log("onenter"+rowid);
                //     return false;
                // },
                successfunc: function(response){
                    console.log("成功");
                    console.log(response);

                    swal(response.responseJSON.message, {
                        icon: "success",
                    });
                    return true;
                },
                errorfunc: function(rowid, response){
                    console.log("失败");
                    console.log(response);
                    swal(response.responseJSON.message, {
                        icon: "error",
                    });
                    return false;
                }
            });
        }


    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function imageFormatter(cellvalue, options, row) {
        // console.log(row);
        // console.log(row["fileName"]);
        // console.log(row["createTime"]);
        // console.log(options);
        // console.log(cellvalue);
        var str;
        if (row["fileType"] === "1") {
            str = "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
        } else {
            str = "<a href='" + cellvalue + "' download='" + row["fileName"] + "'>" + "下载" + "</a>";
        }
        return str;
    }

    function fileSizeFormatter(cellvalue) {
        if (cellvalue <= 1024) {
            return cellvalue + " B";
        }

        if (cellvalue > 1024 && cellvalue < 1048576) {
            return Math.round(cellvalue / 1024.00) + " KB";
        }

        if (cellvalue > 1048576) {
            return Math.round(cellvalue / 1048576.00) + " MB";
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
    $("#jqGrid").jqGrid("setGridParam", {url: '/admin/file/list'}).trigger("reloadGrid");
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