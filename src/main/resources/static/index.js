// 给提交按钮绑定事件
$('#export').click
(
    function ()
    {
        var url = '/export?tokenStr=' + $('input[type=text]').val();
        window.open(url);
    }
);