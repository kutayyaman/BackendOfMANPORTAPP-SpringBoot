package com.kutay.MANPORT.ws.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class JobImplement extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobInterfaceId")
    private JobInterface jobInterface;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serverId")
    private Server server;
}