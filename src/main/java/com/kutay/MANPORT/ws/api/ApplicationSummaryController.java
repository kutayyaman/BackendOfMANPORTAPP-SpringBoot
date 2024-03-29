package com.kutay.MANPORT.ws.api;

import com.kutay.MANPORT.ws.models.GetApplicationsSummaryModel;
import com.kutay.MANPORT.ws.service.IApplicationSummaryService;
import com.kutay.MANPORT.ws.util.ApiPaths;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.ApplicationSummaryCtrl.CTRL)  // /api/applicationsummary
@Api(value = ApiPaths.ApplicationSummaryCtrl.CTRL, description = "Application Summary APIs")
public class ApplicationSummaryController {

    private final IApplicationSummaryService applicationSummaryService;

    public ApplicationSummaryController(IApplicationSummaryService applicationSummaryService) {
        this.applicationSummaryService = applicationSummaryService;
    }


    @GetMapping()
    @ApiOperation(value = "Get ApplicationSummary Operation", response = GetApplicationsSummaryModel.class)
    public GetApplicationsSummaryModel getApplicationsSummary() {
        return applicationSummaryService.getApplicationsSummary();
    }
}
