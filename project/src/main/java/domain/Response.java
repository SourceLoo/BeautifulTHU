package domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by JasonLee on 16/12/3.
 */
@Entity
public class Response {
    @Id
    private Long responseId;
    private String responseContent;
    @ManyToOne
    private User responder;
    @Temporal(TemporalType.TIMESTAMP)
    private Date respondTime;
    private Long likes;
}
