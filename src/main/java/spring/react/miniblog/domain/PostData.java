package spring.react.miniblog.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostData {

    @Id @GeneratedValue
    @Column(name = "write_id")
    private long id;
    private String subject;
    private String textArea;
}
