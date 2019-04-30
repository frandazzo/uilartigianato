/**
 * Created by fgran on 13/06/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/widgets",
    "framework/plugins",
    "framework/webparts",
    "framework/ui"], function (core, model, widgets, plugins, webparts, ui) {


    var exports = {};


    var Iscritti = core.AObject.extend({

        ctor: function (firmId) {
            Iscritti.super.ctor.call(this);
            this.firmId = firmId;
        },

        init: function () {
            var self = this;
            self.initIscrittiSearch();


        },

        initIscrittiSearch: function(){
            var self = this;

            var service = new model.AjaxService();
            service.set("url",BASE + "firm/" + self.firmId + "/iscrizioni");
            service.on("load",function(resp){

                if (resp.length == 0){
                    //visualizzo un messaggio che non è stata trovata una iscrizione
                    $('#containerIscritti').append('' +
                        '<p class="color-red" style="text-align: center;padding-top: 12%;padding-bottom: 3%;">' +
                        '<i class="material-icons font-size-100">sentiment_dissatisfied</i>' +
                        '</p>' +
                        '<p class="text-center">Nessun iscritto trovato</p>')
                }else{
                    self.initGrid(resp);
                }


            });
            service.on("error",function(e){
                $.notify.error(e);
            });
            service.load();
        },
        initGrid : function(responseData){

            var grid = $('#containerIscritti').dxDataGrid({
                dataSource:responseData,
                columns:[
                    { dataField:"iscrittoCollaboratore", visible : false},
                    { dataField:"iscrittoDataRegistrazione", visible : false, dataType:'date'},
                    { dataField:"iscrittoQuota", visible : false},
                    { dataField:"iscrittoProvincia",  visible : true, visibleIndex : 0},
                    { dataField:"iscrittoSettore", visible : true,  visibleIndex : 1},

                    { dataField:"iscrittoOperatoreDelega", visible : true, visibleIndex : 3},
                    { dataField:"iscrittoContratto", visible : true, visibleIndex : 4},
                    { dataField:"iscrittoCompetenza", visible : true, visibleIndex : 5},
                    { dataField:"iscrittoLivello", visible : false},

                    { dataField:"lavoratoreNomeCompleto", fixed :true, visible : true, visibleIndex: 2,
                        cellTemplate: function (container, options) {

                            var name = options.data.lavoratoreNomeCompleto;



                            var fiscalCode = options.data.lavoratoreCodiceFiscale;
                            var companyId = options.data.companyId;

                            var uri = encodeURI(BASE + "#/summaryworker/remoteIndex?fiscalCode=" + fiscalCode + "&companyId=" + companyId );
                            $("<a />")
                                .text(name)
                                .attr("href", uri)
                                .attr("target", "_blank")
                                // $("<a />")
                                //     .text(completeName)
                                //     .attr("href", "javascript:;")
                                // .on('click', function(){
                                //     ui.Navigation.instance().navigate("summaryworker", "remoteIndex", {
                                //         fiscalCode:fiscalCode
                                //     });
                                // })
                                .appendTo(container);
                        }
                        // calculateCellValue: function (data) {
                        //     var surname = data.lavoratoreCognome;
                        //     var name = data.lavoratoreNome;
                        //    // var datanas =Globalize("it").formatDate(new Date(data.lavoratoreDataNascita));
                        //     return surname + " " + name;// + " (" + datanas + ")";
                        // }


                    },
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
                    { dataField:"lavoratoreCodiceCassaEdile", visible : false},
                    { dataField:"lavoratoreCodiceEdilcassa", visible : false},
                    { dataField:"lavoratoreFondo", visible : false},
                    { dataField:"lavoratoreNote", visible : false},




                ],
                "export": {
                    enabled: true,
                    fileName: "dettaglio_lista",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "iscrittiazienda"
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

            return grid;

        }

    });


    var Deleghe = core.AObject.extend({

        ctor: function (firmId) {
            Deleghe.super.ctor.call(this);
            this.firmId = firmId;
        },

        init: function () {
            var self = this;
            self.initDelegheSearch();


        },

        initDelegheSearch: function(){
            var self = this;

            var service = new model.AjaxService();
            service.set("url",BASE + "firm/" + self.firmId + "/deleghe");
            service.on("load",function(resp){

                if (resp.length == 0){
                    //visualizzo un messaggio che non è stata trovata una iscrizione
                    $('#containerDeleghe').append('' +
                        '<p class="color-red" style="text-align: center;padding-top: 12%;padding-bottom: 3%;">' +
                        '<i class="material-icons font-size-100">sentiment_dissatisfied</i>' +
                        '</p>' +
                        '<p class="text-center">Nessun iscritto trovato</p>')
                }else{
                    self.initGrid(resp);
                }


            });
            service.on("error",function(e){
                $.notify.error(e);
            });
            service.load();
        },
        initGrid : function(responseData){

            var grid = $('#containerDeleghe').dxDataGrid({
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
                columnChooser: {
                    enabled: true
                },
                "export": {
                    enabled: true,
                    fileName: "dettaglio_lista",
                    allowExportSelectedData: true
                },
                // stateStoring: {
                //     enabled: true,
                //     type: "localStorage",
                //     storageKey: "iscrittiazienda"
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

            return grid;

        }

    });

    exports.Iscritti = Iscritti;
    exports.Deleghe = Deleghe;
    return exports;


});