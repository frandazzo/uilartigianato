var MainExtra=function(){var e=function(){if($("#toggle_sidemenu_r").length){$("#skin-toolbox .panel-heading, .toolbox-button").on("click",function(){$("#skin-toolbox").toggleClass("toolbox-open")}),$("#skin-toolbox .panel-heading, .toolbox-button").disableSelection();var e=$("#topbar"),a=$("#sidebar_left"),o=$(".navbar"),r=o.children(".navbar-branding"),t="bg-primary bg-success bg-info bg-warning bg-danger bg-alert bg-system bg-dark bg-light",i="sidebar-light light dark",s={headerSkin:"bg-light",sidebarSkin:"sidebar-default",headerState:"navbar-fixed-top",breadcrumbState:"relative",breadcrumbHidden:"visible"},n="admin-settings",d=localStorage.getItem(n);null===d&&(localStorage.setItem(n,JSON.stringify(s)),d=localStorage.getItem(n)),function(){var n=JSON.parse(d);s=n,$.each(n,function(s,n){switch(s){case"headerSkin":o.removeClass(t).addClass(n),r.removeClass(t).addClass(n+" dark"),"bg-light"===n?r.removeClass(t):r.removeClass(t).addClass(n),$('#toolbox-header-skin input[value="bg-light"]').prop("checked",!1),$('#toolbox-header-skin input[value="'+n+'"]').prop("checked",!0);break;case"sidebarSkin":a.removeClass(i).addClass(n),$('#toolbox-sidebar-skin input[value="bg-light"]').prop("checked",!1),$('#toolbox-sidebar-skin input[value="'+n+'"]').prop("checked",!0);break;case"headerState":"navbar-fixed-top"===n?(o.addClass("navbar-fixed-top"),$("#header-option").prop("checked",!0)):(o.removeClass("navbar-fixed-top"),$("#header-option").prop("checked",!1));break;case"sidebarState":"affix"===n?(a.addClass("affix"),$(".nano").nanoScroller({preventPageScrolling:!0}),$("#sidebar-option").prop("checked",!0)):(a.removeClass("affix"),$("#sidebar-option").prop("checked",!1));break;case"breadcrumbState":"affix"===n?(e.addClass("affix"),$("#breadcrumb-option").prop("checked",!0)):(e.removeClass("affix"),$("#breadcrumb-option").prop("checked",!1));break;case"breadcrumbHidden":"hidden"===n?(e.addClass("hidden"),$("#breadcrumb-hidden").prop("checked",!0)):(e.removeClass("hidden"),$("#breadcrumb-hidden").prop("checked",!1))}})}(),$("#toolbox-header-skin input").on("click",function(){var e=$(this),a=e.val();e.attr("id");o.removeClass(t).addClass(a),r.removeClass(t).addClass(a+" dark"),s.headerSkin=a,localStorage.setItem(n,JSON.stringify(s))}),$("#toolbox-sidebar-skin input").on("click",function(){var e=$(this).val();a.removeClass(i).addClass(e),s.sidebarSkin=e,localStorage.setItem(n,JSON.stringify(s))}),$("#header-option").on("click",function(){var r="navbar-fixed-top";o.hasClass("navbar-fixed-top")?(o.removeClass("navbar-fixed-top"),r="relative",a.removeClass("affix"),$("#sidebar-option").parent(".checkbox-custom").addClass("checkbox-disabled").end().prop("checked",!1).attr("disabled",!0),s.sidebarState="",localStorage.setItem(n,JSON.stringify(s)),e.removeClass("affix"),$("#breadcrumb-option").parent(".checkbox-custom").addClass("checkbox-disabled").end().prop("checked",!1).attr("disabled",!0),s.breadcrumbState="",localStorage.setItem(n,JSON.stringify(s))):(o.addClass("navbar-fixed-top"),r="navbar-fixed-top",$("#sidebar-option").parent(".checkbox-custom").removeClass("checkbox-disabled").end().attr("disabled",!1),$("#breadcrumb-option").parent(".checkbox-custom").removeClass("checkbox-disabled").end().attr("disabled",!1)),s.headerState=r,localStorage.setItem(n,JSON.stringify(s))}),$("#sidebar-option").on("click",function(){var e="";a.hasClass("affix")?(a.removeClass("affix"),$(".nano").nanoScroller({flash:!0}),$(".nano-content").attr("style",""),e=""):(a.addClass("affix"),$(".nano.affix").nanoScroller({preventPageScrolling:!0}),e="affix"),$(window).trigger("resize"),s.sidebarState=e,localStorage.setItem(n,JSON.stringify(s))}),$("#breadcrumb-option").on("click",function(){var a="";e.hasClass("affix")?(e.removeClass("affix"),a=""):(e.addClass("affix"),a="affix"),s.breadcrumbState=a,localStorage.setItem(n,JSON.stringify(s))}),$("#breadcrumb-hidden").on("click",function(){var a="";e.hasClass("hidden")?(e.removeClass("hidden"),a=""):(e.addClass("hidden"),a="hidden"),s.breadcrumbHidden=a,localStorage.setItem(n,JSON.stringify(s))}),$("#clearLocalStorage").on("click",function(){new PNotify({title:"Sei sicuro??",hide:!1,type:"dark",addclass:"mt50",buttons:{closer:!1,sticker:!1},confirm:{confirm:!0,buttons:[{text:"Continua",addClass:"btn-sm btn-info"},{text:"Annulla",addClass:"btn-sm btn-danger"}]},history:{history:!1}}).get().on("pnotify.confirm",function(){localStorage.clear(),location.reload()}).on("pnotify.cancel",function(){})})}},a=function(){var e=$.fullscreen.isNativelySupported();$(".request-fullscreen").click(function(){e?$.fullscreen.isFullScreen()?$.fullscreen.exit():$("html").fullscreen({overflow:"visible"}):alert("Your browser does not support fullscreen mode.")})};return{init:function(){e(),a()}}}();