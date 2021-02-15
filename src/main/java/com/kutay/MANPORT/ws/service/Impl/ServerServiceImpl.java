package com.kutay.MANPORT.ws.service.Impl;

import com.kutay.MANPORT.ws.domain.Country;
import com.kutay.MANPORT.ws.domain.Server;
import com.kutay.MANPORT.ws.repository.ServerRepository;
import com.kutay.MANPORT.ws.service.IServerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements IServerService {
    private final ServerRepository serverRepository;

    public ServerServiceImpl(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public List<Server> findAll() {
        return serverRepository.findAll();
    }

    @Override
    public List<Server> findAllByCountry(Country country) {
        return serverRepository.findAllByCountry(country);
    }
}