package com.europoint.exchangerates;

import java.util.List;

public class ClientRequest {

    private String id;
    private String name;
    private String parentClientId;
    private List<Permission> permissions;

    // getter and setters


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getParentClientId() {
        return parentClientId;
    }
    public void setParentClientId(String parentClientId) {
        this.parentClientId = parentClientId;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

}

class Permission {

    private String type;
    private String contributorId;
    private String exchangeRateListId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContributorId() {
        return contributorId;
    }

    public void setContributorId(String contributorId) {
        this.contributorId = contributorId;
    }

    public String getExchangeRateListId() {
        return exchangeRateListId;
    }

    public void setExchangeRateListId(String exchangeRateListId) {
        this.exchangeRateListId = exchangeRateListId;
    }
}


