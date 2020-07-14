package org.thingsboard.server.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.model.sql.AcideventEntity;
import org.thingsboard.server.dao.sql.secgate.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AcideventService {
    /**
     *
     */
    @Autowired
    private AcideventRepository aciDao;
    @Autowired
    private EventsRepository eventDao;

    @Autowired
    private IphdrRepository iphdrDao;

    @Autowired
    private UdphdrRepository udphdrDao;

    @Autowired
    private DataRepository dataDao;

    public List<AcideventEntity> findAllAcidevent() {
        // TODO Auto-generated method stub
        return aciDao.findAll();
    }


    public Optional<AcideventEntity> findAcideventByCid(Integer cid) {

        // TODO Auto-generated methodstub
        return aciDao.findById(cid);
    }

    public int queryAbnormaltraffic() {
        // TODO Auto-generated method stub
        System.out.println(eventDao.count());
        return  eventDao.queryeventCount();
    }



    public int querydevicecount() {
        // TODO Auto-generated method stub
        return iphdrDao.queryipsrcCount();
    }



    public int proCount() {
        // TODO Auto-generated method stub
        int sum=0;
        List<Integer> ii = iphdrDao.queryIpprogroup();
        for(int i=0; i<ii.size(); i++) {
//            Integer c = ii.get(i);
            Number num = (Number) ii.get(i);
            Integer c=num.intValue();
            if(c==1) {
                sum = udphdrDao.queryTcpUdpport()+1;
                break;
            }
            else {
                sum = udphdrDao.queryTcpUdpport();
            }
        }
        return sum;
    }


    public double cacheData() {
        // TODO Auto-generated method stub
        return dataDao.queryCachedataCount();
    }
    public List<Object> queryAcid() {
        // TODO Auto-generated method stub
        List<Object>  list = new ArrayList<Object>();
        List<Object>  ad = aciDao.Acideventlist();
/*		for (int i = 0; i <ad.size(); i++) {
			System.out.println(ad.get(i));
			} */
        for (Object row : ad) {
            Object[] rowArray = (Object[]) row;
            Map<String, Object> mapArr = new HashMap<String, Object>();
            mapArr.put("cid", rowArray[0]);
            mapArr.put("sid", rowArray[1]);
            mapArr.put("signature", rowArray[2]);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datestr = sdf.format(rowArray[3]);
            mapArr.put("timestamp", datestr);
            mapArr.put("ipDst", rowArray[4]);
            mapArr.put("ipProto", rowArray[5]);
            mapArr.put("ipSrc", rowArray[6]);
            mapArr.put("sigClassId", rowArray[7]);
            mapArr.put("sigName", rowArray[8]);
            mapArr.put("sigPriority", rowArray[9]);
            if(rowArray[10]!=null) {
                mapArr.put("layer4Dport", rowArray[10]);
                mapArr.put("layer4Sport", rowArray[11]);
            }
            else if(rowArray[12]!=null) {
                mapArr.put("Dport", rowArray[12]);
                mapArr.put("Sport", rowArray[13]);
            }
            else {
                mapArr.put("Dport", null);
                mapArr.put("Sport", null);
            }
            list.add(mapArr);
        }
        return  list;
    }

}
