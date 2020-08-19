package org.thingsboard.server.controller.idscontroller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.server.bean.Acidevent;

import org.thingsboard.server.service.ids.AcideventPageService;

//@Api(tags = "边缘网络流量表")
@RestController
@RequestMapping(value = "/api")
public class AcideventPageController {

    @Autowired
    private AcideventPageService iacids;

    @ApiOperation(value="分页获取网络流量表",notes="page是指第几页，size是每页数量")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/acid/byPage", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<Acidevent> getbyAcidevent(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5")int size){
        try {
            return iacids.findAcideventbyPage(page,size);
        } catch (Exception e) {
            return null;
        }

    }
}
