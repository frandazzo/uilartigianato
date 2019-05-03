define(["analisi/riepilogoTotData"
], function(dataService) {

    var exports = {};

    var Pivot = function(){

    };

    Pivot.prototype.init = function(){

        var self = this;

        dataService.getPivotData().done(function (data) {

            var pivotGridChart = $("#pivotgrid-chart").dxChart({
                commonSeriesSettings: {
                    type: "bar"
                },
                tooltip: {
                    enabled: true,
                    customizeTooltip: function (args) {
                        var valueText =  args.originalValue;

                        return {
                            html: args.seriesName + "<div class='currency'>"
                                + valueText + "</div>"
                        };
                    }
                },
                "export": {
                    enabled: true
                },
                size: {
                    height: 200
                },
                adaptiveLayout: {
                    width: 450
                }
            }).dxChart("instance");

            var pivotGrid = $("#grid").dxPivotGrid({
                // height: 800,
                fieldChooser: {
                    allowSearch: true
                },
                scrolling: { mode: "virtual" },
                fieldPanel: { visible: true },
                allowSorting: true,
                allowSortingBySummary: true,
                allowFiltering: true,
                showBorders: true,
                showColumnGrandTotals: false,
                showRowGrandTotals: true,
                showRowTotals: true,
                showColumnTotals: true,
                "export": {
                    enabled: true,
                    fileName: "PivotTotale"
                },
                onCellClick: function(e) {

                },
                loadPanel: {
                    position: {
                        of: $(window),
                        at: "center",
                        my: "center"
                    }
                },
                dataSource: {
                    store: {
                        type: 'array',
                        data:data
                    },
                    fields: [
                        { dataField: "anno", area: "column", sortByPath: [] },
                        { dataField: "regione", area: "row",  sortOrder: "desc" },
                        // { dataField: "categoria", area: "filter",  sortOrder: "desc" },
                        { dataField: "provincia", area: "filter",  sortOrder: "desc" },
                        { dataField: "nazionalita", area: "filter",  sortOrder: "desc" },
                        { dataField: "settore", area: "filter",  sortOrder: "desc" },
                        { dataField: "id_Lavoratore", caption: "Num. Lavoratori", summaryType: "count", area: "data" }
                    ],
                    // remoteOperations: true,
                    // store: DevExpress.data.AspNet.createStore({
                    //     key: "ID",
                    //     loadUrl: generateUrl()
                    // })
                }
            }).dxPivotGrid("instance");


            pivotGrid.bindChart(pivotGridChart, {
                dataFieldsDisplayMode: "splitPanes",
                alternateDataFields: false
            });

        });
    };

    exports.Pivot = Pivot;

    return exports;
});
