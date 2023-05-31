package spring.react.miniblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.react.miniblog.domain.PostData;
import spring.react.miniblog.domain.User;

public interface PostRepository extends JpaRepository<PostData, Long> {

    PostData save(PostData postData);

}
