define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "quote/quoteAssocView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};


    var QuoteAssocController = fcontrollers.CrudController.extend({
        ctor: function () {
            QuoteAssocController.super.ctor.call(this);
        },

        index: function(params) {


            var workername = "";




            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "quoteassociative/search",
                data: {}
            });



            var view = new views.SearchQuoteAppView(service);


            return view;
        }

    });

    //controller per la visualizzazione del dettaglio quota
    var QuotaDettaglioController = fcontrollers.Controller.extend({
        ctor: function(){
            QuotaDettaglioController.super.ctor.call(this);
        },
        index: function(params) {

            var quotaId = params.id;
            var url = BASE + "quoteassociative/dettaglio/"+ quotaId;
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.QuotaDettaglioRemoteView(service, quotaId);

        }
    });


    exports.QuoteAssocController = QuoteAssocController;
    exports.QuotaDettaglioController = QuotaDettaglioController;

    return exports;

});