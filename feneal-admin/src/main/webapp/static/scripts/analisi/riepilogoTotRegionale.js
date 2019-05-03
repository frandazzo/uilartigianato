define(["analisi/riepilogoTotData"
], function(dataService) {

    var exports = {};

    var riepilogo = function(region){

        this.region = region;


        console.log("Regione: " + region );

        this.currentYear = null;
        this.anni = [];


    };

    riepilogo.prototype.init = function(){
        var self = this;

        //  $.loader.show({parent:'body'});

        dataService.getAnniIscrizioni().done(function (arrayAnni) {

            if (arrayAnni.length == 0){
                alert("Nessun anno trovato per le iscrizioni!!!");
                return;
            }

            self.currentYear = arrayAnni[0];

            // Call horizontalNav on the navigations wrapping element
            self.disposeTabs(arrayAnni);
            self.showCharts(self.currentYear, self.region);
            self.anni = arrayAnni;
        });
    };

    riepilogo.prototype.disposeTabs = function (years) {
        var self = this;
        var tabs = [];
        $.each(years, function (index, year) {
            tabs.push({
                text: year
            });
        });

        $("#longtabs > .tabs-container").dxTabs({
            dataSource: tabs,
            selectedIndex: 0,
            onItemClick: function (e) {
                var index = e.itemIndex;
                self.currentYear = self.anni[index];
                self.showCharts(self.currentYear, self.region);
                $('.mapContainer').show();
            }
        });

    };


    riepilogo.prototype.showCharts = function(year, region){
        var self = this;
        $.loader.show({parent:'.charts'});


        dataService.getIscrittiPerAreaGeografica(year, region).done(function (iscritti) {
            var results = iscritti.iscritti;
            $(".donught1").dxPieChart({
                type: "doughnut",
                dataSource: results,
                palette: "Red",
                title: {
                    text:  "<b>Percentuale per provincia</b> - "+ self.currentYear,
                    subtitle: region ? region : ""
                },
                legend: {
                    horizontalAlignment: "left",
                    verticalAlignment: "center",
                    margin: 5,
                    customizeText: function (arg) {

                        return arg.pointName
                    }
                },
                onPointClick: function(arg) {
                    arg.target.select();
                },
                "export": {
                    enabled: true
                },
                tooltip: {
                    enabled: true,
                    format: function(v){
                        return v
                    },
                    customizeTooltip: function() {
                        return { text: this.argumentText + "<br>"  + this.percentText};
                    }
                },
                series: [{
                    argumentField: "label",
                    valueField: "total",
                    hoverStyle: {
                        color: "#ffd700"
                    }

                }]
            });
            $(".lines1").dxChart({
                dataSource: results,
                rotated: true,
                palette: "Red",
                series: {

                },
                tooltip: {
                    enabled: true,
                    format: function(v){
                        return v
                    },
                    customizeTooltip: function() {
                        return { text: this.argumentText + "<br>"  + this.valueText};
                    }
                },
                title: {
                    text:  "<b>Totale per provincia</b> - "+ self.currentYear,

                    subtitle: region ? region : ""
                },
                "export": {
                    enabled: true
                },
                legend: {
                    visible: false
                },
                commonSeriesSettings: {
                    type: "bar",
                    valueField: "total",
                    argumentField: "label",

                },
                equalBarWidth: false,
                seriesTemplate: {
                    nameField: "label"
                },

            });

            // $(".donught1").show();
            // $(".lines1").show();

            dataService.getIscrittiPerCategoria(year, region).done(function(iscritti){

                var results = iscritti.iscritti;
                $(".donught2").dxPieChart({
                    type: "doughnut",
                    dataSource: results,
                    palette: "Red",
                    title: {
                        text: "<b>Percentuale per categoria - </b>" + self.currentYear,
                        subtitle: region ? region : ""
                    },
                    legend: {
                        horizontalAlignment: "left",
                        verticalAlignment: "center",
                        margin: 5,
                        customizeText: function (arg) {

                            return arg.pointName
                        }
                    },
                    onPointClick: function(arg) {
                        arg.target.select();
                    },
                    "export": {
                        enabled: true
                    },
                    tooltip: {
                        enabled: true,
                        format: function(v){
                            return v
                        },
                        customizeTooltip: function() {
                            return { text: this.argumentText + "<br>"  + this.percentText};
                        }
                    },
                    series: [{
                        argumentField: "label",
                        valueField: "total",
                        hoverStyle: {
                            color: "#ffd700"
                        },

                    }]
                });
                $(".lines2").dxChart({
                    dataSource: results,
                    rotated: true,
                    palette: "Red",
                    series: {

                    },
                    title: {
                        text: "<b>Totale per categoria - </b>" + self.currentYear,
                        subtitle: region ? region : ""
                    },
                    tooltip: {
                        enabled: true,
                        format: function(v){
                            return v
                        },
                        customizeTooltip: function() {
                            return { text: this.argumentText + "<br>"  + this.valueText};
                        }
                    },
                    "export": {
                        enabled: true
                    },
                    legend: {
                        visible: false
                    },
                    commonSeriesSettings: {
                        type: "bar",
                        valueField: "total",
                        argumentField: "label",

                    },
                    equalBarWidth: false,
                    seriesTemplate: {
                        nameField: "label"
                    },

                });
                // $(".donught2").show();
                // $(".lines2").show();

                $.loader.hide({parent:'.charts'});
            })

        })
    };

    exports.Riepilogo = riepilogo;

    return exports;
});
