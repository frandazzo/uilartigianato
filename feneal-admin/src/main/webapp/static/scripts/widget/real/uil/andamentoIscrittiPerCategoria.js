/**
 * Created by fgran on 23/03/2017.
 */
define([
    "framework/core",
    "framework/model",
    "framework/widgets",
    "framework/plugins",
    "framework/webparts",
    "framework/ui", "widgetManager"], function (core, model, widgets, plugins, webparts, ui, widgetManager) {


    var exports = {};



    var Andamento = widgetManager.DashboardWidget.extend({
        ctor:function(widgetName, widgetParams){
            Andamento.super.ctor.call(this, widgetName, widgetParams);

        },
        prepareWidget: function(){

            var self = this;

            var service = new model.AjaxService();

            service.set("url",BASE + "widget/real/andamentoIscrittiPerCategoria/getData");

            service.on("load",function(resp){

                $('.andamento-iscritti-provincia-container').find("#container-highcharts").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Iscritti '
                    },
                    subtitle: {
                        text: '(per categoria)'
                    },
                    credits:false,
                    xAxis: {
                        categories: resp.anni,
                        crosshair: true,
                        title: {
                            text: 'Anni'
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Numero Iscritti'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y} iscritti</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0
                        }
                    },
                    series: resp.values
                });

                $('.andamento-iscritti-provincia-container').find("#container-highcharts").children().addClass("full-width-important");

            });
            service.on("error",function(e){
                alert(e);
            });
            service.load();



        },
        renderHtmlForm : function(editPanel, params){

        }




    });


    exports.andamento = Andamento;

    return exports;


});