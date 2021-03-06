package applica.feneal.domain.model.geo;

import org.springframework.util.StringUtils;

import java.util.Hashtable;


public class ComprensoriTrentino {

    private Hashtable<String,String> comprensori;

    public ComprensoriTrentino (){
        comprensori = new Hashtable<>();
        comprensori.put("aldino * aldein","OLTRADIGE-BASSA ATESINA");
        comprensori.put("andriano * andrian","OLTRADIGE-BASSA ATESINA");
        comprensori.put("anterivo * altrei","OLTRADIGE-BASSA ATESINA");
        comprensori.put("appiano sulla strada del vino * eppan","OLTRADIGE-BASSA ATESINA");
        comprensori.put("avelengo * hafling","BURGRAVIATO");
        comprensori.put("badia * abtei","VAL PUSTERIA");
        comprensori.put("barbiano * barbian","VAL D'ISARCO");
        comprensori.put("bolzano * bozen","BOLZANO");
        comprensori.put("braies * prags","VAL PUSTERIA");
        comprensori.put("brennero * brenner","ALTA VAL D'ISARCO");
        comprensori.put("bressanone * brixen","VAL D'ISARCO");
        comprensori.put("bronzolo * branzoll","OLTRADIGE-BASSA ATESINA");
        comprensori.put("brunico * bruneck","VAL PUSTERIA");
        comprensori.put("caines * kuens","BURGRAVIATO");
        comprensori.put("caldaro sulla strada del vino * kaltern","OLTRADIGE-BASSA ATESINA");
        comprensori.put("campo di trens * freienfeld","ALTA VAL D'ISARCO");
        comprensori.put("campo tures * sand in taufers","VAL PUSTERIA");
        comprensori.put("castelbello ciardes * kastelbell tschars","VAL VENOSTA");
        comprensori.put("castelrotto * kastelruth","SALTO-SCILIAR");
        comprensori.put("cermes * tscherms","BURGRAVIATO");
        comprensori.put("chienes * kiens","VAL PUSTERIA");
        comprensori.put("chiusa * klausen","VAL D'ISARCO");
        comprensori.put("cornedo all'isarco * karneid","SALTO-SCILIAR");
        comprensori.put("cortaccia sulla strada del vino * kurtatsch","OLTRADIGE-BASSA ATESINA");
        comprensori.put("cortina sulla strada del vino * kurtinig","OLTRADIGE-BASSA ATESINA");
        comprensori.put("corvara in badia * kurfar","VAL PUSTERIA");
        comprensori.put("curon venosta * graun in vinschgau","VAL VENOSTA");
        comprensori.put("dobbiaco * toblach","VAL PUSTERIA");
        comprensori.put("egna * neumarkt","OLTRADIGE-BASSA ATESINA");
        comprensori.put("falzes * pfalzen","VAL PUSTERIA");
        comprensori.put("fie' allo sciliar * vols am schlern","SALTO-SCILIAR");
        comprensori.put("fortezza * franzensfesten","ALTA VAL D'ISARCO");
        comprensori.put("funes * villnoss","VAL D'ISARCO");
        comprensori.put("gais * gais","VAL PUSTERIA");
        comprensori.put("gargazzone * gargazon","BURGRAVIATO");
        comprensori.put("glorenza * glurns","VAL VENOSTA");
        comprensori.put("laces * latsch","VAL VENOSTA");
        comprensori.put("lagundo * algund","BURGRAVIATO");
        comprensori.put("laion * lajen","VAL D'ISARCO");
        comprensori.put("laives * leifers","OLTRADIGE-BASSA ATESINA");
        comprensori.put("lana * lana","BURGRAVIATO");
        comprensori.put("lasa * laas","VAL VENOSTA");
        comprensori.put("lauregno * laurein","BURGRAVIATO");
        comprensori.put("luson * lusen","VAL D'ISARCO");
        comprensori.put("magre' sulla strada del vino * margreid","OLTRADIGE-BASSA ATESINA");
        comprensori.put("malles venosta * mals","VAL VENOSTA");
        comprensori.put("la valle * wengen","VAL PUSTERIA");
        comprensori.put("marlengo * marling","BURGRAVIATO");
        comprensori.put("martello * martell","VAL VENOSTA");
        comprensori.put("meltina * molten","SALTO-SCILIAR");
        comprensori.put("merano * meran","BURGRAVIATO");
        comprensori.put("marebbe * enneberg","VAL PUSTERIA");
        comprensori.put("montagna * montan","OLTRADIGE-BASSA ATESINA");
        comprensori.put("moso in passiria * moos in passeier","BURGRAVIATO");
        comprensori.put("nalles * nals","BURGRAVIATO");
        comprensori.put("naturno * naturns","BURGRAVIATO");
        comprensori.put("naz sciaves * natz schabs","VAL D'ISARCO");
        comprensori.put("nova levante * welschnofen","SALTO-SCILIAR");
        comprensori.put("nova ponente * deutschnofen","SALTO-SCILIAR");
        comprensori.put("ora * auer","OLTRADIGE-BASSA ATESINA");
        comprensori.put("ortisei * st ulrich","SALTO-SCILIAR");
        comprensori.put("parcines * partschins","BURGRAVIATO");
        comprensori.put("monguelfo * welsberg","VAL PUSTERIA");
        comprensori.put("plaus * plaus","BURGRAVIATO");
        comprensori.put("ponte gardena * waidbruck","VAL D'ISARCO");
        comprensori.put("postal * burgstall","BURGRAVIATO");
        comprensori.put("prato allo stelvio * prad am stilfserioch","VAL VENOSTA");
        comprensori.put("perca * percha","VAL PUSTERIA");
        comprensori.put("proves * proveis","BURGRAVIATO");
        comprensori.put("racines * ratschings","ALTA VAL D'ISARCO");
        comprensori.put("predoi * prettau","VAL PUSTERIA");
        comprensori.put("renon * ritten","SALTO-SCILIAR");
        comprensori.put("rifiano * riffian","BURGRAVIATO");
        comprensori.put("rio di pusteria * muhlbach","VAL D'ISARCO");
        comprensori.put("rodengo * rodeneck","VAL D'ISARCO");
        comprensori.put("salorno * salurn","OLTRADIGE-BASSA ATESINA");
        comprensori.put("rasun anterselva * rasen antholz","VAL PUSTERIA");
        comprensori.put("san genesio atesino * jenesien","SALTO-SCILIAR");
        comprensori.put("san leonardo in passiria * st leonhard in passeier","BURGRAVIATO");
        comprensori.put("san candido * innichen","VAL PUSTERIA");
        comprensori.put("san lorenzo di sebato * st lorenzen","VAL PUSTERIA");
        comprensori.put("san martino in passiria * st martin in passeier","BURGRAVIATO");
        comprensori.put("san pancrazio * st pankraz","BURGRAVIATO");
        comprensori.put("santa cristina valgardena * st christina in groden","SALTO-SCILIAR");
        comprensori.put("sarentino * sarntal","SALTO-SCILIAR");
        comprensori.put("scena * schonna","BURGRAVIATO");
        comprensori.put("san martino in badia * st martin in thurn","VAL PUSTERIA");
        comprensori.put("selva di val gardena * wolkenstein in groden","SALTO-SCILIAR");
        comprensori.put("senales * schnals","VAL VENOSTA");
        comprensori.put("selva dei molini * muhlwald","VAL PUSTERIA");
        comprensori.put("silandro * schlanders","VAL VENOSTA");
        comprensori.put("sluderno * schluderns","VAL VENOSTA");
        comprensori.put("stelvio * stilfs","VAL VENOSTA");
        comprensori.put("sesto * sexten","VAL PUSTERIA");
        comprensori.put("terlano * terlan","OLTRADIGE-BASSA ATESINA");
        comprensori.put("termeno sulla strada del vino * tramin","OLTRADIGE-BASSA ATESINA");
        comprensori.put("tesimo * tisens","BURGRAVIATO");
        comprensori.put("tires * tiers","SALTO-SCILIAR");
        comprensori.put("tirolo * tirol","BURGRAVIATO");
        comprensori.put("trodena * truden","OLTRADIGE-BASSA ATESINA");
        comprensori.put("tubre * taufers in munsterthal","VAL VENOSTA");
        comprensori.put("ultimo * ulten","BURGRAVIATO");
        comprensori.put("vadena * pfatten","OLTRADIGE-BASSA ATESINA");
        comprensori.put("terento * terenten","VAL PUSTERIA");
        comprensori.put("val di vizze * pfitsch","ALTA VAL D'ISARCO");
        comprensori.put("valdaora * olang","VAL PUSTERIA");
        comprensori.put("valle aurina * ahrntal","VAL PUSTERIA");
        comprensori.put("valle di casies * gsies","VAL PUSTERIA");
        comprensori.put("varna * vahrn","VAL D'ISARCO");
        comprensori.put("verano * voran","BURGRAVIATO");
        comprensori.put("vandoies * vintl","VAL PUSTERIA");
        comprensori.put("villandro * villanders","VAL D'ISARCO");
        comprensori.put("vipiteno * sterzing","ALTA VAL D'ISARCO");
        comprensori.put("velturno * feldthurns","VAL D'ISARCO");
        comprensori.put("villabassa * villabassa","VAL PUSTERIA");
        comprensori.put("senale-san felice * unsere liebe frau im walde-st felix","BURGRAVIATO");
        comprensori.put("ala","ROVERETO E VALLAGARINA");
        comprensori.put("albiano","ROTALIANA E CEMBRA");
        comprensori.put("aldeno","VAL DI NON E SOL");
        comprensori.put("amblar","VAL DI NON E SOL");
        comprensori.put("andalo","VAL DI NON E SOL");
        comprensori.put("arco","ALTO GARDA E LEDRO");
        comprensori.put("avio","ROVERETO E VALLAGARINA");
        comprensori.put("baselga di pine'","VALSUGANA E PRIMIERO");
        comprensori.put("bedollo","VALSUGANA E PRIMIERO");
        comprensori.put("bersone","GIUDICARIE E RENDENA");
        comprensori.put("besenello","ROVERETO E VALLAGARINA");
        comprensori.put("bezzecca","ALTO GARDA E LEDRO");
        comprensori.put("bieno (tn)","VALSUGANA E PRIMIERO");
        comprensori.put("bleggio inferiore","GIUDICARIE E RENDENA");
        comprensori.put("bleggio superiore","GIUDICARIE E RENDENA");
        comprensori.put("bocenago","GIUDICARIE E RENDENA");
        comprensori.put("bolbeno","GIUDICARIE E RENDENA");
        comprensori.put("bondo","GIUDICARIE E RENDENA");
        comprensori.put("bondone","TRENTO E VALLE DEI LAGHI");
        comprensori.put("borgo valsugana","VALSUGANA E PRIMIERO");
        comprensori.put("bosentino","VALSUGANA E PRIMIERO");
        comprensori.put("breguzzo","GIUDICARIE E RENDENA");
        comprensori.put("brentonico","ROVERETO E VALLAGARINA");
        comprensori.put("bresimo","VAL DI NON E SOL");
        comprensori.put("brez","VAL DI NON E SOL");
        comprensori.put("brione (bs)","GIUDICARIE E RENDENA");
        comprensori.put("caderzone","GIUDICARIE E RENDENA");
        comprensori.put("cagno'","VAL DI NON E SOL");
        comprensori.put("calavino","TRENTO E VALLE DEI LAGHI");
        comprensori.put("calceranica al lago","VALSUGANA E PRIMIERO");
        comprensori.put("caldes","VAL DI NON E SOL");
        comprensori.put("caldonazzo","VALSUGANA E PRIMIERO");
        comprensori.put("calliano (tn)","ROVERETO E VALLAGARINA");
        comprensori.put("campitello di fassa","VAL DI FIEMME E FASSA");
        comprensori.put("campodenno","VAL DI NON E SOL");
        comprensori.put("canale san bovo","VALSUGANA E PRIMIERO");
        comprensori.put("canazei","VAL DI FIEMME E FASSA");
        comprensori.put("capriana","VAL DI FIEMME E FASSA");
        comprensori.put("carano","VAL DI FIEMME E FASSA");
        comprensori.put("carisolo","GIUDICARIE E RENDENA");
        comprensori.put("carzano","VALSUGANA E PRIMIERO");
        comprensori.put("castel condino","GIUDICARIE E RENDENA");
        comprensori.put("castelfondo","VAL DI NON E SOL");
        comprensori.put("castello-molina di fiemme","VAL DI FIEMME E FASSA");
        comprensori.put("castello tesino","VALSUGANA E PRIMIERO");
        comprensori.put("castelnuovo","VALSUGANA E PRIMIERO");
        comprensori.put("cavalese","VAL DI FIEMME E FASSA");
        comprensori.put("cavareno","VAL DI NON E SOL");
        comprensori.put("cavedago","TRENTO E VALLE DEI LAGHI");
        comprensori.put("cavedine","ROVERETO E VALLAGARINA");
        comprensori.put("cavizzana","VAL DI NON E SOL");
        comprensori.put("cembra","ROTALIANA E CEMBRA");
        comprensori.put("centa san nicolo'","VALSUGANA E PRIMIERO");
        comprensori.put("cimego","GIUDICARIE E RENDENA");
        comprensori.put("cimone","TRENTO E VALLE DEI LAGHI");
        comprensori.put("cinte tesino","VALSUGANA E PRIMIERO");
        comprensori.put("cis","VAL DI NON E SOL");
        comprensori.put("civezzano","TRENTO E VALLE DEI LAGHI");
        comprensori.put("cles","VAL DI NON E SOL");
        comprensori.put("cloz","VAL DI NON E SOL");
        comprensori.put("commezzadura","VAL DI NON E SOL");
        comprensori.put("concei","ALTO GARDA E LEDRO");
        comprensori.put("condino","ALTO GARDA E LEDRO");
        comprensori.put("coredo","VAL DI NON E SOL");
        comprensori.put("croviana","VAL DI NON E SOL");
        comprensori.put("cunevo","VAL DI NON E SOL");
        comprensori.put("daiano","VAL DI FIEMME E FASSA");
        comprensori.put("dambel","VAL DI NON E SOL");
        comprensori.put("daone","GIUDICARIE E RENDENA");
        comprensori.put("dare'","GIUDICARIE E RENDENA");
        comprensori.put("denno","VAL DI NON E SOL");
        comprensori.put("dimaro","VAL DI NON E SOL");
        comprensori.put("don","VAL DI NON E SOL");
        comprensori.put("dorsino","GIUDICARIE E RENDENA");
        comprensori.put("drena","ALTO GARDA E LEDRO");
        comprensori.put("dro","ALTO GARDA E LEDRO");
        comprensori.put("faedo","ROTALIANA E CEMBRA");
        comprensori.put("fai della paganella","VAL DI NON E SOL");
        comprensori.put("faver","ROTALIANA E CEMBRA");
        comprensori.put("fiave'","GIUDICARIE E RENDENA");
        comprensori.put("fiera di primiero","VALSUGANA E PRIMIERO");
        comprensori.put("fierozzo","VALSUGANA E PRIMIERO");
        comprensori.put("flavon","VAL DI NON E SOL");
        comprensori.put("folgaria","VAL DI NON E SOL");
        comprensori.put("fondo","VAL DI NON E SOL");
        comprensori.put("fornace","VALSUGANA E PRIMIERO");
        comprensori.put("frassilongo","VALSUGANA E PRIMIERO");
        comprensori.put("garniga terme","ROVERETO E VALLAGARINA");
        comprensori.put("giovo","ROTALIANA E CEMBRA");
        comprensori.put("giustino","GIUDICARIE E RENDENA");
        comprensori.put("grauno","TRENTO E VALLE DEI LAGHI");
        comprensori.put("grigno","VALSUGANA E PRIMIERO");
        comprensori.put("grumes","TRENTO E VALLE DEI LAGHI");
        comprensori.put("imer","VALSUGANA E PRIMIERO");
        comprensori.put("isera","ROVERETO E VALLAGARINA");
        comprensori.put("ivano-fracena","VALSUGANA E PRIMIERO");
        comprensori.put("lardaro","GIUDICARIE E RENDENA");
        comprensori.put("lasino","TRENTO E VALLE DEI LAGHI");
        comprensori.put("lavarone","VALSUGANA E PRIMIERO");
        comprensori.put("lavis","TRENTO E VALLE DEI LAGHI");
        comprensori.put("levico terme","VALSUGANA E PRIMIERO");
        comprensori.put("lisignago","ROTALIANA E CEMBRA");
        comprensori.put("livo (co)","VAL DI NON E SOL");
        comprensori.put("lomaso","ROVERETO E VALLAGARINA");
        comprensori.put("lona-lases","ROTALIANA E CEMBRA");
        comprensori.put("luserna (tn)","ROVERETO E VALLAGARINA");
        comprensori.put("male'","GIUDICARIE E RENDENA");
        comprensori.put("malosco","VAL DI NON E SOL");
        comprensori.put("massimeno","GIUDICARIE E RENDENA");
        comprensori.put("mazzin","VAL DI FIEMME E FASSA");
        comprensori.put("mezzana","VAL DI NON E SOL");
        comprensori.put("mezzano","VALSUGANA E PRIMIERO");
        comprensori.put("mezzocorona","ROTALIANA E CEMBRA");
        comprensori.put("mezzolombardo","ROTALIANA E CEMBRA");
        comprensori.put("moena","VAL DI FIEMME E FASSA");
        comprensori.put("molina di ledro","ALTO GARDA E LEDRO");
        comprensori.put("molveno","TRENTO E VALLE DEI LAGHI");
        comprensori.put("monclassico","VAL DI NON E SOL");
        comprensori.put("montagne","GIUDICARIE E RENDENA");
        comprensori.put("mori","ROVERETO E VALLAGARINA");
        comprensori.put("nago-torbole","ALTO GARDA E LEDRO");
        comprensori.put("nanno","VAL DI NON E SOL");
        comprensori.put("nave san rocco","TRENTO E VALLE DEI LAGHI");
        comprensori.put("nogaredo","ROVERETO E VALLAGARINA");
        comprensori.put("nomi","ROVERETO E VALLAGARINA");
        comprensori.put("novaledo","VALSUGANA E PRIMIERO");
        comprensori.put("ospedaletto","VALSUGANA E PRIMIERO");
        comprensori.put("ossana","VAL DI NON E SOL");
        comprensori.put("padergnone","ROVERETO E VALLAGARINA");
        comprensori.put("palu' del fersina","VALSUGANA E PRIMIERO");
        comprensori.put("panchia'","VAL DI FIEMME E FASSA");
        comprensori.put("ronzo-chienis","ROVERETO E VALLAGARINA");
        comprensori.put("pejo","VAL DI NON E SOL");
        comprensori.put("pellizzano","VAL DI NON E SOL");
        comprensori.put("pelugo","GIUDICARIE E RENDENA");
        comprensori.put("pergine valsugana","VALSUGANA E PRIMIERO");
        comprensori.put("pieve di bono","GIUDICARIE E RENDENA");
        comprensori.put("pieve di ledro","ALTO GARDA E LEDRO");
        comprensori.put("pieve tesino","VALSUGANA E PRIMIERO");
        comprensori.put("pinzolo","GIUDICARIE E RENDENA");
        comprensori.put("pomarolo","ROVERETO E VALLAGARINA");
        comprensori.put("pozza di fassa","VAL DI FIEMME E FASSA");
        comprensori.put("praso","GIUDICARIE E RENDENA");
        comprensori.put("predazzo","VAL DI FIEMME E FASSA");
        comprensori.put("preore","GIUDICARIE E RENDENA");
        comprensori.put("prezzo","TRENTO E VALLE DEI LAGHI");
        comprensori.put("rabbi","VAL DI NON E SOL");
        comprensori.put("ragoli","GIUDICARIE E RENDENA");
        comprensori.put("revo'","VAL DI NON E SOL");
        comprensori.put("riva del garda","ALTO GARDA E LEDRO");
        comprensori.put("romallo","VAL DI NON E SOL");
        comprensori.put("romeno","VAL DI NON E SOL");
        comprensori.put("roncegno","VALSUGANA E PRIMIERO");
        comprensori.put("ronchi valsugana","VALSUGANA E PRIMIERO");
        comprensori.put("roncone","GIUDICARIE E RENDENA");
        comprensori.put("ronzone","VAL DI NON E SOL");
        comprensori.put("rovere' della luna","ROTALIANA E CEMBRA");
        comprensori.put("rovereto (tn)","ROVERETO E VALLAGARINA");
        comprensori.put("ruffre'","VAL DI NON E SOL");
        comprensori.put("rumo","VAL DI NON E SOL");
        comprensori.put("sagron mis","VALSUGANA E PRIMIERO");
        comprensori.put("samone (tn)","VALSUGANA E PRIMIERO");
        comprensori.put("san lorenzo in banale","GIUDICARIE E RENDENA");
        comprensori.put("san michele all'adige","ROTALIANA E CEMBRA");
        comprensori.put("sant'orsola terme","VALSUGANA E PRIMIERO");
        comprensori.put("sanzeno","VAL DI NON E SOL");
        comprensori.put("sarnonico","VAL DI NON E SOL");
        comprensori.put("scurelle","VALSUGANA E PRIMIERO");
        comprensori.put("segonzano","ROTALIANA E CEMBRA");
        comprensori.put("sfruz","VAL DI NON E SOL");
        comprensori.put("siror","VALSUGANA E PRIMIERO");
        comprensori.put("smarano","VAL DI NON E SOL");
        comprensori.put("soraga","VAL DI FIEMME E FASSA");
        comprensori.put("sover","ROTALIANA E CEMBRA");
        comprensori.put("spera","VALSUGANA E PRIMIERO");
        comprensori.put("spiazzo","GIUDICARIE E RENDENA");
        comprensori.put("spormaggiore","VAL DI NON E SOL");
        comprensori.put("sporminore","VAL DI NON E SOL");
        comprensori.put("stenico","GIUDICARIE E RENDENA");
        comprensori.put("storo","GIUDICARIE E RENDENA");
        comprensori.put("strembo","GIUDICARIE E RENDENA");
        comprensori.put("strigno","VALSUGANA E PRIMIERO");
        comprensori.put("taio","VAL DI NON E SOL");
        comprensori.put("tassullo","VAL DI NON E SOL");
        comprensori.put("telve di sotto","VALSUGANA E PRIMIERO");
        comprensori.put("telve di sopra","VALSUGANA E PRIMIERO");
        comprensori.put("tenna","VALSUGANA E PRIMIERO");
        comprensori.put("tenno","ALTO GARDA E LEDRO");
        comprensori.put("terlago","ALTO GARDA E LEDRO");
        comprensori.put("terragnolo","ROVERETO E VALLAGARINA");
        comprensori.put("terres","VAL DI NON E SOL");
        comprensori.put("terzolas","VAL DI NON E SOL");
        comprensori.put("tesero","VAL DI FIEMME E FASSA");
        comprensori.put("tiarno di sopra","ALTO GARDA E LEDRO");
        comprensori.put("tiarno di sotto","ALTO GARDA E LEDRO");
        comprensori.put("tione di trento","GIUDICARIE E RENDENA");
        comprensori.put("ton","VAL DI NON E SOL");
        comprensori.put("tonadico","VALSUGANA E PRIMIERO");
        comprensori.put("torcegno","VALSUGANA E PRIMIERO");
        comprensori.put("trambileno","ROVERETO E VALLAGARINA");
        comprensori.put("transacqua","VALSUGANA E PRIMIERO");
        comprensori.put("trento","TRENTO E VALLE DEI LAGHI");
        comprensori.put("tres","VAL DI NON E SOL");
        comprensori.put("tuenno","VAL DI NON E SOL");
        comprensori.put("valda","TRENTO E VALLE DEI LAGHI");
        comprensori.put("valfloriana","VAL DI FIEMME E FASSA");
        comprensori.put("vallarsa","ROVERETO E VALLAGARINA");
        comprensori.put("varena","VAL DI FIEMME E FASSA");
        comprensori.put("vattaro","VALSUGANA E PRIMIERO");
        comprensori.put("vermiglio","VAL DI NON E SOL");
        comprensori.put("vervo'","VAL DI NON E SOL");
        comprensori.put("vezzano","TRENTO E VALLE DEI LAGHI");
        comprensori.put("vignola-falesina","VALSUGANA E PRIMIERO");
        comprensori.put("vigo di fassa","VAL DI FIEMME E FASSA");
        comprensori.put("vigolo vattaro","VALSUGANA E PRIMIERO");
        comprensori.put("vigo rendena","GIUDICARIE E RENDENA");
        comprensori.put("villa agnedo","VALSUGANA E PRIMIERO");
        comprensori.put("villa lagarina","TRENTO E VALLE DEI LAGHI");
        comprensori.put("villa rendena","GIUDICARIE E RENDENA");
        comprensori.put("volano","ROVERETO E VALLAGARINA");
        comprensori.put("zambana","ROTALIANA E CEMBRA");
        comprensori.put("ziano di fiemme","VAL DI FIEMME E FASSA");
        comprensori.put("zuclo","GIUDICARIE E RENDENA");


    }

    public String getComprensorio(String comune){

        if (StringUtils.isEmpty(comune))
            return "";

        if (comprensori.containsKey(comune.toLowerCase()))
            return comprensori.get(comune.toLowerCase());

        return "";

    }


}
