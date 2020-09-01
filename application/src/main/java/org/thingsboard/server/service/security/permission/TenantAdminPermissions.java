/**
 * Copyright © 2016-2019 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.service.security.permission;

import com.google.common.util.concurrent.ListenableFuture;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.HasTenantId;
import org.thingsboard.server.common.data.User;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.id.UserId;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.security.Authority;
import org.thingsboard.server.controller.idscontroller.ConstantConfValue;
import org.thingsboard.server.dao.attributes.AttributesService;
import org.thingsboard.server.plugin.bean.BaseIdentityAttr;
import org.thingsboard.server.plugin.bean.DynamicClassReflectUtils;
import org.thingsboard.server.service.security.model.SecurityUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component(value="tenantAdminPermissions")
public class TenantAdminPermissions extends AbstractPermissions {
    @Autowired
    EnforcerFactory enforcerFactory;
    @Autowired
    AttributesService attributesService;


    public TenantAdminPermissions() {
        super();
        put(Resource.ADMIN_SETTINGS, PermissionChecker.allowAllPermissionChecker);
        put(Resource.ALARM, tenantEntityPermissionChecker);
        put(Resource.ASSET, tenantEntityPermissionChecker);
//        put(Resource.DEVICE, tenantDevicePermissionChecker);
        put(Resource.DEVICE, tenantEntityPermissionChecker);
        put(Resource.CUSTOMER, tenantEntityPermissionChecker);
        put(Resource.DASHBOARD, tenantEntityPermissionChecker);
        put(Resource.ENTITY_VIEW, tenantEntityPermissionChecker);
        put(Resource.TENANT, tenantPermissionChecker);
        put(Resource.RULE_CHAIN, tenantEntityPermissionChecker);
        put(Resource.USER, userPermissionChecker);
        put(Resource.WIDGETS_BUNDLE, widgetsPermissionChecker);
        put(Resource.WIDGET_TYPE, widgetsPermissionChecker);



    }



    public PermissionChecker tenantDevicePermissionChecker = new PermissionChecker() {

        @Override
        public boolean hasPermission(SecurityUser user, Operation operation, EntityId entityId, HasTenantId entity) {
            ABACEnforcer enforcer = enforcerFactory.getEnforcer();

            try {
                List<AttributeKvEntry> attributeKvEntries = attributesService.findAll(user.getTenantId(),user.getId(),DataConstants.IDENTITY_SCOPE).get();
                Map<String, Object> propertiesMap = new HashMap<>();
                propertiesMap.put("id",user.getId().toString());
                propertiesMap.put("tenantId",user.getTenantId().getId());
                propertiesMap.put("type",user.getId().getEntityType().toString());
                propertiesMap.put("name",user.getFirstName()+user.getLastName());
                propertiesMap.put("email",user.getEmail());
                for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
                    propertiesMap.put(attributeKvEntry.getKey(),attributeKvEntry.getValue());
                }
                Object obj = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),propertiesMap);

                if(enforcer.enforcer(obj,"DEVICE",entityId,operation.toString())){
                    System.out.println(user.getEmail() + " EXEC Permission  " + entityId +" -- " + operation + "--- ALLOWED");
                    if(operation.equals(Operation.DELETE)){
                        enforcer.removeFilteredPolicy(1,"DEVICE",entityId.toString());
                    }
                    return true;
                }
                System.out.println(user.getEmail() + " EXEC Permission  " + entityId +" -- " + operation + "--- DENIED");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    public static final PermissionChecker tenantEntityPermissionChecker = new PermissionChecker() {

        @Override
        public boolean hasPermission(SecurityUser user, Operation operation, EntityId entityId, HasTenantId entity) {

            if (!user.getTenantId().equals(entity.getTenantId())) {
                return false;
            }
            return true;
        }
    };

    private static final PermissionChecker tenantPermissionChecker =
            new PermissionChecker.GenericPermissionChecker(Operation.READ, Operation.READ_ATTRIBUTES, Operation.READ_TELEMETRY) {

                @Override
                public boolean hasPermission(SecurityUser user, Operation operation, EntityId entityId, HasTenantId entity) {
                    if (!super.hasPermission(user, operation, entityId, entity)) {
                        return false;
                    }
                    if (!user.getTenantId().equals(entityId)) {
                        return false;
                    }
                    return true;
                }

            };

    private static final PermissionChecker userPermissionChecker = new PermissionChecker<UserId, User>() {

        @Override
        public boolean hasPermission(SecurityUser user, Operation operation, UserId userId, User userEntity) {
            if (userEntity.getAuthority() == Authority.SYS_ADMIN) {
                return false;
            }
            if (!user.getTenantId().equals(userEntity.getTenantId())) {
                return false;
            }
            return true;
        }

    };

    private static final PermissionChecker widgetsPermissionChecker = new PermissionChecker() {

        @Override
        public boolean hasPermission(SecurityUser user, Operation operation, EntityId entityId, HasTenantId entity) {
            if (entity.getTenantId() == null || entity.getTenantId().isNullUid()) {
                return operation == Operation.READ;
            }
            if (!user.getTenantId().equals(entity.getTenantId())) {
                return false;
            }
            return true;
        }

    };
}
