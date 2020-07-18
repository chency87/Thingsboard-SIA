package org.thingsboard.server.service.ids;

//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.PageInfo;
import org.thingsboard.server.dao.model.sql.*;
import org.thingsboard.server.dao.sql.secgate.*;

import java.util.*;
import org.thingsboard.server.bean.Acidevent;
@Service
public class AcideventService {
    /**
     *
     */
//    @Autowired
//    private AcideventRepository aciDao;
    @Autowired
    private EventsRepository eventDao;
    @Autowired
    private SignatureRepository sigDao;
    @Autowired
    private IphdrRepository iphdrDao;
    @Autowired
    private TcphdrRepoeitory tcphdrDao;
    @Autowired
    private UdphdrRepository udphdrDao;

    @Autowired
    private DataRepository dataDao;
/*
    public List<AcideventEntity> findAllAcidevent() {
        // TODO Auto-generated method stub
        return aciDao.findAll();
    }


    public Optional<AcideventEntity> findAcideventByCid(Integer cid) {

        // TODO Auto-generated methodstub
        return aciDao.findById(cid);
    }*/
//    public List<Acidevent> findAllAcidevent() {
//        // TODO Auto-generated method stub
//        return aciDao.Acideventlist();
//    }
//    public List<Acidevent> findAllAcidevent() {
//        List<Acidevent>  list = new ArrayList<>();
//        List<EventsEntity> a = eventDao.findAll();
//        List<IphdrEntity> b = iphdrDao.findAll();
//        List<SignatureEntity> c = sigDao.findAll();
//        List<TcphdrEntity> d = tcphdrDao.findAll();
//        List<UdphdrEntity> e = udphdrDao.findAll();
//        for (EventsEntity eventsEntity : a) {
//            Acidevent acidevent = new Acidevent();
//
//            Integer cid = eventsEntity.getCid();
//            acidevent.setCid(cid);
//            Integer sid = eventsEntity.getSid();
//            acidevent.setSid(sid);
//            Integer signature = eventsEntity.getSignature();
//            acidevent.setSignature(signature);
////            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String datestr = sdf.format(eventsEntity.getTimestamp());
//            acidevent.setTimestamp(eventsEntity.getTimestamp());
//            for (IphdrEntity iphdrEntity : b) {
//                if (cid.equals(iphdrEntity.getCid()) && sid.equals(iphdrEntity.getSid())) {
//                    acidevent.setIpDst(iphdrEntity.getIpDst());
//                    acidevent.setIpProto(iphdrEntity.getIpProto());
//                    acidevent.setIpSrc(iphdrEntity.getIpSrc());
//                }
//            }
//            for (SignatureEntity signatureEntity : c) {
//                if (signature.equals(signatureEntity.getSig_id())) {
//                    acidevent.setSigClassId(signatureEntity.getSig_class_id());
//                    acidevent.setSigName(signatureEntity.getSig_name());
//                    acidevent.setSigPriority(signatureEntity.getSig_priority());
//                }
//            }
//            for (TcphdrEntity tcphdrEntity : d) {
//                if (cid.equals(tcphdrEntity.getCid()) && sid.equals(tcphdrEntity.getSid())) {
//                    acidevent.setTDport(tcphdrEntity.getTcpDport());
//                    acidevent.setTSport(tcphdrEntity.getTcpSport());
//                } else {
//                    for (UdphdrEntity udphdrEntity : e) {
//                        if (cid.equals(udphdrEntity.getCid()) && sid.equals(udphdrEntity.getSid())) {
//                            acidevent.setUDport(udphdrEntity.getUdpDport());
//                            acidevent.setUSport(udphdrEntity.getUdpSport());
//                        }
//                    }
//                }
//            }
//            list.add(acidevent);
//        }
//        return  list;
////        return aciDao.Acideventlist();
//    }
//    public List<Acidevent> findAll(int page,int size){
//        PageHelper.startPage(page,size);
//        return findAll(page,size);
//    }

