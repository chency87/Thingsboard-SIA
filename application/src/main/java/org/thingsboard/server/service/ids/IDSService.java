package org.thingsboard.server.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.IDSConsole;
import org.thingsboard.server.bean.Port;
import org.thingsboard.server.bean.Signatures;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.EventSnortEntity;
import org.thingsboard.server.dao.model.sql.IphdrEntity;
import org.thingsboard.server.dao.model.sql.SensorEntity;
import org.thingsboard.server.dao.sql.event.EventRepository;
import org.thingsboard.server.dao.sql.secgate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IDSService {

    @Autowired
    IphdrRepository iphdrRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    EventSnortRepository eventSnortRepository;


    @Autowired
    IphdrRepository iphdRepository;

    @Autowired
    TcphdrRepoeitory tcphdrRepoeitory;

    @Autowired
    UdphdrRepository  udphdrRepository;

    @Autowired
    SignatureRepository signatureRepository;



    //TODO：Alert Information
    public List<IDSConsole> getAlertInformation() {
        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();

        boolean id = iphdrRepository.existsById(2);
        int icmpSize = iphdrRepository.findIphdrEntitiesByIpProto(1).size();
        int udpSize = iphdrRepository.findIphdrEntitiesByIpProto(2).size();
        int tcpSize = iphdrRepository.findIphdrEntitiesByIpProto(3).size();
        Integer total = icmpSize + udpSize + tcpSize;

        idsConsoles.add(new IDSConsole("ICMP Alerts",icmpSize, (icmpSize/total)*100));
        idsConsoles.add(new IDSConsole("UDP Alerts",udpSize, (icmpSize/total)*100));
        idsConsoles.add(new IDSConsole("TCP Alerts",tcpSize, (icmpSize/total)*100));
        idsConsoles.add(new IDSConsole("Total Alerts",total, 100));

        return idsConsoles;
    }

    //TODO:Sensors information
    public List<IDSConsole> getSensors() {

        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
/*        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("sensor1");
        idsConsole1.setNum(500);
        idsConsole1.setProportion(50);

        idsConsole2.setName("sensor2");
        idsConsole2.setNum(500);
        idsConsole2.setProportion(50);
        idsConsoles.add(idsConsole1);
        idsConsoles.add(idsConsole2);*/
        IDSConsole idsConsole = new IDSConsole();
        List<SensorEntity> sensorEntities = sensorRepository.findAll();
        for (SensorEntity sensorEntity : sensorEntities){
            Integer sid = sensorEntity.getSid();
//            List<Object> bySidAndSignature = eventSnortRepository.findBySidAndSignature(sid);
            long count = eventSnortRepository.getSigntureCount(sid).size();
            Integer alertCount = eventSnortRepository.getAlertCount(sid);
            String hostname = sensorEntity.getHostname();
            idsConsoles.add(new IDSConsole(hostname, (int) count,alertCount));
        }

        return idsConsoles;
    }

    public List<IDSConsole> getTopSources() {

      /* */ ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("192.168.0.11");
        idsConsole1.setNum(5);
        idsConsole1.setProportion(50);

        idsConsole2.setName("192.168.0.12");
        idsConsole2.setNum(5);
        idsConsole2.setProportion(50);
        idsConsoles.add(idsConsole1);
        idsConsoles.add(idsConsole2);

        List<Object> ipSrcAndCount = iphdrRepository.getIPSrcAndCount();
        List<IphdrEntity> all = iphdrRepository.findAll();



        return idsConsoles;
    }


    public List<IDSConsole> getTopTargets() {

        ArrayList<IDSConsole> idsConsoles = new ArrayList<>();
        IDSConsole idsConsole1 = new IDSConsole();
        IDSConsole idsConsole2 = new IDSConsole();

        idsConsole1.setName("192.168.1.21");
        idsConsole1.setNum(5);
        idsConsole1.setProportion(50);

        idsConsole2.setName("192.168.1.22");
        idsConsole2.setNum(5);
        idsConsole2.setProportion(50);
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

    public List<CountSig> getSignatures2() {

      return eventSnortRepository.getSigntureCount2(1);
    }
}
