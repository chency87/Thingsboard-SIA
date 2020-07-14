package org.thingsboard.server.controller.idscontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.dao.model.sql.AcideventEntity;
import org.thingsboard.server.service.ids.AcideventService;

import java.util.List;
import java.util.Optional;

@RestController
public class AcideventController {

    @Autowired
    private AcideventService iacids;

    @RequestMapping(value = "/acid/list")
    public List<AcideventEntity> findAllAcidevent()  {
        return iacids.findAllAcidevent();
    }

    @RequestMapping(value = "/acid/findcid")
    public Optional<AcideventEntity> findAcideventByCid(@RequestParam(name = "cid")Integer cid) {
        // TODO Auto-generated method stub
        return iacids.findAcideventByCid(cid);
    }


    @RequestMapping(value = "/aci")
    public List<Object> Acid() {
        // TODO Auto-generated method stub

        return iacids.queryAcid();
    }


}
