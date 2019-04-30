/**
 * Created by fgran on 07/04/2016.
 */

define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "lavoratori/lavoratoriViews"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};

    


    //questo controller gestisce la visualizzazione di un riepilogo dati del lavoratore
    var LavoratoreSummaryController = fcontrollers.Controller.extend({
        ctor: function(){
            LavoratoreSummaryController.super.ctor.call(this);
        },
        index: function(params) {

            var workerId = params.id;

            var url = BASE + "worker/summary/"+ workerId ;
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.WorkerSummaryRemoteView(service, workerId);

        },
        remoteIndex: function(params) {

            var workerId = params.fiscalCode;
            var companyId = params.companyId;
            var url ="";

            url = BASE + "worker/remote/"+ workerId + "/" + companyId;


            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.WorkerSummaryRemoteView(service, null,workerId);

        },
        indexStoricoAggiornamenti: function(params) {

            var workerId = params.workerId;

            var url = BASE + "workerlastversionpage/"+ workerId ;
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.WorkerLastVersionRemoteView(service, workerId);

        },

    });

    //controllerv per la gestione della mascera di creazione o aggirnamento di un lavoratore
    var LavoratoreEditController = fcontrollers.Controller.extend({
        ctor: function(){
            LavoratoreEditController.super.ctor.call(this);
        },
        index: function(params) {

            //questo è l'id del lavoratore che nel caso sia valorizzato indica un aggiornamento
            var workerId = "";

            if (params)
                if (params.id)
                    workerId = params.id;

            var url = BASE + "worker";

            if (workerId)
                url = url + "/" + workerId;

            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: url,
                data: {}
            });



            var view = new views.EditWorkerAppView(service, workerId);
            // view.set("title", "Crea o modifica lavoratore");

            return view;
        }
    });


    //controller che porta alla mascherina custom per la ricerca dei lavoratori
    var LavoratoriSearchController = fcontrollers.Controller.extend({
        ctor: function(){
            LavoratoriSearchController.super.ctor.call(this);
        },
        index: function(params) {
            
            
            var workername = "";
            
            if (params)
                if (params.workername)
                    workername = params.workername;
            
            
            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "workers/search",
                data: {}
            });


            
            var view = new views.SearchWorkersAppView(service, workername);
           // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });



    //controller che porta alla mascherina custom per la ricerca dei lavoratori
    var LavoratoriSearchNazionaleController = fcontrollers.Controller.extend({
        ctor: function(){
            LavoratoriSearchNazionaleController.super.ctor.call(this);
        },
        index: function(params) {


            var workername = "";

            if (params)
                if (params.workername)
                    workername = params.workername;


            var service = new fmodel.FormService();
            service.set({
                method: "GET",
                url: BASE + "workers/search/nazionale",
                data: {}
            });



            var view = new views.SearchWorkersAppView(service, workername, true);
            // view.set("title", "Ricerca lavoratori");

            return view;
        }

    });
   
    
    exports.LavoratoreEditController = LavoratoreEditController;
    exports.LavoratoreSummaryController = LavoratoreSummaryController;
    exports.LavoratoriSearchController = LavoratoriSearchController;
    exports.LavoratoriSearchNazionaleController = LavoratoriSearchNazionaleController;

    return exports;
});
