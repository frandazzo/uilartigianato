/**
 * Created by david on 14/04/2016.
 */


define([
    "framework/core",
    "framework/model",
    "framework/widgets",
    "framework/plugins",
    "framework/webparts",
    "framework/ui"], function (core, model, widgets, plugins, webparts, ui) {


    var exports = {};


    var Matrice = core.AObject.extend({

        ctor: function (workerId) {
            Matrice.super.ctor.call(this);
            this.workerId = workerId;
        },
        initGridIscrizioni : function(responseData){



            var grid = $('#containerdettaglio').dxDataGrid({
                dataSource:responseData,
                columns:[
                    
                    { dataField:"nomeProvincia",  visible : true, visibleIndex: 0},
                    { dataField:"anno",  visible : true, visibleIndex: 1},
                    { dataField:"azienda", visible : true, visibleIndex: 2,
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var name = options.data.azienda;
                            var companyId = options.data.companyId;

                            if (!name)
                                return;
                            $("<a />")
                                .text(name)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    ui.Navigation.instance().navigate("summaryfirm", "remoteIndex", {
                                        companyId : companyId,
                                        description:encodeURIComponent(name.replace("&", "*_").replace("'", "~_"))
                                    });
                                })
                                .appendTo(container);
                        }},
                    { dataField:"settore",  visible : true, visibleIndex: 3},
                    { dataField:"contratto",  visible : true, visibleIndex: 4},
                    { dataField:"operatoreDelega",  visible : true, visibleIndex: 5},
                    { dataField:"piva",  visible : false},
                    { dataField:"livello",  visible : false},
                    { dataField:"quota",  visible : false}



                ],
                searchPanel: {
                    visible: false

                },
                summary: {
                    totalItems: [{
                        column: "categoria",
                        summaryType: "count",
                        customizeText: function(data) {
                            return "Iscrizioni trovate: " + data.value;
                        }
                    }]
                },
                columnChooser: {
                    enabled: true
                },
                // onCellClick: function (clickedCell) {
                //     alert(clickedCell.column.dataField);
                // },
                "export": {
                    enabled: true,
                    fileName: "iscrizioni",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "iscrizionilavoratore"
                // },
                paging:{
                    pageSize: 35
                },
                sorting:{
                    mode:"multiple"
                },
                onContentReady: function (e) {
                    var columnChooserView = e.component.getView("columnChooserView");
                    if (!columnChooserView._popupContainer) {
                        columnChooserView._initializePopupContainer();
                        columnChooserView.render();
                        columnChooserView._popupContainer.option("dragEnabled", false);
                    }
                },
                rowAlternationEnabled: true,
                showBorders: true,
                allowColumnReordering:true,
                allowColumnResizing:true,
                columnAutoWidth: true,
                selection:{
                    mode:"multiple",
                    showCheckBoxesMode: "always"
                },
                hoverStateEnabled: true



            }).dxDataGrid("instance");

            return grid;

        },

        init: function () {

            var self = this;
            self.initGraph();
            self.initGridData();


        },
        
        initGridData: function(){


            var self = this;

            var service = new model.AjaxService();
            service.set("url",BASE + "worker/" + self.workerId + "/iscrizionidetail");
            service.on("load",function(resp){

                if (resp.length == 0){
                    //visualizzo un messaggio che non è stata trovata una iscrizione
                    $('#containerdettaglio').append('' +
                        '<p class="color-red" style="text-align: center;padding-top: 12%;padding-bottom: 3%;">' +
                        '<i class="material-icons font-size-100">sentiment_dissatisfied</i>' +
                        '</p>' +
                        '<p class="text-center">Nessuna iscrizione trovata</p>')
                }else{
                    self.initGridIscrizioni(resp);
                }


            });
            service.on("error",function(e){
                $.notify.error(e);
            });
            service.load();
            
     
           
        },
        initGraph: function(){
            var self = this;

            var service = new model.AjaxService();
            service.set("url",BASE + "worker/" + self.workerId + "/iscrizionichart");
            service.on("load",function(resp){

                if (resp.anni.length == 0){
                    //visualizzo un messaggio che non è stata trovata una iscrizione
                    $('#containerMatrice').append('' +
                        '<p class="color-red" style="text-align: center;padding-top: 12%;padding-bottom: 3%;">' +
                        '<i class="material-icons font-size-100">sentiment_dissatisfied</i>' +
                        '</p>' +
                        '<p class="text-center">Nessuna iscrizione trovata</p>')
                }else{
                    self.initWidget(resp);
                }


            });
            service.on("error",function(e){
                $.notify.error(e);
            });
            service.load();
        },

        constructSeriesData : function(chartElements){

            var result = [];

            $.each(chartElements, function(index, element){
                var a = {};
                a.x = element.x;
                a.y = element.y;
                a.value = element.value;


                //adesso devo renderizzare la lista dei settori
                var settori = element.settori;
                if (settori.length == 0)
                    result.push(a);
                else{
                    var dataLabel = {
                        useHTML: true,
                        enabled: true,
                    };

                    var labelClass = "inps";
                    
                    $.each(settori, function(ind, settore){
                        if (!dataLabel.format)
                            dataLabel.format = '';
                        dataLabel.format = dataLabel.format   + '<span class="worker-chart-label-if">' + settore + '</span> </br>'
                    });

                      //una volta costruito il data label posso assegnarlo all'elemento
                    a.dataLabels = dataLabel;
                    result.push(a);
                }

            });


            return result;

        },
        arrayContains : function(array, value){
            var found = false;
            $.each(array, function(index, elem){
               if (elem == value){
                   found = true;
                   return false;
               }

            });

            return found;
        },
        constructDataAxes:function(provincesIds, provinces, loggedUserProvinceIds){
            var self = this;

            var result = [];

            var i = 0;
            $.each(provincesIds, function(index, element){
                var a = {};

                a.from = element;
                a.to = element;
                //se la provincia si trova tra quelle dell'utente loggate gli do il colre #EEEEEF
                //akltrimenti il colore white

                if (self.arrayContains(loggedUserProvinceIds, element)){
                    a.color = '#EEEEEF';
                }else{
                    a.color = 'white';
                }
                a.name = provinces[i];
                // {
                //     from:11,
                //         to: 11,
                //     color: '#EEEEEF',
                //     name: 'Matera'
                // }

                result.push(a);
                i++;
            });


            return result;
        },
        initWidget: function(response){
            var self = this;
            var otherData = response.workerName;

            //devo costruire le serie
            var data = self.constructSeriesData(response.chartElements);
            //rimangono da costruire i dataaxes
            var dataAxex = self.constructDataAxes(response.provincesIds, response.provinces, response.loggedUserProvinceIds);


            $('#containerMatrice').highcharts({

                chart: {
                    type: 'heatmap',
                    marginTop: 40,
                    marginBottom: 80,


                    backgroundColor: "#E3EDEE",
                    borderColor: "#B2E3FA",
                    borderRadius: 20,
                    borderWidth: 2,
                    plotBorderWidth: 3,
                    plotBorderColor: "#1A90D0",
                    plotShadow: true
                },


                title: {
                    text: otherData,
                    floating: true,
                    style: { "color": "#333333", "fontSize": "22px" },
                    useHTML:false
                },


                xAxis: {
                    //categories: ['1990', '1995', '2000', '2010'],
                    categories: response.anni,

                },

                yAxis: {
                    categories: response.provinces,
                    // alternateGridColor: '#FDFFD5'
                    title:{
                        text:""
                    },
                    lineColor: "blu",
                    lineWidth: 0

                },

                colorAxis: {
                    dataClassColor:"category",
                    dataClasses : dataAxex,
                    // dataClasses: [{
                    //     from:1,
                    //     to: 1,
                    //     color: 'white',
                    //     name: 'Roma'
                    // }, {
                    //     from:11,
                    //     to: 11,
                    //     color: '#EEEEEF',
                    //     name: 'Matera'
                    // },
                    //     {
                    //         from:21,
                    //         to: 221,
                    //         color: 'white',
                    //         name: 'Potenza'
                    //     }],
                    labels:{

                        enabled:true
                    }
                },



                tooltip: {
                    enabled:false
                },
                //series: data,
                series: [{
                    name: 'Sales per employee',
                    borderWidth: 1,
                    data: data,
                    // data: [
                    //     {x:0, y:0,  value:1,
                    //         dataLabels: {
                    //             //backgroundColor:'white',
                    //             //shape:"circle",
                    //             useHTML: true,
                    //             enabled: true,
                    //             format: '<span class="worker-chart-label-edile">Edile</span>',
                    //
                    //
                    //         }},
                    //     {x:0, y:1, value:11,
                    //         dataLabels: {
                    //             //backgroundColor:'red',
                    //             // shape:"diamond",
                    //             useHTML: true,
                    //             enabled: true,
                    //             format:  '<span class="worker-chart-label-edile">Edile</span>',
                    //
                    //
                    //         }},
                    //     {x:0, y:2, value:21,
                    //         dataLabels: {
                    //             useHTML: true,
                    //             enabled: true,
                    //             format:  '<span class="worker-chart-label-edile">Edile</span>',
                    //         }},
                    //
                    //     {x:1, y:0,  value:1,
                    //         dataLabels: {
                    //             useHTML: true,
                    //             enabled: true,
                    //             format: '<span class="worker-chart-label-if">Impianti fissi</span> </br>' +
                    //             '<span class="worker-chart-label-inps">Inps</span>',
                    //
                    //
                    //         }},
                    //     {x:1, y:1, value:11},
                    //     {x:1, y:2, value:21},
                    //     {x:2, y:0,  value:1},
                    //     {x:2, y:1, value:11},
                    //     {x:2, y:2, value:21,
                    //         dataLabels: {
                    //             useHTML: true,
                    //             enabled: true,
                    //             format: '<span class="worker-chart-label-inps">Inps</span>',
                    //
                    //
                    //         }}
                    // ],
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        formatter: function(){
                            return this.point.name;
                        }
                    }
                }]

            });
        }

    });



    var MatriceDeleghe = core.AObject.extend({

        ctor: function (workerId) {
            MatriceDeleghe.super.ctor.call(this);
            this.workerId = workerId;
        },
        initGridIscrizioni : function(responseData){



            var grid = $('#containerMatriceDeleghe').dxDataGrid({
                dataSource:responseData,
                columns:[
                    { dataField:"delegaDataDocumento", visible : true, visibleIndex: 4, dataType:'date'},
                    { dataField:"delegaDataInoltro", visible : false, dataType:'date'},
                    { dataField:"delegaDataAccettazione", visible : false, dataType:'date'},
                    { dataField:"delegaDataAttivazione", visible : false, dataType:'date'},
                    { dataField:"delegaDataAnnullamento", visible : false, dataType:'date'},
                    { dataField:"delegaDataRevoca", visible : false, dataType:'date'},
                    { dataField:"delegaOperatore",  visible : true, visibleIndex: 1},
                    { dataField:"delegaProvincia",  visible : true, visibleIndex: 6},
                    { dataField:"regione",  visible : false},
                    { dataField:"delegaBreviMano",  visible : false},

                    { dataField:"delegaSettore", visible : true, visibleIndex: 2},
                    { dataField:"delegaContract", visible : false, caption: "Contratto"},
                    { dataField:"delegaStato", visible : true, visibleIndex: 5,

                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var stato = options.data.delegaStato;

                            var badgeClass = "";
                            if (stato == "Accettata" || stato == 'Attivata')
                                badgeClass = "label-success";
                            else
                                badgeClass = "label-default";

                            $("<span class='label'/>")
                                .text(stato)
                                .addClass(badgeClass)
                                .appendTo(container);
                        }
                    },
                    { dataField:"delegaCollaboratore", visible : false},
                    { dataField:"delegaNote", visible : false},
                    { dataField:"delegaCausaleSottoscrizione", visible : false},
                    { dataField:"delegaCausaleRevoca", visible : false},
                    { dataField:"delegaCausaleAnnullamento", visible : false},
                    { dataField:"delegaId", visible : false,
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var id = options.data.delegaId;


                            $("<a />")
                                .text(id)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    ui.Navigation.instance().navigate("editdelega", "index", {
                                        fs: this.fullScreenForm,
                                        workerId : options.data.lavoratoreId,
                                        id: id
                                    });
                                })
                                .appendTo(container);
                        }
                    },

                    { dataField:"aziendaRagioneSociale", visible : false, visibleIndex: 6,

                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var name = options.data.aziendaRagioneSociale;


                            $("<a />")
                                .text(name)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    ui.Navigation.instance().navigate("summaryfirm", "index", {
                                        id: options.data.aziendaId
                                    });
                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"aziendaCitta", visible : false},
                    { dataField:"aziendaProvincia", visible : false},
                    { dataField:"aziendaCap", visible : false},
                    { dataField:"aziendaIndirizzo", visible : false},
                    { dataField:"aziendaNote", visible : false},
                    { dataField:"aziendaId", visible : false},

                    { dataField:"lavoratoreNomeCompleto", fixed :true, visible : true, visibleIndex: 0,
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            // var surname = options.data.lavoratoreCognome;
                            // var name = options.data.lavoratoreNome;
                            // var datanas =Globalize.format(new Date(options.data.lavoratoreDataNascita), "dd/MM/yyyy");
                            var completeName = options.data.lavoratoreNomeCompleto;//surname + " " + name + " (" + datanas + ")";
                            var fiscalCode = options.data.lavoratoreCodiceFiscale;
                            var companyId = options.data.companyId;
                            $("<a />")
                                .text(completeName)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    ui.Navigation.instance().navigate("summaryworker", "remoteIndex", {
                                        fiscalCode:fiscalCode,
                                        companyId : companyId
                                    });
                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"lavoratoreNome", visible : false},
                    { dataField:"lavoratoreCognome", visible : false},
                    { dataField:"lavoratoreSesso", visible : false},
                    { dataField:"lavoratoreCodiceFiscale", visible : false},
                    { dataField:"lavoratoreDataNascita", visible : false,dataType:'date',},
                    { dataField:"lavoratoreNazionalita", visible : false},
                    { dataField:"lavoratoreProvinciaNascita", visible : false},
                    { dataField:"lavoratoreLuogoNascita", visible : false},
                    { dataField:"lavoratoreProvinciaResidenza", visible : false},
                    { dataField:"lavoratoreCittaResidenza", visible : false},
                    { dataField:"lavoratoreIndirizzo", visible : false},
                    { dataField:"lavoratoreCap", visible : false},
                    { dataField:"lavoratoreTelefono", visible : false},
                    { dataField:"lavoratoreCellulare", visible : false},
                    { dataField:"lavoratorMail", visible : false},


                    { dataField:"lavoratoreAttribuzione1", visible : false},
                    { dataField:"lavoratoreAttribuzione2", visible : false},
                    { dataField:"lavoratoreAttribuzione3", visible : false, caption: 'Incarico' },

                    { dataField:"lavoratoreFondo", visible : false, caption: 'Lavoratore Attribuzione 3' },
                    { dataField:"lavoratoreNote", visible : false},
                    { dataField:"lavoratoreId", visible : false},





                ],
                searchPanel: {
                    visible: false

                },
                summary: {
                    totalItems: [{
                        column: "categoria",
                        summaryType: "count",
                        customizeText: function(data) {
                            return "Deleghe trovate: " + data.value;
                        }
                    }]
                },
                columnChooser: {
                    enabled: true
                },
                // onCellClick: function (clickedCell) {
                //     alert(clickedCell.column.dataField);
                // },
                "export": {
                    enabled: true,
                    fileName: "deleghe",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "iscrizionilavoratore"
                // },
                paging:{
                    pageSize: 35
                },
                sorting:{
                    mode:"multiple"
                },
                onContentReady: function (e) {
                    var columnChooserView = e.component.getView("columnChooserView");
                    if (!columnChooserView._popupContainer) {
                        columnChooserView._initializePopupContainer();
                        columnChooserView.render();
                        columnChooserView._popupContainer.option("dragEnabled", false);
                    }
                },
                rowAlternationEnabled: true,
                showBorders: true,
                allowColumnReordering:true,
                allowColumnResizing:true,
                columnAutoWidth: true,
                selection:{
                    mode:"multiple",
                    showCheckBoxesMode: "always"
                },
                hoverStateEnabled: true



            }).dxDataGrid("instance");

            return grid;

        },

        init: function () {

            var self = this;
            self.initGridData();


        },

        initGridData: function(){


            var self = this;

            var service = new model.AjaxService();
            service.set("url",BASE + "worker/" + self.workerId + "/deleghedetail");
            service.on("load",function(resp){

                if (resp.length == 0){
                    //visualizzo un messaggio che non è stata trovata una iscrizione
                    $('#containerMatriceDeleghe').append('' +
                        '<p class="color-red" style="text-align: center;padding-top: 12%;padding-bottom: 3%;">' +
                        '<i class="material-icons font-size-100">sentiment_dissatisfied</i>' +
                        '</p>' +
                        '<p class="text-center">Nessuna delega trovata</p>')
                }else{
                    self.initGridIscrizioni(resp);
                }


            });
            service.on("error",function(e){
                $.notify.error(e);
            });
            service.load();



        }







    });




    exports.matrice = Matrice;
    exports.matriceDeleghe = MatriceDeleghe;
    return exports;


});