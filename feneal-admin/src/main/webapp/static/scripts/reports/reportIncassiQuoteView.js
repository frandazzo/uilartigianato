/**
 * Created by fgran on 15/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils", "reports/reportResultsConfigurer"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils, resultsConfigurer) {

    var exports = {};

    var RepositoryServiceFactory = core.AObject.extend({
        ctor: function() {
            RepositoryServiceFactory.super.ctor.call(this);
        },
        searchIqa: function(searchParams){
            var route = BASE + "iqa/report" ;

            var svc =  this.__createService(true, route, searchParams);
            return svc;
        },
        __createService: function (isJsonContentType, route, data){

            //definisco il servizio
            var service = new  fmodel.AjaxService();

            //se sono dati json ne imposto il content type
            if (isJsonContentType)
                service.set("contentType", "application/json");
            else
                service.set("contentType", "application/x-www-form-urlencoded; charset=UTF-8");

            //se ci sono dati li trasformoi in stringa json
            //e li accodo al servizio
            if (data){
                if(typeof(data) == 'string'){

                    service.set("data", data);
                }
                else{
                    var stringified1 = JSON.stringify(data);
                    service.set("data", stringified1);
                }
            }
            service.set("url", route);
            service.set("method", "POST");
            return service;
        }



    });



    var ReportIncassiQuoteAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            ReportIncassiQuoteAppView.super.ctor.call(this, formService);
            this.geoUtils = new geoUtils.GeoUtils();
            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "580px");

                //qui attacco levento on change della select delle company
                $('select[name="company"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){

                        self.geoUtils.loadProvincesForCompany(selectedVal, "", $('select[name="province"]'), true);

                        //imposto il valore di default del searchble filed renderer
                        $('[data-property="firm"]').attr("data-prefilter-value", selectedVal);
                    }

                });

                var selectedCompany = $('select[name="company"]').val();
                self.geoUtils.loadProvincesForCompany(selectedCompany, "", $('select[name="province"]'), true);
                $('[data-property="firm"]').attr("data-prefilter-value", selectedCompany);







            });

            self.formView.on("submit", function(){

                var data = self.normalizeSubmitResult(self.formView.form);

                var factory = new RepositoryServiceFactory();
                var svc = factory.searchIqa(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    //inizializzo la griglia devexpress
                    var grid = self.initGrid(response);
                    //una volta ottenuti i risultati la griglia devexpress mostra una loader
                    //di attesa per la renderizzazione degli stessi! in quel momento rendo
                    //visibile l'intera area
                    //scrollando fino a rendere visibile la griglia
                    $('html, body').animate({scrollTop: $('#reportContainer').offset().top - 160}, 1400, "swing");

                    //configuro la navigabilità e la toolbar delle actions del report
                    var reportResultsConfigurer = new resultsConfigurer.ReportUiConfigurer(grid, "incassiQuote");
                    reportResultsConfigurer.init();



                    //solo il nazionale può effettuare pagamenti
                    if (window.appcontext.roleid == 5){
                        $('.request-info').remove();
                        //aggiungo un pulsante per l'invio di deleghe per il pagamento
                        var btn = $('<div class="col-md-12 col-xs-12 margin-bottom-10 p0 request-info" title="" data-placement="top" data-toggle="tooltip" data-original-title="Effettua bonifico">'+
                            '<button class="btn btn-primary full-width request-info-territori" type="button">'+
                            '<span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>'+
                            '</button>'+
                            '</div>');



                        var toolboxContainer = $('.toolbox-seach-report').find('.back-white').children("div");
                        var smallToolboxContainer = $('.toolbox-seach-report-xs').find('.back-white').children("div");
                        toolboxContainer.append(btn);
                        smallToolboxContainer.append(btn.clone());
                        btn.tooltip();

                        $('.request-info').click(function(){
                            //ottengo la lista delle righe selezionate
                            var selectedrows = grid.getSelectedRowsData();


                            if (selectedrows.length == 0) {
                                $.notify.error("Selezionare almeno un elemento");
                                return false;
                            }


                            var container = $('<div class="request-info-territori-container"></div>');

                            var data = {};
                            var selectedrows = grid.getSelectedRowsData();
                            data.quoteIds = selectedrows.map(function (officer) {
                                return officer.id
                            });
                            // data.province = provinceSelected;

                            var path = BASE + "iqa/bonifico/form";
                            var formService = new fmodel.FormService();
                            formService.set("url", path);
                            formService.set("method", "POST");

                            formService.set("contentType", "application/json");

                            var form = new fviews.FormView(formService);
                            form.container = container;

                            form.on("render", function () {
                                // //codice per rimuovoere il pulsante salva - annulla
                                container.find(".panel-footer, .panel-heading").hide();
                            });

                            form.show();

                            var dialog = container.modalDialog({
                                autoOpen: true,
                                title: "Esegui Bonifico",
                                destroyOnClose: true,
                                height: 500,
                                width: 800,
                                buttons: {
                                    Salva: {
                                        primary: true,
                                        command: function() {


                                            // Validazione e-mail
                                            var mails = $("input[name=note]").val();

                                            if (!mails)
                                            {
                                                alert("Inserire delle note per il bonifico");
                                                return;
                                            }
                                            data.note = mails;

                                            // Se la validazione è OK invio i dati per la mail al server
                                            var svc = new  fmodel.AjaxService();

                                            svc.set("url", BASE + "iqa/bonifico");
                                            svc.set("contentType", "application/json");
                                            svc.set("data", JSON.stringify(data));
                                            svc.set("method", "POST");

                                            svc.on("load", function(response){
                                                $.loader.hide({parent:'body'});
                                                dialog.modalDialog("close");
                                                if (response = "OK")
                                                    $.notify.success("Il pagamento è stato inoltrato con successo")
                                                else
                                                    alert(response);
                                            });
                                            svc.on("error", function(error){
                                                $.loader.hide({parent:'body'});
                                                $.notify.error(error);
                                            });

                                            svc.load();
                                            $(dialog).modalDialog("close");
                                            $.loader.show({parent:'body'});
                                        }
                                    }
                                }
                            });


                        });
                    }




                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    alert("Errore: "  + error);
                });

                svc.load();
                $.loader.show({parent:'body'});



            });

            self.formView.form.on("cancel", function() {
                self.close();
            });



        },
        initGrid : function(responseData){



            var grid = $('#reportContainer').dxDataGrid({
                dataSource:responseData,
                columns:[
                    // { dataField:"otherfiled", caption:"AAAA", visible : true, calculateCellValue:function(data){
                    //     return data.delegaSettore;
                    // }},

                    { dataField:"dataRegistrazione", visible : true, visibleIndex: 3, dataType:'date'},

                    { dataField:"dataDocumento", visible : true, visibleIndex: 8, dataType:'date'},
                    { dataField:"dataInizio", visible : true, visibleIndex: 4, dataType:'date'},
                    { dataField:"dataFine", visible : true, visibleIndex: 5, dataType:'date'},
                    { dataField:"quota", visible : true, visibleIndex: 6},
                    { dataField:"contratto", visible : false},



                    { dataField:"dataBonifico", visible : true, visibleIndex: 9, dataType:'date'},
                    { dataField:"bonificoId", visible : true, visibleIndex: 8},
                    { dataField:"noteBonifico", visible :  true, visibleIndex: 10},






                    { dataField:"tipoDocumento",visible : false},
                    { dataField:"livello", visible : false},
                    { dataField:"provincia",  visible : true, visibleIndex: 1},
                    { dataField:"regione",  visible : true, visibleIndex: 0},

                    { dataField:"settore", visible : true, visibleIndex: 7},
                    { dataField:"operatoreDelega", visible : false},
                    { dataField:"idQuota", visible : false,
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var id = options.data.idQuota;


                            $("<a />")
                                .text(id)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    alert('naviga quota con id: '  + id);
                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"lavoratoreNome", visible : false},
                    { dataField:"lavoratoreCognome", visible : false},
                    { dataField:"lavoratoreCodiceFiscale", visible : false},
                    { dataField:"aziendaRagioneSociale", visible : true, visibleIndex: 7,

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

                    { dataField:"lavoratoreNomeCompleto", fixed :true, visible : true, visibleIndex: 0,
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var fiscalCode = options.data.lavoratoreCodiceFiscale;
                            var companyId = options.data.companyId;

                            $("<a />")
                                .text(options.data.lavoratoreNomeCompleto)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    ui.Navigation.instance().navigate("summaryworker", "remoteIndex", {
                                        fiscalCode:fiscalCode,
                                        companyId:companyId
                                    });
                                })
                                .appendTo(container);
                        }

                    }
                ],
                // searchPanel: {
                //     visible: true
                //
                // },
                summary: {
                    totalItems: [{
                        column: "lavoratoreNomeCompleto",
                        summaryType: "count",
                        customizeText: function(data) {
                            return "Incassi quote trovati: " + data.value;
                        }
                    }]
                },
                // columnChooser: {
                //     enabled: true
                // },
                // onCellClick: function (clickedCell) {
                //     alert(clickedCell.column.dataField);
                // },
                "export": {
                    enabled: false,
                    fileName: "iqa",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "reportiqa"
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

                // masterDetail: {
                //     enabled: true,
                //     template: function(container, options) {
                //         var currentData = options.data;
                //         container.addClass("internal-grid-container");
                //         $("<div>").text(currentData.delegaSettore  + " Dettagli:").appendTo(container);
                //         $("<div>")
                //             .addClass("internal-grid")
                //             .dxDataGrid({
                //                 columnAutoWidth: true,
                //                 columns: [{
                //                     dataField: "id"
                //                 }, {
                //                     dataField: "description",
                //                     caption: "Description",
                //                     calculateCellValue: function(rowData) {
                //                         return rowData.description + "ciao ciao";
                //                     }
                //                 }],
                //                 dataSource: currentData.details
                //             }).appendTo(container);
                //     }
                // }

            }).dxDataGrid("instance");

            return grid;

        },
        normalizeSubmitResult: function(form){

            //metto tutto in un data array....
            var dataArray = [];
            var formData = form.serializeArray();

            for(var i=0; i<formData.length; i++) {
                dataArray.push({
                    property: formData[i].name,
                    value: formData[i].value
                });
            }

            //tiro fuori un oggetto javascript correttamente serializzato


            //devo ciclare tra tutti gli oggetti  e verificare se ci sono proprietà con lo stesso nome
            // che provvedero' ad inserire in un array
            //questo buffer conterrà il nome della proprietà e una lista che conterrà tutti gli oggetti con lo stesso nome di proprietà
            var propertyBuffer = {};

            //ciclo adesso sugli oggetti della load request
            for (var prop in dataArray){


                //se la proprietà non cè nel buffer la aggiungo creando una nuova lista a cui aggiungo il valore della proprietà stessa

                //prendo il nome della proprietà che farà da key nel buffer
                var propName =  dataArray[prop].property;
                if (!propertyBuffer[propName]){
                    propertyBuffer[propName] = [];
                    propertyBuffer[propName].push(dataArray[prop]);
                }else{
                    propertyBuffer[propName].push(dataArray[prop]);
                }




            }


            //adesso faccio l'inverso: ricostruisco l'oggetto a partire dal buffer
            var data = {};
            for(var propName in propertyBuffer){

                if (propertyBuffer[propName].length == 1) //se ce n'è solo una ne riprendo la property
                {
                    data[propName] =  propertyBuffer[propName][0].value;
                }else{
                    data[propName] = this.__constructArrayOfValues(propertyBuffer[propName]);

                }
            }
            return data;
        },
        submit: function(e){

        },
        close: function(){
            alert("close");
        },
        getBreadcrumbItems: function() {
            var self = this;
            return [
                {
                    pageTitle: window.appcontext.categoryName
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Report incassi quote"
                    //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
                }
            ];
        },
        getToolbarButtons: function() {
            var self = this;

            return [
                // {
                //     text: "Crea anagrafica",
                //     command: function() {
                //
                //         ui.Navigation.instance().navigate("editworker", "index", {
                //             fs: this.fullScreenForm
                //         });
                //     },
                //     icon: "pencil"
                // }

            ];

        },
        createToolbar: function() {
            var buttons = this.getToolbarButtons();

            var $t = $("#toolbar");
            if(!$t.toolbar("isToolbar")) {
                $t.toolbar();
            }

            $t.toolbar("clear");
            var size = buttons.length;
            for(var i = 0; i < size; i++) {
                var button = buttons[i];
                $t.toolbar("add", button);
            }
        },
        createBreadcrumbs: function() {
            var items = this.getBreadcrumbItems();

            var $b = $("#breadcrumbs");
            if(!$b.breadcrumbs("isBreadcrumbs")) {
                $b.breadcrumbs();
            }

            $b.breadcrumbs('clear');
            $b.breadcrumbs('addAll', items);
        }
    });


    exports.ReportIncassiQuoteAppView = ReportIncassiQuoteAppView;


    return exports;

});