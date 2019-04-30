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
            if (!this.anno){
                var annoDefault = $(".andamento-deleghe-container").find('input[name="hidden-year"]').val();
                this.anno = annoDefault;
            }

            if (!this.settore){
                var settoreDefault = $(".andamento-deleghe-container").find('input[name="hidden-settore"]').val();
                this.settore = settoreDefault;
            }

        },
        prepareWidget: function(){

            var self = this;




            var service = new model.AjaxService();

            service.set("url",BASE + "widget/real/andamentoDeleghe/getData");

            service.on("load",function(resp){


                var cat = "";
                if (!self.anno)
                    cat = resp.anni;
                else
                    cat = resp.anniLabels;

                var text = "";
                if (self.anno)
                    text = "(anno: " + self.anno;

                if (self.settore){
                    text = text + ", settore: " + self.settore + ")";
                }else{
                    text = text + ")";
                }

                $('.andamento-deleghe-container').find("#container-highcharts").highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Deleghe '
                    },
                    subtitle: {
                        text: text
                    },
                    xAxis: {
                        categories:  cat,
                        crosshair: true,
                        title: {
                            text: 'Anni'
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Numero deleghe'
                        }
                    },
                    credits:false,
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

                $('.andamento-deleghe-container').find("#container-highcharts").children().addClass("full-width-important");

            });
            service.on("error",function(e){
                alert(e);
            });
            service.load();



        },
        renderHtmlForm : function(editPanel, params){

             var settore;
             var year;
            //
            if (params.length == 0){
                year = new Date().getFullYear();
                settore = "";
            }else {
                //è sicuramente la provincia  -- (attenzione sto facendo il caso che il widget richieda entrambi i parametri)
                year = params[0].value;
                if (params.length == 2)
                    settore = params[1].value;
            }
            //
            //
            //
             var form = $("<form></form>");
            
            var yearSelect = $(".andamento-deleghe-container").find('div.param-fields-container').find('select[name="year"]').clone();
            var settoreSelect = $(".andamento-deleghe-container").find('div.param-fields-container').find('select[name="settore"]').clone();
            //recuperato il riferimento alla select ne imposto il valore
            yearSelect.val(year);
            settoreSelect.val(settore);
            //li aggiungo al form
            
            form.append(yearSelect);
            form.append(settoreSelect);
            //aggiungo il pulsante submit
            form.append("<input type='submit' value='Submit'>");


            editPanel.append(form);

        }




    });


    exports.andamento = Andamento;

    return exports;


});