package org.thingsboard.server.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.dao.model.sql.AcideventEntity;
import org.thingsboard.server.dao.model.sql.DataEntity;
import org.thingsboard.server.dao.model.sql.IphdrEntity;
import org.thingsboard.server.dao.model.sql.UdphdrEntity;
import org.thingsboard.server.dao.sql.secgate.AcideventRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public interface AcideventService {
    @Autowired
    private AcideventRepository aciDao;

    public List<AcideventEntity> findAllAcidevent() {
        // TODO Auto-generated method stub
        return aciDao.findAll();
    }


    public Optional<AcideventEntity> findAcideventByCid(Integer cid) {

        // TODO Auto-generated methodstub
        return aciDao.findById(cid);
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
