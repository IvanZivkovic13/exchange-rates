package com.europoint.exchangerates;

import java.util.List;

public class ExchangeRateListVersionRequest {

    private String exchangeRateListId;
    private String validFrom;
    private String validTo;
    private String contributorRefNumber;
    private List<ExchangeRateRequest> exchangeRates;

    public String getExchangeRateListId() {
        return exchangeRateListId;
    }

    public void setExchangeRateListId(String exchangeRateListId) {
        this.exchangeRateListId = exchangeRateListId;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getContributorRefNumber() {
        return contributorRefNumber;
    }

    public void setContributorRefNumber(String contributorRefNumber) {
        this.contributorRefNumber = contributorRefNumber;
    }

    public List<ExchangeRateRequest> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRateRequest> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}


    class ExchangeRateRequest {

        private String baseCurrencyCode;
        private String quoteCurrencyCode;
        private String type;
        private String countryCode;
        private int unit;
        private double value;


        public String getBaseCurrencyCode() {
            return baseCurrencyCode;
        }

        public void setBaseCurrencyCode(String baseCurrencyCode) {
            this.baseCurrencyCode = baseCurrencyCode;
        }

        public String getQuoteCurrencyCode() {
            return quoteCurrencyCode;
        }

        public void setQuoteCurrencyCode(String quoteCurrencyCode) {
            this.quoteCurrencyCode = quoteCurrencyCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
  }
