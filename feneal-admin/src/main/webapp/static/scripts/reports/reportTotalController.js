define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "reports/reportTotalView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};

    //controller che porta alla mascherina custom per la ricerca dei lavoratori
    var TotalReportController = fcontrollers.Controller.extend({
        ctor: function(){
            TotalReportController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "totalform",
                data: {}
            });



            var view = new views.ReportTotalAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });


    exports.TotalReportController = TotalReportController;

    return exports;
});
