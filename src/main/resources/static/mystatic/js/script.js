layui.define(function(exports){ //提示：模块也可以依赖其它模块，如：layui.define('layer', callback);
    let obj = {

        translate: function (that) {
            console.log(that)
        }
    };

    //输出test接口
    exports('translate', obj);
});