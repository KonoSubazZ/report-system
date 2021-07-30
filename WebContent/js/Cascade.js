/**
 * @license Cascade JS v3.0.7 (2015-5-27)
 *
 * (c) 2015-2020 FreshFlower
 *
 * Blog: http://freshflower.iteye.com/
 */

//===================
// 初始化实例
//var obj = $.cascade({
//    container: { first: '#firstid', second: '#secondid', third: '#thirdid' },
//    method: 'post',
//    cache: true,
//    dataUrl: { first: '/url/province', second: '/url/city?province=', third: '/url/area?city=' },
//    field: {
//        first: { text: 'provincename', value: 'provincecode' },
//        second: { text: 'cityname', value: 'citycode' },
//        third: { text: 'areaname', value: 'areacode' }
//    },
//    options: {
//        first: { text: 'Please Choose', value: '' },
//        second: { text: '所有城市', value: '-' }, //在下拉控件上显示"所有城市"的项, 其值为"-"
//        third: { text: '', value: '' }  //为空表示只显示数据项
//    },
//    startText: ['浙江省', '杭州市', '西湖区'],     //起始加载省市区的
//    startValue: ['10', '100571', '10057103']    //起始加载的值(与startText二选一, 优先使用startValue)
//});
//
//===================
// 方法调用
// 1. 设置选中值: obj.setValues(['10', '100571', '10057103']);  //依照数组的值依次设定 一级下拉框,二级下拉框, 三级下拉框的值
// 2. 设置选中文本: obj.setTexts(['浙江省', '杭州市']);  //依照数组的值依次设定 一级下拉框,二级下拉框的选中文本, (三级下拉框不设定,默认第一个选项)
//

