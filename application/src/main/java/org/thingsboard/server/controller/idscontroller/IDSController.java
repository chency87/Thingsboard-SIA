package org.thingsboard.server.controller.idscontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.bean.IDSConsole;
import org.thingsboard.server.bean.Port;
import org.thingsboard.server.bean.Signatures;
import org.thingsboard.server.service.ids.IDSService;

import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class IDSController {

    @Autowired
    IDSService idsService;

    @RequestMapping(value = "/ids/alertinformation", method = RequestMethod.GET)
    public List<IDSConsole> getAlertInformation(){


        return idsService.getAlertInformation();
    }
    @RequestMapping(value = "/ids/sensors", method = RequestMethod.GET)
    public List<IDSConsole> getSensors(){

        return idsService.getSensors();
    }
    @RequestMapping(value = "/ids/topsources", method = RequestMethod.GET)
    public List<IDSConsole> getTopSources(){

        return idsService.getTopSources();
    }

    @RequestMapping(value = "/ids/toptargets", method = RequestMethod.GET)
    public List<IDSConsole> getTopTargets(){

        return idsService.getTopTargets();
    }

    @RequestMapping(value = "/ids/toptargetports", method = RequestMethod.GET)
    public Map<String, ArrayList<Port>> getTopTargetPort(){

        return idsService.getTopTargetPort();
    }

    @RequestMapping(value = "/ids/signatures")
    public List<Signatures> getSignatures(){

        return idsService.getSignatures();
    }


}
