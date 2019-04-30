package applica.feneal.admin.viewmodel;

public class BonificoDto {
    private String note;
    private long[] quoteIds;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long[] getQuoteIds() {
        return quoteIds;
    }

    public void setQuoteIds(long[] quoteIds) {
        this.quoteIds = quoteIds;
    }
}
