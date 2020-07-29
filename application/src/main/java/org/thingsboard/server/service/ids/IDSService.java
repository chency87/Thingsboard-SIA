package org.thingsboard.server.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.IDSConsole;
import org.thingsboard.server.bean.IPAndNum;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.SensorEntity;
import org.thingsboard.server.dao.model.sql.SignatureEntity;
import org.thingsboard.server.dao.sql.secgate.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public List<IDSConsole> getAlertInformation() throws Exception{
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
    public List<IDSConsole> getSensors() throws Exception{
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
    public List<IPAndNum> getTopSources() throws Exception{
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
    public List<IPAndNum> getTopTargets() throws Exception{
        ArrayList<IPAndNum> ipAndNums = new ArrayList<>();
        List<CountDstIP> ipDstAndCount = iphdrRepository.getIPDstAndCount();
        Stream<CountDstIP> limit = ipDstAndCount.stream().limit(5);
        limit.forEach(i -> { ipAndNums.add(new IPAndNum(i.getCount(),longToIp(i.getIpDst()))); });
        return ipAndNums;
    }

    public Map<String, Object> getTopTargetPort() throws Exception{
        Map<String, Object> map = new HashMap<>();
        List<CountUdpPort> udpPort = udphdrRepository.getUdpPort();
        Stream<CountUdpPort> udpPortStream = udpPort.stream().limit(5);
        List<CountTcpPort> tcpPort = tcphdrRepoeitory.getTcpPort();
        Stream<CountTcpPort> tcpPortStream = tcpPort.stream().limit(5);
        map.put("TCP", udpPortStream);
        map.put("UDP", tcpPortStream);
        return map;
    }

    public Page<SignatureEntity> getSignatures(int size,int page) throws Exception{

        Pageable pageable = PageRequest.of(page, size);
        Page<SignatureEntity> all = signatureRepository.findAll(pageable);
        return all;
    }

    //此功能未实现
    public double getTrafficTrend() {

/*        Timestamp t = new Timestamp(new Date().getTime());
        String s = t.toString();

        int num= eventSnortRepository.getTrafficTrend2(s);*/
        double num = 0.56;

        return num;
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


    /**
     *method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
     dateString 需要转换为timestamp的字符串
     dataTime timestamp
     */
    public final static java.sql.Timestamp string2Time(String dateString)
            throws java.text.ParseException {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", Locale.ENGLISH);//设定格式
        //dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
        dateFormat.setLenient(false);
        java.util.Date timeDate = dateFormat.parse(dateString);//util类型
        java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
        return dateTime;
    }
    /**
     *method 将字符串类型的日期转换为一个Date（java.sql.Date）
     dateString 需要转换为Date的字符串
     dataTime Date
     */
    public final static java.sql.Date string2Date(String dateString)
            throws java.lang.Exception {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        dateFormat.setLenient(false);
        java.util.Date timeDate = dateFormat.parse(dateString);//util类型
        java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());//sql类型
        return dateTime;
    }

}
