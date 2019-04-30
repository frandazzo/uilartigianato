/**
 * Created by angelo on 15/11/2017.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "trace/traceView"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};


    var TraceLoginController = fcontrollers.Controller.extend({
        ctor: function(){
            TraceLoginController.super.ctor.call(this);
        },
        index: function(params) {

            var url = BASE + "trace/logins";
            var service = new fmodel.AjaxService();
            service.set({
                url: url
            });
            return new views.TraceLoginRemoteView(service);
        }
    });



    exports.TraceLoginController = TraceLoginController;


    return exports;
});