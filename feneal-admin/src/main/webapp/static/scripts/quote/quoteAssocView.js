/**
 * Created by angelo on 28/04/2016.
 */

define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils", "reports/reportResultsConfigurer"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils, resultsConfigurer) {

    var exports = {};


    // var QuoteAssocGridAppView = fviews.CrudGridAppView.extend({
    //     ctor: function(gridService){
    //         QuoteAssocGridAppView.super.ctor.call(this, gridService);
    //
    //         this.on("complete", function() {
    //             $("a[data-property=tipoDocumento]").click(function() {
    //
    //                 var quotaId = $(this).parents("tr").attr("data-entity-id");
    //
    //                 ui.Navigation.instance().navigate("dettaglioquote", "index", {
    //                     fs: this.fullScreenForm,
    //                     id: quotaId
    //                 });
    //             });
    //
    //                 $("a[data-property=importedLogFilePath]").click(function() {
    //
    //                     var quotaId = $(this).parents("tr").attr("data-entity-id");
    //
    //                     location.href = BASE + "quoteassociative/" + quotaId + "/downloadlog";
    //                 });
    //
    //
    //             $("a[data-property=originalFileServerName]").click(function() {
    //
    //                 var quotaId = $(this).parents("tr").attr("data-entity-id");
    //
    //                 location.href = BASE + "quoteassociative/" + quotaId + "/downloadoriginal";
    //             });
    //
    //             $("a[data-property=xmlFileServerName]").click(function() {
    //
    //                 var quotaId = $(this).parents("tr").attr("data-entity-id");
    //
    //                 location.href = BASE + "quoteassociative/" + quotaId + "/downloadxml";
    //             });
    //
    //         });
    //     },
    //
    //     edit: function(id) {
    //         return false;
    //     },
    //
    //     getToolbarButtons: function() {
    //         return [];
    //     },
    //
    //     getBreadcrumbItems: function() {
    //         return [
    //             {
    //                 pageTitle: "_APPNAME_"
    //             },
    //             {
    //                 icon: "glyphicon glyphicon-home",
    //                 href: BASE
    //             },
    //             {
    //                 label: msg.CRUD_BREADCRUMB_GRID
    //             }
    //         ];
    //     }
    //
    // });

    var SearchQuoteAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            SearchQuoteAppView.super.ctor.call(this, formService);

            var self = this;




            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                //inserisco il result container
                var cont = $('<div class="col-md-12">  </div>');
                cont.append('<div id="resultsContainer"> </div>');

                $('div.panel.panel-dark').after(cont);




            });

        },

        searchQuote: function(params) {
            var self = this;

            var searchWorkerUrl =  "quote";

            var svc = new fmodel.AjaxService();
            svc.url = BASE + searchWorkerUrl;
            svc.set("method", "GET");
            svc.set("data", params);

            svc.on({
                load: function(response){
                    $.loader.hide({parent:'body'});

                    self.initGrid(response);

                },
                error: function (error){
                    $.notify.error(error);
                }
            });
            svc.load();
            $.loader.show({parent:'body'});

        },
        //inizializzazione della griglia dei risultati
        initGrid: function(responseData){
            var self = this;


            //instanzio la griglia dei risultati locali...)
            $('#resultsContainer').dxDataGrid({
                dataSource:responseData,
                columns:[

                    { dataField:"id", visible : true,
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var name = options.data.id;

                            $("<a />")
                                .text(name)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    ui.Navigation.instance().navigate("dettaglioquote", "index", {
                                        fs: this.fullScreenForm,
                                        id: options.data.id
                                    });
                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"provincia", visible : true, caption:"Provincia" },
                    { dataField:"dataRegistrazione", visible : true, dataType:'date', caption:"Data registrazione"},
                    { dataField:"dataDocumento", visible : false, dataType:'date', caption:"Data documento"},
                    { dataField:"azienda", visible : true, caption:"Azienda"},
                    { dataField:"competenza", visible : true, caption:"Competenza"},
                    { dataField:"totalAmount", visible : true, caption:"Importo"},
                    { dataField:"importedLogFilePath", visible : true, caption:"Log importazione",
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var name = options.data.importedLogFilePath;

                            $("<a />")
                                .text(name)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    location.href = BASE + "quoteassociative/" + options.data.id + "/downloadlog";
                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"originalFileServerName", visible : true, caption:"File importato",
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var name = options.data.originalFileServerName;

                            $("<a />")
                                .text(name)
                                .attr("href", "javascript:;")
                                .on('click', function(){
                                    location.href = BASE + "quoteassociative/" + options.data.id + "/downloadoriginal";
                                })
                                .appendTo(container);
                        }
                    },
                    // { dataField:"xmlFileServerName", visible : true, caption:"File xml normalizzato",
                    //     cellTemplate: function (container, options) {
                    //         //container.addClass("img-container");
                    //         var name = options.data.xmlFileServerName;
                    //
                    //         $("<a />")
                    //             .text(name)
                    //             .attr("href", "javascript:;")
                    //             .on('click', function(){
                    //                 location.href = BASE + "quoteassociative/" + options.data.id + "/downloadxml";
                    //             })
                    //             .appendTo(container);
                    //     }
                    // }

                ],
                summary: {
                    totalItems: [{
                        column: "id",
                        summaryType: "count",
                        customizeText: function(data) {
                            return "Elementi trovati: " + data.value;
                        }
                    }]
                },
                paging:{
                    pageSize: 35
                },
                sorting:{
                    mode:"multiple"
                },
                rowAlternationEnabled: true,
                showBorders: true,
                allowColumnReordering:true,
                allowColumnResizing:true,
                columnAutoWidth: true,
                selection:{
                    mode:"none"
                },
                groupPanel: {
                    visible: true   // or "auto"
                },
                headerFilter: {
                    visible: true   // or "auto"
                },
                searchPanel: {
                    visible: true ,  // or "auto"
                    placeholder: "Cerca"
                },
                hoverStateEnabled: true

            }).dxDataGrid("instance");



        },
        // Siccome il form di ricerca è costituito da due tab, vado a considerare solo i campi visibili dal tab selezionato
        serializeSearchForm: function() {
            var self = this;
            var properties = self.form.serializeArray();

            //tolgo dalla lista delle proprietà tutte quelle che non sono visibili
            //(le filtro con la funzione grep di jquery
            $.each(self.form.element.find("[data-component=field]"), function(i, o) {
                if (!$(o).is(':visible')) {
                    var property = $(o).data("property");
                    //dammi tutte le prorietà che non si chiamano come la propriatà non visibile...
                    properties = $.grep(properties, function(e) {
                        return e.name != property;
                    });
                }
            });

            return properties;
        },

        submit: function(e){
            var self = this;


            $(".tbody-workers").html("");

            self.searchQuote(self.serializeSearchForm());
        },
        close: function(){
            //alert("close");
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
                    label: "Ricerca quote associative"

                }
            ];
        },
        getToolbarButtons: function() {
            var self = this;
            return [];

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


    var QuotaDettaglioRemoteView = fviews.RemoteContentView.extend({
        ctor: function(service, quotaId){
            QuotaDettaglioRemoteView.super.ctor.call(this, service);

            var self = this;
            self.quotaId = quotaId;
            self.quoteDetails = null;
            self.companyId = null;
            self.gridData = null;

            self.on("load", function(){

                //qui inserisco tutto il codice di inizializzazione della vista
                self.createToolbar();
                self.createBreadcrumbs();


                $.loader.hide({parent:'body'});

                //inizializzo la griglia devexpress
                var grid = self.initGrid(self.quoteDetails);
                //una volta ottenuti i risultati la griglia devexpress mostra una loader
                //di attesa per la renderizzazione degli stessi! in quel momento rendo
                //visibile l'intera area
                //scrollando fino a rendere visibile la griglia
                $('html, body').animate({scrollTop: $('#reportContainer').offset().top - 160}, 1400, "swing");

                //configuro la navigabilità e la toolbar delle actions del report
                var reportResultsConfigurer = new resultsConfigurer.ReportUiConfigurer(grid, "quoteAssociative");
                reportResultsConfigurer.init();


                //$.loader.show({parent:'body'});


            });

        },

        initGrid : function(responseData){

            var self = this;

            function moveEditColumnToLeft(dataGrid) {
                dataGrid.columnOption("command:edit", {
                    visibleIndex: -1,
                    width: 80
                });
            }

            this.gridData = responseData;

            var grid = null;

            //solo se sono un nazionale posso modificare
            if (window.appcontext.roleid == "5"){
                grid = $('#reportContainer').dxDataGrid({
                    dataSource:responseData,
                    columns:[
                        { allowEditing:false, dataField:"lavoratoreNomeCompleto", visible : true, caption:"Lavoratore",

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
                                            companyId: companyId
                                        });
                                    })
                                    .appendTo(container);
                            },
                            calculateCellValue: function (data) {
                                return data.lavoratoreNomeCompleto;
                            }


                        },
                        { allowEditing:false, dataField:"aziendaRagioneSociale", visible : true, caption:"Azienda",
                            cellTemplate: function (container, options) {
                                //container.addClass("img-container");
                                var name = options.data.aziendaRagioneSociale;
                                var companyId = options.data.companyId;
                                $("<a />")
                                    .text(name)
                                    .attr("href", "javascript:;")
                                    .on('click', function(){
                                        ui.Navigation.instance().navigate("summaryfirm", "index", {
                                            id: options.data.aziendaId,
                                            companyId: companyId
                                        });
                                    })
                                    .appendTo(container);
                            }

                        },
                        { allowEditing:false, dataField:"ripresaDati", visible : false},
                        { allowEditing:false, dataField:"id", visible : false},
                        { allowEditing:false, dataField:"regione", visible : true, caption:"Regione"},
                        { allowEditing:false, dataField:"provincia", visible : true, caption:"Provincia"},
                        { allowEditing:false, dataField:"dataRegistrazione", visible : true, dataType:'date', caption:"Data registrazione"},
                        { allowEditing:false, dataField:"dataDocumento", visible : false, dataType:'date', caption:"Data documento"},
                        { allowEditing:false, dataField:"tipoDocumento", visible : false,  caption:"Tipo documento"},
                        { allowEditing:false, dataField:"settore", visible : true, caption:"Settore"},
                        { allowEditing:false, dataField:"operatoreDelega", visible : true, caption:"Operatore delega"},
                        { allowEditing:true, dataField:"dataInizio", visible : true, dataType:'date', caption:"Data inizio"},
                        { allowEditing:true, dataField:"dataFine", visible : true, dataType:'date', caption:"Data fine"},
                        { allowEditing:false, dataField:"contratto", visible : true, caption:"Contratto"},
                        { allowEditing:true, dataField:"quota", visible : true, caption:"Quota"},
                        { allowEditing:true, dataField:"livello", visible : true, caption:"Livello"},
                        { allowEditing:true, dataField:"note", visible : true, caption:"Note"}
                    ],
                    "export": {
                        enabled: false,
                        fileName: "dettaglio_quote",
                        allowExportSelectedData: true
                    },
                    // stateStoring: {
                    //     enabled: true,
                    //     type: "localStorage",
                    //     storageKey: "dettaglioquote"
                    // },
                    paging:{
                        pageSize: 35
                    },
                    sorting:{
                        mode:"multiple"
                    },
                    groupPanel: {
                        visible: true   // or "auto"
                    },
                    headerFilter: {
                        visible: true   // or "auto"
                    },
                    searchPanel: {
                        visible: true ,  // or "auto"
                        placeholder: "Cerca"
                    },
                    onRowUpdated: function(e) {
                        console.log(e);
                        //alert("RowUpdated");
                    },
                    onRowUpdating: function(e) {
                        console.log(e);


                        //alert("RowRemoving");
                        var id = e.key;
                        var updatedData = e.newData;

                        var svc = new fmodel.AjaxService();
                        svc.url = BASE + "quoteitem/update/" + self.quotaId + "/" + id ;
                        svc.set("method", "POST");
                        svc.set("contentType", "application/json");
                        svc.set("data", JSON.stringify(updatedData));
                        svc.on({
                            load: function(response){


                                $.notify.success("Complete");


                                //posso aggiungere la riga alla griglia

                            },
                            error: function (error){
                                e.cancel = true;
                                $.notify.error(error);
                            }
                        });
                        svc.load();



                        //alert("RowUpdating");
                    },
                    onRowRemoved: function(e) {
                        console.log(e);
                        //alert("RowRemoved");

                    },
                    onRowRemoving: function(e) {
                        console.log(e);
                        //alert("RowRemoving");
                        var id = e.key;

                        var svc = new fmodel.AjaxService();
                        svc.url = BASE + "quoteitem/delete/" + self.quotaId + "/" + id ;
                        svc.set("method", "DELETE");
                        svc.set("contentType", "application/json");
                        svc.on({
                            load: function(response){


                                $.notify.success("Complete");


                                //posso aggiungere la riga alla griglia

                            },
                            error: function (error){
                                e.cancel = true;
                                $.notify.error(error);
                            }
                        });
                        svc.load();





                    },
                    keyExpr: "id",
                    onContentReady: function (e) {
                        var columnChooserView = e.component.getView("columnChooserView");
                        if (!columnChooserView._popupContainer) {
                            columnChooserView._initializePopupContainer();
                            columnChooserView.render();
                            columnChooserView._popupContainer.option("dragEnabled", false);
                        }


                        moveEditColumnToLeft(e.component);
                    },
                    onCellPrepared: function(e) {
                        if(e.rowType === "data" && e.column.command === "edit") {
                            var isEditing = e.row.isEditing,
                                $links = e.cellElement.find(".dx-link");

                            $links.text("");

                            if(isEditing){
                                $links.filter(".dx-link-save").addClass("dx-icon-save");
                                $links.filter(".dx-link-cancel").addClass("dx-icon-revert");
                            } else {
                                $links.filter(".dx-link-edit").addClass("dx-icon-edit");
                                $links.filter(".dx-link-delete").addClass("dx-icon-trash");
                            }
                        }
                    },
                    summary: {
                        totalItems: [{
                            column: "lavoratoreNomeCompleto",
                            summaryType: "count",
                            customizeText: function(data) {
                                return "Elementi trovati: " + data.value;
                            }
                        }]
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
                    hoverStateEnabled: true,
                    editing: {
                        mode: "row",
                        allowUpdating: true,
                        allowDeleting: true,
                        allowAdding: false,
                        texts:{
                            confirmDeleteMessage: 'Sicuro di voler elimnare la riga corrente?'
                        }
                    },

                }).dxDataGrid("instance");

            }else{
                grid = $('#reportContainer').dxDataGrid({
                    dataSource:responseData,
                    columns:[
                        { allowEditing:false, dataField:"lavoratoreNomeCompleto", visible : true, caption:"Lavoratore",

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
                                            companyId: companyId
                                        });
                                    })
                                    .appendTo(container);
                            },
                            calculateCellValue: function (data) {
                                return data.lavoratoreNomeCompleto;
                            }


                        },
                        { allowEditing:false, dataField:"aziendaRagioneSociale", visible : true, caption:"Azienda",
                            cellTemplate: function (container, options) {
                                //container.addClass("img-container");
                                var name = options.data.aziendaRagioneSociale;
                                var companyId = options.data.companyId;
                                $("<a />")
                                    .text(name)
                                    .attr("href", "javascript:;")
                                    .on('click', function(){
                                        ui.Navigation.instance().navigate("summaryfirm", "index", {
                                            id: options.data.aziendaId,
                                            companyId: companyId
                                        });
                                    })
                                    .appendTo(container);
                            }

                        },
                        { allowEditing:false, dataField:"ripresaDati", visible : false},
                        { allowEditing:false, dataField:"id", visible : false},
                        { allowEditing:false, dataField:"regione", visible : true, caption:"Regione"},
                        { allowEditing:false, dataField:"provincia", visible : true, caption:"Provincia"},
                        { allowEditing:false, dataField:"dataRegistrazione", visible : true, dataType:'date', caption:"Data registrazione"},
                        { allowEditing:false, dataField:"dataDocumento", visible : false, dataType:'date', caption:"Data documento"},
                        { allowEditing:false, dataField:"tipoDocumento", visible : false,  caption:"Tipo documento"},
                        { allowEditing:false, dataField:"settore", visible : true, caption:"Settore"},
                        { allowEditing:false, dataField:"operatoreDelega", visible : true, caption:"Operatore delega"},
                        { allowEditing:false, dataField:"dataInizio", visible : true, dataType:'date', caption:"Data inizio"},
                        { allowEditing:false, dataField:"dataFine", visible : true, dataType:'date', caption:"Data fine"},
                        { allowEditing:false, dataField:"contratto", visible : true, caption:"Contratto"},
                        { allowEditing:false, dataField:"quota", visible : true, caption:"Quota"},
                        { allowEditing:false, dataField:"livello", visible : true, caption:"Livello"},
                        { allowEditing:false, dataField:"note", visible : true, caption:"Note"}
                    ],
                    "export": {
                        enabled: false,
                        fileName: "dettaglio_quote",
                        allowExportSelectedData: true
                    },
                    paging:{
                        pageSize: 35
                    },
                    sorting:{
                        mode:"multiple"
                    },
                    groupPanel: {
                        visible: true   // or "auto"
                    },
                    headerFilter: {
                        visible: true   // or "auto"
                    },
                    searchPanel: {
                        visible: true ,  // or "auto"
                        placeholder: "Cerca"
                    },
                    onContentReady: function (e) {
                        var columnChooserView = e.component.getView("columnChooserView");
                        if (!columnChooserView._popupContainer) {
                            columnChooserView._initializePopupContainer();
                            columnChooserView.render();
                            columnChooserView._popupContainer.option("dragEnabled", false);
                        }
                    },
                    summary: {
                        totalItems: [{
                            column: "lavoratoreNomeCompleto",
                            summaryType: "count",
                            customizeText: function(data) {
                                return "Elementi trovati: " + data.value;
                            }
                        }]
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

            }




            return grid;

        },

        onServiceLoad: function(quoteDetailsViewResponse) {
            var self = this;
            self.quoteDetails = quoteDetailsViewResponse.quoteDetails;
            self.companyId = quoteDetailsViewResponse.quotaCompanyId;


            $.loader.hide({ parent: this.container });
            this.content = _E("div").html(quoteDetailsViewResponse.content);
            this.container.empty().append(this.content);
            this.invoke("load");
        },
        duplicateQuotaForm : function() {
            var self = this;
            var formService = new fmodel.FormService();
            formService.set("method", "GET");
            formService.set("data", {});
            formService.set("url", BASE + "duplicatequoteform");

            var container = $('<div class="duplica-container"></div>');

            var formView = new fviews.FormView(formService);
            formView.container = container;

            formView.on("render", function() {
                $(".duplica-container").find(".panel-footer, .panel-heading").hide();
                $(".panel-body").css("overflow", "hidden");
                $(".duplica-container").find("div[data-property=description] .field-label").css("line-height", "20px");
            });



            return formView;
        },
        createAddNewItemForm : function() {
            var self = this;
            var formService = new fmodel.FormService();
            formService.set("method", "GET");
            formService.set("data", {});
            formService.set("url", BASE + "quoteitem/" + self.quotaId);

            var container = $('<div class="quote-item-container"></div>');

            var formView = new fviews.FormView(formService);
            formView.container = container;

            formView.on("render", function() {
                $(".quote-item-container").find(".panel-footer, .panel-heading").hide();
                $(".panel-body").css("overflow", "scroll");
                $(".panel-default .panel-body").css("overflow", "hidden");
                $(".quote-item-container").find("div[data-property=description] .field-label").css("line-height", "20px");


                //imposto il valore di default per la company in entrambe le
                //singlesearchble renderers
                $('[data-property="firm"]').attr("data-prefilter-value",self.companyId );
                $('[data-property="worker"]').attr("data-prefilter-value",self.companyId );



            });



            return formView;
        },

        modifyCompetenzaQuotaForm : function() {
            var self = this;
            var formService = new fmodel.FormService();
            formService.set("method", "GET");
            formService.set("data", {});
            formService.set("url", BASE + "quoteassociativeform");

            var container = $('<div class="modify-container"></div>');

            var formView = new fviews.FormView(formService);
            formView.container = container;

            formView.on("render", function() {
                $(".modify-container").find(".panel-footer, .panel-heading").hide();
                $(".panel-body").css("overflow", "hidden");
                $(".modify-container").find("div[data-property=description] .field-label").css("line-height", "20px");
            });



            return formView;
        },

        
        validateDateForm: function(data){
            var result = {};
            result.errors = [];


            var dateRegExp = /^\d{2}[/]\d{2}[/]\d{4}$/
            if (!data.dataInizio.match(dateRegExp)){
               result.errors.push(
                    {
                        property: "dataInizio",
                        message: "Data inizio mancante"
                    }
                );
            }




            if (!data.dataFine.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataFine",
                        message: "Data fine mancante"
                    }
                );
            }

        return result;
    },
        
        validateForm: function(data){
            var result = {};
            result.errors = [];

           

            if (data.worker == ""){
                result.errors.push(
                    {
                        property: "worker",
                        message: "Lavoratore mancante"
                    }
                );
            }
            

               

            if (data.firm == ""){
                result.errors.push(
                    {
                        property: "firm",
                        message: "Azienda mancante"
                    }
                );
            }

            var dateRegExp = /^\d{2}[/]\d{2}[/]\d{4}$/
            if (!data.dataInizio.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataInizio",
                        message: "Data inizio mancante"
                    }
                );
            }




            if (!data.dataFine.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataFine",
                        message: "Data fine mancante"
                    }
                );
            }






            return result;
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
                    propertyBuffer[propName] = new Array();
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
        createToolbar: function() {
            var self = this;
            var button = {
                text: "Elimina quota",
                command: function() {

                    var dialog = $("<p>Sicuro di voler eliminare la quota?</p>").modalDialog({
                        autoOpen: true,
                        title: "Elimina",
                        destroyOnClose: true,
                        height: 100,
                        width:  400,
                        buttons: {
                            send: {
                                label: "OK",
                                primary: true,
                                command: function () {

                                    var svc = new fmodel.AjaxService();
                                    svc.url = BASE + "quoteassociative/delete/" + self.quotaId;
                                    svc.set("method", "DELETE");
                                    svc.on({
                                        load: function(response){

                                            $(dialog).modalDialog("close");
                                            $.notify.success("Operazione completata");

                                            ui.Navigation.instance().navigate("quoteassociative", "index", {
                                                fs: this.fullScreenForm
                                            });
                                        },
                                        error: function (error){
                                            $.notify.error(error);
                                        }
                                    });
                                    svc.load();

                                }

                            }
                        }
                    });
                },
                icon: "a glyphicons glyphicons-delete"
            };

            var button1 = {
                text: "Aggiungi nuova posizione",
                command: function() {

                    var formView = self.createAddNewItemForm();
                    formView.show();

                  

                    var dialog = formView.container.modalDialog({
                        autoOpen: true,
                        title: "Aggiungi item",
                        destroyOnClose: true,
                        height: 550,
                        width: 600,
                        buttons: {
                            OK: {
                                primary: true,
                                command: function() {

                                    var data = self.normalizeSubmitResult(formView.form);

                                    //verifico se mancano dati obbligatori
                                    formView.form.resetValidation();

                                    var errors = self.validateForm(data);
                                    if (errors.errors.length){
                                        formView.form.handleValidationErrors(errors);
                                        return;
                                    }


                                    var svc = new fmodel.AjaxService();
                                    svc.url = BASE + "quoteitem/add/" + self.quotaId ;
                                    svc.set("method", "POST");
                                    svc.set("contentType", "application/json");
                                    svc.set("data", JSON.stringify(data));
                                    svc.on({
                                        load: function(response){

                                            self.gridData.splice(0, 0, response);
                                            $('#reportContainer').dxDataGrid('instance').refresh();
                                            $(dialog).modalDialog("close");
                                            $.notify.success("Complete");


                                           //posso aggiungere la riga alla griglia

                                        },
                                        error: function (error){
                                            $.notify.error(error);
                                        }
                                    });
                                    svc.load();
                                }
                            }
                        }
                    });



                },
                icon: "a glyphicon glyphicon-plus"
            };


            var button2 = {
                text: "Duplica quota",
                command: function() {

                    var formView1 = self.duplicateQuotaForm();
                    formView1.show();

                    var dialog = formView1.container.modalDialog({
                        autoOpen: true,
                        title: "Duplica quota",
                        destroyOnClose: true,
                        height: 230,
                        width: 580,
                        buttons: {
                            OK: {
                                primary: true,
                                command: function() {

                                     var data = self.normalizeSubmitResult(formView1.form);
                                    //
                                    // //verifico se mancano dati obbligatori
                                     formView1.form.resetValidation();
                                    //
                                    //
                                    var errors = self.validateDateForm(data);
                                    if (errors.errors.length){
                                        formView1.form.handleValidationErrors(errors);
                                        return;
                                    }
                                    //
                                    //
                                    $.loader.show({parent:'body'});
                                    $(dialog).modalDialog("close");
                                    var svc = new fmodel.AjaxService();
                                    svc.url = BASE + "duplicaquoteitem/" + self.quotaId ;
                                    svc.set("method", "POST");
                                    svc.set("contentType", "application/json");
                                    svc.set("data", JSON.stringify(data));
                                    svc.on({
                                        load: function(response){

                                            $.loader.hide({parent:'body'});

                                            $.notify.success("Complete");

                                            ui.Navigation.instance().navigate("quoteassociative", "index", {
                                                fs: this.fullScreenForm
                                            });

                                        },
                                        error: function (error){
                                            $.loader.hide({parent:'body'});
                                            $.notify.error(error);

                                        }
                                    });
                                    svc.load();



                                }
                            }
                        }
                    });



                },
                icon: "a glyphicon glyphicon-plus"
            };

            var button3 = {
                text: "Modifica competenza quota",
                command: function() {

                    var formView = self.modifyCompetenzaQuotaForm();
                    formView.show();



                    var dialog = formView.container.modalDialog({
                        autoOpen: true,
                        title: "Modifica competenza quota",
                        destroyOnClose: true,
                        height: 230,
                        width: 580,
                        buttons: {
                            OK: {
                                primary: true,
                                command: function() {

                                    var data = self.normalizeSubmitResult(formView.form);
                                    //
                                    // //verifico se mancano dati obbligatori
                                    formView.form.resetValidation();
                                    //
                                    //
                                    var errors = self.validateDateForm(data);
                                    if (errors.errors.length){
                                        formView.form.handleValidationErrors(errors);
                                        return;
                                    }
                                    //
                                    //

                                    $.loader.show({parent: 'body', zIndex: 99999});
                                    var svc = new fmodel.AjaxService();
                                    svc.url = BASE + "modifycompetencequoteassociativeitem/" + self.quotaId ;
                                    svc.set("method", "POST");
                                    svc.set("contentType", "application/json");
                                    svc.set("data", JSON.stringify(data));
                                    svc.on({
                                        load: function(response){
                                            $.loader.hide({parent:'body'});
                                            $(dialog).modalDialog("close");
                                            $.notify.success("Operazione completata");

                                            ui.Navigation.instance().navigate("dettaglioquote", "index", {
                                                fs: this.fullScreenForm,
                                                id: self.quotaId,
                                                t: new Date().getTime()
                                            });
                                        },
                                        error: function(error){
                                            $.loader.hide({parent:'body'});
                                            $.notify.error(error);
                                        }
                                    });
                                    svc.load();
                                }
                            }
                        }
                    });



                },
                icon: "a glyphicon glyphicon-pencil"
            };


            var $t = $("#toolbar");
            if(!$t.toolbar("isToolbar")) {
                $t.toolbar();
            }

            $t.toolbar("clear");


            //se sono un nazionale allora giiungo i pulsanti di funzionalità
            if (window.appcontext.categoryType == "2"){
                $t.toolbar("add", button);
                $t.toolbar("add", button1);
                $t.toolbar("add", button2);
                $t.toolbar("add", button3);
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
                    label: "Elenco quote",

                    href: ui.Navigation.instance().navigateUrl("quoteassociative", "index", {})
                },
                {
                    label: "Dettaglio quota"

                }
            ];
        }
    });




    exports.SearchQuoteAppView = SearchQuoteAppView;

    exports.QuotaDettaglioRemoteView = QuotaDettaglioRemoteView;

    return exports;

});
