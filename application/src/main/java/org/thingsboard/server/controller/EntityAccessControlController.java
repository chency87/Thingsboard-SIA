package org.thingsboard.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.*;
import org.thingsboard.server.service.security.permission.Operation;
import org.thingsboard.server.service.security.permission.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EntityAccessControlController extends BaseController {

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/access/policy", method = RequestMethod.GET)
    @ResponseBody
    public List<List<String>> getAccessPolicy(@RequestParam String entityType,
                                                        @RequestParam String entityId) throws ThingsboardException {
        EntityId id = entityIdAdaptor(entityType,entityId);
        accessControlService.checkPermission(getCurrentUser(), Resource.valueOf(entityType), Operation.READ_ACCESS_RULE, id, null);
        return enforcerFactory.getEnforcer().getFilteredPolicy(1,entityType,entityId);
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/access/policy", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveAccessPolicy(
                                    @RequestParam(name = "subRule") String subRule,
                                    @RequestParam(name = "entityType") String entityType,
                                    @RequestParam(name = "entityId") String entityId,
                                    @RequestParam(name = "action") String action ) throws ThingsboardException {
        EntityId id = entityIdAdaptor(entityType,entityId);
        accessControlService.checkPermission(getCurrentUser(), Resource.valueOf(entityType), Operation.WRITE_ACCESS_RULE, id, null);
        return enforcerFactory.getEnforcer().addPolicy(subRule,entityType,entityId,action);
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/access/policy", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteAccessPolicy( @RequestParam(name = "subRule") String subRule,
                                       @RequestParam(name = "entityType") String entityType,
                                       @RequestParam(name = "entityId") String entityId,
                                       @RequestParam(name = "action") String action ) throws ThingsboardException {
        return enforcerFactory.getEnforcer().removePolicy(subRule,entityType,entityId,action);
    }

    private EntityId entityIdAdaptor(String  entityType, String entityId){
        EntityId id = null;
        switch (Resource.valueOf(entityType)){
            case USER:
                id = new UserId(toUUID(entityId));
                break;
            case DEVICE:
                id =new DeviceId(toUUID(entityId));
                break;
            case TENANT:
                id = new TenantId(toUUID(entityId));
                break;
            case CUSTOMER:
                id =new CustomerId(toUUID(entityId));
                break;
            default:
                id = new DeviceId(toUUID(entityId));
        }
        return id;
    }

}
