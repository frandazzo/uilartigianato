/**
 * Created by fgran on 13/12/2017.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "analisi/analisiView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};




    //questo controller gestisce la visualizzazione di un riepilogo dati del lavoratore
    //ISCRITTI
    var RiepilogoController = fcontrollers.Controller.extend({
        ctor: function(){
            RiepilogoController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/riepilogo";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.RiepilogoRemoteView(service);

        }
    });
    var PivotController = fcontrollers.Controller.extend({
        ctor: function(){
            PivotController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/pivot";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.PivotRemoteView(service);

        }
    });

    //DELEGHE
    var PivotDelegheController = fcontrollers.Controller.extend({
        ctor: function(){
            PivotController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/pivotdeleghe";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.PivotDelegheRemoteView(service);

        }
    });
    var RiepilogoDelegheController = fcontrollers.Controller.extend({
        ctor: function(){
            RiepilogoController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/riepilogodeleghe";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.RiepilogoDelegheRemoteView(service);

        }
    });

    //PROV UNC
    var PivotProvUncController = fcontrollers.Controller.extend({
        ctor: function(){
            PivotProvUncController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/pivotprovunc";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.PivotProvUncRemoteView(service);

        }
    });
    var RiepilogoProvUncController = fcontrollers.Controller.extend({
        ctor: function(){
            RiepilogoProvUncController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/riepilogoUnc";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.RiepilogoProvUncRemoteView(service);

        }
    });


    //PROV BIL
    var PivotProvBilController = fcontrollers.Controller.extend({
        ctor: function(){
            PivotProvBilController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/pivotprovbil";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.PivotProvBilRemoteView(service);

        }
    });
    var RiepilogoProvBilController = fcontrollers.Controller.extend({
        ctor: function(){
            RiepilogoProvBilController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/riepilogoBil";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.RiepilogoPorvBilRemoteView(service);

        }
    });

    //TOTALE
    var PivotTotController = fcontrollers.Controller.extend({
        ctor: function(){
            PivotTotController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/pivottot";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.PivotTotRemoteView(service);

        }
    });
    var RiepilogoTotController = fcontrollers.Controller.extend({
        ctor: function(){
            RiepilogoTotController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/riepilogoTot";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.RiepilogoTotRemoteView(service);

        }
    });

    //RAPPRESENTANZA
    var PivotRappController = fcontrollers.Controller.extend({
        ctor: function(){
            PivotRappController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/pivotrap";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.PivotRapRemoteView(service);

        }
    });
    var RiepilogoRappController = fcontrollers.Controller.extend({
        ctor: function(){
            RiepilogoRappController.super.ctor.call(this);
        },
        index: function(params) {


            var url = BASE + "analisi/riepilogoRap";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.RiepilogoRapRemoteView(service);

        }
    });





    exports.RiepilogoController = RiepilogoController;
    exports.PivotController = PivotController;

    exports.PivotDelegheController = PivotDelegheController;
    exports.RiepilogoDelegheController = RiepilogoDelegheController;

    exports.PivotProvUncController = PivotProvUncController;
    exports.RiepilogoProvUncController = RiepilogoProvUncController;

    exports.PivotProvBilController = PivotProvBilController;
    exports.RiepilogoProvBilController = RiepilogoProvBilController;

    exports.PivotTotController = PivotTotController;
    exports.RiepilogoTotController = RiepilogoTotController;

    exports.PivotRappController = PivotRappController;
    exports.RiepilogoRappController = RiepilogoRappController;
    return exports;
});
