package applica.feneal.services.impl;

import applica.feneal.domain.data.geo.CitiesRepository;
import applica.feneal.domain.data.geo.CountriesRepository;
import applica.feneal.domain.data.geo.ProvinceRepository;
import applica.feneal.domain.data.geo.RegonsRepository;
import applica.feneal.domain.model.core.lavoratori.FiscalData;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Country;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.model.geo.Region;
import applica.feneal.services.GeoService;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.library.options.OptionsManager;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import it.uilwebapp.services.*;
import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fgran on 09/04/2016.
 */
@Service
public class GeoServiceImpl implements GeoService {
@Autowired
    OptionsManager optMan;

   @Autowired
   private CountriesRepository couRep;

    @Autowired
    private RegonsRepository regRep;

    @Autowired
    private CitiesRepository citRep;

    @Autowired
    private ProvinceRepository proRep;

    private static final String[] citta_bolzano= {
            "Avelengo ",
            "Badia ",
            "Barbiano ",
            "Bolzano ",
            "Braies ",
            "Brennero ",
            "Bressanone ",
            "Bronzolo ",
            "Brunico ",
            "Caines ",
            "Caldaro Sulla Strada Del Vino ",
            "Campo Di Trens ",
            "Campo Tures ",
            "Castelbello Ciardes ",
            "Castelrotto ",
            "Cermes ",
            "Chienes ",
            "Chiusa ",
            "Cornedo All'Isarco ",
            "Cortaccia Sulla Strada Del Vino ",
            "Cortina Sulla Strada Del Vino ",
            "Corvara In Badia ",
            "Curon Venosta ",
            "Dobbiaco ",
            "Egna ",
            "Falzes ",
            "Fie' Allo Sciliar ",
            "Fortezza ",
            "Funes ",
            "Gais ",
            "Gargazzone ",
            "Glorenza ",
            "La Valle ",
            "Laces ",
            "Lagundo ",
            "Laion ",
            "Laives ",
            "Lana ",
            "Lasa ",
            "Lauregno ",
            "Luson ",
            "Magre' Sulla Strada Del Vino ",
            "Malles Venosta ",
            "Marebbe ",
            "Martello ",
            "Meltina ",
            "Merano ",
            "Monguelfo ",
            "Montagna ",
            "Moso In Passiria ",
            "Nalles ",
            "Naturno ",
            "Naz Sciaves ",
            "Nova Levante ",
            "Nova Ponente ",
            "Ora ",
            "Ortisei ",
            "Parcines ",
            "Perca ",
            "Plaus ",
            "Ponte Gardena ",
            "Postal ",
            "Prato Allo Stelvio ",
            "Predoi ",
            "Proves ",
            "Racines ",
            "Rasun Anterselva ",
            "Renon ",
            "Rifiano ",
            "Rio Di Pusteria ",
            "Rodengo ",
            "Salorno ",
            "San Candido ",
            "San Genesio Atesino ",
            "San Leonardo In Passiria ",
            "San Lorenzo Di Sebato ",
            "San Pancrazio ",
            "Santa Cristina Valgardena ",
            "Sarentino ",
            "Scena ",
            "Selva Dei Molini ",
            "Selva Di Val Gardena ",
            "Senale-san Felice ",
            "Senales ",
            "Sesto ",
            "Silandro ",
            "Sluderno ",
            "Stelvio ",
            "Terento ",
            "Terlano ",
            "Termeno Sulla Strada Del Vino ",
            "Tesimo ",
            "Tires ",
            "Tirolo ",
            "Trodena ",
            "Tubre ",
            "Ultimo ",
            "Vadena ",
            "Val Di Vizze ",
            "Valdaora ",
            "Valle Aurina ",
            "Valle Di Casies ",
            "Vandoies ",
            "Varna ",
            "Velturno ",
            "Verano ",
            "Villabassa",
            "Villandro ",
            "Vipiteno ",
            "Acereto ",
            "Albes ",
            "Alliz ",
            "Anterselva ",
            "Burgusio ",
            "Caminata In Tures ",
            "Castelbello ",
            "Casteldarne ",
            "Cauria ",
            "Cengles ",
            "Ceves ",
            "Ciardes ",
            "Clusio ",
            "Coldrano ",
            "Colfosco ",
            "Colle In Casies ",
            "Colli In Pusteria ",
            "Colsano ",
            "Corti In Pusteria ",
            "Corvara In Passiria ",
            "Corzes ",
            "Covelano ",
            "Curon ",
            "Elle ",
            "Eores ",
            "Faogna Di Sotto ",
            "Fleres ",
            "Foiana ",
            "Fundres ",
            "Gries ",
            "Grimaldo ",
            "Gudon ",
            "Issengo ",
            "Lacinigo ",
            "Lappago ",
            "Laudes ",
            "Lazfons ",
            "Longiaru' ",
            "Lutago ",
            "Magre' ",
            "Maia Alta ",
            "Maia Bassa ",
            "Malles ",
            "Mantana ",
            "Maranza ",
            "Mareta ",
            "Mazia ",
            "Millan-sarness ",
            "Molini Di Tures ",
            "Monghezzo Di Fuori ",
            "Montassilone ",
            "Monte Di Mezzodi' ",
            "Monte Di Tramontana ",
            "Monte San Candido ",
            "Montechiaro ",
            "Montefontana ",
            "Monteponente ",
            "Morter",
            "Mules ",
            "Naz ",
            "Novacella ",
            "Onies ",
            "Oris ",
            "Planol ",
            "Plata ",
            "Prati ",
            "Prato Alla Drava ",
            "Quarazze ",
            "Rasun Di Sopra ",
            "Rasun Di Sotto ",
            "Resia ",
            "Ridanna ",
            "Rina ",
            "Riomolino ",
            "Riscone ",
            "Riva Di Tures ",
            "San Felice ",
            "San Giacomo ",
            "San Giorgio ",
            "San Giovanni ",
            "San Leonardo ",
            "San Martino Al Monte ",
            "San Martino In Badia ",
            "San Martino In Casies ",
            "San Martino In Passiria ",
            "San Pietro ",
            "San Sigismondo ",
            "San Valentino Al Brennero ",
            "San Valentino Alla Mutta ",
            "Sant'Andrea In Monte ",
            "Santa Maddalena In Casies ",
            "Scaleres ",
            "Sciaves ",
            "Slingia ",
            "Spinga ",
            "Stava ",
            "Stilves ",
            "Tabla ",
            "Tanas ",
            "Tarces ",
            "Tarres ",
            "Telves ",
            "Teodone ",
            "Tesido ",
            "Tiso ",
            "Trens ",
            "Tunes ",
            "Valas ",
            "Valgiovo ",
            "Vallarga ",
            "Valle San Silvestro ",
            "Vallelunga ",
            "Valles ",
            "Vandoies Di Sopra ",
            "Vandoies Di Sotto ",
            "Vanga ",
            "Versciago ",
            "Vezzano ",
            "Villa Ottone ",
            "Villa Santa Caterina ",
            "Villabassa ",
            "Vizze ",
            "Marlengo ",
            "Morter ",
            "Prato in venosta ",
            "Rasun valdaora ",
            "Senale "


    };
    private static final String[] citta_bolzano_tradotto= {
            "Avelengo * Hafling",
            "Badia * Abtei",
            "Barbiano * Barbian",
            "Bolzano * Bozen",
            "Braies * Prags",
            "Brennero * Brenner",
            "Bressanone * Brixen",
            "Bronzolo * Branzoll",
            "Brunico * Bruneck",
            "Caines * Kuens",
            "Caldaro Sulla Strada Del Vino * Kaltern",
            "Campo Di Trens * Freienfeld",
            "Campo Tures * Sand In Taufers",
            "Castelbello Ciardes * Kastelbell Tschars",
            "Castelrotto * Kastelruth",
            "Cermes * Tscherms",
            "Chienes * Kiens",
            "Chiusa * Klausen",
            "Cornedo All'Isarco * Karneid",
            "Cortaccia Sulla Strada Del Vino * Kurtatsch",
            "Cortina Sulla Strada Del Vino * Kurtinig",
            "Corvara In Badia * Kurfar",
            "Curon Venosta * Graun In Vinschgau",
            "Dobbiaco * Toblach",
            "Egna * Neumarkt",
            "Falzes * Pfalzen",
            "Fie' Allo Sciliar * Vols Am Schlern",
            "Fortezza * Franzensfesten",
            "Funes * Villnoss",
            "Gais * Gais",
            "Gargazzone * Gargazon",
            "Glorenza * Glurns",
            "La Valle * Wengen",
            "Laces * Latsch",
            "Lagundo * Algund",
            "Laion * Lajen",
            "Laives * Leifers",
            "Lana * Lana",
            "Lasa * Laas",
            "Lauregno * Laurein",
            "Luson * Lusen",
            "Magre' Sulla Strada Del Vino * Margreid",
            "Malles Venosta * Mals",
            "Marebbe * Enneberg",
            "Martello * Martell",
            "Meltina * Molten",
            "Merano * Meran",
            "Monguelfo * Welsberg",
            "Montagna * Montan",
            "Moso In Passiria * Moos In Passeier",
            "Nalles * Nals",
            "Naturno * Naturns",
            "Naz Sciaves * Natz Schabs",
            "Nova Levante * Welschnofen",
            "Nova Ponente * Deutschnofen",
            "Ora * Auer",
            "Ortisei * St Ulrich",
            "Parcines * Partschins",
            "Perca * Percha",
            "Plaus * Plaus",
            "Ponte Gardena * Waidbruck",
            "Postal * Burgstall",
            "Prato Allo Stelvio * Prad Am Stilfserioch",
            "Predoi * Prettau",
            "Proves * Proveis",
            "Racines * Ratschings",
            "Rasun Anterselva * Rasen Antholz",
            "Renon * Ritten",
            "Rifiano * Riffian",
            "Rio Di Pusteria * Muhlbach",
            "Rodengo * Rodeneck",
            "Salorno * Salurn",
            "San Candido * Innichen",
            "San Genesio Atesino * Jenesien",
            "San Leonardo In Passiria * St Leonhard In Passeier",
            "San Lorenzo Di Sebato * St Lorenzen",
            "San Pancrazio * St Pankraz",
            "Santa Cristina Valgardena * St Christina In Groden",
            "Sarentino * Sarntal",
            "Scena * Schonna",
            "Selva Dei Molini * Muhlwald",
            "Selva Di Val Gardena * Wolkenstein In Groden",
            "Senale-san Felice * Unsere Liebe Frau Im Walde-st Felix",
            "Senales * Schnals",
            "Sesto * Sexten",
            "Silandro * Schlanders",
            "Sluderno * Schluderns",
            "Stelvio * Stilfs",
            "Terento * Terenten",
            "Terlano * Terlan",
            "Termeno Sulla Strada Del Vino * Tramin",
            "Tesimo * Tisens",
            "Tires * Tiers",
            "Tirolo * Tirol",
            "Trodena * Truden",
            "Tubre * Taufers In Munsterthal",
            "Ultimo * Ulten",
            "Vadena * Pfatten",
            "Val Di Vizze * Pfitsch",
            "Valdaora * Olang",
            "Valle Aurina * Ahrntal",
            "Valle Di Casies * Gsies",
            "Vandoies * Vintl",
            "Varna * Vahrn",
            "Velturno * Feldthurns",
            "Verano * Voran",
            "Villabassa",
            "Villandro * Villanders",
            "Vipiteno * Sterzing",
            "Acereto * Ahornach",
            "Albes * Albeins",
            "Alliz * Allitz",
            "Anterselva * Antholz",
            "Burgusio * Burgeis",
            "Caminata In Tures * Kematen",
            "Castelbello * Kastelbell",
            "Casteldarne * Ehrenburg",
            "Cauria * Gfrill",
            "Cengles * Tschengls",
            "Ceves * Tschofs",
            "Ciardes * Tschars",
            "Clusio * Schleis",
            "Coldrano * Goldrain",
            "Colfosco * Colfuschg",
            "Colle In Casies * Pichl In Gsies",
            "Colli In Pusteria * Pichlern",
            "Colsano * Galsaun",
            "Corti In Pusteria * Hofern",
            "Corvara In Passiria * Rabenstein",
            "Corzes * Kortsch",
            "Covelano * Goflan",
            "Curon * Graun",
            "Elle * Ellen",
            "Eores * Afers",
            "Faogna Di Sotto * Unterpfennberg",
            "Fleres * Pflersch",
            "Foiana * Vollan",
            "Fundres * Pfunders",
            "Gries * Gries",
            "Grimaldo * Greinwalden",
            "Gudon * Gufidaun",
            "Issengo * Issing",
            "Lacinigo * Latschinig",
            "Lappago * Lappach",
            "Laudes * Laatsch",
            "Lazfons * Latzfons",
            "Longiaru' * Campill",
            "Lutago * Luttach",
            "Magre' * Margreid",
            "Maia Alta * Obermais",
            "Maia Bassa * Untermais",
            "Malles * Mals",
            "Mantana * Monthal",
            "Maranza * Maransen",
            "Mareta * Mareit",
            "Mazia * Matsch",
            "Millan-sarness * Milland-sarns",
            "Molini Di Tures * Muhlen",
            "Monghezzo Di Fuori * Getzenberg",
            "Montassilone * Tesselberg",
            "Monte Di Mezzodi' * Sonnenberg",
            "Monte Di Tramontana * Nordersberg",
            "Monte San Candido * Innichberg",
            "Montechiaro * Lichtenberg",
            "Montefontana * Tomberg",
            "Monteponente * Pfeffersberg",
            "Morter",
            "Mules * Mauls",
            "Naz * Natz",
            "Novacella * Neustift",
            "Onies * Onach",
            "Oris * Eyrs",
            "Planol * Planeil",
            "Plata * Platt",
            "Prati * Wiesen",
            "Prato Alla Drava * Winnbach",
            "Quarazze * Gratsch",
            "Rasun Di Sopra * Oberrasen",
            "Rasun Di Sotto * Niederrasen",
            "Resia * Reschen",
            "Ridanna * Riednaun",
            "Rina * Welschellen",
            "Riomolino * Muhlbach",
            "Riscone * Reischach",
            "Riva Di Tures * Rain Taufers",
            "San Felice * St Felix",
            "San Giacomo * St Jakob In Ahrn",
            "San Giorgio * St Georgen",
            "San Giovanni * St Johann In Ahrn",
            "San Leonardo * St Leonhard",
            "San Martino Al Monte * St Martin Am Vorberg",
            "San Martino In Badia * St Martin In Thurn",
            "San Martino In Casies * St Martin In Gsies",
            "San Martino In Passiria * St Martin In Passeier",
            "San Pietro * St Peter In Ahrn",
            "San Sigismondo * St Sigmund",
            "San Valentino Al Brennero * Brenner",
            "San Valentino Alla Mutta * St Valentin Auf Der Haide",
            "Sant'Andrea In Monte * St Andra",
            "Santa Maddalena In Casies * St Magdalena In Gsies",
            "Scaleres * Schalder",
            "Sciaves * Schabs",
            "Slingia * Schlinig",
            "Spinga * Spinges",
            "Stava * Staben",
            "Stilves * Stilfes",
            "Tabla * Tabland",
            "Tanas * Tannas",
            "Tarces * Tartsch",
            "Tarres * Tarsch",
            "Telves * Telfes",
            "Teodone * Dietenheim",
            "Tesido * Taisten",
            "Tiso * Theis",
            "Trens * Trens",
            "Tunes * Thuins",
            "Valas * Flaas",
            "Valgiovo * Jaufenthal",
            "Vallarga * Weitental",
            "Valle San Silvestro * Vahlen",
            "Vallelunga * Langtaufers",
            "Valles * Vals",
            "Vandoies Di Sopra * Obervintl",
            "Vandoies Di Sotto * Niedervintl",
            "Vanga * Wangen",
            "Versciago * Vierschach",
            "Vezzano * Vezzan",
            "Villa Ottone * Uttenheim",
            "Villa Santa Caterina * Aufhofen",
            "Villabassa * Niederdorf",
            "Vizze * Pfitsch",
            "Marlengo * marling",
            "Morter * morter",
            "Prato in venosta * prad",
            "Rasun valdaora * rasen olang",
            "Senale * unsere liebe frau im walde"
    };


