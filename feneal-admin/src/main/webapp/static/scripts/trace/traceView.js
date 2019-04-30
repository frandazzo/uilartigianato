/**
 * Created by angelo on 16/11/2017.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins", "reports/reportResultsConfigurer"], function(core, fmodel, fviews, ui, widgets, plugins, resultsConfigurer) {

    var exports = {};

    var TraceLoginRemoteView = fviews.RemoteContentView.extend({
        ctor: function(service){
            TraceLoginRemoteView.super.ctor.call(this, service);

            var self = this;

            self.traceLogins = null;

            self.on("load", function(){

                //qui inserisco tutto il codice di inizializzazione della vista
                self.createToolbar();
                self.createBreadcrumbs();


                $.loader.hide({parent:'body'});

                //inizializzo la griglia devexpress
                var grid = self.initGrid(self.traceLogins);
                //una volta ottenuti i risultati la griglia devexpress mostra una loader
                //di attesa per la renderizzazione degli stessi! in quel momento rendo
                //visibile l'intera area
                //scrollando fino a rendere visibile la griglia
                $('html, body').animate({scrollTop: $('#reportContainer').offset().top - 160}, 1400, "swing");

                //configuro la navigabilità e la toolbar delle actions del report che visualizza la tracciatura login
                var reportResultsConfigurer = new resultsConfigurer.ReportUiConfigurer(grid, "tracciatura login", false);
                reportResultsConfigurer.init();


                //$.loader.show({parent:'body'});


            });

        },

        initGrid : function(responseData) {


            var grid = $('#reportContainer').dxDataGrid({
                dataSource:responseData,
                columns:[
                    { dataField:"id", visible : false},
                    { dataField:"year", visible : true, caption:"Anno"},
                    { dataField:"month", visible : true, caption:"Mese"},
                    { dataField:"company", visible : true, caption:"Azienda"},
                    { dataField:"username", visible : true, caption:"Utente"},
                    { dataField:"counterWebsite", visible : true, caption:"Contatore accessi"}
                ],
                "export": {
                    enabled: false,
                    fileName: "tracciatura_login",
                    allowExportSelectedData: true
                },
                stateStoring: {
                    enabled: false,
                    type: "localStorage",
                    storageKey: "tracelogins"
                },
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
                summary: {
                    totalItems: [{
                        column: "id",
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
                    mode:"none"
                },
                hoverStateEnabled: true

            }).dxDataGrid("instance");

            return grid;

        },

        onServiceLoad: function(traceLoginsViewResponse) {
            var self = this;

            self.traceLogins = traceLoginsViewResponse.traceLogins;

            $.loader.hide({ parent: this.container });
            this.content = _E("div").html(traceLoginsViewResponse.content);
            this.container.empty().append(this.content);
            this.invoke("load");
        },

        createToolbar: function() {
            var self = this;
            var $t = $("#toolbar");

            if(!$t.toolbar("isToolbar")) {
                $t.toolbar();
            }

            $t.toolbar("clear");
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
                    pageTitle: "Uil Artigianato"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Tracciatura Login"
                }
            ];
        }
    });



    exports.TraceLoginRemoteView = TraceLoginRemoteView;


    return exports;

});