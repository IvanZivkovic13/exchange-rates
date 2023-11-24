package com.europoint.exchangerates;

public class ExchangeRateListRequest {

    private String id;
    private String contributorId;
    private String exchangeRateListTypeId;
    private String referenceCurrencyCode;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContributorId() {
        return contributorId;
    }

    public void setContributorId(String contributorId) {
        this.contributorId = contributorId;
    }

    public String getExchangeRateListTypeId() {
        return exchangeRateListTypeId;
    }

    public void setExchangeRateListTypeId(String exchangeRateListTypeId) {
        this.exchangeRateListTypeId = exchangeRateListTypeId;
    }

    public String getReferenceCurrencyCode() {
        return referenceCurrencyCode;
    }

    public void setReferenceCurrencyCode(String referenceCurrencyCode) {
        this.referenceCurrencyCode = referenceCurrencyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
