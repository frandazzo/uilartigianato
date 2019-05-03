define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "reports/reportProvenienzaUncView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};

    //controller che porta alla mascherina custom per la ricerca dei lavoratori
    var ProvenienzaUncReportController = fcontrollers.Controller.extend({
        ctor: function(){
            ProvenienzaUncReportController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "provenienzauncform",
                data: {}
            });



            var view = new views.ReportProvenienzaUncAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });


    exports.ProvenienzaUncReportController = ProvenienzaUncReportController;

    return exports;
});
