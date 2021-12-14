//文件上传
$(document).ready(function () {
    $("#kv-explorer").fileinput({
        'language':'zh',
        'uploadUrl': '/admin/upload/file',
        overwriteInitial: false,
        initialPreviewAsData: true,
        previewTemplates: ['image','video','audio','text','object'],
        maxFileSize: 102400,
        maxFilesNum: 6
    });
});

// $("#kv-explorer").on("fileuploaded", function(event, file, previewId, index) {
//     //file.response 得到后台处理后的结果
//     //file.filescount 得到文件个数
//     //index 当前文件的下标
//     console.log(event);
//     console.log(previewId);
//     console.log(file.response);
//     if (index === file.filescount-1 && file.response.resultCode === 200) {//当所有文件传输成功时
//         //在这里执行文件全部上传成功的方法
//         return {
//             message: file.response.message,
//             data: null
//         };
//     }else{
//         return {
//             message: file.response.message,
//             data: null
//         };
//     }
//
// });