package com.eyeofender.enderpearl.sql;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "PlayerRank")
public class SQLPlayerRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SQLPlayer player;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SQLRank rank;

    @NotNull
    private Timestamp timestamp;

    @Column
    private Timestamp expiry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SQLPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SQLPlayer player) {
        this.player = player;
    }

    public SQLRank getRank() {
        return rank;
    }

    public void setRank(SQLRank rank) {
        this.rank = rank;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getExpiry() {
        return expiry;
    }

    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }

}
