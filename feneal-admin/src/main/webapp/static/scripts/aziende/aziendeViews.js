/**
 * Created by fgran on 07/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets",
    "framework/plugins",
    "framework/webparts",
    "geoUtils"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils) {

    var exports = {};

    var RepositoryServiceFactory = core.AObject.extend({
        ctor: function() {
            RepositoryServiceFactory.super.ctor.call(this);
        },
        saveFirm: function(worker){
            var route = BASE + "firm" ;

            return this.__createService(true, route, worker);
        },
        searchFirms: function(data){
            var route = BASE + "searchfirm" ;

            return this.__createService(true, route, data);
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


    var SearchFirmGridView = fviews.GridAppView.extend({
        ctor: function(gridService) {
            SearchFirmGridView.super.ctor.call(this, gridService);
            
            this.geoUtils = new geoUtils.GeoUtils();
            var self = this;

            self.on("complete", function(){
                self.get("grid").showSearchForm();
                $(".panel-title").text("Ricerca aziende");


                var nationalityItalianId = 100;
                // Carico la lista delle province italiane
                self.geoUtils.loadProvinces(nationalityItalianId, "", $('select[name="province"]'));

                //qui attacco levento on change della select delle province
                $('select[name="province"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadCities(selectedVal, "", $('select[name="city"]'));

                    }
                    else
                        $('select[name="city"]').empty().append("<option selected='selected' value=''>Select</option>");
                });

            })
            
        },
        edit: function(id){
            //alert("edit discussion number :" + id);
            ui.Navigation.instance().navigate("summaryfirm", "index", {
                fs: this.fullScreenForm,
                id: id

            });
        },
        getToolbarButtons: function() {
            var self = this;
            var buttons = [];
            buttons.push(
                {
                    text: "Crea nuova azienda",
                    command: function () {
                        ui.Navigation.instance().navigate("editfirm", "index", {
                            fs: this.fullScreenForm

                        });
                    },
                    icon: "plus"
                }
            );

            if (this.gridService.get("searchFormIncluded")) {
                buttons.push(
                    {
                        text: "Cerca",
                        icon: "search",
                        command: function () {
                            self.get("grid").showSearchForm();
                        }
                    });
            }

            // buttons.push(
            //     {
            //         text: msg.TOOLBAR_REFRESH,
            //         command: function() {
            //             self.gridService.reload();
            //         },
            //         icon: "refresh"
            //     }
            // );

            return buttons;
        },

        getBreadcrumbItems: function() {
            return [
                {
                    pageTitle: window.appcontext.categoryName
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Ricerca aziende"
                }
            ];
        }

    });

    var EditAziendaAppView = fviews.FormAppView.extend({
        ctor: function(formService, firmId) {
            EditAziendaAppView.super.ctor.call(this, formService);

            this.geoUtils = new geoUtils.GeoUtils();
            var self = this;
            self.firmId = firmId;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                var nationalityItalianId = 100;

                var provinceVal = $('select[name="province"]').data("value");

                // Carico la lista delle province
                self.geoUtils.loadProvinces(nationalityItalianId, provinceVal, $('select[name="province"]'));

                //qui attacco levento on change della select delle province
                $('select[name="province"]').change(function(){

                    var selectedVal = $(this).val();

                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadCities(selectedVal, "", $('select[name="city"]'));
                    }
                    else
                        $('select[name="city"]').empty().append("<option selected='selected' value=''>Select</option>");
                });
                if(self.firmId)
                    self.geoUtils.loadCities($('select[name="province"]').data("value"), $('select[name="city"]').data("value"), $('select[name="city"]'));


            });

            self.formView.form.on("cancel", function() {
                self.close();
            });


            self.formView.on("submit", function(){
                //al click del pulsante submint rimuovo le validazioni
                self.formView.form.resetValidation();

                //recupero i dati normalizzati
                //questa è una versione un po più elaborata della semplice serializzazione del form perchè restituisce
                //le proprièta con lo stesso nome raggruppate...
                //ad esempio se ho tre valori category : 1 , category : 3, category :7 ricevero una sola porpietà category : [1,3,7]
                //nello stesso oggetto
                var data = self.normalizeSubmitResult(self.formView.form);
                var errors = self.validate(data);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }

                var factory = new RepositoryServiceFactory();
                var svc = factory.saveFirm(data);


                svc.on("load", function(response){
                    //imposto l'handler per la navigazione verso l'utente selezionato
                    $.loader.hide({parent:'body'});
                    ui.Navigation.instance().navigate("summaryfirm", "index", {
                        id:response
                    })
                });
                svc.on("error", function(response){
                    $.loader.hide({parent:'body'});
                    $.notify.error(response);
                });

                svc.load();
                $.loader.show({parent:'body'});



            });



        },
        validate: function(data){
            var result = {};
            result.errors = [];


            if (!data.description)
                result.errors.push(
                    {
                        property: "description",
                        message: "Ragione sociale mancante"
                    }
                );

            return result;
        },
        submit: function(e){


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
        close: function(){
            var self = this;
            if (self.firmId){
                //se sto in modifica vado alla anagrafica dell'utente
                ui.Navigation.instance().navigate("summaryfirm", "index", {
                    id:self.firmId
                })
            }else{
                //vado alla ricerca dell'utente
                ui.Navigation.instance().navigate("searchfirms", "index", {

                });

            }
        },
        getBreadcrumbItems: function() {
            var self = this;
            var title = "Crea anagrafica";
            if (self.firmId)
                title = "Modfica anagrafica";
            return [
                {
                    pageTitle: window.appcontext.categoryName
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: title
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
                //             fs: this.fullScreenForm,
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

    var FirmSummaryRemoteView = fviews.RemoteContentView.extend({
        ctor: function(service, firmId, firmDescription){
            FirmSummaryRemoteView.super.ctor.call(this, service);

            var self = this;
            this.firmId = firmId;

            this.on("load", function(){

                //alert("data loaded");
                //qui inserisco tutto il codice di inizializzazione della vista
                self.createToolbar();
                self.createBreadcrumbs();

                $("button.firm-docs").click(function(){
                    ui.Navigation.instance().navigate("firmdocscrud", "list", {
                        firmId: self.firmId,
                        e : "documentoazienda"
                    })
                });

            });

        },
        onServiceLoad: function(html) {
            var self = this;
            $.loader.hide({ parent: this.container });
            this.content = _E("div").html(html);
            this.container.empty().append(this.content);
            if (!self.firmId)
                self.firmId = self.content.find('#id').val();
            this.invoke("load");

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
        },


        getToolbarButtons: function() {
            var self = this;

            if (window.appcontext.categoryType == "1"){
                return [
                    {
                        text: "Crea anagrafica",
                        command: function() {

                            ui.Navigation.instance().navigate("editfirm", "index", {
                                fs: this.fullScreenForm
                            });
                        },
                        icon: "pencil"
                    },
                    {
                        text: "Modifica",
                        command: function() {

                            ui.Navigation.instance().navigate("editfirm", "index", {
                                fs: this.fullScreenForm,
                                id: self.firmId
                            });
                        },
                        icon: "pencil"
                    },
                    {
                        text: "Elimina",
                        command: function() {


                            var dialog = $("<p>Sicuro di voler eliminare l'elemento selezionato?</p>").modalDialog({
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
                                            svc.url = BASE + "firm/" + self.firmId;
                                            svc.set("method", "DELETE");
                                            svc.on({
                                                load: function(response){

                                                    $(dialog).modalDialog("close");
                                                    $.notify.success("Operazione completata");

                                                    //ritonrno alla modalità di ricerca
                                                    ui.Navigation.instance().navigate("searchfirms", "index", {
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
                    }
                ];

            }
            return [];

        },
        getBreadcrumbItems: function() {
            var self = this;

            if (window.appcontext.categoryType == "1"){

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
                        label: "Anagrafica azienda"

                    }
                ];

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
                    href: ui.Navigation.instance().navigateUrl("searchfirmsnazionale", "index", {})
                },
                {
                    label: "Anagrafica azienda"

                }
            ];

        }

    });



    var SearchFirmsNazionaleAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            SearchFirmsNazionaleAppView.super.ctor.call(this, formService);
            //se eseguo la ricerca dal tasto cerca nella toolbar inizializzo la griglia di ricerca con unaprericerca in base al nome
            //e cognome specificati nel parametro initialsearchParms


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

        searchWorkers: function(params) {
            var self = this;

            var searchWorkerUrl =  "localfirms";

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

                    { dataField:"description", visible : true, visibleIndex: 0, caption:"Ragione sociale",
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var desc = options.data.description;
                            var id = options.data.id;

                            $("<a />")
                                .text(desc)
                                .attr("href", "javascript:;")
                                .on('click', function(){

                                    ui.Navigation.instance().navigate("summaryfirm", "index", {
                                        fs: this.fullScreenForm,
                                        id: id

                                    });



                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"alternativeDescription", caption: "Descrizione alternativa", visible: true}


                ],
                summary: {
                    totalItems: [{
                        column: "description",
                        summaryType: "count",
                        customizeText: function(data) {

                            if (responseData.length <= 500)
                                return "Elementi trovati: " + data.value;
                            else
                                return "Oltre 500 elementi";
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
                hoverStateEnabled: true

            }).dxDataGrid("instance");



        },
        // Siccome il form di ricerca è costituito da due tab, vado a considerare solo i campi visibili dal tab selezionato
        serializeWorkerSearchForm: function() {
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

            // Imposto l'URL di ricerca in base al tipo di ricerca che sto eseguendo
            self.localSearch = true;



            self.searchWorkers(self.serializeWorkerSearchForm());
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
                    label: "Ricerca azienda"

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









    var GlobalSearchFirmsNazionaleAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            GlobalSearchFirmsNazionaleAppView.super.ctor.call(this, formService);
            //this.geoUtils = new geoUtils.GeoUtils();

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "255px");

                // var selectedCompany = $('select[name="company"]').val();
                // self.geoUtils.loadProvincesForCompany(selectedCompany, "", $('select[name="province"]'), true);
                // $('[data-property="firm"]').attr("data-prefilter-value", selectedCompany);

            });

            self.formView.on("submit", function(){

                var data = self.normalizeSubmitResult(self.formView.form);

                var factory = new RepositoryServiceFactory();
                var svc = factory.searchFirms(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    //inizializzo la griglia devexpress
                    var grid = self.initGrid(response);
                    //una volta ottenuti i risultati la griglia devexpress mostra una loader
                    //di attesa per la renderizzazione degli stessi! in quel momento rendo
                    //visibile l'intera area
                    //scrollando fino a rendere visibile la griglia
                    $('html, body').animate({scrollTop: $('#reportContainer').offset().top - 160}, 1400, "swing");



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
                    { dataField:"description", visible : true, visibleIndex: 0, caption:"Ragione sociale",
                        cellTemplate: function (container, options) {
                            //container.addClass("img-container");
                            var desc = options.data.description;
                            var id = options.data.id;

                            $("<a />")
                                .text(desc)
                                .attr("href", "javascript:;")
                                .on('click', function(){

                                    ui.Navigation.instance().navigate("summaryfirm", "index", {
                                        fs: this.fullScreenForm,
                                        id: id

                                    });



                                })
                                .appendTo(container);
                        }
                    },
                    { dataField:"alternativeDescription", caption: "Regione", visible: true}


                ],
                searchPanel: {
                    visible: true

                },
                summary: {
                    totalItems: [{
                        column: "description",
                        summaryType: "count",
                        customizeText: function(data) {
                            return "Trovate: " + data.value;
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
                columnAutoWidth: true,
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
                    label: "Ricerca aziende"
                    //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
                }
            ];
        },
        getToolbarButtons: function() {
            var self = this;
            return [
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








    exports.GlobalSearchFirmsNazionaleAppView = GlobalSearchFirmsNazionaleAppView;
    exports.SearchFirmsNazionaleAppView = SearchFirmsNazionaleAppView;
    exports.SearchFirmGridView = SearchFirmGridView;
    exports.FirmSummaryRemoteView = FirmSummaryRemoteView;
    exports.EditAziendaAppView = EditAziendaAppView;

    return exports;

});