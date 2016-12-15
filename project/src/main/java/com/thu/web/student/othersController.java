package com.thu.web.student;

import com.thu.domain.Role;
import com.thu.domain.RoleRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by source on 12/9/16.
 */
@RestController
public class othersController {

    @Autowired
    private RoleRepository roleRepository;

    // 得到所有部门名字
    @GetMapping("/getDept/all")
    public Object getAllDepts()
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("dept_list", jsonArray);

        List<Role> roles = roleRepository.findAll();
        for(Role role: roles)
        {
            JSONObject tmp = new JSONObject();
            tmp.put("name", role.getRole());

            jsonArray.put(tmp);
        }
        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }
}
