package applica.feneal.admin.viewmodel.quote;

import java.util.List;

public class UiDettaglioQuotaView {

    private String content;
    private List<UiDettaglioQuota> quoteDetails;
    private long quotaCompanyId;

    public long getQuotaCompanyId() {
        return quotaCompanyId;
    }

    public void setQuotaCompanyId(long quotaCompanyId) {
        this.quotaCompanyId = quotaCompanyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<UiDettaglioQuota> getQuoteDetails() {
        return quoteDetails;
    }

    public void setQuoteDetails(List<UiDettaglioQuota> quoteDetails) {
        this.quoteDetails = quoteDetails;
    }
}
