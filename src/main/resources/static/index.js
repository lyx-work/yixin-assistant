/*-----------------页面初始化----------------*/
let today = new Date();
let year = today.getFullYear();
let month = (today.getMonth()+1)/10 >= 1 ? today.getMonth()+1 : '0'+(today.getMonth()+1);
let day = today.getDate()/10 >= 1 ? today.getDate() : '0'+today.getDate();
let todayStr =  year + "-" + month + "-" + day;
$('#day').val(todayStr);
$('#mDay').val(todayStr);

$('#function').hide();
getPic();
/*-----------------//页面初始化----------------*/



/*-----------------认证按钮绑定事件----------------*/
$('#sub').click
(
    function ()
    {
        let userName = $('#userName').val();
        let kaptcha = $('#kaptcha').val();
        if (kaptcha == null || kaptcha == '')
        {
            alert('必须输入验证码');
        }
        else if (userName == null || userName == '')
        {
            alert('必须选择要登录的账号');
        }
        else
        {
            $.ajax
            (
                {
                    url : '/set-token', //请求的url
                    type : 'POST', //以何种方法发送报文
                    dataType : 'json', //预期的服务器返回的数据类型
                    data: "kaptcha="+kaptcha+"&userName="+userName,
                    contentType:'application/x-www-form-urlencoded',
                    success : function (rep) //请求成功执行的访求
                    {
                        if (!rep.success)
                        {
                            alert(rep.msg);
                        }
                        else
                        {
                            $('#status span').text(rep.msg);
                            $('#auth').hide();
                            $('#function').show();
                        }
                    },
                    error : function () //请求失败执行的方法
                    {
                        alert('请求失败');
                    }
                }
            );
        }
    }
);
/*-----------------//认证按钮绑定事件----------------*/



/*-----------------点击图片刷新----------------*/
$('#picBox').click
(
    function ()
    {
        getPic();
    }
);
/*-----------------//点击图片刷新----------------*/



$('#fun1').click
(
    function ()
    {
        let day = $('#day').val();

        if (day==null || day=='')
        {
            alert('必须输入日期');
        }
        else
        {
            window.open('/hw-today-data?day='+day);
        }
    }
);

$('#fun2').click
(
    function ()
    {
        let day = $('#mDay').val();

        if (day==null || day=='')
        {
            alert('必须输入日期');
        }
        else
        {
            window.open('/wq-today-data?day='+day);
        }
    }
);

// --------------------------------------------------------------------------
function getPic()
{
    $.ajax
    (
        {
            url : '/get-pic', //请求的url
            type : 'GET', //以何种方法发送报文
            dataType : 'json', //预期的服务器返回的数据类型
            data: $('#mForm').serialize(),
            success : function (rep) //请求成功执行的访求
            {
                $('#pic').attr('src', rep.data);
            },
            error : function () //请求失败执行的方法
            {
                alert('请求失败');
            }
        }
    );
}