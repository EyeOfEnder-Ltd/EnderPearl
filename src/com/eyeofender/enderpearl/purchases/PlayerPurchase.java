package com.eyeofender.enderpearl.purchases;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "PlayerPurchases")
public class PlayerPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String minigame;

    @NotNull
    private String type;

    @NotNull
    private String purchase;

    @NotNull
    private Timestamp timestamp;

    @Column
    private Timestamp expiry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinigame() {
        return minigame;
    }

    public void setMinigame(String minigame) {
        this.minigame = minigame;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getExpiry() {
        return timestamp;
    }

    public void setExpiry(Timestamp expiry) {
        this.timestamp = expiry;
    }

}
