package com.europoint.exchangerates;

import java.util.List;

public class ExchangeRateListVersionTemplateRequest {

        private String clientId;
        private String name;
        private List<Rule> rules;


        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Rule> getRules() {
            return rules;
        }

        public void setRules(List<Rule> rules) {
            this.rules = rules;
        }

        public static class Rule {

            private String templateId;
            private String currencyCode;
            private String refExchangeRateType;
            private String targetExchangeRateType;
            private Offset offset;

            public String getTemplateId() {
                return templateId;
            }

            public void setTemplateId(String templateId) {
                this.templateId = templateId;
            }

            public String getCurrencyCode() {
                return currencyCode;
            }

            public void setCurrencyCode(String currencyCode) {
                this.currencyCode = currencyCode;
            }

            public String getRefExchangeRateType() {
                return refExchangeRateType;
            }

            public void setRefExchangeRateType(String refExchangeRateType) {
                this.refExchangeRateType = refExchangeRateType;
            }

            public String getTargetExchangeRateType() {
                return targetExchangeRateType;
            }

            public void setTargetExchangeRateType(String targetExchangeRateType) {
                this.targetExchangeRateType = targetExchangeRateType;
            }

            public Offset getOffset() {
                return offset;
            }

            public void setOffset(Offset offset) {
                this.offset = offset;
            }
        }



        public static class Offset {

            private int value;
            private String type;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

