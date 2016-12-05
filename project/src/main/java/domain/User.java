package domain;

import javax.persistence.*;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by JasonLee on 16/12/1.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String uname;
    @Column(nullable = false)
    private String passwd;
    @ManyToOne(optional = false)
    private Role role;
    @Column(nullable = false)
    private String mobileNumber;
    private String fixedNumber;
    private String email;
    @Column(nullable = false)
    private String idNumber;

    protected User () {}

}

