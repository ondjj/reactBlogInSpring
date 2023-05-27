package spring.react.miniblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.react.miniblog.domain.PostData;

@RestController
@RequestMapping("/api")
public class WriteController {

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostData postData){

        return ResponseEntity.ok().build();
    }
}
