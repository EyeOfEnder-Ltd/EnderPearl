package com.eyeofender.enderpearl.ranks;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.entity.Player;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;

@Entity
@Table(name = "global_ranks")
public class RankEntry {

    @Id
    @Length(max = 16)
    private String name;

    @NotEmpty
    @Length(max = 30)
    private String rank;

    @Column
    private Date expiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(Player player) {
        this.name = player.getName();
    }

    public String getType() {
        return rank;
    }

    public void setType(String type) {
        this.rank = type;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

}
