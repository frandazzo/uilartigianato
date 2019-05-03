/**
 * Created by fgran on 14/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils",
    "reports/reportResultsConfigurer"], function(core, fmodel, fviews, ui, widgets,
                                                 plugins, webparts, geoUtils,
                                                 resultsConfigurer) {

    var exports = {};

    var RepositoryServiceFactory = core.AObject.extend({
        ctor: function() {
            RepositoryServiceFactory.super.ctor.call(this);
        },
        searchProvenienzeUNC: function(searchParams){
            var route = BASE + "provenienzeunc/report" ;

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



    var ReportProvenienzaUncAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            ReportProvenienzaUncAppView.super.ctor.call(this, formService);
            this.geoUtils = new geoUtils.GeoUtils();

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "515px");

                // Modifico il valore della checkbox (on --> true)
                $("input[type=checkbox]").change(function() {
                    if($(this).is(":checked"))
                        $(this).val(true);
                    else
                        $(this).val(false);
                }).change();

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
                var svc = factory.searchProvenienzeUNC(data);


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
                    var reportResultsConfigurer = new resultsConfigurer.ReportUiConfigurer(grid, "deleghe");
                    reportResultsConfigurer.init();


                    //solo il nazionale può effettuare pagamenti
                    if (window.appcontext.roleid == 5){
                        $('.request-info').remove();
                        //aggiungo un pulsante per l'invio di deleghe per il pagamento
                        var btn = $('<div class="col-md-12 col-xs-12 margin-bottom-10 p0 request-info" title="" data-placement="top" data-toggle="tooltip" data-original-title="Effettua pagamento deleghe">'+
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
                            // var provinceSelected = $("select[name=provinceSelect]").val();

                            if (selectedrows.length == 0) {
                                $.notify.error("Selezionare almeno un elemento");
                                return false;
                            }

                            // if (!provinceSelected) {
                            //     $.notify.error("Selezionare una provincia");
                            //     return false;
                            // }

                            var container = $('<div class="request-info-territori-container"></div>');

                            var data = {};
                            var selectedrows = grid.getSelectedRowsData();
                            data.deleghe = selectedrows;
                            // data.province = provinceSelected;

                            var path = BASE + "deleghe/pagamento";
                            var formService = new fmodel.FormService();
                            formService.set("url", path);
                            formService.set("method", "POST");
                            formService.set("data", JSON.stringify(data));
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
                                title: "Crea pagamento",
                                destroyOnClose: true,
                                height: 500,
                                width: 800,
                                buttons: {
                                    Salva: {
                                        primary: true,
                                        command: function() {
                                            form.form.resetValidation();

                                            // Validazione e-mail
                                            var mails = $("input[name=importo]").val();

                                            var dataInizio = $("input[name=dataInizio]").val();
                                            var dataFine = $("input[name=dataFine]").val();


                                            var importo = parseFloat(mails);

                                            if (isNaN(importo) || importo <= 0)
                                            {
                                                alert("Inserire un importo valido");
                                                return;
                                            }

                                            if (!dataInizio){
                                                alert("Inserire una dataInizio valida");
                                                return;
                                            }

                                            if (!dataFine){
                                                alert("Inserire una dataFine valida");
                                                return;
                                            }


                                            data.importo = importo;
                                            data.dataFine = dataFine;
                                            data.dataInizio = dataInizio;


                                            // Se la validazione è OK invio i dati per la mail al server
                                            var svc = new  fmodel.AjaxService();

                                            svc.set("url", BASE + "deleghe/pagamento/execute");
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

                    { dataField:"delegaDataDocumento", visible : true, visibleIndex: 4, dataType:'date'},
                    { dataField:"delegaDataInoltro", visible : false, dataType:'date'},
                    { dataField:"delegaDataAccettazione", visible : false, dataType:'date'},
                    { dataField:"delegaDataAttivazione", visible : false, dataType:'date'},
                    { dataField:"delegaDataAnnullamento", visible : false, dataType:'date'},
                    { dataField:"delegaDataRevoca", visible : false, dataType:'date'},
                    { dataField:"delegaOperatore",  visible : true, visibleIndex: 1},
                    { dataField:"delegaProvincia",  visible : false},
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

                    { dataField:"aziendaRagioneSociale", visible : true, visibleIndex: 6,

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



                    // { dataField:"delegaProvince"},
                    // { dataField:"delegaDataDocumento",
                    //     customizeText: function(data) {
                    //         return Globalize.format(data.value, "dd/MM/yyyy");
                    //     }},
                    // { dataField:"delegaSettore"},
                    // { dataField:"delegaEnteBilaterale"},
                    // { dataField:"aziendaRagioneSociale"}

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
                            return "Deleghe trovate: " + data.value;
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
                    fileName: "deleghe",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "reportdeleghe"
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
                    label: "Report provenienza UNC"
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


    exports.ReportProvenienzaUncAppView = ReportProvenienzaUncAppView;


    return exports;

});