    private boolean compresoTraCittaDiBolzanoIncomplete(String cittaSenzaTraduzione){
        for (String s : citta_bolzano) {
            if (s.trim().toLowerCase().equals(cittaSenzaTraduzione))
                return true;
        }
        return false;
    }

    private String retrieveCittaDiBolzanoTradotta(String cittaSenzaTraduzione){
        int i = 0;
        for (String s : citta_bolzano) {
            if (s.trim().toLowerCase().equals(cittaSenzaTraduzione)){
                return citta_bolzano_tradotto[i];
            }
            i++;
        }
        return cittaSenzaTraduzione;
    }


    private String checkIfCittaInProvinciaDiBolzano(String citta){
        if (StringUtils.isEmpty(citta))
            return citta;


        return  retrieveCittaDiBolzanoTradotta(citta);
    }

    @Override
    public Country getCountryByName(String countryName) {
        LoadRequest req = LoadRequest.build().filter("description",countryName, Filter.EQ);
        return couRep.find(req).findFirst().orElse(null);
    }

    @Override
    public Province getProvinceByName(String provinceName) {
        LoadRequest req = LoadRequest.build().filter("description",provinceName, Filter.EQ);
        return proRep.find(req).findFirst().orElse(null);
    }

    @Override
    public City getCityByName(String cityName) {

        String c = checkIfCittaInProvinciaDiBolzano(cityName);


        LoadRequest req = LoadRequest.build().filter("description",c, Filter.EQ);
        return citRep.find(req).findFirst().orElse(null);
    }

