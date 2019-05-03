define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "reports/reportProvBilView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};

    //controller che porta alla mascherina custom per la ricerca dei lavoratori
    var ProvBilReportController = fcontrollers.Controller.extend({
        ctor: function(){
            ProvBilReportController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "provbilform",
                data: {}
            });



            var view = new views.ReportProvBilAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });


    exports.ProvBilReportController = ProvBilReportController;

    return exports;
});
