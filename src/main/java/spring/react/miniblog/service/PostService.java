package spring.react.miniblog.service;

import org.springframework.stereotype.Service;
import spring.react.miniblog.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


}
