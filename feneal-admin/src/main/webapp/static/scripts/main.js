window.SPA_CONTAINER = $("#spa-container"); //single page application container, where the ajax views are loaded for default

require([
    "framework/plugins",
    "framework/core",
    "framework/ui",
    "framework/views",
    "framework/controllers",
    "framework/helpers",
    "controllers",
    "users/userscontroller",
    "lavoratori/lavoratoriController",
    "listelavoro/listeLavoroController",
    "aziende/aziendeController",
    "documenti/aziendaDocumentiController",
    "reports/reportIscrittiController",
    "reports/reportIncassiQuoteController",
    "reports/reportDelegheController",
    "reports/reportDocumentiController",
    "reports/reportDocumentiAziendaController",
    "reports/reportComunicazioniController",
    "deleghe/delegheController",
    "quote/quoteImpiantiController",
    "documenti/documentiController",
    "comunicazioni/comunicazioniController",
    "versamenti/versamentiController",
    "importazioneDB/importazioneController",
    "quote/quoteAssocController",  "framework/widgets","importData/importDataController",
    "analisi/analisiController",
    "trace/traceController",
    "reports/reportProvenienzaUncController",
    "reports/reportProvBilController",
    "reports/reportTotalController"], function(
        _p, core, ui, fviews, fcontrollers, fhelpers,
        controllers, usercontroller,
        lavController, listeLavoroController, azController, azDocController,
        iscrittiReportController,
        iqaReportController,
        delController, docReportController, docAziendaReportController,
        commReportController,  delegheController,
        quoteImpiantiController,
        docController,   comController,
        versController, importazController, quoteAssocController, fwidgets, importController,analisiController,
        traceController,
        provenienzaReportController,
        provBilReportController,
        totadslReportController) {

    $.datepicker.setDefaults( $.datepicker.regional[ "IT" ] );

    //register all js controllers here
    //specify singleton if controller must be a singleton
    (function registerRoutes() {
        ui.Navigation.instance().registerController("crud", function() { return new fcontrollers.CrudController(); }, "singleton");
        ui.Navigation.instance().registerController("librarydemo", function() { return new controllers.LibraryDemoController(); });
        ui.Navigation.instance().registerController("users", function() { return new usercontroller.UsersController(); }, "singleton");
        ui.Navigation.instance().registerController("tracelogins", function() { return new traceController.TraceLoginController(); }, "singleton");
        //percorsi per il lavoratore 
        ui.Navigation.instance().registerController("searchworkers", function() { return new lavController.LavoratoriSearchController(); }, "singleton");

        ui.Navigation.instance().registerController("searchworkersnazionale", function() { return new lavController.LavoratoriSearchNazionaleController(); }, "singleton");
        
        ui.Navigation.instance().registerController("summaryworker", function() { return new lavController.LavoratoreSummaryController(); }, "singleton");
        ui.Navigation.instance().registerController("editworker", function() { return new lavController.LavoratoreEditController(); }, "singleton");

        //percorsi per il crud dei documenti, delle richieste, delle comunicazioni e dello storico versamenti
        ui.Navigation.instance().registerController("documenticrud", function() { return new docController.DocumentiController(); }, "singleton");

        ui.Navigation.instance().registerController("comunicazionicrud", function() { return new comController.ComunicazioniController(); }, "singleton");
        ui.Navigation.instance().registerController("versamenti", function() { return new versController.VersamentiController(); }, "singleton");
        ui.Navigation.instance().registerController("dettaglioversamenti", function() { return new versController.VersamentiDettaglioController(); }, "singleton");

        //percorsi per l'azienda
        ui.Navigation.instance().registerController("editfirm", function() { return new azController.AziendaEditController(); }, "singleton");
        ui.Navigation.instance().registerController("summaryfirm", function() { return new azController.AziendaSummaryController(); }, "singleton");
        ui.Navigation.instance().registerController("searchfirms", function() { return new azController.SearchFirmController(); }, "singleton");

        ui.Navigation.instance().registerController("searchfirmsnazionale", function() { return new azController.GlobalSearchNazionaleFirmController(); }, "singleton");
        
        ui.Navigation.instance().registerController("firmdocscrud", function() { return new azDocController.AziendaDocumentiController(); }, "singleton");

        //percorso per liste di lavoro
        ui.Navigation.instance().registerController("searchlistelavoro", function() { return new listeLavoroController.ListeLavoroSearchController(); }, "singleton");
        ui.Navigation.instance().registerController("summarylistelavoro", function() { return new listeLavoroController.ListeLavoroSummaryController(); }, "singleton");
        ui.Navigation.instance().registerController("comparelistelavoro", function() { return new listeLavoroController.ListeLavoroComparisonController(); }, "singleton");

        //percorso per i reports
        ui.Navigation.instance().registerController("reportiscritti", function() { return new iscrittiReportController.IscrittiReportController(); }, "singleton");
        
        ui.Navigation.instance().registerController("reportiqa", function() { return new iqaReportController.IncassiQuoteReportController(); }, "singleton");
        ui.Navigation.instance().registerController("reportdeleghe", function() { return new delController.DelegheReportController(); }, "singleton");
        ui.Navigation.instance().registerController("reportdocumenti", function() { return new docReportController.DocumentiReportController(); }, "singleton");
        ui.Navigation.instance().registerController("reportdocumentiazienda", function() { return new docAziendaReportController.DocumentiAziendaReportController(); }, "singleton");
        ui.Navigation.instance().registerController("reportcomunicazioni", function() { return new commReportController.ComunicazioniReportController(); }, "singleton");




        //nuovi report
        //creati dsa felice il 30/04/2019

        //PROVENIENZA UNC
        //report
        ui.Navigation.instance().registerController("reportprovenienzaunc", function() { return new provenienzaReportController.ProvenienzaUncReportController(); }, "singleton");
        //analisi
        ui.Navigation.instance().registerController("pivotprovenienzaunc", function() { return new analisiController.PivotProvUncController(); }, "singleton");
        //riepilogo
        ui.Navigation.instance().registerController("riepilogoprovenienzaunc", function() { return new analisiController.RiepilogoProvUncController(); }, "singleton");



        //PROVENIENZA BILATERALITA
        //report
        ui.Navigation.instance().registerController("reportprovenienzabilat", function() { return new provBilReportController.ProvBilReportController(); }, "singleton");
        //analisi
        ui.Navigation.instance().registerController("pivotprovenienzabilat", function() { return new analisiController.PivotProvBilController(); }, "singleton");
        //riepilogo
        ui.Navigation.instance().registerController("riepilogoprovenienzabilat", function() { return new analisiController.RiepilogoProvBilController(); }, "singleton");

        //TOTALE
        //report
        ui.Navigation.instance().registerController("reporttotale", function () {return new totadslReportController.TotalReportController(); }, "singleton");
        //analisi
        ui.Navigation.instance().registerController("pivottotale", function() { return new analisiController.PivotTotController(); }, "singleton");
        //riepilogo
        ui.Navigation.instance().registerController("riepilogototale", function() { return new analisiController.RiepilogoTotController(); }, "singleton");



        //Percorsi deleghe
        ui.Navigation.instance().registerController("deleghehome", function() { return new delegheController.DelegheHomeController(); }, "singleton");
        ui.Navigation.instance().registerController("editdelega", function() { return new delegheController.EditDelegaController(); }, "singleton");
        ui.Navigation.instance().registerController("deletedelega", function() { return new delegheController.DeleteDelegaController(); }, "singleton");
        ui.Navigation.instance().registerController("delegastatecontroller", function() { return new delegheController.DelegaStateController(); }, "singleton");

       
        ui.Navigation.instance().registerController("importadeleghe", function() { return new delegheController.ImportaDelegheController(); }, "singleton");

        //Percorsi quote
        ui.Navigation.instance().registerController("quoteimpianti", function() { return new quoteImpiantiController.QuoteImpiantiController(); }, "singleton");

        ui.Navigation.instance().registerController("quoteimpiantinew", function() { return new quoteImpiantiController.QuoteImpiantiControllerNew(); }, "singleton");




        ui.Navigation.instance().registerController("quotesettore", function() { return new quoteImpiantiController.QuoteSettoreController(); }, "singleton");
        ui.Navigation.instance().registerController("quotebrevimano", function() { return new quoteImpiantiController.QuoteBreviManoController(); }, "singleton");
        ui.Navigation.instance().registerController("quotemanuali", function() { return new quoteImpiantiController.QuoteManualiController(); }, "singleton");
        
        ui.Navigation.instance().registerController("quoteassociative", function() { return new quoteAssocController.QuoteAssocController(); }, "singleton");
        ui.Navigation.instance().registerController("dettaglioquote", function() { return new quoteAssocController.QuotaDettaglioController; }, "singleton");

        //Percorsi per importazione DB Nazionale
        ui.Navigation.instance().registerController("importazioneDB", function() { return new importazController.ImportazioneDBController(); }, "singleton");


        //importazioni massive
        ui.Navigation.instance().registerController("importanagrafichegenerali", function() { return new importController.ImportaAnagraficheController(); }, "singleton");
        ui.Navigation.instance().registerController("importdeleghegenerali", function() { return new importController.ImportaDelegheController(); }, "singleton");
        ui.Navigation.instance().registerController("importquotegenerali", function() { return new importController.ImportaQuoteController(); }, "singleton");

        //importazioni massive FELICE
        ui.Navigation.instance().registerController("importdatiunc", function() { return new importController.ImportaDatiUncController(); }, "singleton");
        ui.Navigation.instance().registerController("importdatibil", function() { return new importController.ImportaDatiBilController(); }, "singleton");







        ui.Navigation.instance().registerController("importdocumentigenerali", function() { return new importController.ImportaDocumentiController(); }, "singleton");

        ui.Navigation.instance().registerController("riepilogo", function() { return new analisiController.RiepilogoController(); }, "singleton");
        ui.Navigation.instance().registerController("pivot", function() { return new analisiController.PivotController(); }, "singleton");

        ui.Navigation.instance().registerController("riepilogodeleghe", function() { return new analisiController.RiepilogoDelegheController(); }, "singleton");
        ui.Navigation.instance().registerController("pivotdeleghe", function() { return new analisiController.PivotDelegheController(); }, "singleton");


    })();


    fhelpers.AutoForm.instance().scan();

    //ajax navigation entry point
    ui.Navigation.instance().start();



    fwidgets.Form.registerComponent("customFileUploader", function(element){


        var uploadButton = $(element).find("[data-sub-component=upload_button]");
        var deleteButton = $(element).find("[data-sub-component=delete_button]");
        var downloadButton = $(element).find("[data-sub-component=download_button]");
        var filenameInput = $(element).find("[data-sub-component=file_name_input]");
        var progress = $(element).find("[data-sub-component=progress]");
        var upload_value = $(element).find("[data-sub-component=upload_value]");
        var action = $(element).attr("data-action");
        var path = $(element).attr("data-path");

        var fileUploader = new fwidgets.FileUploader();

        fileUploader.set({
            url: BASE + action,
            button : uploadButton,
            progressBar: progress,
            data: { path: path },
            input: upload_value,
            filenameInput: filenameInput,
            downloadButton: downloadButton
        });

        //pulsante per rimuovere l'allegato
        deleteButton.click(function(){
            upload_value.val("");
            filenameInput.val("");
        });


        fileUploader.on("complete", function(data){

            //qui recupero il nome del file fisico e il nmome del file orginale
            var  originalFilename = filenameInput.val();
            var  physicalFilename = data.value;

           


        });


        this.callback("after-render", function() {
            fileUploader.init();
        });



    });

});
