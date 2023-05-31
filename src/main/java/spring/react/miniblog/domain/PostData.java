package spring.react.miniblog.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostData {

    @Id @GeneratedValue
    @Column(name = "write_id")
    private long id;
    private String subject;
    private String textArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PostData(String subject, String textArea, User user) {
        this.subject = subject;
        this.textArea = textArea;
        this.user = user;
    }
}
