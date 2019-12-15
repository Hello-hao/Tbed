/*
AUTHOR   : Hitesh Jariwala
TEMPLATE : Avail - Creative Coming Soon Template
VERSION  : 1.0

TABLE OF CONTENTS
1.0 TEMPLATE SETTINGS
2.0 FUNCTIONS & PUBLIC VARIABLES
2.1 set works container height (only on large devices)
2.2 function initialize photo swipe
3.0 window.resize FUNCTION
3.1 adjust works container height
3.2 activate custom scrollbar on works container (only on large devices)
4.0 window.load FUNCTION
4.1 hide preloader & show home section
4.1.1 activate word rotator plugin
4.2 show half overlay on home section
5.0 window.scroll FUNCTION
6.0 document.ready FUNCTION
6.1 adjust works container height
6.2 show / hide menu button (in small devices) clicked
6.3 menu link clicked
6.4 show subscribe button clicked
6.5 hide subscribe button clicked
6.6 check form filled or not
6.7 menu list clicked
6.8 show menu button clicked (on small devices)
6.9 more info button clicked
6.10 subscribe button clicked
6.11 close subscribe button clicked
6.12 activate custom scrollbar on works container (only on large devices)
6.13 activate photo swipe
6.14 activate slideshow background using backstretch
6.15 activate slideshow background with kenburns effect
6.16 activate single image background + star effect (constellation)
6.17 activate YouTube video background
6.18 activate self hosted video background
6.19 activate countdown
6.20 init 1 column carousel
6.21 init home slider
6.22 init 3 column carousel
6.23 init logo carousel
6.24 init google map
6.25 validate and submit subscribe form
6.26 validate and submit contact us form
*/