    @Override
    public Region getREgionByName(String regionName) {
        LoadRequest req = LoadRequest.build().filter("description",regionName, Filter.EQ);
        return regRep.find(req).findFirst().orElse(null);
    }

    @Override
    public Country getCountryById(int countryId) {
        return couRep.get(countryId).orElse(null);
    }

    @Override
    public Province getProvinceById(int provinceId) {
        return proRep.get(provinceId).orElse(null);
    }

    @Override
    public City getCityById(int cityId) {
        return citRep.get(cityId).orElse(null);
    }

    @Override
    public Region getREgionById(int regionId) {
        return regRep.get(regionId).orElse(null);
    }

    @Override
    public FiscalData getFiscalData(String fiscalCode) throws RemoteException {
        UilUtils svc = null;
        CalcolaDatiFiscaliResponse result = null;
        try {
            svc = new UilUtilsStub(null, optMan.get("applica.fenealgestutils"));

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        CalcolaDatiFiscali f = new CalcolaDatiFiscali();
        f.setCodiceFiscale(fiscalCode);
        result = svc.calcolaDatiFiscali(f);

        org.datacontract.schemas._2004._07.win_uilutils.FiscalData webResult = result.getCalcolaDatiFiscaliResult();

        FiscalData f1  = new FiscalData();

        City city = getCityByName(webResult.getComune());
        String cityId = city!=null?city.getId().toString():"";
        Province prov = null;
        if (city != null)
            prov = getProvinceById((city.getIdProvince()));
        String provinceId = prov!=null?prov.getId().toString():"";
        Country country =getCountryByName(webResult.getNazione());
        String countryId = country!=null?country.getId().toString():"";

        String sex = webResult.getSesso().equals("FEMMINA")?"F":"M";

        // mock
        f1.setComune(cityId);  // id comune Matera = 3972
        SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
        f1.setDatanascita(form.format(webResult.getDataNascita().getTime()));
        f1.setProvincia(provinceId);  // id prov Matera = 77
        f1.setNazione(countryId);   // codice Italia = 100
        f1.setSex(sex);
        return f1;
    }

    @Override
    public String calculateFiscalCode(String name, String surname, Date birthDate, String sex, String birthPlace, String birthNation) throws RemoteException {
        UilUtils svc = null;
        CalcolaCodiceFiscaleResponse result = null;
        try {
            svc = new UilUtilsStub(null, optMan.get("applica.fenealgestutils"));

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        CalcolaCodiceFiscale params = new CalcolaCodiceFiscale();
        params.setCognome(surname);
        params.setNome(name);
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDate);

        params.setDataNascita(cal);
        params.setNomeComuneNascita(birthPlace);

        params.setNomeNazione(birthNation);
        if (sex.equals("F"))
            sex = "FEMMINA";
        params.setSesso(sex);

        result = svc.calcolaCodiceFiscale(params);
        return result.getCalcolaCodiceFiscaleResult();
    }
}
