package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@Table(name = "MEMBER")
@SequenceGenerator(name = "seq_member")
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR", table = "MY_SEQUENCE", pkColumnName = "MEMBER_SEQ", allocationSize = 1)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_member")
    private Long id;
    @Column(name = "name")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
