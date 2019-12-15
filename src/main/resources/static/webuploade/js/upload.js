
function d(val) {return val+parseInt('3e7',16);}
(function( $ ){
    // 当domReady的时候开始初始化
    var d = 0;
    $(function() {
        var $wrap = $('#uploader'),

            // 图片容器
            $queue = $( '<ul class="filelist"></ul>' )
                .appendTo( $wrap.find( '.queueList' ) ),

            // 状态栏，包括进度和控制按钮
            $statusBar = $wrap.find( '.statusBar' ),

            // 文件总体选择信息。
            $info = $statusBar.find( '.info' ),

            // 上传按钮
            $upload = $wrap.find( '.uploadBtn' ),

            // 没选择文件之前的内容。
            $placeHolder = $wrap.find( '.placeholder' ),

            $progress = $statusBar.find( '.progress' ).hide(),

            // 添加的文件数量
            fileCount = 0,

            // 添加的文件总大小
            fileSize = 0,

            // 优化retina, 在retina下这个值是2
            ratio = window.devicePixelRatio || 1,

            // 缩略图大小
            thumbnailWidth = 110 * ratio,
            thumbnailHeight = 110 * ratio,

            // 可能有pedding, ready, uploading, confirm, done.
            state = 'pedding',

            // 所有文件的进度信息，key为file id
            percentages = {},
            // 判断浏览器是否支持图片的base64
            isSupportBase64 = ( function() {
                var data = new Image();
                var support = true;
                data.onload = data.onerror = function() {
                    if( this.width != 1 || this.height != 1 ) {
                        support = false;
                    }
                }
                data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
                return support;
            } )(),

            // 检测是否已经安装flash，检测flash的版本
            flashVersion = ( function() {
                var version;

                try {
                    version = navigator.plugins[ 'Shockwave Flash' ];
                    version = version.description;
                } catch ( ex ) {
                    try {
                        version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                                .GetVariable('$version');
                    } catch ( ex2 ) {
                        version = '0.0';
                    }
                }
                version = version.match( /\d+/g );
                return parseFloat( version[ 0 ] + '.' + version[ 1 ], 10 );
            } )(),

            supportTransition = (function(){
                var s = document.createElement('p').style,
                    r = 'transition' in s ||
                            'WebkitTransition' in s ||
                            'MozTransition' in s ||
                            'msTransition' in s ||
                            'OTransition' in s;
                s = null;
                return r;
            })(),

            // WebUploader实例
            uploader;

        if ( !WebUploader.Uploader.support('flash') && WebUploader.browser.ie ) {

            // flash 安装了但是版本过低。
            if (flashVersion) {
                (function(container) {
                    window['expressinstallcallback'] = function( state ) {
                        switch(state) {
                            case 'Download.Cancelled':
                                alert('您取消了更新！')
                                break;

                            case 'Download.Failed':
                                alert('安装失败')
                                break;

                            default:
                                alert('安装已成功，请刷新！');
                                break;
                        }
                        delete window['expressinstallcallback'];
                    };

                    var swf = './expressInstall.swf';
                    // insert flash object
                    var html = '<object type="application/' +
                            'x-shockwave-flash" data="' +  swf + '" ';

                    if (WebUploader.browser.ie) {
                        html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
                    }

                    html += 'width="100%" height="100%" style="outline:0">'  +
                        '<param name="movie" value="' + swf + '" />' +
                        '<param name="wmode" value="transparent" />' +
                        '<param name="allowscriptaccess" value="always" />' +
                    '</object>';

                    container.html(html);

                })($wrap);

            // 压根就没有安转。
            } else {
                $wrap.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
            }

            return;
        } else if (!WebUploader.Uploader.support()) {
            alert( 'Web Uploader 不支持您的浏览器！');
            return;
        }

        // 实例化
        if(VisitorUpload==1){
            uploader = WebUploader.create({
                pick: {
                    id: '#filePicker',
                    label: '点击选择文件'
                },
                formData: {
                    setday:isday,
                    upurlk:qq
                },
                dnd: '#wrapper',
                paste: '#wrapper',
                //swf: 'https://hellohao-cloud.oss-cn-beijing.aliyuncs.com/Uploader.swf',
                swf: '/webuploade/Uploader.sw',
                chunked: false,//分片上传
                chunkSize: 512 * 1024,
                server: '/upimg',
                method:'POST',
                // runtimeOrder: 'flash',
                compress: false,//不启用压缩
                resize: false,//尺寸不改变
                accept: {
                    title: 'Images',
                    //extensions: 'gif,jpg,jpeg,bmp,png',
                    extensions:suffix,
                    mimeTypes: 'image/*'
                },

                // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
                disableGlobalDnd: true,
                fileNumLimit: imgcount, //做多允许上传几个
                //fileSizeLimit: 2000 * 1024 * 1024,    // 200 M  文件总大小
                fileSingleSizeLimit: filesize    // 50 M  单文件大小
            });

        // 拖拽时不接受 js, txt 文件。
        uploader.on( 'dndAccept', function( items ) {
            console.log(items)
            var denied = false,
                len = items.length,
                i = 0,
                // 修改js类型
                unAllowed = 'text/plain;application/javascript ';

            for ( ; i < len; i++ ) {
                // 如果在列表里面
                if ( ~unAllowed.indexOf( items[ i ].type ) ) {
                    denied = true;
                    break;
                }
            }

            return !denied;
        });

        // 文件上传成功
        uploader.on( 'uploadSuccess', function(file,response) {
            $("#address").css('display', 'block');
            if(response.imgurls==-100){
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("本站已禁用游客上传,请登录本站。", {icon: 2});
                });
                // arr_url += '未配置存储源，请先后台配置存储源\r\n';
                // arr_markdown += '未配置存储源，请先后台配置存储源\r\n';
                // arr_html += '未配置存储源，请先后台配置存储源\r\n';
            }else if(response.imgurls==-1){
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("未配置存储源，或存储源配置不正确。", {icon: 2});
                });
            }else if(response.imgurls==-5){
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("上传失败，可用空间不足", {icon: 2});
                });
            } else if(response.imgurls==403){
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("非法调用，请刷新页面后重试", {icon: 2});
                });
            }else if(response.imgurls==-6){
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("图片超出大小。", {icon: 2});
                });
            }else if(response.imgurls==911){
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("你目前不能上传图片,请联系管理员", {icon: 2});
                });
            }else{
                arr_url += response.imgurls + '\r\n';
                arr_markdown += '!['+response.imgnames+'](' + response.imgurls + ')\r\n';
                arr_html += '<img src="' + response.imgurls + '" alt="'+response.imgnames+'" title="'+response.imgnames+'" /> \r\n';
            }
            if(urltypes==1){
                $("#urls").text(arr_url);
            }else if(urltypes==2){
                $("#urls").text(arr_markdown);
            }else{
                $("#urls").text(arr_html);
            }
        });

        // 文件上传失败，显示上传出错
        uploader.on( 'uploadError', function( file ) {
            layui.use('layer', function () {
                layer = layui.layer;
                layer.msg("文件上传失败，或许你的存储源配置不正确。", {icon: 2});
            });
        });

        // uploader.on('filesQueued', function() {
        //     uploader.sort(function( a, b ) {
        //         if ( a.name < b.name )
        //           return -1;
        //         if ( a.name > b.name )
        //           return 1;
        //         return 0;
        //     });
        // });

        // 添加“添加文件”的按钮，
        uploader.addButton({
            id: '#filePicker2',
            label: '继续添加'
        });
        // 添加“添加下一个”模型的按钮，
        // uploader.addButton({
        //     id: '#addModel',
        //     label: '添加下一个'
        // });
        uploader.on('ready', function() {
            window.uploader = uploader;
        });
        }else{
            $('#urlsc').html('<a style="color: #4ebd87;font-size: 0.9em;cursor:pointer;font-weight: bold;">已禁止游客上传,请登陆后使用</a>')
        }
        // 当有文件添加进来时执行，负责view的创建
        function addFile( file ) {
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="title">' + file.name + '</p>' +
                    '<p class="imgWrap"></p>'+
                    '<p class="progress"><span></span></p>' +
                    '</li>' ),

                $btns = $('<div class="file-panel">' +
                    '<span class="cancel">删除</span>' +
                    '<span class="rotateRight">向右旋转</span>' +
                    '<span class="rotateLeft">向左旋转</span></div>').appendTo( $li ),
                $prgress = $li.find('p.progress span'),
                $wrap = $li.find( 'p.imgWrap' ),
                $info = $('<p class="error"></p>'),

                showError = function( code ) {
                    switch( code ) {
                        case 'exceed_size':
                            text = '文件大小超出';
                            break;

                        case 'interrupt':
                            text = '上传暂停';
                            break;

                        default:
                            text = '上传失败，请重试';
                            break;
                    }

                    $info.text( text ).appendTo( $li );
                };

            if ( file.getStatus() === 'invalid' ) {
                showError( file.statusText );
            } else {
                // @todo lazyload
                $wrap.text( '预览中' );
                uploader.makeThumb( file, function( error, src ) {
                    var img;

                    if ( error ) {
                        $wrap.text( '不能预览' );
                        return;
                    }

                    if( isSupportBase64 ) {
                        img = $('<img src="'+src+'">');
                        $wrap.empty().append( img );
                    } else {
                        $.ajax('../../server/preview.php', {
                            method: 'POST',
                            data: src,
                            dataType:'json'
                        }).done(function( response ) {
                            if (response.result) {
                                img = $('<img src="'+response.result+'">');
                                $wrap.empty().append( img );
                            } else {
                                $wrap.text("预览出错");
                            }
                        });
                    }
                }, thumbnailWidth, thumbnailHeight );

                percentages[ file.id ] = [ file.size, 0 ];
                file.rotation = 0;
            }

            file.on('statuschange', function( cur, prev ) {
                if ( prev === 'progress' ) {
                    $prgress.hide().width(0);
                } else if ( prev === 'queued' ) {
                    $li.off( 'mouseenter mouseleave' );
                    $btns.remove();
                }

                // 成功
                if ( cur === 'error' || cur === 'invalid' ) {
                    showError( file.statusText );
                    percentages[ file.id ][ 1 ] = 1;
                } else if ( cur === 'interrupt' ) {
                    showError( 'interrupt' );
                } else if ( cur === 'queued' ) {
                    percentages[ file.id ][ 1 ] = 0;
                } else if ( cur === 'progress' ) {
                    $info.remove();
                    $prgress.css('display', 'block');
                } else if ( cur === 'complete' ) {
                    $li.append( '<span class="success"></span>' );
                }

                $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
            });

            $li.on( 'mouseenter', function() {
                $btns.stop().animate({height: 30});
            });

            $li.on( 'mouseleave', function() {
                $btns.stop().animate({height: 0});
            });

            $btns.on( 'click', 'span', function() {
                var index = $(this).index(),
                    deg;

                switch ( index ) {
                    case 0:
                        uploader.removeFile( file );
                        return;

                    case 1:
                        file.rotation += 90;
                        break;

                    case 2:
                        file.rotation -= 90;
                        break;
                }

                if ( supportTransition ) {
                    deg = 'rotate(' + file.rotation + 'deg)';
                    $wrap.css({
                        '-webkit-transform': deg,
                        '-mos-transform': deg,
                        '-o-transform': deg,
                        'transform': deg
                    });
                } else {
                    $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
                    // use jquery animate to rotation
                    // $({
                    //     rotation: rotation
                    // }).animate({
                    //     rotation: file.rotation
                    // }, {
                    //     easing: 'linear',
                    //     step: function( now ) {
                    //         now = now * Math.PI / 180;

                    //         var cos = Math.cos( now ),
                    //             sin = Math.sin( now );

                    //         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
                    //     }
                    // });
                }


            });

            $li.appendTo( $queue );
        }

        // 负责view的销毁
        function removeFile( file ) {
            var $li = $('#'+file.id);

            delete percentages[ file.id ];
            updateTotalProgress();
            $li.off().find('.file-panel').off().end().remove();
        }

        function updateTotalProgress() {
            var loaded = 0,
                total = 0,
                spans = $progress.children(),
                percent;

            $.each( percentages, function( k, v ) {
                total += v[ 0 ];
                loaded += v[ 0 ] * v[ 1 ];
            } );

            percent = total ? loaded / total : 0;


            spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
            spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
            updateStatus();
        }

        function updateStatus() {
            var text = '', stats;
            if ( state === 'ready' ) {
                text = '<a onclick="setday()" id="setday">图片期限</a>&nbsp;选中' + fileCount + '张图片，共' +
                        WebUploader.formatSize( fileSize ) + '。';
            } else if ( state === 'confirm' ) {
                stats = uploader.getStats();
                if ( stats.uploadFailNum ) {
                    text = '已成功上传' + stats.successNum+ '文件，'+
                        stats.uploadFailNum + '文件上传失败，<a class="retry" href="#" style="color: #4ebd87;">重新上传</a>失败或<a class="ignore" href="#" style="color: #4ebd87;">忽略</a>'
                }

            } else {
                stats = uploader.getStats();
                text = '共' + fileCount + '个（' +
                        WebUploader.formatSize( fileSize )  +
                        '），已上传' + stats.successNum + '个';

                if ( stats.uploadFailNum ) {
                    text += '，失败' + stats.uploadFailNum + '个';
                }
            }

            $info.html( text);
            if(isday>0){
                $('#setday').text(isday+'天后销毁');
            }else{
                $('#setday').text('图片期限');
            }
        }

        function setState( val ) {
            var file, stats;

            if ( val === state ) {
                return;
            }

            $upload.removeClass( 'state-' + state );
            $upload.addClass( 'state-' + val );
            state = val;

            switch ( state ) {
                case 'pedding':
                    $placeHolder.removeClass( 'element-invisible' );
                    $queue.hide();
                    $statusBar.addClass( 'element-invisible' );
                    uploader.refresh();
                    break;

                case 'ready':
                    $placeHolder.addClass( 'element-invisible' );
                    $( '#filePicker2' ).removeClass( 'element-invisible');
                    $queue.show();
                    $statusBar.removeClass('element-invisible');
                    uploader.refresh();
                    break;

                case 'uploading':
                    $( '#filePicker2' ).addClass( 'element-invisible' );
                    $progress.show();
                    $upload.text( '暂停上传' );
                    break;

                case 'paused':
                    $progress.show();
                    $upload.text( '继续上传' );
                    break;

                case 'confirm':
                    $progress.hide();
                    $( '#filePicker2' ).removeClass( 'element-invisible' );
                    $upload.text( '开始上传' );
                    /*$('#filePicker2 + .uploadBtn').click(function () {
                        window.location.reload();
                    });*/
                    stats = uploader.getStats();
                    // alert("成功个数："+stats.successNum);
                    // alert("失败个数："+stats.uploadFailNum);
                    if ( stats.successNum && !stats.uploadFailNum ) {
                        setState( 'finish' );
                        return;
                    }
                    break;
                case 'finish':
                    //图片全部上传成功，并且0失败
                    stats = uploader.getStats();
                    if ( stats.successNum ) {
                        //document.getElementById("shareUrl").style.visibility = 'visible';
                        //createQrcode();
                    } else {
                        // 没有成功的图片，重设
                        state = 'done';
                        location.reload();
                    }
                    break;
            }

            updateStatus();
        }

        uploader.onUploadProgress = function( file, percentage ) {
            var $li = $('#'+file.id),
                $percent = $li.find('.progress span');

            $percent.css( 'width', percentage * 100 + '%' );
            percentages[ file.id ][ 1 ] = percentage;
            updateTotalProgress();
        };

        uploader.onFileQueued = function( file ) {
            fileCount++;
            fileSize += file.size;

            if ( fileCount === 1 ) {
                $placeHolder.addClass( 'element-invisible' );
                $statusBar.show();
            }

            addFile( file );
            setState( 'ready' );
            updateTotalProgress();
        };

        uploader.onFileDequeued = function( file ) {
            fileCount--;
            fileSize -= file.size;

            if ( !fileCount ) {
                setState( 'pedding' );
            }

            removeFile( file );
            updateTotalProgress();

        };

        uploader.on( 'all', function( type ) {
            var stats;
            switch( type ) {
                case 'uploadFinished':
                    setState( 'confirm' );
                    break;

                case 'startUpload':
                    uploader.options.formData.upurlk = GetDateStr(new Date());
                    setState( 'uploading' );
                    break;

                case 'stopUpload':
                    setState( 'paused' );
                    break;

            }
        });

        uploader.onError = function( code ) {
            if(code=='F_DUPLICATE'){
                //alert('复制速度过快')
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("复制速度过快", {icon: 2});
                });
                }
            else if(code=='Q_EXCEED_NUM_LIMIT'){
                //alert('单次上传数量受限制')
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("单次上传数量受限制", {icon: 2});
                });
            }
            else if(code=='F_EXCEED_SIZE'){
                //alert('图片大小受限制')
                layui.use('layer', function () {
                    layer = layui.layer;
                    layer.msg("图片大小受限制", {icon: 2});
                });
            }
            else{alert( 'Eroor: ' + code );}

        };
        $upload.on('click', function() {
            if ( $(this).hasClass( 'disabled' ) ) {
                return false;
            }

            if ( state === 'ready' ) {
                uploader.upload();
            } else if ( state === 'paused' ) {
                uploader.upload();
            } else if ( state === 'uploading' ) {
                uploader.stop();
            }
        });

        $info.on( 'click', '.retry', function() {
            uploader.retry();
        } );

        $info.on( 'click', '.ignore', function() {
            alert( 'todo' );
        } );

        $upload.addClass( 'state-' + state );
        updateTotalProgress();
    });

})( jQuery );

function GetDateStr(dd) {
    var a = dd.getFullYear();
    var b = dd.getMonth();
    var c = dd.getDate();
    //var d = dd.getHours();
    //var e = dd.getMinutes();
    return $.base64.encode(d((a+b+c))+"");
}
