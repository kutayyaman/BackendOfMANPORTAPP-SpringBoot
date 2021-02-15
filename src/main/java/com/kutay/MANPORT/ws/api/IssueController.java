package com.kutay.MANPORT.ws.api;

import com.kutay.MANPORT.ws.domain.Issue;
import com.kutay.MANPORT.ws.dto.TopIssueDTO;
import com.kutay.MANPORT.ws.service.IIssueService;
import com.kutay.MANPORT.ws.util.ApiPaths;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.IssueCtrl.CTRL)
@Api(value = ApiPaths.IssueCtrl.CTRL, description = "Issue APIs")
public class IssueController {

    private final IIssueService issueService;

    public IssueController(IIssueService issueService) {
        this.issueService = issueService;
    }


    @GetMapping("/top3") // "/api/issue/top3"
    @ApiOperation(value = "Find Top 3 Issues", response = List.class)
    public List<TopIssueDTO> findTop3Issues() {
        List<TopIssueDTO> top3TopIssueList = issueService.findTop3();
        return top3TopIssueList;
    }
}