/**
 * Created by fgran on 02/01/2018.
 */
define(["framework/model",
], function(fmodel) {

    var exports = {};


    exports.getPivotData = function() {

        var deferred = $.Deferred();

        var service = new  fmodel.AjaxService();

        service.set("url", BASE + "analisi/pivotdeleghe/data");
        service.set("method", "GET");

        service.on("load", function(response) {
            deferred.resolve(response);
        });
        service.on("error", function(error){
            deferred.reject(error);

        });

        service.load();

        return deferred.promise();


    };

    exports.getAnniIscrizioni = function() {

        var deferred = $.Deferred();

        var service = new  fmodel.AjaxService();

        service.set("url", BASE + "analisi/riepilogodeleghe/anni");
        service.set("method", "GET");

        service.on("load", function(response) {
            deferred.resolve(response);
        });
        service.on("error", function(error){
            deferred.reject(error);

        });

        service.load();

        return deferred.promise();


    };


    exports.getIscrittiPerAreaGeografica = function(anno, regione) {

        var deferred = $.Deferred();

        if (!anno){

            deferred.resolve({
                legenda: [],
                iscritti: []
            });

            return deferred.promise();
        }


        var service = new  fmodel.AjaxService();

        var url = BASE + "analisi/riepilogodeleghe/areageografica?year=" + anno;
        if (regione)
            url = url + "&region=" + encodeURIComponent(regione);
        else
            url = url + "&region=noregion";

        service.set("url", url);
        service.set("method", "GET");

        service.on("load", function(response) {
            deferred.resolve(response);
        });
        service.on("error", function(error){
            deferred.reject(error);

        });

        service.load();

        return deferred.promise();

    }


    exports.getIscrittiPerCategoria = function(anno, regione){
        var deferred = $.Deferred();

        if (!anno){

            deferred.resolve({
                legenda: [],
                iscritti: []
            });

            return deferred.promise();
        }


        var service = new  fmodel.AjaxService();

        var url = BASE + "analisi/riepilogodeleghe/categoria?year=" + anno;
        if (regione)
            url = url + "&region=" + encodeURIComponent(regione);
        else
            url = url + "&region=noregion";

        service.set("url", url);
        service.set("method", "GET");

        service.on("load", function(response) {
            deferred.resolve(response);
        });
        service.on("error", function(error){
            deferred.reject(error);

        });

        service.load();

        return deferred.promise();

    };

    return exports;

});