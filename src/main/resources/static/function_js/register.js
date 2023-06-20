 //注册的js

    function register()
    {//定义一个注册的函数


        var user = {
        username: $("#usernameByLogin").val(),
        email: $("#email").val(),
        password : $("#password_once").val()
    };
        var password_confirm = $("#password_confirm").val();

        /**
         * “==”表示“等同”，会在进行相等比较之前会进行必要值的类型转换。简单来说，就是先把值转换为一样的类型再进行相等比较。
         * 就算比较的值的类型不相同，也可以通过强制转换类型成一样的，不会发生错误。
         *
         * “===”表示“恒等”，不会执行类型转换，因此如果两个值不是相同类型，
         * 那么当比较时，它将返回false。如果比较两个变量，它们的类型彼此不兼容，则会产生编译错误。
         */

        function email_cheak(email)
    {
        var szReg=/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
        var email_cheak = szReg.test(email);
        return email_cheak;
    }

        function passWord_check(password)
    {
        return /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[\W_]).{8,}$/.test(password);
    }


        if (user.username==""||user.email=="")
    {

        cocoMessage.warning("账号或邮箱不能为空")
        return  false;
    }
        if (password_confirm!==user.password)
    {
        cocoMessage.error("两次密码不一致");
        return false;
    }

        if (passWord_check(password_confirm)==false)
    {
        cocoMessage.warning("密码必须含数字字母以及特殊符号并且至少八位")
        return false;
    }
        if (email_cheak(user.email)==false)
    {
        cocoMessage.warning("邮箱格式不符合规范");
        /**
         * 邮箱不能以 - _ .以及其它特殊字符开头和结束
         *
         * 邮箱域名结尾为2~5个字母，比如cn、com、name
         */
        return  false;
    }




// alert(1)
        $.ajax(
    {
        url: "/register",
        type: "post",
        dataType: "text", //代表ajax给到的是字符串类型的数据
        contentType : 'application/json',
        data: JSON.stringify(user),
        success: function (data)
    {
        //后端按照json传给前端，所以这里要用Json.parse
        // 不然就算可以获取到字典数据,但是还是无法获取里面对象属性值，
        // 所以这里应该parse解析一下
        var data_only = JSON.parse(data)
        // console.log("数据传输"+data_only)
        console.log(data_only.code)

        $("#signUp").click(function(){
        $("#login-box").addClass('right-panel-active')
    })

        if (data_only.code===1)
    {
        cocoMessage.success(data_only.data,3000);
        $("#login-box").removeClass('right-panel-active')
        setTimeout(3000);

        // cocoMessage.success("请登录");


    }
        else {
        cocoMessage.error(data_only.msg)
    }
    }

    }
        )
    }