(function ($) {
    "use strict";

    /*-- ================================ --
    1.0 TEMPLATE SETTINGS
    /*-- ================================ --*/
    $.bg_type = 3;
    /*
    * 1. Backstretch slideshow background
    * 2. Slideshow background with Kenburns Effect
    * 3. Single image background + star effect (constellation)
    * 4. YouTube video background
    * 5. Self hosted video background
    */
    $.launch_date = [7, 9, 2016]; 										//-- launch date [d,m,yyyy], for example 7 September 2016 : [7,9,2016]
    //$.bg_urls = ["img/background/sample8.jpg"];
    $.youtube_url = ""; 												//-- just the last words after https://www.youtube.com/watch?v=
    $.self_host_video_path = ""; 									//-- self hosted video path
    $.self_host_video_filename = ""; 								//-- self hosted video filename "WITHOUT .MP4 EXTENSION"
    $.latitude_longitude = [[21.1991367, 72.84016666]]; 						//-- google map latitude and longitude, $.latitude_longitude = [[latitude,longitude]];
    $.map_marker_url = "img/marker.png"; 							//-- Map marker image url
    $.map_marker_info = '<h6 class="text-darkgrey">Main Office</h6><p class="map-marker-info">Open at 09:00 - 15:00<br>Monday till Saturday</p>';
    $.enable_wordrotator = true; 									//-- enabling wordrotator on headline text
    $.wordrotator_words = ['<h4>也许...</h4>', '<h4>这将是你用过最好用的图床站</h4>']; //-- Array of words, for headline text


    /*-- ================================ --
    2.0 FUNCTIONS & PUBLIC VARIABLES
    /*-- ================================ --*/
    $.is_changing_section = false;

    //-- 2.1 set works container height (only on large devices)
    function InitWorksContHeight() {
        if ($(window).width() > 768) {
            var works_height = $(window).height() - 185;
            var item_margin = ($(window).height() - works_height) * 0.9;

            $('.works-container').height(works_height);
            $('.works-container .work').css({
                'margin-top': item_margin,
                'margin-bottom': item_margin
            });
        }
    }

    //-- 2.2 function initialize photo swipe
    function InitPhotoSwipe(current_index) {
        var pswpElement = document.querySelectorAll('.pswp')[0];

        //-- build items array
        var items = [];

        $('.works-container').find('.work').each(function (index, element) {
            var value = {
                src: $(this).find('.icon-link').attr('href'),
                w: 2500,
                h: 1500,
                title: $(this).find('.icon-link').data('title')
            };

            items.push(value);
        });

        //-- define options
        var options = {
            index: current_index,
            showHideOpacity: true,
            getThumbBoundsFn: false,
            shareEl: false
        };

        var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);
        gallery.init();
    }

    /*-- ================================ --
    3.0 window.resize FUNCTION
    /*-- ================================ --*/
    $(window).resize(function (e) {
        //-- 3.1 adjust works container height
        InitWorksContHeight();

        //-- 3.2 activate custom scrollbar on works container (only on large devices)
        if ($(window).width() > 768) {
            $(".works-container").mCustomScrollbar({
                theme: "minimal",
                scrollInertia: 500,
                alwaysShowScrollbar: 2
            });
        }
    });
    //-- end window.resize function

    /*-- ================================ --
    4.0 window.load FUNCTION
    /*-- ================================ --*/
    //$(window).load(function (e) {
    $(window).ready(function (e) {
        //-- 4.1 hide preloader & show home section
        var delay = setTimeout(function () {
            $('.preloader-container').addClass('is-hidden');
            $('.page-container').addClass('is-visible');
            //-- show logo, menu & home section content
            $('.img-logo, nav, .home-section .left-side, .home-section .right-side').addClass('is-visible');

            //-- 4.1.1 activate word rotator plugin
            if ($.enable_wordrotator) {
                $("#wordsrotator").wordsrotator({
                    autoLoop: true,                  										//-- auto rotate words
                    randomize: false,                										//-- show random entries from the words array
                    stopOnHover: false,              										//-- stop animation on hover
                    changeOnClick: false,            										//-- force animation run on click
                    animationIn: "fadeInDown",          									//-- css class for entrace animation
                    animationOut: "fadeOutUp",        										//-- css class for exit animation
                    speed: 4000,               		 										//-- delay in milliseconds between two words
                    words: $.wordrotator_words  											//-- Array of words, it may contain HTML values
                });
            }

            clearTimeout(this);
        }, 1000);

        //-- 4.2 show half overlay on home section
        var show_half_overlay = setTimeout(function () {
            $('.home-section').find('.half-overlay').addClass('is-visible');

            clearTimeout(this);
        }, 2300);
    });
    //-- end window.load function

    /*-- ================================ --
    5.0 window.scroll FUNCTION
    /*-- ================================ --*/
    $(window).scroll(function (e) {

    });
    //-- end window.scroll function


    /*-- ================================ --
    6.0 document.ready FUNCTION
    /*-- ================================ --*/
    $(document).ready(function (e) {
        //-- 6.1 adjust works container height
        InitWorksContHeight();

        //-- 6.2 show / hide menu button (in small devices) clicked
        $('.menu-button-phone').on('click', function () {
            if ($('nav.small-device ul').hasClass('is-visible')) {
                $('nav.small-device ul').removeClass('is-visible');
            }
            else {
                $('nav.small-device ul').addClass('is-visible');
            }
        });

        //-- 6.3 menu link clicked
        $('.menu-link').on('click', function () {
            //-- set new active menu
            $('nav').find('.active').removeClass('active');
            $(this).addClass('active');

            //-- slide the section-container
            var current_section = $(this).data('section');
            var section_pos = $(this).data('section-pos');
            var section_posy = $(this).data('section-posy');

            $('.section-container').css({
                'transform': 'translate(' + section_pos + '%,' + section_posy + '%)'
            });

            //-- hide & show the half overlay
            $('section').each(function (index, element) {
                $(this).find('.half-overlay').removeClass('is-visible');
            });
            $('.' + current_section + '-section').find('.half-overlay').addClass('is-visible');

            //-- hide the menu & change nav-title (only on small devices)
            if ($(window).width() < 769) {
                $('nav.small-device ul').removeClass('is-visible');

                $('nav.small-device .nav-title').html($('nav.small-device ul').find('.active').data('nav-title'));
            }
        });

        //-- 6.4 show subscribe button clicked
        $('.show-subscribe').on('click', function () {
            $('.subscribe-popup').addClass('is-visible');
        });

        //-- 6.5 hide subscribe button clicked
        $('.hide-subscribe').on('click', function () {
            $('.subscribe-popup').removeClass('is-visible');
        });

        //-- 6.6 check form filled or not
        $('input, textarea').on('focusout', function () {
            if ($(this).val() != "" && $(this).val() != " ") {
                $(this).addClass('filled');
            }
            else {
                $(this).removeClass('filled');
            }
        });

        //-- 6.7 menu list clicked
        $('.menu-list').find('a').each(function (index, element) {
            $(this).on('click', function () {
                if (!$.is_changing_section && !$(this).hasClass('active')) {
                    //-- change section
                    ChangeSection($(this));

                    //-- hide menu list (only on extra small devices)
                    if ($(window).width() < 769) {
                        $('ul.menu-list').css({
                            height: 52
                        });
                        $('.menu-bg').css({
                            height: 108
                        });
                    }
                }
            });
        });

        //-- 6.8 show menu button clicked (on small devices)
        $('a.show-menu').on('click', function () {
            if ($('ul.menu-list').height() == 52) {
                $('ul.menu-list').css({
                    height: 245
                });
                $('.menu-bg').css({
                    height: 298
                });
            }
            else {
                $('ul.menu-list').css({
                    height: 52
                });
                $('.menu-bg').css({
                    height: 108
                });
            }
        });

        //-- 6.9 more info button clicked
        $('.more-info-button').on('click', function () {
            //-- show about-section
            ChangeSection($(this));
        });

        //-- 6.10 subscribe button clicked
        $('.subscribe-button').on('click', function () {
            //-- show subscribe section
            $('.page-container .section-container').css({
                transform: 'translate(-100%,100%)'
            });

            //-- change bg-container darkness
            $('.bg-container').addClass('dark');
        });

        //-- 6.11 close subscribe button clicked
        $('.close-subscribe-button .close-button').on('click', function () {
            //-- show home section again
            $('.page-container .section-container').css({
                transform: 'translate(0%,0%)'
            });

            //-- change bg-container darkness
            $('.bg-container').removeClass('dark');
        });

        //-- 6.12 activate custom scrollbar on works container (only on large devices)
        if ($(window).width() > 768) {
            $(".works-container").mCustomScrollbar({
                theme: "minimal",
                scrollInertia: 500,
                alwaysShowScrollbar: 2
            });
        }

        //-- 6.13 activate photo swipe
        $('.works-container').find('.icon-link').each(function (index, element) {
            $(this).on('click', function (e) {
                e.preventDefault();

                //-- get index
                var index = $(this).parent().parent().index();

                //-- activate photo swipe
                InitPhotoSwipe(index);
            });
        });

        //-- 6.14 activate slideshow background using backstretch
        if ($.bg_type == 1) {
            $(".bg-container").backstretch($.bg_urls, {
                duration: 6000,
                fade: 'normal'
            });
        }

        //-- 6.15 activate slideshow background with kenburns effect
        else if ($.bg_type == 2) {
            var i = 0;
            for (i; i < $.bg_urls.length; i++) {
                var html_code = '<img src="' + $.bg_urls[i] + '" alt="bg-' + i + '" />';

                //-- append image to bg-container
                $('.bg-container').append(html_code);
            }

            //-- activate kenburns
            $(".bg-container").kenburnsy({
                fullscreen: true
            });
        }

        //-- 6.16 activate single image background + star effect (constellation)
        else if ($.bg_type == 3) {
            $(".bg-container").backstretch([
				$.bg_urls
			], {
			    duration: 6000,
			    fade: 'normal'
			});

            var canvas = '<canvas id="bg-canvas"> </canvas>';
            $('.bg-container').prepend(canvas);

            //-- init star effect
            if ($(window).width() < 700) {
                $('canvas').constellation({
                    distance: 40,
                    star: {
                        color: 'rgba(255, 255, 255, .7)',
                        width: 1
                    },
                    line: {
                        color: 'rgba(0, 0, 0, .7)',
                        width: 0.2
                    }
                });
            }
            else {
                $('canvas').constellation({
                    star: {
                        color: 'rgba(255, 255, 255, .7)',
                        width: 1
                    },
                    line: {
                        color: 'rgba(0, 0, 0, .7)',
                        width: 0.2
                    }
                });
            }
        }

        //-- 6.17 activate YouTube video background
        else if ($.bg_type == 4) {
            //-- put the video to the body
            var vid_elem = '<a id="video" class="player" data-property="{videoURL:\'' + $.youtube_url + '\',containment:\'.bg-container\', showControls:false, autoPlay:true, loop:true, mute:true, startAt:0, opacity:1, addRaster:false, quality:\'large\'}"></a>';

            $('body').prepend(vid_elem);

            //-- activate plugin
            if ($(window).width() >= 1200) {
                /*
                * Please note that this player doesn’t work as background for small devices (smartphone) due to the restriction policy adopted by all on 
                * managing multimedia files via javascript. It fallsback to the default Youtube player if used as player (not applied to the body). You 
                * could set the fallback background by initializing the "$.bg_urls" variable in the TEMPLATE
                * SETTINGS
                */

                $('.player').mb_YTPlayer();
            }
            else {
                $(".bg-container").backstretch([
					$.bg_urls
				], {
				    duration: 6000,
				    fade: 'normal'
				});
            }
        }

        //-- 6.18 activate self hosted video background
        else if ($.bg_type == 5) {
            //-- activate plugin
            if ($(window).width() >= 1200) {
                /*
                * Please note that this plugin doesn’t work as background for small devices (smartphone) due to the restriction policy adopted by all on 
                * managing multimedia files via javascript. You could set the fallback background by initializing the "$.bg_urls" variable in the TEMPLATE
                * SETTINGS 
                */

                var videobackground = new $.backgroundVideo($('.bg-container'), {
                    "align": "centerXY",
                    "width": 1280,
                    "height": 720,
                    "path": $.self_host_video_path,
                    "filename": $.self_host_video_filename,
                    "types": ["mp4"]
                });
            }
            else {
                $(".bg-container").backstretch([
					$.bg_urls
				], {
				    duration: 6000,
				    fade: 'normal'
				});
            }
        }

        //-- 6.19 activate countdown
        if ($('#countdown').is(':visible')) {
            $('#countdown').countDown({
                targetDate: {
                    'day': $.launch_date[0],
                    'month': $.launch_date[1],
                    'year': $.launch_date[2],
                    'hour': 0,
                    'min': 0,
                    'sec': 0
                },
                omitWeeks: true
            });
        }

        //-- 6.20 init 1 column carousel
        if ($('.carousel-col-1').is(':visible')) {
            $('.carousel-col-1').each(function (index, element) {
                $(this).owlCarousel({
                    navigation: false,
                    slideSpeed: 300,
                    paginationSpeed: 400,
                    singleItem: true,
                    autoHeight: false,
                    autoPlay: 4000
                });
            });
        }

        //-- 6.21 init home slider
        if ($('.home-slider').is(':visible')) {
            $('.home-slider').owlCarousel({
                singleItem: true,
                navigation: false,
                pagination: true,
                mouseDrag: false,
                touchDrag: false,
                autoPlay: 6000,
                addClassActive: true,
                transitionStyle: "fade",
                afterAction: function () {
                    if ($(window).width() < 768) {
                        //-- move pagination
                        $('.owl-controls').insertAfter('.owl-item.active .text-container .headline-desc');

                        //-- hide navigation background
                        $('.menu-small-device').removeClass('is-visible');

                        //-- set scroll top to 0
                        $('.home-slider .text-container').scrollTop(0);
                    }
                }
            });
        }

        //-- 6.22 init 3 column carousel
        if ($('.carousel-col-3').is(':visible')) {
            $('.carousel-col-3').each(function (index, element) {
                $(this).owlCarousel({
                    items: 3,
                    itemsDesktop: [1199, 3],
                    itemsDesktopSmall: [1025, 2],
                    itemsTablet: [768, 2],
                    itemsMobile: [600, 1],
                    navigation: false,
                    slideSpeed: 300,
                    paginationSpeed: 400
                });
            });
        }

        //-- 6.23 init logo carousel
        if ($('.logo-carousel').is(':visible')) {
            $('.logo-carousel').each(function (index, element) {
                $(this).owlCarousel({
                    items: 4,
                    itemsDesktop: [1199, 4],
                    itemsDesktopSmall: [1025, 3],
                    itemsTablet: [768, 2],
                    slideSpeed: 300,
                    autoPlay: 3000,
                    pagination: false,
                    navigation: true,
                    paginationSpeed: 400
                });
            });
        }

        //-- 6.24 init google map
        if ($('#map').is(':visible')) {
            var map = new google.maps.Map(document.getElementById('map'), {
                zoom: 15,
                scrollwheel: false,
                navigationControl: true,
                mapTypeControl: false,
                scaleControl: false,
                draggable: true,
                disableDefaultUI: false,
                styles: [{ "featureType": "administrative", "elementType": "all", "stylers": [{ "visibility": "on" }, { "lightness": 33}] }, { "featureType": "landscape", "elementType": "all", "stylers": [{ "color": "#f2e5d4"}] }, { "featureType": "poi.park", "elementType": "geometry", "stylers": [{ "color": "#c5dac6"}] }, { "featureType": "poi.park", "elementType": "labels", "stylers": [{ "visibility": "on" }, { "lightness": 20}] }, { "featureType": "road", "elementType": "all", "stylers": [{ "lightness": 20}] }, { "featureType": "road.highway", "elementType": "geometry", "stylers": [{ "color": "#c5c6c6"}] }, { "featureType": "road.arterial", "elementType": "geometry", "stylers": [{ "color": "#e4d7c6"}] }, { "featureType": "road.local", "elementType": "geometry", "stylers": [{ "color": "#fbfaf7"}] }, { "featureType": "water", "elementType": "all", "stylers": [{ "visibility": "on" }, { "color": "#acbcc9"}]}],
                center: new google.maps.LatLng($.latitude_longitude[0][0], $.latitude_longitude[0][1]),
                mapTypeId: google.maps.MapTypeId.ROADMAP
            });
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng($.latitude_longitude[0][0], $.latitude_longitude[0][1]),
                map: map,
                icon: $.map_marker_url
            });
            var infowindow = new google.maps.InfoWindow({
                content: $.map_marker_info
            });
            google.maps.event.addListener(marker, 'click', function () {
                infowindow.open(map, marker);
            });
        }
        //-- end init google map

        //-- 6.25 validate and submit subscribe form
        $('.subscribe-form').validate({
            rules: {
                EMAIL: {
                    required: true,
                    email: true
                }
            },
            messages: {
                EMAIL: {
                    required: "Please insert your email address",
                    email: "Your email address is not valid"
                }
            },
            highlight: function (element, errorClass, validClass) {
                $(element).parent().addClass('form-error');
            },
            unhighlight: function (element, errorClass, validClass) {
                $(element).parent().removeClass('form-error');
            },
            errorPlacement: function (error, element) {

            },
            submitHandler: function (form) {
                var url_dest = $(form).attr('action');
                var form_data = $(form).serialize();
                var form_method = $(form).attr('method');

                //-- show loading
                $(form).find('.form-notif').find('label').remove();
                $(form).find('.form-notif').append('<label class="loading">Please wait</label>').addClass('is-visible');

                $.ajax({
                    type: form_method,
                    url: url_dest,
                    data: form_data,
                    cache: false,
                    dataType: 'json',
                    contentType: "application/json; charset=utf-8",
                    error: function (err) {
                        //-- reset form
                        $(form).trigger('reset');

                        //-- set element to focusout and remove error class
                        $('.subscribe-email').focusout();
                        $(form).find('.form-error').removeClass('form-error');

                        //-- hide loading
                        var wait_loading = setTimeout(function () {
                            $(form).find('.form-notif').removeClass('is-visible');

                            clearTimeout(this);
                        }, 1000);

                        //-- show notif error
                        var show_notif = setTimeout(function () {
                            $(form).find('.form-notif').find('label').remove();
                            $(form).find('.form-notif').append('<label class="notif-error">Could not connect to the registration server. Please try again later.</label>');
                            $(form).find('.form-notif').addClass('is-visible');

                            clearTimeout(this);
                        }, 1500);

                        //-- wait 5 seconds and then hide the notification
                        var hide_notif = setTimeout(function () {
                            $(form).find('.form-notif').removeClass('is-visible');

                            clearTimeout(this);
                        }, 7000);
                    },
                    success: function (data) {
                        if (data.result == "success") {
                            //-- reset form
                            $(form).trigger('reset');
                            $(form).find('.filled').each(function (index, element) {
                                $(this).removeClass('filled');
                            });

                            //-- set element to focusout and remove error class
                            $('.subscribe-email').focusout();
                            $(form).find('.form-error').removeClass('form-error');

                            //-- hide loading
                            var wait_loading = setTimeout(function () {
                                $(form).find('.form-notif').removeClass('is-visible');

                                clearTimeout(this);
                            }, 1000);

                            //-- show notif success
                            var show_notif = setTimeout(function () {
                                $(form).find('.form-notif').find('label').remove();
                                $(form).find('.form-notif').append('<label class="notif-success">Thank you for subscribing us.</label>');
                                $(form).find('.form-notif').addClass('is-visible');

                                clearTimeout(this);
                            }, 1500);

                            //-- wait 5 seconds and then hide the notification
                            var hide_notif = setTimeout(function () {
                                $(form).find('.form-notif').removeClass('is-visible');

                                clearTimeout(this);
                            }, 7000);
                        }
                        else {
                            //-- reset form
                            $(form).trigger('reset');
                            $(form).find('.filled').each(function (index, element) {
                                $(this).removeClass('filled');
                            });

                            //-- set element to focusout and remove error class
                            $('.subscribe-email').focusout();
                            $(form).find('.form-error').removeClass('form-error');

                            //-- hide loading
                            var wait_loading = setTimeout(function () {
                                $(form).find('.form-notif').removeClass('is-visible');

                                clearTimeout(this);
                            }, 1000);

                            //-- show notif error
                            var show_notif = setTimeout(function () {
                                $(form).find('.form-notif').find('label').remove();
                                $(form).find('.form-notif').append('<label class="notif-error">Error</label>');
                                $(form).find('.form-notif').addClass('is-visible');

                                clearTimeout(this);
                            }, 1500);

                            //-- wait 5 seconds and then hide the notification
                            var hide_notif = setTimeout(function () {
                                $(form).find('.form-notif').removeClass('is-visible');

                                clearTimeout(this);
                            }, 7000);
                        }
                    }
                });

                return false;
            }
        });
        //-- end validate and submit subscribe form


        //-- 6.26 validate and submit contact us form
        $('.contact-form').validate({
            rules: {
                email: {
                    required: true,
                    email: true
                },
                subject: {
                    required: true
                },
                name: {
                    required: true
                },
                message: {
                    required: true
                }
            },
            highlight: function (element, errorClass, validClass) {
                $(element).parent().addClass('form-error');
            },
            unhighlight: function (element, errorClass, validClass) {
                $(element).parent().removeClass('form-error');
            },
            errorPlacement: function (error, element) {

            },
            submitHandler: function (form) {
                var url_dest = $(form).attr('action');
                var form_data = $(form).serialize();

                //-- show loading
                $(form).find('.form-notif').find('label').remove();
                $(form).find('.form-notif').append('<label class="loading">Please wait</label>').addClass('is-visible');

                $.post(url_dest, form_data, function (data) {
                    var success = data;

                    if (success) {
                        //-- reset form
                        $(form).trigger('reset');
                        $(form).find('.filled').each(function (index, element) {
                            $(this).removeClass('filled');
                        });

                        //-- hide loading
                        var wait_loading = setTimeout(function () {
                            $(form).find('.form-notif').removeClass('is-visible');

                            clearTimeout(this);
                        }, 1000);

                        //-- show notif success
                        var show_notif = setTimeout(function () {
                            $(form).find('.form-notif').find('label').remove();
                            $(form).find('.form-notif').append('<label class="notif-success">Thank you for contacting us. We will reply you shortly.</label>');
                            $(form).find('.form-notif').addClass('is-visible');

                            clearTimeout(this);
                        }, 1500);

                        //-- wait 5 seconds and then hide the notification
                        var hide_notif = setTimeout(function () {
                            $(form).find('.form-notif').removeClass('is-visible');

                            clearTimeout(this);
                        }, 7000);
                    }
                    else {
                        //-- reset form
                        $(form).trigger('reset');
                        $(form).find('.filled').each(function (index, element) {
                            $(this).removeClass('filled');
                        });

                        //-- hide loading
                        var wait_loading = setTimeout(function () {
                            $(form).find('.form-notif').removeClass('is-visible');

                            clearTimeout(this);
                        }, 1000);

                        //-- show notif error
                        var show_notif = setTimeout(function () {
                            $(form).find('.form-notif').find('label').remove();
                            $(form).find('.form-notif').append('<label class="notif-error">Error</label>');
                            $(form).find('.form-notif').addClass('is-visible');

                            clearTimeout(this);
                        }, 1500);

                        //-- wait 5 seconds and then hide the notification
                        var hide_notif = setTimeout(function () {
                            $(form).find('.form-notif').removeClass('is-visible');

                            clearTimeout(this);
                        }, 7000);
                    }
                });

                return false;
            }
        });
        //-- end validate and submit contact us form

        //Menu script
        $(document).delegate('.open', 'click', function (event) {            
            $(this).addClass('oppenned');
            event.stopPropagation();
        })
        $(document).delegate('body', 'click', function (event) {
            $('.open').removeClass('oppenned');
        })
        $(document).delegate('.oppenned', 'click', function (event) {
            $('.open').removeClass('oppenned');
            event.stopPropagation();
        });
    });
    //-- end document.ready function
})(jQuery);