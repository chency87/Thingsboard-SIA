package org.thingsboard.server.controller.idscontroller;

//import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
//import org.thingsboard.server.dao.model.sql.AcideventEntity;
//import org.thingsboard.server.dao.sql.secgate.Acidevent;
import org.thingsboard.server.bean.PageInfo;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.service.ids.AcideventService;
import org.thingsboard.server.bean.Acidevent;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class AcideventController{

    @Autowired
    private AcideventService iacids;

//    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
//    @RequestMapping(value = "/acid/list", method = RequestMethod.GET)
//  //  public List<AcideventEntity> findAllAcidevent()  {
//    public List<Acidevent> findAllAcidevent()  {
//        return iacids.findAllAcidevent();
//    }
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/acid/page", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<Acidevent> getAcidevent(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5")int size){
        try {
            return iacids.findAllAcideventbyPage(page,size);
        } catch (Exception e) {
            return null;
        }

    }
//    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
//    @RequestMapping(value = "/acid/findcid", method = RequestMethod.GET)
//   // public Optional<AcideventEntity> findAcideventByCid(@RequestParam(name = "cid")Integer cid) {
//    public Optional<Acidevent> findAcideventByCid(@RequestParam(name = "cid")Integer cid) {
//        return iacids.findAcideventByCid(cid);
//    }
//
//    @PreAuthorize("hasAnyAuthority('SYS_ADMIN','TENANT_ADMIN','CUSTOMER_USER')")
//    @RequestMapping(value = "/aci",method = RequestMethod.GET)
//    public List<Object> Acid() {
//        // TODO Auto-generated method stub
//
//        return iacids.queryAcid();
//    }


}
