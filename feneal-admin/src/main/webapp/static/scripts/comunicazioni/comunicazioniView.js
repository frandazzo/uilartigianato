/**
 * Created by fgran on 28/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils) {

    var exports = {};


    var ComunicazioniCrudGridAppView = fviews.CrudGridAppView.extend({
        ctor: function(gridService, workerId){
            ComunicazioniCrudGridAppView.super.ctor.call(this, gridService);

            this.workerId = workerId;
        },
        create: function() {
            var self = this;
            ui.Navigation.instance().navigate("comunicazionicrud", "create", {
                e: this.gridService.get("formIdentifier"),
                fs: this.fullScreenForm,
                g: this.gridService.get("identifier"),
                workerId : self.workerId
            });
        },

        edit: function(id) {
            var self = this;
            ui.Navigation.instance().navigate("comunicazionicrud", "edit", {
                e: this.gridService.get("formIdentifier"),
                fs: this.fullScreenForm,
                id: id,
                g: this.gridService.get("identifier"),
                workerId : self.workerId
            });
        },
        getToolbarButtons: function() {
            var self = this;

            if (window.appcontext.categoryType != "1"){
                return [];

            }


            var buttons = [];
            buttons.push(
                {
                    text: msg.TOOLBAR_CREATE,
                    command: function () {
                        self.create();
                    },
                    icon: "plus"
                }
            );

            if (this.gridService.get("searchFormIncluded")) {
                buttons.push(
                    {
                        text: msg.TOOLBAR_SEARCH,
                        icon: "search",
                        command: function () {
                            self.get("grid").showSearchForm();
                        }
                    });
            }

            buttons.push(
                {
                    text: msg.TOOLBAR_REFRESH,
                    command: function() {
                        self.gridService.reload();
                    },
                    icon: "refresh"
                }
            );

            buttons.push(
                {
                    text: msg.TOOLBAR_ACTIONS,
                    group: 'selected',
                    type: "menu",
                    alignRight: true,
                    items: [
                        {
                            label: msg.TOOLBAR_SELECT_ALL,
                            command: function() {
                                self.grid.selectAll();
                            }
                        },
                        {
                            label: msg.TOOLBAR_UNSELECT_ALL,
                            command: function() {
                                self.grid.unselectAll();
                            }
                        },
                        { separator: true },
                        {
                            label: msg.TOOLBAR_DELETE_SELECTION,
                            command: function() {



                                var dialog = $("<p>Sicuro di voler eliminare gli elementi selezionati?</p>").modalDialog({
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

                                                var ids = self.grid.getSelection();
                                                if(ids.length == 0) {
                                                    $.notify.warn(msg.MSG_PLEASE_SELECT_ROW);
                                                } else {

                                                    //ciclo su tutti gli id cercando la riga che
                                                    //contiene una categoria divera da quella dell'utente logato
                                                    var onlyCategoryElements = true;
                                                    $.each(ids, function(index, elem){
                                                        var row = $("tr[data-entity-id='" + elem + "']");
                                                        var categoryElem = row.find("span[data-property='userCompleteName']");
                                                        var owner = categoryElem.attr("data-property-value");

                                                        if (owner != window.appcontext.loggeduser){
                                                            //if (window.appcontext.roleid == 4){
                                                                $.notify.warn("Eliminazione permessa solo per  " + owner);
                                                                //eso dal ciclo
                                                                onlyCategoryElements = false;
                                                                return false;
                                                           // }

                                                        }

                                                    });

                                                    if (onlyCategoryElements === true)
                                                        self.remove(ids);


                                                    dialog.modalDialog("close");
                                                }
                                            }

                                        }
                                    }
                                });




                            },
                            important: true
                        }
                    ],
                    icon: "asterisk"
                }
            );

            return buttons;
        },
        getBreadcrumbItems: function() {
            var self = this;

            var searchWorkerLink = "searchworkers";

            if (window.appcontext.categoryType != "1"){
                searchWorkerLink = "searchworkersnazionale";
            }

            return [
                {
                    pageTitle: window.appcontext.categoryName
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Ricerca lavoratore",
                    //vado alla ricerca dei lavoratori
                    href: ui.Navigation.instance().navigateUrl(searchWorkerLink, "index", {})
                },
                {
                    label: "Anagrafica " + localStorage.getItem("workerName"),
                    href: ui.Navigation.instance().navigateUrl("summaryworker", "index", {
                        id: self.workerId
                    })
                },
                {
                    label: "Lista comunicazioni"
                }
            ];
        }

    });

    var ComunicazioniCrudFormAppView = fviews.CrudFormAppView.extend({
        ctor: function(formService, communicationId, workerId){
            ComunicazioniCrudFormAppView.super.ctor.call(this, formService);

            this.communicationId = communicationId;
            this.workerId = workerId;
        },
        getToolbarButtons: function() {
            var self = this;

            if (window.appcontext.categoryType != "1"){
                return [];

            }

            return [
                {
                    text: "Annulla",
                    command: function() {
                        self.close();
                    },
                    icon: "arrow-left"
                },
                {
                    text: "Salva",
                    command: function() {
                        self.form.submit();
                    },
                    icon: "save"
                }
            ];

        },

        getBreadcrumbItems: function() {
            var self = this;
            var title = "Crea comunicazione";
            if (self.communicationId)
                title = "Modifica comunicazione";

            var searchWorkerLink = "searchworkers";

            if (window.appcontext.categoryType != "1"){
                searchWorkerLink = "searchworkersnazionale";
            }

            return [
                {
                    pageTitle: window.appcontext.categoryName
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Ricerca lavoratore",
                    //vado alla ricerca dei lavoratori
                    href: ui.Navigation.instance().navigateUrl(searchWorkerLink, "index", {})
                },
                {
                    label: "Anagrafica " + localStorage.getItem("workerName"),
                    href: ui.Navigation.instance().navigateUrl("summaryworker", "index", {
                        id: self.workerId
                    })
                },

                {
                    label: "Lista comunicazioni",
                    href: ui.Navigation.instance().navigateUrl("comunicazionicrud", "index", {
                        e: self.formService.get("gridIdentifier"),
                        reload: 1,
                        workerId : self.workerId
                    })
                },
                {
                    label: title
                }
            ];
        },

        close: function() {
            var self = this;
            ui.Navigation.instance().navigate("comunicazionicrud", "index", {
                e: this.formService.get("gridIdentifier"),
                reload: 1,
                fs: 1,
                workerId : self.workerId
            });
        }

    });


    exports.ComunicazioniCrudGridAppView = ComunicazioniCrudGridAppView;
    exports.ComunicazioniCrudFormAppView = ComunicazioniCrudFormAppView;
    return exports;

});
