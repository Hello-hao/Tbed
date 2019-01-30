/**
 * Created by zhaozl on 2015/11/4.
 */
var fixTable = (function() {

    // 初始化
    // padding:表示浏览器窗口减去表格高度后的空余高度
    var initFixTable = function(tableObj, padding, callback) {

        // 渲染新表头
        _initNewHeader(tableObj);

        // 显示表格(有限隐藏表格防止刷新时闪现的需要隐藏的列)
        tableObj.show();

        // 根据浏览器窗口调整表格高度(必须先设置高度再计算表头宽度，因为可能出现的滚动条会影响宽度的计算)
        _setHeight(tableObj, padding);

        // 计算表头宽度
        recalculateHeader(tableObj);

        $(window).resize(function() {
            setTimeout(function() {

                // 根据浏览器窗口调整表格高度
                _setHeight(tableObj, padding);

                // 计算表头宽度
                recalculateHeader(tableObj);
            }, 200);
        });

        // 监听滚动是否到底
        tableObj.parent().scroll(function(e) {
            if ($(this).scrollTop() + $(this).height() >= tableObj.height()) {
                if (callback) {
                    callback();
                }
            }
        });
    };

    // 初始化新表头
    var _initNewHeader = function(tableObj) {

        // 通过第一行的td设置最小宽度
        var tds = tableObj.contents().find("tr").eq(1).children("td");

        // 创建新表头
        var headerWrapper = document.createElement("div");
        headerWrapper.className = "ft-th-wrapper clearfix";

        // 最小总宽度
        var totalWidth = 0;

        // 原始表头
        var oriThs = tableObj.contents().find("tr").eq(0).children("th");

        // 根据原始表头的信息生成新表头
        for (var i = 0; i < oriThs.length; i++) {

            // 复制原始表头的内容
            var newTh = document.createElement("div");
            $(newTh).html($(oriThs[i]).html());
            $(headerWrapper).append(newTh);

            // 复制原始表头的name属性
            if ($(oriThs[i]).attr("name") !== undefined) {
                $(newTh).attr("name", $(oriThs[i]).attr("name"));
            }

            // 复制原始表头的class
            if ($(oriThs[i]).attr("class") !== undefined) {
                $(newTh).attr("class", $(oriThs[i]).attr("class"));
            }

            // 根据原始表头配置的min-width配置表格的最小宽度、固定宽度
            // 规则：如果设置了固定宽度，就不要设置最小宽度了
            // 最大宽度不起作用，这里不设置
            // 将td中保留一个不设置固定宽度，那它本身的宽度是由总宽度-固定宽度总和决定的，只要总宽度与固定宽度不变，它的宽度就不会变
            var tmpMinWidth = $(oriThs[i]).attr("min-width");
            if (tmpMinWidth) {
                tds.eq(i).css("min-width", tmpMinWidth);
            }

            // 设置固定宽度
            var tmpSolidWidth = $(oriThs[i]).attr("solid-width");
            if (tmpSolidWidth) {
                tds.eq(i).css("width", tmpSolidWidth);
            }

            // 根据原始表头display配置表格的display
            if ($(oriThs[i]).css("display") === "none") {
                tableObj.contents().find("tr").each(function() {
                    $(this).children("td").eq(i).hide();
                });
            }

            // 累计计算min-width得出表格的最小宽度
            if (tmpMinWidth) {
                totalWidth += parseInt(tmpMinWidth.slice(0, -2));
            }
            if (tmpSolidWidth) {
                totalWidth += parseInt(tmpSolidWidth.slice(0, -2));
            }
        }

        // 渲染新表头
        tableObj.before(headerWrapper);

        // 删除原始表头
        oriThs.eq(0).closest("tr").remove();

        // 创建滚动div
        var scrollObj = document.createElement("div");
        scrollObj.className = "ft-table-wrapper";
        $(scrollObj).html(tableObj[0]);

        // 设置滚动区域最小总宽度为min-width总和 + 50
        $(scrollObj).css("min-width", totalWidth + 50 + "px");

        // 渲染滚动div
        $(headerWrapper).after(scrollObj);
    };

    // 根据浏览器窗口调整表格高度
    var _setHeight = function(tableObj, padding) {
        var height = $(window).height() - padding;
        if (height < 100) {
            height = 100;
        }

        tableObj.parent().css("height", height + "px");
    };

    // 计算表头宽度
    var recalculateHeader = function(tableObj) {
        var ths = tableObj.parent().prev().children("div");
        var tds = tableObj.contents().find("tr").eq(0).children("td");

        var offset = 0;
        for (var i = 0; i < tds.length; i++) {
            if (i > 0) {
                ths.eq(i).css("left", offset + "px");
            }
            var tmpWidth = tds.eq(i).width() + 16 + 2;
            ths.eq(i).css("width", tmpWidth + "px");

            if (ths.eq(i).css("display") !== "none") {
                offset += tmpWidth;
            }
        }
    };

    // 根据索引号显示列
    var showColumn = function(tableObj, idx) {
        tableObj.contents().find("tr").each(function() {
            $(this).children("td").eq(idx).show();
        });
        tableObj.parent().prev().children("div").eq(idx).show();

        // 计算表头宽度
        recalculateHeader(tableObj);
    };

    // 根据索引号隐藏列
    var hideColumn = function(tableObj, idx) {
        tableObj.contents().find("tr").each(function() {
            $(this).children("td").eq(idx).hide();
        });
        tableObj.parent().prev().children("div").eq(idx).hide();

        // 计算表头宽度
        recalculateHeader(tableObj);
    };

    return {
        initFixTable: initFixTable,
        renderHeader: recalculateHeader,
        showColumn: showColumn,
        hideColumn: hideColumn
    };
})();