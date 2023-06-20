//一个登录功能的js
function login()
{
    // let elementById = document.getElementById("emailOrCountLogin");
    // let passwordLogin = document.getElementById("passwordLogin");
    var user = {
        username : $("#emailOrCountLogin").val(),
        password: $("#passwordLogin").val(),
        codeVerify : $("#code").val()
    }
    // if (user.username=="")
    // {
    //     cocoMessage.warning("请填写账户信息");
    //     return false;
    // }
    // if (user.password=="")
    // {
    //     cocoMessage.warning("请填写密码");
    //     return false;
    // }
    // if (user.codeVerify=="")
    // {
    //     cocoMessage.warning("请填写验证码");
    //     return false;
    // }
    console.log("执行脚本");
    $.ajax({
        url: "/loginInFo",
        type: "post",
        dataType: "text",//代表ajax给到的是字符串类型的数据
        /**
         * contentType: "application/json”，首先明确一点，这也是一种文本类型（和text/json一样），表示json格式的字符串，如果ajax中设置为该类型，
         * 则发送的json对象必须要使用JSON.stringify进行序列化成字符串才能和设定的这个类型匹配。
         */
        contentType: "application/json",
        data : JSON.stringify(user),
        success: function (data)
        {
            //JSON.parse() 方法用来解析 JSON 字符串，构造由字符串描述的 JavaScript 值或对象
            var dataLast = JSON.parse(data);
            console.log(dataLast)
            console.log(dataLast.code)
            if (dataLast.code==1)
            {
                cocoMessage.success(dataLast.data,3000)
                console.log("执行成功登录");
                window.location.href = "/tologin";


            }else {
                cocoMessage.error(dataLast.msg,3000)
            }
        }
    })


}