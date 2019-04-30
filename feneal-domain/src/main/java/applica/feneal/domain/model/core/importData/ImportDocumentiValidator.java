package applica.feneal.domain.model.core.importData;

import fr.opensagres.xdocreport.core.utils.StringUtils;

import java.util.Hashtable;

/**
 * Created by fgran on 11/05/2017.
 */
public class ImportDocumentiValidator extends applica.framework.management.csv.RowValidator {

    @Override
    public void validateRow(Hashtable<String, String> row) {
        if (row.get("FISCALE") == null) {
            this.error = "Manca il campo cod. fiscale";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("FISCALE").toString().trim())) {
                this.error = "Manca il campo cod. fiscale";
                this.valid = false;
            }
        }

        if (row.get("COGNOME_UTENTE") == null) {
            this.error = this.error +  "\n Manca il campo cognome utente";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("COGNOME_UTENTE").toString().trim())) {
                this.error = this.error + "\n Manca il campo cognome utente";
                this.valid = false;
            }
        }


        if (row.get("NOME_UTENTE") == null) {
            this.error = this.error +  "\n Manca il campo nome utente";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("NOME_UTENTE").toString().trim())) {
                this.error = this.error + "\n Manca il campo nome utente";
                this.valid = false;
            }
        }


        if (row.get("TIPO_DOCUMENTO") == null) {
            this.error = this.error +  "\n Manca il campo TIPO_DOCUMENTO";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("TIPO_DOCUMENTO").toString().trim())) {
                this.error = this.error + "\n Manca il campo TIPO_DOCUMENTO";
                this.valid = false;
            }
        }



    }

}