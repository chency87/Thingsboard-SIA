package org.thingsboard.server.service.security.permission;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityAccessContext {
    private Object sub;
    private Object type;
    private Object id;
    private Object act;
    private Object env;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((act == null) ? 0 : act.hashCode());
        result = prime * result + ((env == null) ? 0 : env.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((sub == null) ? 0 : sub.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecurityAccessContext other = (SecurityAccessContext) obj;
        if (act == null) {
            if (other.act != null)
                return false;
        } else if (!act.equals(other.act))
            return false;
        if (env == null) {
            if (other.env != null)
                return false;
        } else if (!env.equals(other.env))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (sub == null) {
            if (other.sub != null)
                return false;
        } else if (!sub.equals(other.sub))
            return false;
        return true;
    }

}
