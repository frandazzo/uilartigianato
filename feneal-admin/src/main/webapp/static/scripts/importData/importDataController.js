/**
 * Created by fgran on 18/04/2017.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "importData/importDataView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};



    var ImportaDelegheController = fcontrollers.Controller.extend({
        ctor: function(){
            ImportaDelegheController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "import/deleghe",
                data: {}
            });



            var view = new views.ImportaDelegheAppView(service);


            return view;
        }

    });

    var ImportaAnagraficheController = fcontrollers.Controller.extend({
        ctor: function(){
            ImportaAnagraficheController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "import/anagrafiche",
                data: {}
            });



            var view = new views.ImportaAnagraficheAppView(service);


            return view;
        }
 
    });


    var ImportaQuoteController = fcontrollers.Controller.extend({
        ctor: function(){
            ImportaQuoteController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "import/quote",
                data: {}
            });



            var view = new views.ImportaQuoteAppView(service);


            return view;
        }

    });

    var ImportaDocumentiController = fcontrollers.Controller.extend({
        ctor: function(){
            ImportaDocumentiController.super.ctor.call(this);
        },
        index: function(params) {

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "import/documenti",
                data: {}
            });



            var view = new views.ImportaDocumentiAppView(service);


            return view;
        }

    });


    exports.ImportaDocumentiController = ImportaDocumentiController;
    exports.ImportaQuoteController = ImportaQuoteController;
    exports.ImportaDelegheController = ImportaDelegheController;
    exports.ImportaAnagraficheController = ImportaAnagraficheController;


    return exports;
});
