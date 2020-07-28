package org.thingsboard.server.service.ids;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.Acidevent;

import org.thingsboard.server.dao.model.sql.*;
import org.thingsboard.server.dao.sql.secgate.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcideventPageService {

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
    @Autowired
    private EventSnortRepository eventSnortRepository;

    public PageInfo findAcideventbyPage(int page, int size) {
        PageHelper.startPage(page,size);
        PageInfo<Acidevent> pageInfo = new PageInfo<>();
        PageRequest request = PageRequest.of(page-1,size);
        Page<EventSnortEntity> aa = eventSnortRepository.findAll(request);
        List<EventSnortEntity> a = aa.getContent();
//        System.out.println(a);
        List<Acidevent>  list = new ArrayList<>();
        for (EventSnortEntity eventsEntity :a) {
            Acidevent acidevent = new Acidevent();

            Integer cid = eventsEntity.getCid();
            acidevent.setCid(cid);
            Integer sid = eventsEntity.getSid();
            acidevent.setSid(sid);
            Integer signature = eventsEntity.getSignature();
            acidevent.setSignature(signature);

            acidevent.setTimestamp(eventsEntity.getTimestamp());
            List<IphdrEntity> b = iphdrDao.findAllByCidAndSid(cid,sid);
            for (IphdrEntity iphdrEntity : b) {
                if (cid.equals(iphdrEntity.getCid()) && sid.equals(iphdrEntity.getSid())) {
                    acidevent.setIpDst(iphdrEntity.getIpDst());
                    acidevent.setIpProto(iphdrEntity.getIpProto());
                    acidevent.setIpSrc(iphdrEntity.getIpSrc());
                }
            }
            List<SignatureEntity> c = sigDao.findAllBySig_id(signature);
            for (SignatureEntity signatureEntity : c) {
                if (signature.equals(signatureEntity.getSig_id())) {
                    acidevent.setSigClassId(signatureEntity.getSig_class_id());
                    acidevent.setSigName(signatureEntity.getSig_name());
                    acidevent.setSigPriority(signatureEntity.getSig_priority());
                }
            }
            List<TcphdrEntity> d = tcphdrDao.findAllByCidAndSid(cid,sid);
            List<UdphdrEntity> e = udphdrDao.findAllByCidAndSid(cid,sid);
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
        pageInfo =  new PageInfo<>(list);
        return pageInfo;
    }

}
