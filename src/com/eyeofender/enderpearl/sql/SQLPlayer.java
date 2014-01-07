package com.eyeofender.enderpearl.sql;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "Player")
public class SQLPlayer {

    @Id
    private String uuid;

    @NotNull
    private String name;

    @Column
    private String lastIp;

    @NotNull
    private Timestamp lastJoin;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Timestamp getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(Timestamp lastJoin) {
        this.lastJoin = lastJoin;
    }

}
