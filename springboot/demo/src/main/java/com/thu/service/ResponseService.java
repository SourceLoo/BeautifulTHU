package com.thu.service;

import com.thu.domain.Response;
import com.thu.domain.ResponseRepositiry;
import com.thu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by JasonLee on 16/12/6.
 */
@Service
public class ResponseService {
    @Autowired
    private ResponseRepositiry responseRepositiry;

    public Response respond(String responseContent, User responder) {
        return new Response(responseContent, responder);
    }

    public boolean editResponse(Long id, String content) {
        Response response = responseRepositiry.findByResponseId(id);
        if (response == null) {
            return false;
        }
        response.setResponseContent(content);
        response.setRespondTime(new Date());
        try {
            responseRepositiry.save(response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
