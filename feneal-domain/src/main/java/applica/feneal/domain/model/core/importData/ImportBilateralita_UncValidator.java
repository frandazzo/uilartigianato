package applica.feneal.domain.model.core.importData;

import fr.opensagres.xdocreport.core.utils.StringUtils;

import java.util.Hashtable;

public class ImportBilateralita_UncValidator extends applica.framework.management.csv.RowValidator {

    @Override
    public void validateRow(Hashtable<String, String> row) {
        if (row.get("Codice Fiscale") == null) {
            this.error = "Manca il campo cod. fiscale";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("Codice Fiscale").toString().trim())) {
                this.error = "Manca il campo cod. fiscale";
                this.valid = false;
            }
        }

        if (row.get("Cognome") == null) {
            this.error = this.error + "\n Manca il campo cognome utente";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("Cognome").toString().trim())) {
                this.error = this.error + "\n Manca il campo cognome utente";
                this.valid = false;
            }
        }


        if (row.get("Nome") == null) {
            this.error = this.error + "\n Manca il campo nome utente";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("Nome").toString().trim())) {
                this.error = this.error + "\n Manca il campo nome utente";
                this.valid = false;
            }
        }


        if (row.get("Categoria") == null) {
            this.error = this.error + "\n Manca il campo categoria";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("Categoria").toString().trim())) {
                this.error = this.error + "\n Manca il campo categoria";
                this.valid = false;
            }
        }

        if (row.get("Territorio") == null) {
            this.error = this.error + "\n Manca il campo Territorio";
            this.valid = false;
        } else {
            if (StringUtils.isEmpty(row.get("Territorio").toString().trim())) {
                this.error = this.error + "\n Manca il campo Territorio";
                this.valid = false;
            }
        }

    }

}