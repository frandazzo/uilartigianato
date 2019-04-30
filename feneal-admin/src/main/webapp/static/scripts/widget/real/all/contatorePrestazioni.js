/**
 * Created by fgran on 23/03/2017.
 */
/**
 * Created by fgran on 22/03/2017.
 */
define([
    "framework/core",
    "framework/model",
    "framework/widgets",
    "framework/plugins",
    "framework/webparts",
    "framework/ui", "widgetManager"], function (core, model, widgets, plugins, webparts, ui, widgetManager) {


    var exports = {};


    var Contatore = widgetManager.DashboardWidget.extend({
        ctor:function(widgetName, widgetParams){
            Contatore.super.ctor.call(this, widgetName, widgetParams);


            if (!this.anno){
                var annoDefault = $(".contatore-prestazioni-container").find('input[name="hidden-year"]').val();
                this.anno = annoDefault;
            }

            if (!this.mese){
                var meseDefault = $(".contatore-prestazioni-container").find('input[name="hidden-month"]').val();
                this.mese = meseDefault;
            }


        },
        prepareWidget: function(){

            var self  =this;

            var service = new model.AjaxService();

            service.set("url",BASE + "widget/real/contatorePrestazioni/getData");

            service.on("load",function(resp){


                var dataValues = [];

                $.each(resp.iscrittiPerContesto, function(index, elem){
                    var datas = [];
                    var data = {};
                    data.name = elem.contesto;
                    data.data = elem.numIscritti;
                    //aggingo la coppia chiave valore che verrà renderizzata nella torta
                    datas = [''+data.name+'',data.data];
                    //aggiungo alla lista dei valori
                    dataValues.push(datas);


                });
                // var datas = [];
                //
                // var data = {};
                // data.name = "NASPI";
                // data.data = resp.iscrittiEdili;
                // //aggingo la coppia chiave valore che verrà renderizzata nella torta
                // datas = [''+data.name+'',data.data];
                // //aggiungo alla lista dei valori
                // dataValues.push(datas);
                //
                // data = {};
                // data.name = "DICHIARAZIONE";
                // data.data = resp.iscrittiImpiantiFissi;
                // //aggingo la coppia chiave valore che verrà renderizzata nella torta
                // datas = [''+data.name+'',data.data];
                // //aggiungo alla lista dei valori
                // dataValues.push(datas);
                //
                // data = {};
                // data.name = "INPDAP";
                // data.data = resp.iscrittiInps;
                // //aggingo la coppia chiave valore che verrà renderizzata nella torta
                // datas = [''+data.name+'',data.data];
                // //aggiungo alla lista dei valori
                // dataValues.push(datas);


                // Define chart color patterns
                var highColors = [bgWarning, bgPrimary, bgInfo, bgAlert,
                    bgDanger, bgSuccess, bgSystem, bgDark
                ];

                var pie1 = $('#high-pie-contatore-prestazioni');

                if (pie1.length) {

                    // Pie Chart1
                    $('#high-pie-contatore-prestazioni').highcharts({
                        chart:{
                            type:'pie',
                            height: 300,
                            plotBackgroundColor: null,
                            plotBorderWidth: null,
                            plotShadow: false
                        },
                        credits: false,
                        title: {
                            text: 'Prestazioni per tipo'
                        },
                        subtitle: {
                            text: '(anno ' + self.anno +  '; mese ' +  self.mese + ')'
                        },
                        tooltip: {
                            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                        },


                        // responsive: {
                        //     rules: [{
                        //         condition: {
                        //             maxWidth: $('#high-pie-contatore-iscritti').width(),
                        //             minWidth: 400,
                        //
                        //
                        //         },
                        //         chartOptions: {
                        //             // legend: {
                        //             //     align: 'center',
                        //             //     verticalAlign: 'bottom',
                        //             //     layout: 'horizontal'
                        //             // },
                        //             credits: {
                        //                 enabled: false
                        //             }
                        //         }
                        //     }]
                        // },


                        plotOptions: {
                            pie: {
                                //center: ['30%', '50%'],
                                allowPointSelect: true,
                                cursor: 'pointer',
                                dataLabels: {
                                    enabled: true
                                },
                                showInLegend: true
                            },
                            series: {
                                dataLabels: {
                                    enabled: true,
                                    formatter: function() {
                                        return this.point.name + " " +  Math.round(this.percentage*100)/100 + ' %';
                                    },
                                    // distance: -30,
                                    color:'black'
                                }
                            }
                        },
                        colors: highColors,
                        legend: {
                            x: 20,
                            floating: false,
                            verticalAlign: "bottom",
                            layout: "horizontal",
                            align: 'center',
                            itemMarginTop: 10,
                           // useHTML: true,
                            labelFormatter: function() {
                                return   this.name + ' ' +  this.y  ;
                            }
                        },
                        series: [{
                            type: 'pie',
                            name: 'Percentuale',
                            data: dataValues
                        }]
                    });
                }

                $('#high-pie-contatore-prestazioni').children().addClass("full-width-important");

            });
            service.on("error",function(e){
                alert(e);
            });
            service.load();

        },
        renderHtmlForm : function(editPanel, params){

            var year;

            if (params.length == 0){
                year = new Date().getFullYear();
            }else{
                year = params[0].value;

            }

            var year;
            var month;
            if (params.length == 0){
                year = new Date().getFullYear();
                 month = "";
            }else{
                month = params[0].value;
                year = params[1].value;

            }



            var form = $("<form></form>");

            var monthSelect = $(".contatore-prestazioni-container").find('div.param-fields-container').find('select[name="month"]').clone();

            if (month)
                monthSelect.val(month);
            else
                monthSelect.val(monthSelect.find('option').get(0).value);


            //stessa cosa per l'anno
            var yearSelect = $(".contatore-prestazioni-container").find('div.param-fields-container').find('select[name="year"]').clone();
            //recuperato il riferimento alla select ne imposto il valore
            yearSelect.val(year);
            //li aggiungo al form
            form.append(monthSelect);
            form.append(yearSelect);
            //aggiungo il pulsante submit
            form.append("<input type='submit' value='Submit'>");

            //finalmente aggiungo all'edit-panel
            editPanel.append(form);

        }




    });


    exports.contatore = Contatore;

    return exports;


});