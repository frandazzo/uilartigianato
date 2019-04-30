/**
 * Created by fgran on 15/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "quote/quoteImpiantiView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};
    
    
    var QuoteBreviManoController = fcontrollers.Controller.extend({
        ctor: function(){
            QuoteBreviManoController.super.ctor.call(this);
        },
        index: function(params){
            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "quotebrevimano",
                data: {}
            });


            var view = new views.QuoteBreviManoAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
            
        }
    });


    var QuoteManualiController = fcontrollers.Controller.extend({
        ctor: function(){
            QuoteManualiController.super.ctor.call(this);
        },
        index: function(params){

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "quotemanuali",
                data: {}
            });


            var view = new views.QuoteManualiAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }
    });


    var QuoteImpiantiController = fcontrollers.Controller.extend({
        ctor: function(){
            QuoteImpiantiController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "quoteimpiantifissi",
                data: {}
            });


            var view = new views.QuoteImpiantiAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });


    var QuoteImpiantiControllerNew = fcontrollers.Controller.extend({
        ctor: function(){
            QuoteImpiantiControllerNew.super.ctor.call(this);
        },
        index: function(params) {

            var url = BASE + "quoteimpiantifissinew";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.CreaQuoteRemoteView(service);

        }

    });




    var QuoteSettoreController = fcontrollers.Controller.extend({
        ctor: function(){
            QuoteSettoreController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "quotesettore",
                data: {}
            });


            var view = new views.QuoteSettoreAppView(service);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });

    exports.QuoteImpiantiControllerNew = QuoteImpiantiControllerNew;
    exports.QuoteSettoreController = QuoteSettoreController;
    exports.QuoteImpiantiController = QuoteImpiantiController;
    exports.QuoteBreviManoController =QuoteBreviManoController;
    exports.QuoteManualiController = QuoteManualiController;
    return exports;
});
