package org.thingsboard.server.service.ids;

import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.IDSConsole;
import org.thingsboard.server.bean.Port;
import org.thingsboard.server.bean.Signatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IDSService {

    public List<IDSConsole> getAlertInformation() {

        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("TCP Alerts");
        idsConsole1.setNum(1500);
        idsConsole1.setProportion(50.0);

        idsConsole2.setName("UDP Alerts");
        idsConsole2.setNum(1500);
        idsConsole2.setProportion(50.0);
        idsConsoles.add(idsConsole1);
        idsConsoles.add(idsConsole2);

        return idsConsoles;
    }

    public List<IDSConsole> getSensors() {

        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("sensor1");
        idsConsole1.setNum(500);
        idsConsole1.setProportion(50.0);

        idsConsole2.setName("sensor2");
        idsConsole2.setNum(500);
        idsConsole2.setProportion(50.0);
        idsConsoles.add(idsConsole1);
        idsConsoles.add(idsConsole2);

        return idsConsoles;
    }

    public List<IDSConsole> getTopSources() {

        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("192.168.0.11");
        idsConsole1.setNum(5);
        idsConsole1.setProportion(50.0);

        idsConsole2.setName("192.168.0.12");
        idsConsole2.setNum(5);
        idsConsole2.setProportion(50.0);
        idsConsoles.add(idsConsole1);
        idsConsoles.add(idsConsole2);

        return idsConsoles;
    }


    public List<IDSConsole> getTopTargets() {

        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("192.168.1.21");
        idsConsole1.setNum(5);
        idsConsole1.setProportion(50.0);

        idsConsole2.setName("192.168.1.22");
        idsConsole2.setNum(5);
        idsConsole2.setProportion(50.0);
        idsConsoles.add(idsConsole1);
        idsConsoles.add(idsConsole2);

        return idsConsoles;
    }

    public Map<String,  ArrayList<Port>> getTopTargetPort() {

        Map<String,  ArrayList<Port>> map = new HashMap<>();
        ArrayList<Port> ports1 = new ArrayList<>();
        ArrayList<Port> ports2 = new ArrayList<>();
        ports1.add(new Port(80, 555));
        ports1.add(new Port(139, 186));
        ports2.add(new Port(1434, 1259));
        ports2.add(new Port(53, 242));
        map.put("TCP", ports1);
        map.put("UDP", ports2);
        return map;
    }

    public List<Signatures> getSignatures() {

        ArrayList<Signatures> idsConsoles = new ArrayList<>();
        Signatures signatures1 = new Signatures(1, "WEB-MISC cross site scripting attempt [sid 1497]", 2, 355, 4, 4);
        Signatures signatures2 = new Signatures(1, "P2P Fastrack kazaa/morpheus traffic [sid 1497]", 2, 355, 4, 4);
        idsConsoles.add(signatures1);
        idsConsoles.add(signatures2);
        return idsConsoles;
    }
}