    public PageInfo findAllAcideventbyPage(int page, int size) {
        PageHelper.startPage(page,size);
        PageInfo<Acidevent> pageInfo = new PageInfo<>();
        List<Acidevent>  list = new ArrayList<>();
        List<EventsEntity> a = eventDao.findAll();
        List<IphdrEntity> b = iphdrDao.findAll();
        List<SignatureEntity> c = sigDao.findAll();
        List<TcphdrEntity> d = tcphdrDao.findAll();
        List<UdphdrEntity> e = udphdrDao.findAll();
        for (EventsEntity eventsEntity : a) {
            Acidevent acidevent = new Acidevent();

            Integer cid = eventsEntity.getCid();
            acidevent.setCid(cid);
            Integer sid = eventsEntity.getSid();
            acidevent.setSid(sid);
            Integer signature = eventsEntity.getSignature();
            acidevent.setSignature(signature);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String datestr = sdf.format(eventsEntity.getTimestamp());
            acidevent.setTimestamp(eventsEntity.getTimestamp());
            for (IphdrEntity iphdrEntity : b) {
                if (cid.equals(iphdrEntity.getCid()) && sid.equals(iphdrEntity.getSid())) {
                    acidevent.setIpDst(iphdrEntity.getIpDst());
                    acidevent.setIpProto(iphdrEntity.getIpProto());
                    acidevent.setIpSrc(iphdrEntity.getIpSrc());
                }
            }
            for (SignatureEntity signatureEntity : c) {
                if (signature.equals(signatureEntity.getSig_id())) {
                    acidevent.setSigClassId(signatureEntity.getSig_class_id());
                    acidevent.setSigName(signatureEntity.getSig_name());
                    acidevent.setSigPriority(signatureEntity.getSig_priority());
                }
            }
            for (TcphdrEntity tcphdrEntity : d) {
                if (cid.equals(tcphdrEntity.getCid()) && sid.equals(tcphdrEntity.getSid())) {
                    acidevent.setTDport(tcphdrEntity.getTcpDport());
                    acidevent.setTSport(tcphdrEntity.getTcpSport());
                } else {
                    for (UdphdrEntity udphdrEntity : e) {
                        if (cid.equals(udphdrEntity.getCid()) && sid.equals(udphdrEntity.getSid())) {
                            acidevent.setUDport(udphdrEntity.getUdpDport());
                            acidevent.setUSport(udphdrEntity.getUdpSport());
                        }
                    }
                }
            }
            list.add(acidevent);
        }
             /*
      封装pageInfo数据 :
     	 第一个参数为list集合的数据
     	 第二个参数为当前页
     	 第三个参数为每页显示数据条数
      */
        pageInfo.setPageInfo(list,page,size);
        return pageInfo;
//        return new PageInfo(list);
//        return aciDao.Acideventlist();
    }

//    public Optional<Acidevent> findAcideventByCid(Integer cid) {
//
//        // TODO Auto-generated methodstub
//        return aciDao.findById(cid);
//    }

//
//    public int queryAbnormaltraffic() {
//        // TODO Auto-generated method stub
//        System.out.println(eventDao.count());
//        return  eventDao.queryeventCount();
//    }
//
//
//
//    public int querydevicecount() {
//        // TODO Auto-generated method stub
//        return iphdrDao.queryipsrcCount();
//    }
//
//
//
//    public int proCount() {
//        // TODO Auto-generated method stub
//        int sum=0;
//        List<Integer> ii = iphdrDao.queryIpprogroup();
//        for(int i=0; i<ii.size(); i++) {
////            Integer c = ii.get(i);
//            Number num = (Number) ii.get(i);
//            Integer c=num.intValue();
//            if(c==1) {
//                sum = udphdrDao.queryTcpUdpport()+1;
//                break;
//            }
//            else {
//                sum = udphdrDao.queryTcpUdpport();
//            }
//        }
//        return sum;
//    }
//
//
//    public double cacheData() {
//        // TODO Auto-generated method stub
//        return dataDao.queryCachedataCount();
//    }
//    public List<Object> queryAcid() {
//        // TODO Auto-generated method stub
//        List<Object>  list = new ArrayList<Object>();
//        List<Object>  ad = aciDao.Acideventlist();
////        List<Acidevent>  ad = aciDao.Acideventlist();
//        for (Object row : ad) {
//            Object[] rowArray = (Object[]) row;
//            Map<String, Object> mapArr = new HashMap<String, Object>();
//            mapArr.put("cid", rowArray[0]);
//            mapArr.put("sid", rowArray[1]);
//            mapArr.put("signature", rowArray[2]);
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String datestr = sdf.format(rowArray[3]);
//            mapArr.put("timestamp", datestr);
//            mapArr.put("ipDst", rowArray[4]);
//            mapArr.put("ipProto", rowArray[5]);
//            mapArr.put("ipSrc", rowArray[6]);
//            mapArr.put("sigClassId", rowArray[7]);
//            mapArr.put("sigName", rowArray[8]);
//            mapArr.put("sigPriority", rowArray[9]);
//            if(rowArray[10]!=null) {
//                mapArr.put("layer4Dport", rowArray[10]);
//                mapArr.put("layer4Sport", rowArray[11]);
//            }
//            else if(rowArray[12]!=null) {
//                mapArr.put("Dport", rowArray[12]);
//                mapArr.put("Sport", rowArray[13]);
//            }
//            else {
//                mapArr.put("Dport", null);
//                mapArr.put("Sport", null);
//            }
//            list.add(mapArr);
//        }
//        return  list;
//    }

}
