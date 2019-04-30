/**
 * Created by angelo on 08/05/2016.
 */

define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils) {

    var exports = {};


    var AziendaDocumentiCrudGridAppView = fviews.CrudGridAppView.extend({
        ctor: function(gridService, firmId){
            AziendaDocumentiCrudGridAppView.super.ctor.call(this, gridService);

            this.firmId = firmId;
        },
        create: function() {
            var self = this;
            ui.Navigation.instance().navigate("firmdocscrud", "create", {
                e: this.gridService.get("formIdentifier"),
                fs: this.fullScreenForm,
                g: this.gridService.get("identifier"),
                firmId : self.firmId
            });
        },

        edit: function(id) {
            var self = this;
            ui.Navigation.instance().navigate("firmdocscrud", "edit", {
                e: this.gridService.get("formIdentifier"),
                fs: this.fullScreenForm,
                id: id,
                g: this.gridService.get("identifier"),
                firmId : self.firmId
            });
        },
        getToolbarButtons: function() {
            var self = this;
            var buttons = [];



            if (window.appcontext.categoryType != "1"){
                return [];
            }



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
                                                            //}

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

            var searchfirmsLink = "searchfirms";

            if (window.appcontext.categoryType != "1"){
                searchfirmsLink = "searchfirmsnazionale";
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
                    label: "Ricerca aziende",
                    //vado alla ricerca delle aziende
                    href: ui.Navigation.instance().navigateUrl(searchfirmsLink, "index", {})
                },
                {
                    label: "Anagrafica azienda",
                    href: ui.Navigation.instance().navigateUrl("summaryfirm", "index", {
                        id: self.firmId
                    })
                },
                {
                    label: "Lista documenti"
                }
            ];
        }

    });

    var AziendaDocumentiCrudFormAppView = fviews.CrudFormAppView.extend({
        ctor: function(formService, documentId, firmId){
            AziendaDocumentiCrudFormAppView.super.ctor.call(this, formService);

            this.documentId = documentId;
            this.firmId = firmId;
        },
        getToolbarButtons: function() {
            var self = this;

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
            var title = "Crea documento";
            if (self.documentId)
                title = "Modifica documento";

            return [
                {
                    pageTitle: window.appcontext.categoryName
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Ricerca aziende",
                    //vado alla ricerca delle aziende
                    href: ui.Navigation.instance().navigateUrl("searchfirms", "index", {})
                },
                {
                    label: "Anagrafica azienda",
                    href: ui.Navigation.instance().navigateUrl("summaryfirm", "index", {
                        id: self.firmId
                    })
                },
                {
                    label: "Lista documenti",
                    href: ui.Navigation.instance().navigateUrl("firmdocscrud", "index", {
                        e: self.formService.get("gridIdentifier"),
                        reload: 1,
                        firmId : self.firmId
                    })
                },
                {
                    label: title
                }
            ];
        },

        close: function() {
            var self = this;
            ui.Navigation.instance().navigate("firmdocscrud", "index", {
                e: this.formService.get("gridIdentifier"),
                reload: 1,
                fs: 1,
                firmId : self.firmId
            });
        }

    });


    exports.AziendaDocumentiCrudGridAppView = AziendaDocumentiCrudGridAppView;
    exports.AziendaDocumentiCrudFormAppView = AziendaDocumentiCrudFormAppView;
    return exports;

});
