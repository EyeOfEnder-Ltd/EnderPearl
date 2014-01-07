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
@Table(name = "PlayerServer")
public class SQLPlayerServer {

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
    private SQLServer server;

    @NotNull
    private Timestamp joinTime;

    @Column
    private Timestamp leaveTime;

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

    public SQLServer getServer() {
        return server;
    }

    public void setServer(SQLServer server) {
        this.server = server;
    }

    public Timestamp getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Timestamp joinTime) {
        this.joinTime = joinTime;
    }

    public Timestamp getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Timestamp leaveTime) {
        this.leaveTime = leaveTime;
    }

}
