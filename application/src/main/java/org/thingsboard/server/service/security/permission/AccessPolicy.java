package org.thingsboard.server.service.security.permission;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPolicy {
    private String sub;

    /**将要访问的资源，可以使用  * 作为通配符，例如/user/* */
    private String obj;

    /**用户对资源执行的操作。HTTP方法，GET、POST、PUT、DELETE等，可以使用 * 作为通配符*/
    private String act;

}
