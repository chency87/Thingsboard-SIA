package org.thingsboard.server.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.IDSConsole;
import org.thingsboard.server.bean.IPAndNum;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.SensorEntity;
import org.thingsboard.server.dao.model.sql.SignatureEntity;
import org.thingsboard.server.dao.sql.secgate.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
        int icmpSize = iphdrRepository.countByIpProto(1);
        int udpSize = iphdrRepository.countByIpProto(2);
        int tcpSize = iphdrRepository.countByIpProto(3);
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
        IDSConsole idsConsole = new IDSConsole();
        List<SensorEntity> sensorEntities = sensorRepository.findAll();
        for (SensorEntity sensorEntity : sensorEntities){
            Integer sid = sensorEntity.getSid();
            long count = eventSnortRepository.getSigntureCount(sid).size();
            Integer alertCount = eventSnortRepository.getAlertCount(sid);
            String hostname = sensorEntity.getHostname();
            idsConsoles.add(new IDSConsole(hostname, (int) count,alertCount));
        }
        return idsConsoles;
    }

    /**
     *  统计输入IP及其个数
     * @return
     */
    public List<IPAndNum> getTopSources() {
        ArrayList<IPAndNum> ipAndNums = new ArrayList<>();
        List<CountSrcIP> ipSrcAndCount = iphdrRepository.getIPSrcAndCount();
        Stream<CountSrcIP> limit = ipSrcAndCount.stream().limit(5);
        limit.forEach(i -> { ipAndNums.add(new IPAndNum(i.getCount(), longToIp(i.getIpSrc()))); });
        return ipAndNums;
    }

    /**
     *  统计输出IP及其个数
     * @return
     */
    public List<IPAndNum> getTopTargets() {
        ArrayList<IPAndNum> ipAndNums = new ArrayList<>();
        List<CountDstIP> ipDstAndCount = iphdrRepository.getIPDstAndCount();
        Stream<CountDstIP> limit = ipDstAndCount.stream().limit(5);
        limit.forEach(i -> { ipAndNums.add(new IPAndNum(i.getCount(),longToIp(i.getIpDst()))); });
        return ipAndNums;
    }

    public Map<String, Object> getTopTargetPort() {
        Map<String, Object> map = new HashMap<>();
        List<CountUdpPort> udpPort = udphdrRepository.getUdpPort();
        Stream<CountUdpPort> udpPortStream = udpPort.stream().limit(5);
        List<CountTcpPort> tcpPort = tcphdrRepoeitory.getTcpPort();
        Stream<CountTcpPort> tcpPortStream = tcpPort.stream().limit(5);
        map.put("TCP", udpPortStream);
        map.put("UDP", tcpPortStream);
        return map;
    }

    public List<SignatureEntity> getSignatures() {

 /*       ArrayList<Signatures> idsConsoles = new ArrayList<>();
        Signatures signatures1 = new Signatures(1, "WEB-MISC cross site scripting attempt [sid 1497]", 2, 355, 4, 4);
        Signatures signatures2 = new Signatures(1, "P2P Fastrack kazaa/morpheus traffic [sid 1497]", 2, 355, 4, 4);
        idsConsoles.add(signatures1);
        idsConsoles.add(signatures2);*/
        List<SignatureEntity> all = signatureRepository.findAll();

        return all;
    }

    public List<CountSig> getSignatures2() {

      return eventSnortRepository.getSigntureCount2(1);
    }

    /**
     * IP 转为数字
     * @param ipAddress
     * @return
     */
    private long ipToLong(String ipAddress) {

        long result = 0;

        String[] ipAddressInArray = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {

            long ip = Long.parseLong(ipAddressInArray[3 - i]);

            //left shifting 24,16,8,0 and bitwise OR

            //1. 192 << 24
            //1. 168 << 16
            //1. 1   << 8
            //1. 2   << 0
            result |= ip << (i * 8);

        }
        return result;
    }

    private String longToIp(long ip) {
        StringBuilder result = new StringBuilder(15);

        for (int i = 0; i < 4; i++) {

            result.insert(0,Long.toString(ip & 0xff));

            if (i < 3) {
                result.insert(0,'.');
            }

            ip = ip >> 8;
        }
        return result.toString();
    }


    private String longToIp2(long ip) {

        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }
}
