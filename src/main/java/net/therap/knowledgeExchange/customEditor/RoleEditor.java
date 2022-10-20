package net.therap.knowledgeExchange.customEditor;


import net.therap.knowledgeExchange.domain.Role;
import net.therap.knowledgeExchange.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;

import static java.util.Objects.isNull;

/**
 * @author kawsar.bhuiyan
 * @since 10/20/22
 */
@Component
public class RoleEditor extends PropertyEditorSupport {

    @Autowired
    private RoleService roleService;

    @Override
    public String getAsText() {
        Role role = (Role) getValue();

        return isNull(role) ? "" : role.getType().name();
    }

    @Override
    public void setAsText(String id) {
        Role role = roleService.findById(Integer.parseInt(id));

        this.setValue(role);
    }
}