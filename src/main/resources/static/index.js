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

// ajax获取调用次数
$.ajax
(
{
        url : '/getCount', //请求的url
        type : 'GET', //以何种方法发送报文
        dataType : 'json', //预期的服务器返回的数据类型
        success : function (body) //请求成功执行的访求
        {
            $('#count').text(body.data);
        }
    }
);