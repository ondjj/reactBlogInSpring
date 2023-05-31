package spring.react.miniblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.react.miniblog.domain.PostData;
import spring.react.miniblog.service.PostService;

@RestController
@RequestMapping("/api")
public class WriteController {

    private final PostService postService;

    public WriteController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostData postData){

        String subject = postData.getSubject();
        String content = postData.getTextArea();

        PostData post = new PostData(subject, content, null);



        return ResponseEntity.ok().build();
    }
}
