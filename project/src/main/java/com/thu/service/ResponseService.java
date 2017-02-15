package com.thu.service;

import com.thu.domain.Response;
import com.thu.domain.ResponseRepositiry;
import com.thu.domain.TUser;
import com.thu.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by JasonLee on 16/12/6.
 */
@Service
public class ResponseService {
    @Autowired
    private ResponseRepositiry responseRepositiry;
    @Autowired
    private UserRepository userRepository;

    public Response respond(String responseContent, TUser responder) {
        return new Response(responseContent, responder);
    }

    public boolean editResponse(Long id, String content) {
        Response response = responseRepositiry.findByResponseId(id);
        if (response == null) {
            return false;
        }
        response.setResponseContent(content);
        response.setRespondTime(LocalDateTime.now());
        try {
            responseRepositiry.save(response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // modified by luyq
    @Transactional
    public boolean modifyResponseLike(TUser TUser, Long responseId, boolean op)
    {
        Response response = responseRepositiry.findByResponseId(responseId);
        if (response == null) {
            return false;
        }
        if (op) {
            // vote up
            if (TUser.getLikedRespones().contains(response)) {
                return false;
            }
            response.incrementLikes();
            TUser.getLikedRespones().add(response);
        }
        else {
            // cancel vote
            if (!TUser.getLikedRespones().contains(response))
            {
                return  false;
            }
            response.decrementLikes();
            TUser.getLikedRespones().remove(response);
        }
        try {
            responseRepositiry.save(response);
            userRepository.save(TUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
