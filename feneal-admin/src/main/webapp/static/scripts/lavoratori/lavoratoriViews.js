/**
 * Created by fgran on 07/04/2016.
 */

define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils) {

    var exports = {};

    var RepositoryServiceFactory = core.AObject.extend({
        ctor: function() {
            RepositoryServiceFactory.super.ctor.call(this);
        },
        saveWorker: function(worker){
            var route = BASE + "worker" ;

            return this.__createService(true, route, worker);
        },
        getWorkerLastVersions: function(workerId){
            var route = BASE + "workerlastversion/" + workerId ;

            return this.__createGetService( route);
        },
        __createGetService: function (route){

            //definisco il servizio
            var service = new  fmodel.AjaxService();
            service.set("url", route);

            return service;
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

    var WorkerSummaryRemoteView = fviews.RemoteContentView.extend({
        ctor: function(service, workerId, workerFiscalCode){
            WorkerSummaryRemoteView.super.ctor.call(this, service);

            var self = this;
            this.workerId = workerId;
            this.workerFiscalCode = workerFiscalCode;

            this.on("load", function(){

                //alert("data loaded");
                //qui inserisco tutto il codice di inizializzazione della vista




                    // Gestione pulsante di Visualizza credito residuo SMS
                    $("button.btn-residual-credit").click(function() {
                        var svc = new  fmodel.AjaxService();

                        svc.set("data", {});
                        svc.set("url", BASE + "comunicazioni/creditsms");
                        svc.on("load", function(response){
                            var container = $('<div class="residual-credit-cnt text-bold">'+response+'</div>');

                            var dialog = container.modalDialog({
                                autoOpen: true,
                                title: "Credito residuo SMS",
                                destroyOnClose: true,
                                height: 100,
                                width: 240
                            });
                        });
                        svc.on("error", function(error){
                            $.notify.error(error);
                        });

                        svc.load();
                    });


                // Gestione pulsante INVIA SMS
                $("button.btn-send-sms").click(function() {
                    var formService = new fmodel.FormService();
                    formService.set("method", "GET");
                    
                    if (self.workerId){
                        formService.set("data", {workerId: self.workerId});
                        formService.set("url", BASE + "comunicazioni/sms");
                    }else{
                        formService.set("data", {fiscalCode: self.workerFiscalCode});
                        formService.set("url", BASE + "comunicazioni/sms/fiscalcode");
                    }

                    var container = $('<div class="sms-container"></div>');

                    var formView = new fviews.FormView(formService);
                    formView.container = container;

                    formView.on("render", function() {
                        $(".sms-container").find(".panel-footer, .panel-heading").hide();
                        $(".panel-body").css("overflow", "hidden");

                        // Permessi massimo 160 caratteri per l'invio dell'SMS
                        $("textarea[name=text]").attr("maxlength", "160");
                    });

                    formView.show();

                    var dialog = container.modalDialog({
                        autoOpen: true,
                        title: "Invia SMS",
                        destroyOnClose: true,
                        height: 350,
                        width: 750,
                        buttons: {
                            Invia: {
                                primary: true,
                                command: function() {
                                    formView.form.resetValidation();

                                    // Validazione dati
                                    var province = $(".sms-container").find("select[name=province]").val();
                                    var cell = $(".sms-container").find("input[name=cell]").val();
                                    var text = $(".sms-container").find("textarea[name=text]").val();
                                    var causaleCom = $(".sms-container").find("select[name=causaleCom]").val();

                                    var errors = self.validateSMS(cell, text);

                                    if (errors.errors.length){
                                        formView.form.handleValidationErrors(errors);
                                        return;
                                    }

                                    // Se la validazione è OK invio i dati dell'SMS al server
                                    var svc = new  fmodel.AjaxService();
                                    svc.set("data", {
                                        workerId: self.workerId,
                                        province: province,
                                        cell: cell,
                                        text: text,
                                        causaleCom: causaleCom
                                    });
                                    svc.set("url", BASE + "comunicazioni/sendsms");
                                    svc.set("method", "POST");

                                    svc.on("load", function(response){
                                        $.notify.success("Operazione completata");
                                        dialog.modalDialog("close");
                                    });
                                    svc.on("error", function(error){
                                        $.notify.error(error);
                                    });

                                    svc.load();
                                }
                            }
                        }
                    });
                });



                $("button.deleghe").click(function(){
                    ui.Navigation.instance().navigate("deleghehome", "index", {
                        workerId: self.workerId
                    })
                });

                


                $("button.archiviodocumenti").click(function(){
                    ui.Navigation.instance().navigate("documenticrud", "list", {
                        workerId: self.workerId,
                        e : "documento"
                    })
                });



                $("button.comunicazioni").click(function(){
                    ui.Navigation.instance().navigate("comunicazionicrud", "list", {
                        workerId: self.workerId,
                        e : "comunicazione"
                    })
                });


                $("button.richiesteinfo").click(function(){
                    ui.Navigation.instance().navigate("richiestecrud", "list", {
                        workerId: self.workerId,
                        e : "richiesta"
                    })
                });

                $("button.versamenti").click(function(){
                    ui.Navigation.instance().navigate("versamenti", "list", {
                        workerId: self.workerId,
                        e : "versamento"
                    });

                });

                $("button.storico").click(function(){
                    ui.Navigation.instance().navigate("summaryworker", "indexStoricoAggiornamenti", {
                        workerId: self.workerId,
                        e : "storico"
                    });

                });


                self.createToolbar();
                self.createBreadcrumbs();

            });

        },
        validateSMS: function(cell, text){
            var self = this;
            //devo verificare che almeno il numero di cell e il testo (<=160 car.) siano valorizzati
            var result = {};
            result.errors = [];

            var regex_cell = /^[0-9]+$/;

            if (!cell || !regex_cell.test(cell))
                result.errors.push(
                    {
                        property: "cell",
                        message: "Num. cellulare mancante o formalmente non corretto"
                    }
                );
            if (!text || text.length > 160)
                result.errors.push(
                    {
                        property: "text",
                        message: "Testo mancante o troppo lungo"
                    }
                );

            return result;
        },


        onServiceLoad: function(html) {
            var self = this;
            $.loader.hide({ parent: this.container });

            this.content = _E("div").html(html);
            this.container.empty().append(this.content);
            
            if (!self.workerId)
                self.workerId = self.content.find('#id').val();
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

            if (window.appcontext.categoryType != "1"){
                return [];

            }

            return [
                {
                    text: "Crea anagrafica",
                    command: function() {

                        ui.Navigation.instance().navigate("editworker", "index", {
                            fs: this.fullScreenForm
                        });
                    },
                    icon: "pencil"
                },
                {
                    text: "Modifica",
                    command: function() {

                        ui.Navigation.instance().navigate("editworker", "index", {
                            fs: this.fullScreenForm,
                            id: self.workerId
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
                                        svc.url = BASE + "worker/" + self.workerId;
                                        svc.set("method", "DELETE");
                                        svc.on({
                                            load: function(response){

                                                $(dialog).modalDialog("close");
                                                $.notify.success("Operazione completata");

                                                //ritonrno alla modalità di ricerca
                                                ui.Navigation.instance().navigate("searchworkers", "index", {
                                                    fs: this.fullScreenForm
                                                });
                                            },
                                            error: function (error){
                                                $.notify.error(error);
                                                $(dialog).modalDialog("close");
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
                        label: "Ricerca lavoratore",
                        //vado alla ricerca dei lavoratori
                        href: ui.Navigation.instance().navigateUrl("searchworkers", "index", {})
                    },
                    {
                        label: "Anagrafica lavoratore"
                        //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
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
                    label: "Ricerca lavoratore",
                    //vado alla ricerca dei lavoratori
                    href: ui.Navigation.instance().navigateUrl("searchworkersnazionale", "index", {})
                },
                {
                    label: "Anagrafica lavoratore"
                    //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
                }
            ];
        }

    });




    var EditWorkerAppView = fviews.FormAppView.extend({
        ctor: function(formService, workerId) {
            EditWorkerAppView.super.ctor.call(this, formService);

            this.geoUtils = new geoUtils.GeoUtils();

            var self = this;


            self.workerId = workerId;
            //impostro delle variabili a livello di oggetto per la gestione del calcolo dei fiscaldata
            //queste variabili saranno utilizzate nel calcolo del fiscal data per gestire le chiamate asincrone....
            self.currentProvince;
            self.currentNazione;
            self.currentComune;


            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                // Calcolo il codice fiscale del lavoratore
                $('#id_cfButton').click(function() {
                    var svc = new fmodel.AjaxService();
                    svc.url = BASE + "worker/fiscalcode";
                    svc.set("method", "GET");
                    svc.set("data", {
                        name: $('#id_name').val(),
                        surname: $('#id_surname').val(),
                        dateBirth: $('#id_birthDate').val(),
                        birthPlaceId: $('select[name=birthPlace] :selected').val(),
                        sex: $('select[name=sex] :selected').val(),
                        birthNationId: $('select[name=nationality] :selected').val()
                    });

                    svc.on({
                        load: function (response) {
                            $("#fiscalcodeId").val(response);
                        },
                        error: function (error) {
                            $.notify.error(error);
                        }
                    });
                    svc.load();
                });



                // Calcolo i dati fiscali del lavoratore
                $('#id_cfInverseButton').click(function() {
                    var fiscalcode = $("#fiscalcodeId").val();

                    var svc = new fmodel.AjaxService();
                    svc.url = BASE + "worker/fiscaldata";
                    svc.set("method", "GET");
                    svc.set("data", {fiscalcode: fiscalcode});

                    svc.on({
                        load: function (response) {
                            // riempio i campi con i valori dei dati fiscali dell'oggetto di response
                            $('#id_birthDate').val(response.datanascita);
                            $('select[name=sex]').val(response.sex);

                            //imposto le variabili a livello di modulo
                            self.currentProvince = response.provincia;
                            self.currentNazione = response.nazione;
                            self.currentComune = response.comune;

                            $('select[name=nationality]').val(response.nazione).attr("data-value", response.nazione);
                            $('select[name=nationality]').change();

                            // $('select[name=birthProvince]').val(response.provincia).attr("data-value", response.provincia);
                            //
                            // $('select[name=birthProvince]').change();


                            // $('select[name=birthPlace]').val(response.comune).attr("data-value", response.comune);
                        },
                        error: function (error) {
                            $.notify.error("Codice fiscale non corretto");
                        }
                    });
                    svc.load();
                });


                // Funzionalità GEO
                var nationalityItalianId = 100;

                /* GEO DATI NASCITA */
                //qui attacco levento on change della select della nazionalità
                $('select[name="nationality"]').change(function(){
                    var selectedVal = $(this).val();
                    //carico la lista delle province di nascita se la nazionalità è italiana (id ITALIA --> 1)

                    if (selectedVal && selectedVal == nationalityItalianId) {
                        var provinceVal = $('select[name="birthProvince"]').data("value");
                        if (self.currentNazione == "100"){
                            //se èè valorizzata questa variabile allora il cambiamento della lista delle province è guidato
                            //dall'operazione di calcolo dei fiscal data...
                            provinceVal = self.currentProvince;
                        }

                        self.geoUtils.loadProvinces(selectedVal, provinceVal, $('select[name="birthProvince"]'));
                        self.geoUtils.on("provincesLoaded", function(selectedProvince){
                            //carico la lista dei comuni innescando l'evento change sulle province
                            if (self.currentNazione == "100"){
                                $('select[name=birthProvince]').change();
                                //azzero le variabili che indicano l'utilizzo dell'operazionefiscaldata
                                self.currentNazione = null;
                                self.currentProvince = null;

                            }
                        });

                    }
                    else {
                        $('select[name="birthProvince"]').empty().append("<option selected='selected' value=''>Select</option>").change();
                    }
                });

                if(self.workerId)
                    $('select[name="nationality"]').change();


                //qui attacco levento on change della select delle province
                $('select[name="birthProvince"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){
                        if (self.currentComune){
                            self.geoUtils.loadCities(selectedVal, self.currentComune, $('select[name="birthPlace"]'));
                            self.geoUtils.on("citiesLoaded", function(provinceId, selectedValue){
                                self.currentComune = null;
                            });

                        }else{
                            self.geoUtils.loadCities(selectedVal, "", $('select[name="birthPlace"]'));
                        }


                    }
                    else
                        $('select[name="birthPlace"]').empty().append("<option selected='selected' value=''>Select</option>");
                });
                if(self.workerId)
                    self.geoUtils.loadCities($('select[name="birthProvince"]').data("value"), $('select[name="birthPlace"]').data("value"), $('select[name="birthPlace"]'));


                /* GEO DATI RESIDENZA */

                var selectedProvince = $('select[name="livingProvince"]').data("value");

                self.geoUtils.loadProvinces(nationalityItalianId, selectedProvince, $('select[name="livingProvince"]'));

                //qui attacco levento on change della select delle province
                $('select[name="livingProvince"]').change(function(){

                    var selectedVal = $(this).val();

                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadCities(selectedVal, "", $('select[name="livingCity"]'));

                    }
                    else
                        $('select[name="livingCity"]').empty().append("<option selected='selected' value=''>Select</option>");
                });
                if(self.workerId)
                    self.geoUtils.loadCities($('select[name="livingProvince"]').data("value"), $('select[name="livingCity"]').data("value"), $('select[name="livingCity"]'));



                //al variare della citta di residenza cambio il cap
                $('select[name="livingCity"]').change(function(){
                    var selectedVal = $(this).val();
                    if (selectedVal){
                       // alert('cap');
                        var cap = self.geoUtils.calculateCap(selectedVal, $('input[name="cap"]'));

                    }

                })



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

                if (data.privacy === "0")
                    data.privacy = false;
                else
                    data.privacy = true;

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }


                var factory = new RepositoryServiceFactory();
                var svc = factory.saveWorker(data);


                svc.on("load", function(response){
                    //imposto l'handler per la navigazione verso l'utente selezionato
                    $.loader.hide({parent:'body'});
                    ui.Navigation.instance().navigate("summaryworker", "index", {
                        id:response
                    })
                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    $.notify.error(error);
                });

                svc.load();
                $.loader.show({parent:'body'});



            });



        },
        validate: function(data){
            var result = {};
            result.errors = [];

            if (!data.name)
                result.errors.push(
                    {
                        property: "name",
                        message: "Nome mancante"
                    }
                );

            if (!data.surname)
                result.errors.push(
                    {
                        property: "surname",
                        message: "Cognome mancante"
                    }
                );

            if (!data.birthDate)
                result.errors.push(
                    {
                        property: "birthDate",
                        message: "Data di nascita mancante"
                    }
                );

            if (data.privacy === "0")
                result.errors.push(
                    {
                        property: "privacy",
                        message: "Consenso al trattamento dei dati obbligatorio"
                    }
                );

            if (!data.fiscalcode)
                result.errors.push(
                    {
                        property: "fiscalcode",
                        message: "Codice fiscale mancante"
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
        close: function(){
            var self = this;
            if (self.workerId){
                //se sto in modifica vado alla anagrafica dell'utente
                ui.Navigation.instance().navigate("summaryworker", "index", {
                    id:self.workerId
                })
            }else{
                //vado alla ricerca dell'utente
                ui.Navigation.instance().navigate("searchworkers", "index", {

                });

            }
        },
        getBreadcrumbItems: function() {
            var self = this;
            var title = "Crea anagrafica";
            if (self.workerId)
                title = "Modifica anagrafica";
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





    var SearchWorkersAppView = fviews.FormAppView.extend({
        ctor: function(formService, initialserachParams, nazionale) {
            SearchWorkersAppView.super.ctor.call(this, formService);
            //se eseguo la ricerca dal tasto cerca nella toolbar inizializzo la griglia di ricerca con unaprericerca in base al nome 
            //e cognome specificati nel parametro initialsearchParms
            this.nazionale = nazionale;
            this.workerName = initialserachParams;
            this.geoUtils = new geoUtils.GeoUtils();
            //questo paramttro indica se si tratta di ricerca locale o nel dbnazionale
            this.localSearch = true;

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();


                if (self.workerName) {
                    self.localSearch = true;
                    self.searchWorkers({namesurname: self.workerName});
                }

                //inserisco il result container
                var cont = $('<div class="col-md-12">  </div>');
                cont.append('<div id="resultsContainer"> </div>');



                $('div.panel.panel-dark').after(cont);



            });

        },

        searchWorkers: function(params) {
            var self = this;

            var searchWorkerUrl =  "localworkers";

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

                         { dataField:"surname", fixed :true, fixedPosition:"left",visible : true, visibleIndex: 0, caption:"Cognome",
                            cellTemplate: function (container, options) {
                                //container.addClass("img-container");
                                var surname = options.data.surname;
                                var id = options.data.id;

                                $("<a />")
                                    .text(surname)
                                    .attr("href", "javascript:;")
                                    .on('click', function(){

                                        ui.Navigation.instance().navigate("summaryworker", "index", {
                                            id:id
                                        })

                                    })
                                    .appendTo(container);
                            }
                        },
                        { dataField:"name", visible : true, caption:"Nome",visibleIndex: 1},
                        { dataField:"birthDate", dataType:'date', visible : true, caption:"Data nascita" ,visibleIndex: 2},
                        { dataField:"fiscalcode", visible : true, caption:"Codice fiscale",visibleIndex: 3},

                        { dataField:"nationality", visible : true, caption:"Nazione nascita",visibleIndex: 4},
                        { dataField:"livingProvince", visible : true, caption:"Prov. Residenza",visibleIndex: 5},
                        { dataField:"livingCity", visible : true, caption:"Com. Residenza",visibleIndex: 6},
                        { dataField:"address", visible : true, caption:"Indirizzo",visibleIndex: 7},
                        { dataField:"cap", visible : true, caption:"Cap",visibleIndex: 8},
                        { dataField:"cellphone", visible : true, caption:"Cellulare",visibleIndex: 9},
                        { dataField:"phone", visible : true, caption:"Telefono",visibleIndex: 10},
                        { dataField:"birthProvince", visible : true, caption:"Prov. nascita",visibleIndex: 11},
                        { dataField:"birthPlace", visible : true, caption:"Com. nascita",visibleIndex: 12}


                    ],
                    summary: {
                        totalItems: [{
                            column: "surname",
                            summaryType: "count",
                            customizeText: function(data) {

                                if (responseData.length <= 500)
                                    return "Elementi trovati: " + data.value;
                                else
                                    return "Oltre 500 elementi";
                            }
                        }]
                    },
                    "export": {
                        enabled: false,
                        fileName: "anagrafiche",
                        allowExportSelectedData: true
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
                    label: "Ricerca lavoratore"
                    //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
                }
            ];
        },
        getToolbarButtons: function() {
            var self = this;
            if (self.nazionale ===true)
                return [];
            return [
                {
                    text: "Crea anagrafica",
                    command: function() {

                        ui.Navigation.instance().navigate("editworker", "index", {
                            fs: this.fullScreenForm
                        });
                    },
                    icon: "pencil"
                }

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








    var WorkerLastVersionRemoteView = fviews.RemoteContentView.extend({
        ctor: function(service, workerId){
            WorkerLastVersionRemoteView.super.ctor.call(this, service);

            var self = this;
            this.workerId = workerId;


            this.on("load", function(){


                //qui inserisco tutto il codice di inizializzazione della vista
                var factory = new RepositoryServiceFactory();
                var svc = factory.getWorkerLastVersions(self.workerId);


                svc.on("load", function(response){
                    //imposto l'handler per la navigazione verso l'utente selezionato
                    $.loader.hide({parent:'body'});

                    self.initGrid(response);



                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    $.notify.error(error);
                });

                svc.load();
                $.loader.show({parent:'body'});









            self.createToolbar();
                self.createBreadcrumbs();

            });

        },

        initGrid: function(responseData){
            var self = this;


            //instanzio la griglia dei risultati locali...)
            $('#resultsContainer').dxDataGrid({
                dataSource:responseData,
                columns:[

                    // { dataField:"surname", fixed :true, fixedPosition:"left",visible : true, visibleIndex: 0, caption:"Cognome",
                    //     cellTemplate: function (container, options) {
                    //         //container.addClass("img-container");
                    //         var surname = options.data.surname;
                    //         var id = options.data.id;
                    //
                    //         $("<a />")
                    //             .text(surname)
                    //             .attr("href", "javascript:;")
                    //             .on('click', function(){
                    //
                    //                 ui.Navigation.instance().navigate("summaryworker", "index", {
                    //                     id:id
                    //                 })
                    //
                    //             })
                    //             .appendTo(container);
                    //     }
                    // },

                    { dataField:"creatorUser", visible : true, caption:"Cretato da",visibleIndex: 0},
                    { dataField:"creationDate", dataType:'date', visible : true, caption:"Data creazione" ,visibleIndex: 1},

                    { dataField:"updateUser", visible : true, caption:"Aggiornato da",visibleIndex: 2},
                    { dataField:"lastUpdateDate", dataType:'date', visible : true, caption:"Data aggiornamento" ,visibleIndex: 3},



                    { dataField:"surname", fixedPosition:"left", visible : true, caption:"Cognome",visibleIndex: 4},
                    { dataField:"name", visible : true, caption:"Nome",visibleIndex: 5},
                    { dataField:"birthDate", dataType:'date', visible : true, caption:"Data nascita" ,visibleIndex: 6},
                    { dataField:"fiscalcode", visible : true, caption:"Codice fiscale",visibleIndex: 7},
                    { dataField:"nationality", visible : true, caption:"Nazione nascita",visibleIndex: 8},
                    { dataField:"livingProvince", visible : true, caption:"Prov. Residenza",visibleIndex: 9},
                    { dataField:"livingCity", visible : true, caption:"Com. Residenza",visibleIndex: 10},
                    { dataField:"address", visible : true, caption:"Indirizzo",visibleIndex: 11},
                    { dataField:"cap", visible : true, caption:"Cap",visibleIndex: 12},
                    { dataField:"cellphone", visible : true, caption:"Cellulare",visibleIndex: 13},
                    { dataField:"phone", visible : true, caption:"Telefono",visibleIndex: 14},
                    { dataField:"birthProvince", visible : true, caption:"Prov. nascita",visibleIndex: 15},
                    { dataField:"birthPlace", visible : true, caption:"Com. nascita",visibleIndex: 16}







                ],
                summary: {
                    totalItems: [{
                        column: "surname",
                        summaryType: "count",
                        customizeText: function(data) {

                            if (responseData.length <= 500)
                                return "Elementi trovati: " + data.value;
                            else
                                return "Oltre 500 elementi";
                        }
                    }]
                },
                "export": {
                    enabled: false,
                    fileName: "anagrafiche",
                    allowExportSelectedData: true
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

        onServiceLoad: function(html) {
            var self = this;
            $.loader.hide({ parent: this.container });

            this.content = _E("div").html(html);
            this.container.empty().append(this.content);

            if (!self.workerId)
                self.workerId = self.content.find('#id').val();
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
                        label: "Ricerca lavoratore",
                        //vado alla ricerca dei lavoratori
                        href: ui.Navigation.instance().navigateUrl("searchworkers", "index", {})
                    },
                    {
                        label: "Anagrafica " + localStorage.getItem("workerName"),
                        href: ui.Navigation.instance().navigateUrl("summaryworker", "index", {
                            id: self.workerId
                        })
                    },
                    {
                        label: "Storico aggiornamenti anagrafica"
                        //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
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
                    label: "Ricerca lavoratore",
                    //vado alla ricerca dei lavoratori
                    href: ui.Navigation.instance().navigateUrl("searchworkersnazionale", "index", {})
                },
                {
                    label: "Anagrafica " + localStorage.getItem("workerName"),
                    href: ui.Navigation.instance().navigateUrl("summaryworker", "index", {
                        id: self.workerId
                    })
                },
                {
                    label: "Storico aggiornamenti anagrafica"
                    //href: ui.Navigation.instance().navigateUrl("editworker", "index", {})
                }
            ];
        }

    });








    exports.WorkerLastVersionRemoteView = WorkerLastVersionRemoteView;
    exports.EditWorkerAppView = EditWorkerAppView;
    exports.SearchWorkersAppView = SearchWorkersAppView;
    exports.WorkerSummaryRemoteView = WorkerSummaryRemoteView;

    return exports;

});