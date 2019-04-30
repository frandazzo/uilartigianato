/**
 * Created by angelo on 15/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins","framework/webparts", "geoUtils"],
    function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils) {

    var exports = {};

    var RepositoryServiceFactory = core.AObject.extend({
        ctor: function() {
            RepositoryServiceFactory.super.ctor.call(this);
        },
        quoteImpiantiProceed: function(data){
            var route = BASE + "quoteimpiantifissi/proceed";

            var svc =  this.__createService(true, route, data);
            return svc;
        },
        quoteManualiProceed: function(data){
            var route = BASE + "quotemanuali/proceed";

            var svc =  this.__createService(true, route, data);
            return svc;
        },
        quoteSettoreProceed: function(data){
            var route = BASE + "quotesettore/proceed";

            var svc =  this.__createService(true, route, data);
            return svc;
        },
        quoteBreviManoProceed: function(data){
            var route = BASE + "quotebrevimano/proceed";

            var svc =  this.__createService(true, route, data);
            return svc;
        },
        sendDataForCreationQuote: function(data){
            var route = BASE + "quoteimpiantifissi/createquote" ;

            var svc =  this.__createService(true, route, data);
            return svc;
        },
        getDelegheList : function(data){


            var route = BASE + "aziende/creaquote?name=" + encodeURIComponent(data) ;
            var service = new  fmodel.AjaxService();
            service.set("contentType", "application/x-www-form-urlencoded; charset=UTF-8");
            service.set("url", route);
            service.set("method", "GET");
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

    var QuoteImpiantiAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            QuoteImpiantiAppView.super.ctor.call(this, formService);

            var self = this;

            //instanzio il servizio per il recupero dei dati geografici
            this.geoUtils = new geoUtils.GeoUtils();

            self.formView.on("load", function(){

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "320px");

                //qui attacco levento on change della select delle company
                $('select[name="company"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadProvincesForCompany(selectedVal, "", $('select[name="province"]'));

                        //imposto il valore di default del searchble filed renderer
                        $('[data-property="firm"]').attr("data-prefilter-value", selectedVal);
                    }

                });

                var selectedCompany = $('select[name="company"]').val();
                self.geoUtils.loadProvincesForCompany(selectedCompany, "", $('select[name="province"]'));
                $('[data-property="firm"]').attr("data-prefilter-value", selectedCompany);


            });

            self.formView.on("submit", function(){

                var searchData = self.normalizeSubmitResult(self.formView.form);
                self.formView.form.resetValidation();


                var errors = self.validateFormQuote(searchData);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }




                var factory = new RepositoryServiceFactory();
                var svc = factory.quoteImpiantiProceed(searchData);

                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    $("#resultsContainer").find("tr.data-row").remove();

                    $("#resultsContainer").show();
                    if (response.length == 0) {
                        $(".quote-table").append("<tr class='data-row' ><td colspan='4'>Nessun dato trovato</td></tr>");
                        $(".send-data-btn").hide();
                    } else {
                        $(".proceed-btn").remove();
                        $(".cancel-form-btn").show().click(function() {
                            self.formView.formService.load();
                            $('form').show();
                        });

                        $(".send-data-btn").show();

                        $(".send-data-btn").click(function() {
                            if (searchData.amount == "")
                                searchData.amount = 0;

                            var data = {};
                            data.header = searchData;

                            quoteArr = [];
                            $.each($(".quote-table .data-row"), function(i, o) {
                                var row = {};
                                row.lavoratoreId = $(o).data("workerid");
                                row.importo = $(o).find(".workerAmount").find('input').val();
                               // row.contratto = $(o).find("select[name='contratto']").val();

                                quoteArr.push(row);
                            });

                            data.quoteRows = quoteArr;

                            var svcGen = factory.sendDataForCreationQuote(data);

                            svcGen.on("load", function (response) {

                                $.loader.hide({parent: 'body'});
                                if (response){
                                    $.notify.error(response);
                                }else{
                                    $.notify.success("Operazione completata");
                                    ui.Navigation.instance().navigate("quoteassociative", "index");
                                }


                            });

                            svcGen.on("error", function (error) {
                                $.loader.hide({parent: 'body'});
                                $.notify.error(error);
                            });

                            svcGen.load();
                        });

                        // var contratto = $("select[name='contract']").val();
                        // var optionsContractSelect = $.map($('select[name=contract] option'), function(e) { return e.value; });

                        $.each(response, function (i, o) {
                            var row = "<tr class='data-row' data-workerid='" + o.lavoratoreId + "'>" +
                                "<td >" + o.lavoratoreNomeCompleto + "</td>" +
                                "<td >" + o.settore + "</td>" +
                                "<td >"+o.contratto+"</td>" +
                                "<td class='workerAmount'><input type='number' name='importo'  value='" + o.importo + "'></td>" +
                                "</tr>";

                            $(".quote-table").append(row);
                           // var selectObj = $(row).find("select[name='contratto']");
                        });

                        // $.each(optionsContractSelect, function(i,o) {
                        //     $("select[name=contratto]").append($("<option>", {
                        //         value: o,
                        //         text: o
                        //     }));
                        // });
                        // $("select[name=contratto]").val(contratto);



                        $('form').hide();
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

        validateFormQuote: function(data) {
            var result = {};
            result.errors = [];

            //devo verificare se il settore è un settore edile oppure un qualunque altro settore
            //se si tratta di settore edile devo valorizzare l'ente insieme con l'azienda
            if (data.settore == "EDILE"){
                if (data.firm == ""){
                    result.errors.push(
                        {
                            property: "firm",
                            message: "Azienda mancante"
                        }
                    );
                }
            }

            //devo valorizzare le date di inizio e fine
            var dateRegExp = /^\d{2}[/]\d{2}[/]\d{4}$/
            if (!data.dataInizio.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataInizio",
                        message: "Data inizio non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }
            if (!data.dataFine.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataFine",
                        message: "Data fine non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }

            return result;
        },

        submit: function(e){

        },
        close: function(){
            alert("close");
        }

    });

    var QuoteBreviManoAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            QuoteBreviManoAppView.super.ctor.call(this, formService);

            var self = this;

            self.formView.on("load", function(){

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "320px");

                //recupro il valire del settore dall'input
                var sett = $('select[name="settore"]').val();
                var enteRef = $('select[name="ente"]').closest('.row');
                var firmComponent = $("div[data-property=firm][data-component=field]");
                //recupero il riferimeento alla row dell'ente che deve esserree visualizzata
                //solo se il settore è edile
                if (sett != "EDILE"){
                    enteRef.hide();
                    firmComponent.show();
                }else{
                    enteRef.show();
                    firmComponent.show();
                }

                $('select[name="settore"]').change(function(){

                    var sett = $(this).val();
                    if (sett != "EDILE"){
                        enteRef.hide();
                        firmComponent.show();
                    }else{
                        enteRef.show();
                        firmComponent.show();
                    }
                });
            });

            self.formView.on("submit", function(){

                var searchData = self.normalizeSubmitResult(self.formView.form);
                self.formView.form.resetValidation();


                var errors = self.validateFormQuote(searchData);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }




                var factory = new RepositoryServiceFactory();
                var svc = factory.quoteBreviManoProceed(searchData);

                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});
                    ui.Navigation.instance().navigate("dettaglioquote", "index",{
                        id:response
                    });



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

        validateFormQuote: function(data) {
            var result = {};
            result.errors = [];

            //devo verificare se il settore è un settore edile oppure un qualunque altro settore
            //se si tratta di settore edile devo valorizzare l'ente insieme con l'azienda

            if (!data.worker){
                result.errors.push(
                    {
                        property: "worker",
                        message: "Lavoratore mancante"
                    }
                );
            }


            if (data.settore == "EDILE"){
                if (data.ente == ""){
                    result.errors.push(
                        {
                            property: "ente",
                            message: "Ente bilaterale mancante"
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

            }else if (data.settore == "INPS"){
                //non richiedo azienda...
            }else{
                if (data.firm == ""){
                    result.errors.push(
                        {
                            property: "firm",
                            message: "Azienda mancante"
                        }
                    );
                }
            }

            //devo valorizzare le date di inizio e fine
            var dateRegExp = /^\d{2}[/]\d{2}[/]\d{4}$/
            if (!data.dataInizio.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataInizio",
                        message: "Data inizio non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }
            if (!data.dataFine.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataFine",
                        message: "Data fine non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }

            return result;
        },

        submit: function(e){

        },
        close: function(){
            alert("close");
        }

    });

    var QuoteManualiAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            QuoteManualiAppView.super.ctor.call(this, formService);

            var self = this;

            self.formView.on("load", function(){

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "320px");

                //recupro il valire del settore dall'input
                var sett = $('select[name="settore"]').val();
                var enteRef = $('select[name="ente"]').closest('.row');
                var firmComponent = $("div[data-property=firm][data-component=field]");
                //recupero il riferimeento alla row dell'ente che deve esserree visualizzata
                //solo se il settore è edile
                if (sett != "EDILE"){
                    enteRef.hide();
                    firmComponent.show();
                }else{
                    enteRef.show();
                    firmComponent.show();
                }

                $('select[name="settore"]').change(function(){

                    var sett = $(this).val();
                    if (sett != "EDILE"){
                        enteRef.hide();
                        firmComponent.show();
                    }else{
                        enteRef.show();
                        firmComponent.show();
                    }
                });
            });

            self.formView.on("submit", function(){

                var searchData = self.normalizeSubmitResult(self.formView.form);
                self.formView.form.resetValidation();


                var errors = self.validateFormQuote(searchData);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }




                var factory = new RepositoryServiceFactory();
                var svc = factory.quoteManualiProceed(searchData);

                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    ui.Navigation.instance().navigate("dettaglioquote", "index",{
                        id:response
                    });


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

        validateFormQuote: function(data) {
            var result = {};
            result.errors = [];

            //devo verificare se il settore è un settore edile oppure un qualunque altro settore
            //se si tratta di settore edile devo valorizzare l'ente insieme con l'azienda
            if (data.settore == "EDILE"){
                if (data.ente == ""){
                    result.errors.push(
                        {
                            property: "ente",
                            message: "Ente bilaterale mancante"
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

            }else if (data.settore == "INPS"){
                //non richiedo azienda...
            }else{
                if (data.firm == ""){
                    result.errors.push(
                        {
                            property: "firm",
                            message: "Azienda mancante"
                        }
                    );
                }
            }

            //devo valorizzare le date di inizio e fine
            var dateRegExp = /^\d{2}[/]\d{2}[/]\d{4}$/
            if (!data.dataInizio.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataInizio",
                        message: "Data inizio non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }
            if (!data.dataFine.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataFine",
                        message: "Data fine non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }

            return result;
        },

        submit: function(e){

        },
        close: function(){
            alert("close");
        }

    });

    var QuoteSettoreAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            QuoteSettoreAppView.super.ctor.call(this, formService);

            var self = this;

            self.formView.on("load", function(){

                // Setto la lunghezza delle colonne del form report
                $(".panel.col-div").css("height", "320px");

            });

            self.formView.on("submit", function(){

                var searchData = self.normalizeSubmitResult(self.formView.form);
                self.formView.form.resetValidation();


                var errors = self.validateFormQuote(searchData);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }




                var dialog = $("<p>La procedura potrebbe richiedere alcuni minuti. Continuare?</p>").modalDialog({
                    autoOpen: true,
                    title: "Domanda",
                    destroyOnClose: true,
                    height: 100,
                    width:  400,
                    buttons: {
                        send: {
                            label: "Continua",
                            primary: true,
                            command: function () {
                                var factory = new RepositoryServiceFactory();
                                var svc = factory.quoteSettoreProceed(searchData);


                                svc.on("load", function(response){

                                    $.loader.hide({parent:'body'});
                                    ui.Navigation.instance().navigate("quoteassociative", "index");

                                });

                                svc.on("error", function(error){

                                    $.loader.hide({parent:'body'});
                                    alert("Errore: "  + error);
                                });

                                svc.load();
                                $(dialog).modalDialog("close");
                                $.loader.show({parent:'body'});

                            }

                        }
                    }
                });

            });

            self.formView.form.on("cancel", function() {
                self.close();
            });

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

        validateFormQuote: function(data) {
            var result = {};
            result.errors = [];


            //devo valorizzare le date di inizio e fine
            var dateRegExp = /^\d{2}[/]\d{2}[/]\d{4}$/
            if (!data.dataInizio.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataInizio",
                        message: "Data inizio non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }
            if (!data.dataFine.match(dateRegExp)){
                result.errors.push(
                    {
                        property: "dataFine",
                        message: "Data fine non corretta (inserire data in formato dd/MM/yyyy)"
                    }
                );
            }

            return result;
        },

        submit: function(e){

        },
        close: function(){
            alert("close");
        }

    });

    var CreaQuoteRemoteView = fviews.RemoteContentView.extend({
            ctor: function(service){
                CreaQuoteRemoteView.super.ctor.call(this, service);


                var self = this;

                this.on("load", function(){

                    // alert("data loaded");
                    //qui inserisco tutto il codice di inizializzazione della vista
                    self.initPanel();

                    self.createToolbar();
                    self.createBreadcrumbs();

                });

            },

            onServiceLoad: function(html) {
                var self = this;
                $.loader.hide({ parent: this.container });

                this.content = _E("div").html(html);
                this.container.empty().append(this.content);


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

                return [

                ];

            },
            getBreadcrumbItems: function() {
                var self = this;
                return [
                    {
                        pageTitle: "UIL artigianato"
                    },
                    {
                        icon: "glyphicon glyphicon-home",
                        href: BASE
                    },
                    {
                        label: "Crea quote per azienda",


                    }
                ];
            },
            initPopup : function(){

                var self = this;

                var popupOptions = {
                    contentTemplate: function(content){
                        var template = $('.popup-content').clone();

                        var searchCompany = function(companyName) {
                            if (companyName) {

                                $("#azienda").removeClass('errorMessage');
                                $("#regione").removeClass('errorMessage');
                                $("#provincia").removeClass('errorMessage');

                                var serviceFactory = new RepositoryServiceFactory();
                                var service = serviceFactory.getDelegheList(companyName);

                                service.on("load", function(response){

                                    $.loader.hide({parent:'body'});
                                    self.initAziendeGrid(response, template.find('#aziendeGrid'), popup);
                                });
                                service.on("error", function(error){
                                    $.loader.hide({parent:'body'});
                                    alert(error);
                                });
                                service.load();
                                $.loader.show({parent:'body'});

                            }
                        };


                        var textBox = template.find('#cercaAziendaTextBox')
                            .dxTextBox({
                                placeholder: "Cerca azienda...",
                                onEnterKey: function(e) {
                                    var companyName =
                                        e.component.option('text');

                                    searchCompany(companyName);
                                }
                            });
                        var button = template.find('#cercaAziendaButton')
                            .dxButton({
                                text: "Cerca",
                                onClick: function() {
                                    var companyName =
                                        textBox.dxTextBox("instance")
                                            .option('text');

                                    searchCompany(companyName);
                                }
                            });


                        return template;
                    } ,
                    showTitle: true,
                    title: "Ricerca Aziende",
                    visible: false,
                    dragEnabled: true,
                    closeOnOutsideClick: true,
                    closeOnBackButton: true
                };

                var popup =  $("#popupContainer")
                    .dxPopup(popupOptions).dxPopup("instance");

                return popup;

            },
            calculateSearchData : function (){

                var form = $("#form").dxForm("instance");
                var dataInizio = form.getEditor('dataInizio').option('text');
                var dataFine = form.getEditor('dataFine').option('text');
                var importo = form.getEditor('importo').option('value');


                var searchData = {
                    amount : importo,
                    province: $('#provincia').attr('data-id'),
                    company: $('#regione').attr('data-id'),
                    dataInizio: dataInizio,
                    dataFine: dataFine,
                    firm: $('#azienda').attr('data-id')
                };
                return searchData;
            },
            resetForm : function(){
                $("#resultsContainer").find("tr.data-row").remove();
                $("#resultsContainer").hide();

                $('#ricercaAzienda').show();
                //imposto le proprietà provate
                $('#azienda').html('');
                $('#azienda').attr('data-id', '');
                $('#provincia').html('');
                $('#provincia').attr('data-id', '');
                $('#regione').html('');
                $('#regione').attr('data-id', '');


                //visualizzo il pulsante per azzerare la ricerca
                $('#azzeraRicerca').hide();
                $("#procedi").show();
                $("#send-data-btn").hide();
                $("#cancel-form-btn").hide();
            },
            initPanel: function() {

                var self = this;
                var validationRules = [{
                    type: "required",
                    message: "Campo data obbligatorio"
                }];

                /*$("#dataInizio").dxDateBox();
                $("#dataFine").dxDateBox();
                $("#amount").dxNumberBox();*/


                var form = $("#form").dxForm({
                    readOnly: false,
                    showColonAfterLabel: true,
                    showValidationSummary: true,
                    items: [
                        {
                            dataField: "dataInizio",
                            editorType: "dxDateBox",
                            label: {
                                text: "Data Inizio"
                            },
                            editorOptions: {
                                width: "100%",
                                invalidDateMessage: "Formato data: gg/MM/aaaa"
                            },
                            validationRules: [{
                                type: "required",
                                message: "Campo obbligatorio"
                            }]
                        },
                        {
                            dataField: "dataFine",
                            editorType: "dxDateBox",
                            label: {
                                text: "Data Fine"
                            },
                            editorOptions: {
                                width: "100%",
                                invalidDateMessage: "Formato data: gg/MM/aaaa"
                            },
                            validationRules: [{
                                type: "required",
                                message: "Campo obbligatorio"
                            },
                            {
                                type: "custom",
                                reevaluate: true,
                                message: "La data di inizio deve essere inferiore della data di fine.",
                                validationCallback: function(e) {
                                    var dataInizio = $("#form").dxForm("instance").getEditor("dataInizio").option("value");
                                    var dataFine = e.value;

                                    return dataInizio <= dataFine;
                                }
                            }]
                        },
                        {
                            dataField: "importo",
                            editorType: "dxNumberBox",
                            label: {
                                text: "Importo"
                            },
                            validationRules: [{
                                type: "required",
                                message: "Campo obbligatorio"
                            }]
                        }
                    ]
                }).dxForm("instance");


                $("#ricercaAzienda")
                    .click(function () {
                        var popup = self.initPopup();
                        popup.show();
                    });
                $('#azzeraRicerca').click(function(){
                    self.resetForm();
                });
                $("#cancel-form-btn").click(function() {
                    self.resetForm();
                    $("#form").dxForm("instance").resetValues();
                });

                $("#send-data-btn").click(function() {

                    var searchData = self.calculateSearchData();
                    if (searchData.amount == "")
                        searchData.amount = 0;

                    var data = {};
                    data.header = searchData;

                    quoteArr = [];
                    $.each($(".quote-table .data-row"), function(i, o) {
                        var row = {};
                        row.lavoratoreId = $(o).data("workerid");
                        row.importo = $(o).find(".workerAmount").find('input').val();
                        // row.contratto = $(o).find("select[name='contratto']").val();

                        quoteArr.push(row);
                    });

                    data.quoteRows = quoteArr;
                    var factory = new RepositoryServiceFactory();
                    var svcGen = factory.sendDataForCreationQuote(data);

                    svcGen.on("load", function (response) {

                        $.loader.hide({parent: 'body'});
                        if (response){
                            $.notify.error(response);
                        }else{
                            $.notify.success("Operazione completata");
                            self.resetForm();
                            $("#form").dxForm("instance").resetValues();
                        }


                    });

                    svcGen.on("error", function (error) {
                        $.loader.hide({parent: 'body'});
                        $.notify.error(error);
                    });

                    svcGen.load();
                });

                $('#procedi').click(function(){

                    var result = $("#form").dxForm("instance").validate();

                    var aziendaField = $("#azienda").html();
                    var regioneField = $("#regione").html();
                    var provinciaField = $("#provincia").html();

                    var errorMessage = "Campo obbligatorio.";

                    if (aziendaField.length <= 0) {
                        $("#azienda").addClass('errorMessage').html(errorMessage);
                    }
                    if (regioneField.length <= 0) {
                        $("#regione").addClass('errorMessage').html(errorMessage);
                    }
                    if (provinciaField.length <= 0) {
                        $("#provincia").addClass('errorMessage').html(errorMessage);
                    }

                    var aziendaIsValid = aziendaField.length > 0 && !aziendaField !== errorMessage;
                    var regioneIsValid = regioneField.length > 0 && regioneField !== errorMessage;
                    var provinciaIsValid = provinciaField.length > 0 && provinciaField !== errorMessage;

                    if (result.isValid
                        && aziendaIsValid
                        && regioneIsValid
                        && provinciaIsValid) {

                        var searchData = self.calculateSearchData();

                        var factory = new RepositoryServiceFactory();
                        var svc = factory.quoteImpiantiProceed(searchData);

                        svc.on("load", function(response){

                            $.loader.hide({parent:'body'});



                            $("#resultsContainer").show();

                            $("#procedi").hide();


                            $("#send-data-btn").show();
                            $("#cancel-form-btn").show();
                            $.each(response, function (i, o) {
                                var row = "<tr class='data-row' data-workerid='" + o.lavoratoreId + "'>" +
                                    "<td >" + o.lavoratoreNomeCompleto + "</td>" +
                                    "<td >" + o.settore + "</td>" +
                                    "<td >"+o.contratto+"</td>" +
                                    "<td class='workerAmount'><input type='number' name='importo'  value='" + o.importo + "'></td>" +
                                    "</tr>";

                                $(".quote-table").append(row);
                                // var selectObj = $(row).find("select[name='contratto']");
                            });



                        });
                        svc.on("error", function(error){
                            $.loader.hide({parent:'body'});
                            alert("Errore: "  + error);
                        });

                        svc.load();
                        $.loader.show({parent:'body'});
                    }

                });


            },
            initAziendeGrid: function(data, selector, parentPopup) {
                selector.dxDataGrid({
                    dataSource: data,
                    columns: [
                        { dataField:"azienda", visible : true, visibleIndex: 1,

                            cellTemplate: function (container, options) {
                                //container.addClass("img-container");
                                var name = options.data.azienda;
                                var idAzienda = options.data.idAzienda;
                                var regione = options.data.regione;
                                var idRegione = options.data.idRegione;
                                var provincia = options.data.provincia;
                                var idProvincia = options.data.idProvincia;

                                $("<a />")
                                    .text(name)
                                    .attr("href", "javascript:;")
                                    .on('click', function(){

                                        if (!provincia){
                                            alert("L'azienda specificata non ha deleghe");
                                            return;
                                        }

                                        //nascondo il pulsante di ricerca
                                        $('#ricercaAzienda').hide();
                                        //imposto le proprietà provate
                                        $('#azienda').html(name);
                                        $('#azienda').attr('data-id', idAzienda);
                                        $('#provincia').html(provincia);
                                        $('#provincia').attr('data-id', idProvincia);
                                        $('#regione').html(regione);
                                        $('#regione').attr('data-id', idRegione);

                                        //visualizzo il pulsante per azzerare la ricerca
                                        $('#azzeraRicerca').show();
                                        parentPopup.dispose();

                                        //chiudi il popup
                                        //assegnmo tutti i valori
                                    })
                                    .appendTo(container);
                            }
                        },
                        { dataField:"regione", visible : true},
                        { dataField:"provincia", visible : true}

                    ],
                    showBorders: true
                }).dxDataGrid("instance");
            },


    });

    exports.QuoteImpiantiAppView = QuoteImpiantiAppView;
    exports.QuoteSettoreAppView = QuoteSettoreAppView;
    exports.QuoteManualiAppView = QuoteManualiAppView;
    exports.CreaQuoteRemoteView = CreaQuoteRemoteView;
    exports.QuoteBreviManoAppView = QuoteBreviManoAppView;

    return exports;

});