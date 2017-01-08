package com.thu.web.student;

import com.thu.domain.Role;
import com.thu.domain.RoleRepository;
import com.thu.domain.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by source on 12/9/16.
 */
@RestController
@RequestMapping("/student")
public class GetStatusAndDepartController {

    public final static Map<String, List<Status>> statusMap = new LinkedHashMap<>();
    public final static List<Status> visibleStatus = new ArrayList<>();

    static
    {
        visibleStatus.add(Status.UNCLASSIFIED);
        visibleStatus.add(Status.UNSOLVED);
        visibleStatus.add(Status.SOLVING);
        visibleStatus.add(Status.RECLASSIFY);
        visibleStatus.add(Status.DELAY);
        visibleStatus.add(Status.SOLVED);

        List<Status> unClassifiedStatus = new ArrayList<>();
        unClassifiedStatus.add(Status.UNCLASSIFIED); // 1
        statusMap.put("待分类", unClassifiedStatus);

        List<Status> unSolvedStatus = new ArrayList<>();
        unSolvedStatus.add(Status.UNSOLVED); // 2 3 4 5
        unSolvedStatus.add(Status.SOLVING);
        unSolvedStatus.add(Status.RECLASSIFY);
        unSolvedStatus.add(Status.DELAY);
        statusMap.put("待解决", unSolvedStatus);

        List<Status> SolvedStatus = new ArrayList<>();
        SolvedStatus.add(Status.SOLVED); // 6
        statusMap.put("已解决", SolvedStatus);

    }

    // 得到所有分类
    @GetMapping("/getStatus/all")
    public Object getAllStatus()
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("status_list", jsonArray);

        for(String s: statusMap.keySet())
        {
            JSONObject tmp = new JSONObject();
            tmp.put("name", s);
            jsonArray.put(tmp);
        }
        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }


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
            if("xuesheng".equals(role.getRole()))
                continue;

            JSONObject tmp = new JSONObject();
            tmp.put("name", role.getDisplayName());

            jsonArray.put(tmp);
        }
        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }

}
