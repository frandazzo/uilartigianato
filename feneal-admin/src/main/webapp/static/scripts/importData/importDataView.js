/**
 * Created by fgran on 18/04/2017.
 */
/**
 * Created by fgran on 07/04/2016.
 */
define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/ui",
    "framework/widgets", "framework/plugins"
    ,"framework/webparts","geoUtils"], function(core, fmodel, fviews, ui, widgets, plugins, webparts, geoUtils) {

    var exports = {};

    var RepositoryServiceFactory = core.AObject.extend({
        ctor: function() {
            RepositoryServiceFactory.super.ctor.call(this);
        },
        executeReport: function(reportData){
            var route = BASE + "import/importdeleghe" ;

            var svc =  this.__createService(true, route, reportData);
            return svc;
        },
        executeReportBilateralita: function(reportData){
            var route = BASE + "importDatiBil/importquote" ;

            var svc =  this.__createService(true, route, reportData);
            return svc;
        },
        executeReportUnc: function(reportData){
            var route = BASE + "importDatiUnc/importquote" ;

            var svc =  this.__createService(true, route, reportData);
            return svc;
        },
        executeReportQuote: function(reportData){
            var route = BASE + "import/importquote" ;

            var svc =  this.__createService(true, route, reportData);
            return svc;
        },
        executeReportAnagrafiche: function(reportData){
            var route = BASE + "import/importanagrafiche" ;

            var svc =  this.__createService(true, route, reportData);
            return svc;
        },
        executeReportDocumenti: function(reportData){
            var route = BASE + "import/importdocumenti" ;

            var svc =  this.__createService(true, route, reportData);
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



    var ImportaAnagraficheAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            ImportaAnagraficheAppView.super.ctor.call(this, formService);

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                var templateUrl = BASE + "import/anagrafichetemplate"
                var panelFooter = self.formView.container.find('.panel-body').after("<div></div>");
                panelFooter.append('<a href="' + templateUrl + '">Scarica template</a>');


            });

            self.formView.on("submit", function(){

                var data = self.normalizeSubmitResult(self.formView.form);




                var factory = new RepositoryServiceFactory();
                var svc = factory.executeReportAnagrafiche(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    var href = BASE + response;
                    var dialog = $("<a href='" +  href + "'>"+ "Scarica elaborazione" + "</a>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,

                    });

                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    var dialog = $("<p>"+ error + "</p>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,
                    });
                    //setTimeout(function(){location.reload();}, 5000);
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
        submit: function(e){

        },
        close: function(){
            alert("close");
        },
        getBreadcrumbItems: function() {
            var self = this;
            return [
                {
                    pageTitle: "Importazione anagrafiche"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Importazione anagrafiche"
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


    var ImportaDelegheAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            ImportaDelegheAppView.super.ctor.call(this, formService);

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                var templateUrl = BASE + "import/deleghetemplate"
                var panelFooter = self.formView.container.find('.panel-body').after("<div></div>");
                panelFooter.append('<a href="' + templateUrl + '">Scarica template</a>');


            });

            self.formView.on("submit", function(){

                var data = self.normalizeSubmitResult(self.formView.form);




                var factory = new RepositoryServiceFactory();
                var svc = factory.executeReport(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    var href = BASE + response;
                    var dialog = $("<a href='" +  href + "'>"+ "Scarica elaborazione" + "</a>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,

                    });

                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    var dialog = $("<p>"+ error + "</p>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,
                    });
                    //setTimeout(function(){location.reload();}, 5000);
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
        submit: function(e){

        },
        close: function(){
            alert("close");
        },
        getBreadcrumbItems: function() {
            var self = this;
            return [
                {
                    pageTitle: "Importazione deleghe"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Importazione deleghe"
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



    var ImportaQuoteAppView =  fviews.FormAppView.extend({
        ctor: function(formService) {
            ImportaQuoteAppView.super.ctor.call(this, formService);
            this.geoUtils = new geoUtils.GeoUtils();
            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();


                //qui attacco levento on change della select delle company
                $('select[name="company"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadProvincesForCompany(selectedVal, "", $('select[name="province"]'));
                    }

                });

                var selectedCompany = $('select[name="company"]').val();
                self.geoUtils.loadProvincesForCompany(selectedCompany, "", $('select[name="province"]'));



                var templateUrl = BASE + "import/quotetemplate"
                var panelFooter = self.formView.container.find('.panel-body').after("<div></div>");
                panelFooter.append('<a href="' + templateUrl + '">Scarica template</a>');


                //recupro il valire del settore dall'input
                var sett = $('select[name="settore"]').val();
                var enteRef = $('select[name="ente"]').closest('.row');
                //recupero il riferimeento alla row dell'ente che deve esserree visualizzata
                //solo se il settore è edile
                if (sett != "EDILE"){
                    enteRef.hide();
                }else{
                    enteRef.show();
                }

                $('select[name="settore"]').change(function(){

                    var sett = $(this).val();
                    if (sett != "EDILE"){
                        enteRef.hide();
                    }else{
                        enteRef.show();
                    }
                });


            });

            self.formView.on("submit", function(){


                //eseguo la validazione
                self.formView.form.resetValidation();
                var data = self.normalizeSubmitResult(self.formView.form);
                

                var errors = self.validateFormQuote(data);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }






                var factory = new RepositoryServiceFactory();
                var svc = factory.executeReportQuote(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    var href = BASE + response;
                    var dialog = $("<a href='" +  href + "'>"+ "Scarica elaborazione" + "</a>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,

                    });

                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    var dialog = $("<p>"+ error + "</p>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,
                    });
                    //setTimeout(function(){location.reload();}, 5000);
                });

                svc.load();
                $.loader.show({parent:'body'});



            });

            self.formView.form.on("cancel", function() {
                self.close();
            });



        },
        validateFormQuote: function(data){
            
            var result = {};
            result.errors = [];

            //devo verificare se il settore è un settore edile oppure un qualunque altro settore
            //se si tratta di settore edile devo valorizzare l'ente
            if (data.settore == "EDILE"){
                if (data.ente == ""){
                    result.errors.push(
                        {
                            property: "ente",
                            message: "Ente bilaterale mancante"
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

            if (!data.file1){
                result.errors.push(
                    {
                        property: "file1",
                        message: "File non specificato"
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
                    pageTitle: "Importazione quote"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Importazione quote"
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


    var ImportaDatiUncAppView =  fviews.FormAppView.extend({
        ctor: function(formService) {
            ImportaDatiUncAppView.super.ctor.call(this, formService);
            this.geoUtils = new geoUtils.GeoUtils();
            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();


                //qui attacco levento on change della select delle company
                $('select[name="company"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadProvincesForCompany(selectedVal, "", $('select[name="province"]'));
                    }

                });

                var selectedCompany = $('select[name="company"]').val();
                self.geoUtils.loadProvincesForCompany(selectedCompany, "", $('select[name="province"]'));



                var templateUrl = BASE + "importDatiUnc/quotetemplate"
                var panelFooter = self.formView.container.find('.panel-body').after("<div></div>");
                panelFooter.append('<a href="' + templateUrl + '">Scarica template</a>');


                //recupro il valire del settore dall'input
                var sett = $('select[name="settore"]').val();
                var enteRef = $('select[name="ente"]').closest('.row');
                //recupero il riferimeento alla row dell'ente che deve esserree visualizzata
                //solo se il settore è edile
                if (sett != "EDILE"){
                    enteRef.hide();
                }else{
                    enteRef.show();
                }

                $('select[name="settore"]').change(function(){

                    var sett = $(this).val();
                    if (sett != "EDILE"){
                        enteRef.hide();
                    }else{
                        enteRef.show();
                    }
                });


            });

            self.formView.on("submit", function(){


                //eseguo la validazione
                self.formView.form.resetValidation();
                var data = self.normalizeSubmitResult(self.formView.form);


                var errors = self.validateFormQuote(data);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }






                var factory = new RepositoryServiceFactory();
                var svc = factory.executeReportUnc(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    var href = BASE + response;
                    var dialog = $("<a href='" +  href + "'>"+ "Scarica elaborazione" + "</a>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,

                    });

                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    var dialog = $("<p>"+ error + "</p>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,
                    });
                    //setTimeout(function(){location.reload();}, 5000);
                });

                svc.load();
                $.loader.show({parent:'body'});



            });

            self.formView.form.on("cancel", function() {
                self.close();
            });



        },
        validateFormQuote: function(data){

            var result = {};
            result.errors = [];



            if (!data.file1){
                result.errors.push(
                    {
                        property: "file1",
                        message: "File non specificato"
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
                    pageTitle: "Importazione Dati UNC"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Importazione Dati UNC"
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

    var ImportaDatiBilAppView =  fviews.FormAppView.extend({
        ctor: function(formService) {
            ImportaDatiBilAppView.super.ctor.call(this, formService);
            this.geoUtils = new geoUtils.GeoUtils();
            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();


                //qui attacco levento on change della select delle company
                $('select[name="company"]').change(function(){

                    var selectedVal = $(this).val();
                    //carico la lista delle città
                    if (selectedVal){
                        self.geoUtils.loadProvincesForCompany(selectedVal, "", $('select[name="province"]'));
                    }

                });

                var selectedCompany = $('select[name="company"]').val();
                self.geoUtils.loadProvincesForCompany(selectedCompany, "", $('select[name="province"]'));



                var templateUrl = BASE + "importDatiBil/quotetemplate"
                var panelFooter = self.formView.container.find('.panel-body').after("<div></div>");
                panelFooter.append('<a href="' + templateUrl + '">Scarica template</a>');


                //recupro il valire del settore dall'input
                var sett = $('select[name="settore"]').val();
                var enteRef = $('select[name="ente"]').closest('.row');
                //recupero il riferimeento alla row dell'ente che deve esserree visualizzata
                //solo se il settore è edile
                if (sett != "EDILE"){
                    enteRef.hide();
                }else{
                    enteRef.show();
                }

                $('select[name="settore"]').change(function(){

                    var sett = $(this).val();
                    if (sett != "EDILE"){
                        enteRef.hide();
                    }else{
                        enteRef.show();
                    }
                });


            });

            self.formView.on("submit", function(){


                //eseguo la validazione
                self.formView.form.resetValidation();
                var data = self.normalizeSubmitResult(self.formView.form);


                var errors = self.validateFormQuote(data);

                if (errors.errors.length){
                    self.formView.form.handleValidationErrors(errors);
                    return;
                }






                var factory = new RepositoryServiceFactory();
                var svc = factory.executeReportBilateralita(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    var href = BASE + response;
                    var dialog = $("<a href='" +  href + "'>"+ "Scarica elaborazione" + "</a>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,

                    });

                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    var dialog = $("<p>"+ error + "</p>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,
                    });
                    //setTimeout(function(){location.reload();}, 5000);
                });

                svc.load();
                $.loader.show({parent:'body'});



            });

            self.formView.form.on("cancel", function() {
                self.close();
            });



        },
        validateFormQuote: function(data){

            var result = {};
            result.errors = [];


            if (!data.file1){
                result.errors.push(
                    {
                        property: "file1",
                        message: "File non specificato"
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
                    pageTitle: "Importazione Dati Bilateralità"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Importazione Dati Bilateralità"
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




    var ImportaDocumentiAppView = fviews.FormAppView.extend({
        ctor: function(formService) {
            ImportaDocumentiAppView.super.ctor.call(this, formService);

            var self = this;

            self.formView.on("load", function(){
                self.createToolbar();
                self.createBreadcrumbs();

                var templateUrl = BASE + "import/documentitemplate"
                var panelFooter = self.formView.container.find('.panel-body').after("<div></div>");
                panelFooter.append('<a href="' + templateUrl + '">Scarica template</a>');


            });

            self.formView.on("submit", function(){

                var data = self.normalizeSubmitResult(self.formView.form);




                var factory = new RepositoryServiceFactory();
                var svc = factory.executeReportDocumenti(data);


                svc.on("load", function(response){

                    $.loader.hide({parent:'body'});

                    var href = BASE + response;
                    var dialog = $("<a href='" +  href + "'>"+ "Scarica elaborazione" + "</a>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,

                    });

                });
                svc.on("error", function(error){
                    $.loader.hide({parent:'body'});
                    var dialog = $("<p>"+ error + "</p>").modalDialog({

                        autoOpen: true,
                        title: "",
                        destroyOnClose: true,
                        // height: 250,
                        // width: 400,
                    });
                    //setTimeout(function(){location.reload();}, 5000);
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
        submit: function(e){

        },
        close: function(){
            alert("close");
        },
        getBreadcrumbItems: function() {
            var self = this;
            return [
                {
                    pageTitle: "Importazione anagrafiche"
                },
                {
                    icon: "glyphicon glyphicon-home",
                    href: BASE
                },
                {
                    label: "Importazione anagrafiche"
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

    exports.ImportaDocumentiAppView = ImportaDocumentiAppView;
    exports.ImportaDelegheAppView = ImportaDelegheAppView;
    exports.ImportaQuoteAppView = ImportaQuoteAppView;
    exports.ImportaAnagraficheAppView = ImportaAnagraficheAppView;
    exports.ImportaDatiUncAppView = ImportaDatiUncAppView;
    exports.ImportaDatiBilAppView = ImportaDatiBilAppView;

    return exports;

});
