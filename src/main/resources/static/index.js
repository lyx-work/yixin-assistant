// 给提交按钮绑定事件
$('#export').click
(
    function ()
    {
        if ($('input[type=text]').val() == '')
        {
            alert('必须输入tokenStr');
        }
        else
        {
            var url = '/export?tokenStr=' + $('input[type=text]').val();
            window.open(url);
        }
    }
);