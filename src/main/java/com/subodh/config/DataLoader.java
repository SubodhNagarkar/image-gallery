package com.subodh.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.subodh.model.RolesModel;
import com.subodh.repository.RoleRepository;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
       
        List<RolesModel> roleModels = roleRepository.findByRoleName("ROLE_USER");

        if (roleModels.isEmpty()) {
            RolesModel rolesModel2 = new RolesModel();

            rolesModel2.setRoleName("ROLE_USER");
            roleRepository.save(rolesModel2);
        }
    }


}
