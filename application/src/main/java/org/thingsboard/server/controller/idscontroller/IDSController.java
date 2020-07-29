package org.thingsboard.server.controller.idscontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.bean.IDSConsole;
import org.thingsboard.server.bean.IPAndNum;
import org.thingsboard.server.bean.Port;
import org.thingsboard.server.bean.Signatures;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.SignatureEntity;
import org.thingsboard.server.service.ids.IDSService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IDSController extends BaseController {

    @Autowired
    IDSService idsService;

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/alertinformation", method = RequestMethod.GET)
    public List<IDSConsole> getAlertInformation() throws Exception {
        return idsService.getAlertInformation();
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/sensors", method = RequestMethod.GET)
    public List<IDSConsole> getSensors() throws Exception {
        return idsService.getSensors();
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/topsources", method = RequestMethod.GET)
    public List<IPAndNum> getTopSources() throws Exception {
        return idsService.getTopSources();
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/toptargets", method = RequestMethod.GET)
    public List<IPAndNum> getTopTargets() throws Exception {
        return idsService.getTopTargets();
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/toptargetports", method = RequestMethod.GET)
    public Map<String, Object> getTopTargetPort() throws Exception {
        return idsService.getTopTargetPort();
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/signatures",method = RequestMethod.GET)
    public Page<SignatureEntity> getSignatures(
            @RequestParam int size,
            @RequestParam int page) throws Exception {
        return idsService.getSignatures(size,page);
    }


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/chart/trafficTrend",method = RequestMethod.GET)
    public double trafficTrend() throws Exception {

        return idsService.getTrafficTrend();
    }


    /**
     *  查询数据库测试
     * @return
     */
/*    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/ids/signaturesddd",method = RequestMethod.GET)
    public List<CountSig> getSignatures2(){

        return idsService.getSignatures2();
    }*/

}
