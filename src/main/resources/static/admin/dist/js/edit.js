var blogEditor;
// Tags Input
$('#blogTags').tagsInput({
    width: '100%',
    height: '38px',
    defaultText: '文章标签'
});

//Initialize Select2 Elements
$('.select2').select2()

$(function () {
    blogEditor = editormd("blog-editormd", {
        width: "100%",
        height: 640,
        syncScrolling: "single",
        path: "/admin/plugins/editormd/lib/",
        toolbarModes: 'full',
        /**图片上传配置*/
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"], //图片上传格式
        imageUploadURL: "/admin/upload/mdpic",
        onload: function (obj) { //上传成功之后的回调

        }
    });

    $("#kv-explorer").fileinput({
        'language':'zh',
        'uploadUrl': '/admin/upload/file',
        showPreview:false,
        dropZoneEnabled: false,
        maxFileSize: 102400
    });

    $("#kv-explorer").on("fileuploaded", function(event, file, previewId, index) {
        //file.response 得到后台处理后的结果
        //file.filescount 得到文件个数
        //index 当前文件的下标
        // console.log(event);
        // console.log(previewId);
        // console.log(file.response);
        if (index === file.filescount-1 && file.response.resultCode === 200) {//当所有文件传输成功时
            //在这里执行文件全部上传成功的方法
            // console.log(file.response.data);
            $('#blogCoverImage').val(file.response.data);
        }
    });

});

//保存按钮
$('#confirmButton').click(function () {
    var blogTitle = $('#blogName').val();
    var blogSubUrl = $('#blogSubUrl').val();
    var blogCategoryId = $('#blogCategoryId').val();
    var blogTags = $('#blogTags').val();
    var blogContent = blogEditor.getMarkdown();
    if (isNull(blogTitle)) {
        swal("请输入文章标题", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTitle, 150)) {
        swal("标题过长", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogSubUrl, 150)) {
        swal("路径过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogCategoryId)) {
        swal("请选择文章分类", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogTags)) {
        swal("请输入文章标签", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTags, 150)) {
        swal("标签过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogContent)) {
        swal("请输入文章内容", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTags, 100000)) {
        swal("文章内容过长", {
            icon: "error",
        });
        return;
    }

    var arr =new Array();
    arr = blogTags.split(',');
    // console.log(arr);
    // console.log(arr.length);
    if (arr.length>6) {
        swal("标签数量限制为6", {
            icon: "error",
        });
        return;
    }

    $(this).attr("disabled", "disabled");

    var blogId = $('#blogId').val();
    var blogTitle = $('#blogName').val();
    var blogSubUrl = $('#blogSubUrl').val();
    var blogCategoryId = $('#blogCategoryId').val();
    var blogTags = $('#blogTags').val();
    var blogContent = blogEditor.getMarkdown();
    var blogCoverImage = $('#blogCoverImage').val();
    var blogStatus = $("#blogStatus option:selected").val();
    var enableComment = $("#enableComment option:selected").val();

    // console.log("提交，状态 和 评论"+blogStatus+","+enableComment);

    var url = '/admin/blogs/save';
    var swlMessage = '保存成功';
    var data = {
        "blogTitle": blogTitle, "blogSubUrl": blogSubUrl, "blogCategoryId": blogCategoryId,
        "blogTags": blogTags, "blogContent": blogContent, "blogCoverImage": blogCoverImage, "blogStatus": blogStatus,
        "enableComment": enableComment
    };
    if (blogId > 0) {
        url = '/admin/blogs/update';
        swlMessage = '修改成功';
        data = {
            "blogId": blogId,
            "blogTitle": blogTitle,
            "blogSubUrl": blogSubUrl,
            "blogCategoryId": blogCategoryId,
            "blogTags": blogTags,
            "blogContent": blogContent,
            "blogCoverImage": blogCoverImage,
            "blogStatus": blogStatus,
            "enableComment": enableComment
        };
    }
    // console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        data: data,
        success: function (result) {
            if (result.resultCode == 200) {
                $('#articleModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '返回博客列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/blogs";
                })
            }
            else {
                $('#articleModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

//取消的方法
$('#cancelButton').click(function () {
    window.location.href = "/admin/blogs";
});

$('#uploadBlogCoverImage').click(function () {
    // console.log("uploadBlogCoverImage");
    $('#articleModal').modal('show');
});

$('#previewBlogCoverImage').click(function () {
    // console.log("previewBlogCoverImage");
    if($('#blogCoverImage').val() === null || $('#blogCoverImage').val() === ''){
        swal("请上传或选择封面", {
            icon: "error",
        });
        return;
    }
    $('#picPreview').html();
    $('#picPreview').html("<img src='" + $('#blogCoverImage').val() + "' height=\"450\" width=\"800\"/>");
    $('#picPreviewModal').modal('show');
});



