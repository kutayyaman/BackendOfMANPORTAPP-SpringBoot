package com.kutay.MANPORT.ws.service;

import com.kutay.MANPORT.ws.domain.Application;
import com.kutay.MANPORT.ws.domain.JobInterface;
import com.kutay.MANPORT.ws.domain.RowStatus;
import com.kutay.MANPORT.ws.domain.User;
import com.kutay.MANPORT.ws.dto.AddAJobInterfaceToAnApplicationDTO;
import com.kutay.MANPORT.ws.dto.JobInterfaceDTO;

import java.util.List;

public interface IJobInterfaceService {
    List<JobInterfaceDTO> findAllByApplication(Long appId);

    AddAJobInterfaceToAnApplicationDTO addAJobInterfaceToAnApplication(AddAJobInterfaceToAnApplicationDTO dto, User user);
}
