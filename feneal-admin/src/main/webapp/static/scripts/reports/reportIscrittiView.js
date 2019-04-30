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
        searchIscritti: function(searchParams){
            var route = BASE + "iscritti/report" ;

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



    var ReportIscrittiAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            ReportIscrittiAppView.super.ctor.call(this, formService);
            this.geoUtils = new geoUtils.GeoUtils();

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "470px");


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



                $("div[data-property=delegationActiveExist]").css("margin-bottom", "10px");

                // Modifico il valore della checkbox (on --> true)
                $("input[type=checkbox]").change(function() {
                    if($(this).is(":checked"))
                        $(this).val(true);
                    else
                        $(this).val(false);
                }).change();


            });

            self.formView.on("submit", function(){

                var data = self.normalizeSubmitResult(self.formView.form);

                var factory = new RepositoryServiceFactory();
                var svc = factory.searchIscritti(data);


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
                    var reportResultsConfigurer = new resultsConfigurer.ReportUiConfigurer(grid, "iscritti");
                    reportResultsConfigurer.init();



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

            //Globalize.culture("it-IT");

            var grid = $('#reportContainer').dxDataGrid({
                dataSource:responseData,
                columns:[

                    { dataField:"iscrittoOperatoreDelega", visible : false},
                    { dataField:"iscrittoDataRegistrazione", visible : false, dataType:'date'},
                    { dataField:"iscrittoQuota", visible : false},
                    { dataField:"iscrittoCompetenza", visible : false},
                    { dataField:"iscrittoNote", visible : false},
                    { dataField:"iscrittoContratto", visible : false},
                    { dataField:"iscrittoLivello", visible : false},
                    { dataField:"iscrittoRipresaDati", visible : false},






                    { dataField:"delegaMansione", visible : false},
                    { dataField:"delegaLuogoLavoro", visible : false},
                    { dataField:"delegaNote", visible : false},

                    { dataField:"iscrittoRegione", visible : true, visibleIndex : 0},
                    { dataField:"iscrittoProvincia",  visible : true},
                    { dataField:"lavoratoreNomeCompleto", visible : true,
                        cellTemplate: function (container, options) {

                            var name = options.data.lavoratoreNomeCompleto;
                            var companyId = options.data.companyId;

                            var fiscalCode = options.data.lavoratoreCodiceFiscale;
                            var uri = encodeURI(BASE + "#/summaryworker/remoteIndex?fiscalCode=" + fiscalCode + "&companyId=" + companyId );
                            $("<a />")
                                .text(name)
                                .attr("href", uri)
                                .attr("target", "_blank")
                                .appendTo(container);
                        }
                    },

                    { dataField:"aziendaRagioneSociale", visible : true,

                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var name = options.data.aziendaRagioneSociale;


                            var uri = encodeURI(BASE + "#/summaryfirm/index?id=" + options.data.aziendaId );

                            $("<a />")
                                .text(name)
                                .attr("href", uri)
                                .attr("target", "_blank")
                                .appendTo(container);
                        }
                    },
                    { dataField:"iscrittoSettore", visible : true},
                    { dataField:"iscrittoOperatoreDelega", visible : true},
                    { dataField:"iscrittoContratto", visible : true},

                    { dataField:"aziendaCitta", visible : false},
                    { dataField:"aziendaProvincia", visible : false},
                    { dataField:"aziendaCap", visible : false},
                    { dataField:"aziendaIndirizzo", visible : false},
                    { dataField:"aziendaNote", visible : false},
                    { dataField:"aziendaId", visible : false},


                    { dataField:"lavoratoreNome", visible : false},
                    { dataField:"lavoratoreCognome", visible : false},
                    { dataField:"lavoratoreSesso", visible : false},
                    { dataField:"lavoratoreCodiceFiscale", visible : false},
                    { dataField:"lavoratoreDataNascita", dataType:'date',visible : false},
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
                // searchPanel: {
                //     visible: true
                //
                // },
                summary: {
                    totalItems: [{
                        column: "lavoratoreNomeCompleto",
                        summaryType: "count",
                        customizeText: function(data) {
                            return "Iscritti trovati: " + data.value;
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
                    fileName: "iscritti",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "reportiscritti"
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
                    label: "Report iscritti"
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


    exports.ReportIscrittiAppView = ReportIscrittiAppView;


    return exports;

});