package org.thingsboard.server.controller.idscontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.bean.SystemParameter;
import org.thingsboard.server.service.ids.SystemParameterService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SystemParameterController {
    @Autowired
    private SystemParameterService systemParameterService;

//    @Scheduled(fixedRate = 1000)
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/systemparameter", method = RequestMethod.GET)
    public void sys() throws Exception {
        systemParameters();
    }

    public List<SystemParameter> systemParameters() throws Exception {
        return systemParameterService.getSystemParameters();
    }

}
