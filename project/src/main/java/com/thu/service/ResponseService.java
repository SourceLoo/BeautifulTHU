package com.thu.service;

import com.thu.domain.Response;
import com.thu.domain.ResponseRepositiry;
import com.thu.domain.User;
import com.thu.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by JasonLee on 16/12/6.
 */
@Service
public class ResponseService {
    @Autowired
    private ResponseRepositiry responseRepositiry;
    @Autowired
    private UserRepository userRepository;

    public Response respond(String responseContent, User responder) {
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
    public boolean modifyResponseLike(User user, Long responseId, boolean op)
    {
        Response response = responseRepositiry.findByResponseId(responseId);
        if (response == null) {
            return false;
        }
        if (op) {
            // vote up
            if (user.getLikedRespones().contains(response)) {
                return false;
            }
            response.incrementLikes();
            user.getLikedRespones().add(response);
        }
        else {
            // cancel vote
            if (!user.getLikedRespones().contains(response))
            {
                return  false;
            }
            response.decrementLikes();
            user.getLikedRespones().remove(response);
        }
        try {
            responseRepositiry.save(response);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