; (function ($, window, document, undefined) {

    /**
     * 参数配置及其默认值
     **/
    var Conf = {
        container: { first: '', second: '', third: '' },    //(必填)存储HTML控件的容器, 写法如JQuery的选择器
        dataUrl: { first: '', second: '', third: '' },      //(必填)加载数据的URL 若local=false必填
        field: {                                            //(必填)dataJson数据中,用于绑定控件数据的字段名称
            first: { text: 'text', value: 'value' },
            second: { text: 'text', value: 'value' },
            third: { text: 'text', value: 'value' }
        },
        method: 'GET',                                      //获取数据的方法： POST / GET
        cache: true,                                        //是否缓存数据
        local: false,                                       //是否读取本地数据: true.则需要设置dataJson; false:则需要设定dataUrl
        dataJson: { first: {}, second: {}, third: {} },     //用于存储已取到的数据
        options: {                                          //定义选择器是否显示如"请选择"之类的文本及值, 如果为空则不显示
            first: { text: '', value: '' },
            second: { text: '', value: '' },
            third: { text: '', value: '' }
        }
    };

    /**
     * 回调函数处理
     * @param  cb   函数名
     * @param  args 参数
     */
    function callback(cb, args) {
        if (typeof (cb) == 'function') { cb(args); }
    }

    /**
     * 设置选择框的值或文本
     * @param obj 下拉框对象
     * @param v   值
     * @param t   文本(仅当v为null时有效)
     */
    function setTextValue(obj, v, t) {
        if (v && $(obj).find('option[value=' + v + ']').length > 0) {
            $(obj).val(v);
        } else if (t) {
            $(obj).find('option').each(function (i, o) {
                if (t == $.trim($(o).text())) {
                    $(obj).val($(o).val());
                    return false;
                }
            });
        }
    }

    /**
     * 申明类
     */
    function Cascade() {
        this.init.apply(this, arguments);
    }

    /**
     * 申明类对象的方法
     */
    Cascade.prototype = {

        /**
         * 初始化
         * @param  args 初始化参数
         */
        init: function (args) {
            this.conf = $.extend({}, Conf, args);
            this.conf.loadflag = false;
            this.readyvk = { optText: {}, optValue: {}, startText: [], startValue: [] };
            if ((args.startText instanceof Array) && args.startText.length > 0) {
                this.readyvk.startText.push(args.startText);
            }
            if ((args.startValue instanceof Array) && args.startValue.length > 0) {
                this.readyvk.startValue.push(args.startValue);
            }
            
            var self = this;
            var container = this.conf.container;

            //检查数据配置的正确性
            if (!container.first || !container.second) { throw new Error('Lack of control!'); return; }
            if (!this.conf.local) {
                if (!self.conf.dataUrl.first || !self.conf.dataUrl.second) { throw new Error('Lack of dataUrl!'); return; }
            } else {
                if (!(self.conf.dataJson.first instanceof Array) || JSON.stringify(self.conf.dataJson.second) == '{}') {
                    throw new Error('Incorrect dataJson!'); return;
                }
            }

            //绑定事件,如果有三级则需要设置第二级的事件
            if (container.third != '' && self.conf.dataUrl.third) {
                $(container.second).bind('change', function () {
                    $(container.third).empty();
                    var secondvalue = $(container.second).val();
                    self.bindSelectData('third', secondvalue, function () {
                        self.readyValues('third');
                    });
                });
            }
            //绑定第一级的事件
            $(container.first).bind('change', function () {
                var firstvalue = $(container.first).val();
                $(container.second).empty();
                $(container.third).empty();
                self.bindSelectData('second', firstvalue, function () {
                    self.readyValues('second');
                    $(container.second).trigger('change');
                });
            });

            //绑定第一级的数据
            self.bindSelectData('first', null, function () {
                self.readyValues('first');
                $(container.first).trigger('change');
                self.conf.loadflag = true;
            });
        },

        /**
         * 绑定控件数据
         * @param  selectObj 选择器序号: 可填充值为"first", "second", "third"
         * @param  seloption 父选择器选中的数据项的值
         * @param  func 回调函数
         */
        bindSelectData: function (selectObj, seloption, func) {
            var dataJson = this.conf.dataJson[selectObj];
            var dta = seloption == null || seloption == undefined ? dataJson : dataJson[seloption];
            if (dta == null || dta == undefined || JSON.stringify(dta) == '{}' || this.conf.cache != true) {
                //没有数据时或不缓存时, 向服务器取数据
                var faOpt = selectObj == 'first' ? '' : (selectObj == 'second' ? this.conf.options.first.value : this.conf.options.second.value);
                if (seloption == '' || seloption == faOpt) { return; }
                var param = seloption == null || seloption == undefined ? '' : encodeURIComponent(seloption);
                var self = this;
                var requesturl = this.conf.dataUrl[selectObj] + param;
                jQuery.ajax({
                    url: requesturl + (requesturl.indexOf('?') > 0 ? '&' : '?') + '__=' + Math.random(),
                    type: this.conf.method,     //v1.9之前
                    method: this.conf.method,
                    dataType: 'json',
                    success: function (res) {
                        if (res) {
                            if (!(res instanceof Array)) {
                                throw new Error('The Data type must be an array!'); return;
                            }
                            if (seloption) {
                                self.conf.dataJson[selectObj][seloption] = res;
                            } else {
                                self.conf.dataJson[selectObj] = res;
                            }
                        }
                        self.initData(selectObj, res);
                        callback(func);
                    },
                    error: function () {
                        callback(func);
                    }
                });
            }
            else {
                //存在数据,直接加载
                this.initData(selectObj, dta);
                callback(func);
            }
        },


        /**
         * 填充数据
         * @param  selectObj 选择器序号: 可填充值为"first", "second", "third"
         * @param  dta       要填充的数据
         */
        initData: function (selectObj, dta) {
            var select = $(this.conf.container[selectObj]);
            var field = this.conf.field[selectObj];
            $(select).empty();
            $.each(dta, function (i, o) {
                var o_item = $('<option value="' + o[field.value] + '">' + o[field.text] + '</option>').appendTo($(select));
            });

            var xopt = this.conf.options[selectObj];
            if (xopt && xopt.text) {
                var firstOpt = $('<option value="' + xopt.value + '">' + xopt.text + '</option>').prependTo($(select));
            }
        },

        /**
         * 加载预设值 (优先考虑value)
         * @param  selectObj 选择器序号: 可填充值为"first", "second", "third"
         */
        readyValues: function (selectObj) {
            if (this.readyvk.optValue && this.readyvk.optValue[selectObj]) {
                $(this.conf.container[selectObj]).val(this.readyvk.optValue[selectObj]);
                this.readyvk.optValue[selectObj] = null; return;
            }
            if (this.readyvk.optText && this.readyvk.optText[selectObj]) {
                setTextValue($(this.conf.container[selectObj]), null, this.readyvk.optText[selectObj]);
                this.readyvk.optText[selectObj] = null; return;
            }

            if (this.readyvk.startValue && this.readyvk.startValue.length > 0) {
                this.readyvk.optValue = {};
                var rv = this.readyvk.startValue.pop();
                if (rv.length > 0) { this.readyvk.optValue.first = rv[0]; }
                if (rv.length > 1) { this.readyvk.optValue.second = rv[1]; }
                if (rv.length > 2) { this.readyvk.optValue.third = rv[2]; }
                this.readyValues(selectObj);
            }
            else if (this.readyvk.startText && this.readyvk.startText.length > 0) {
                this.readyvk.optText = {};
                var rv = this.readyvk.startText.pop();
                if (rv.length > 0) { this.readyvk.optText.first = rv[0]; }
                if (rv.length > 1) { this.readyvk.optText.second = rv[1]; }
                if (rv.length > 2) { this.readyvk.optText.third = rv[2]; }
                this.readyValues(selectObj);
            }
            this.readyvk.startValue = [];
            this.readyvk.startText = [];
        },

        /**
         * 设置选择项的文本
         * @param arr 文本数组: 例如: ['浙江省', '杭州市', '西湖区']
         */
        setTexts: function (arr) {
            if (!((arr instanceof Array) && arr.length > 0)) return;
            this.readyvk.startText.push(arr);
            if (this.conf.loadflag == true) {
                var self = this;
                this.bindSelectData('first', null, function () {
                    self.readyValues('first');
                    $(self.conf.container.first).trigger('change');
                });
            }
        },

        /**
         * 设置选择项的值
         * @param arr 值数组: 例如: ['10', '100571', '10057103']
         */
        setValues: function (arr) {
            if (!((arr instanceof Array) && arr.length > 0)) return;
            this.readyvk.startValue.push(arr);
            if (this.conf.loadflag == true) {
                var self = this;
                this.bindSelectData('first', null, function () {
                    self.readyValues('first');
                    $(self.conf.container.first).trigger('change');
                });
            }
        }
    };

    /**
     * 初始化 (外部调用方法)
     * @param args 参数
     */
    $.cascade = function (args) {
        return new Cascade(args);
    };

})(jQuery, window, document);