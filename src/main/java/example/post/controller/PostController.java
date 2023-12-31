package example.post.controller;


import example.post.domain.Post;
import example.post.dto.comment.response.CommentInPost;
import example.post.dto.post.request.SavePostRequest;
import example.post.dto.post.request.UpdatePostRequest;
import example.post.dto.post.response.*;
import example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class PostController {
    private final PostService postService;
    @PostMapping("/post")
    public SavePostResponse savePost(@RequestBody SavePostRequest request){
        Post post = postService.savePost(request);
        return new SavePostResponse(post.getId(), post.getUser().getId());
    }
    @GetMapping("/post")
    public StartPost getPostList(@RequestParam int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 20, Sort.by("createAt").descending());
        return new StartPost(postService.countPost(), postService.findAllPosts(pageRequest));
    }
    @GetMapping("/post/{postId}")
    public PostWithAllDetailResponse getDetail(@PathVariable Long postId){
        Post post = postService.findById(postId);
        List<CommentInPost> commentsResponse = post.getComments().stream()
                .map(comment -> new CommentInPost(comment.getId(), comment.getContent(), comment.getCreateAt(), comment.getUser().getId())).toList();
        PostOnlyPostResponse postResponse = new PostOnlyPostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreateAt(), post.getUser().getId());
        return new PostWithAllDetailResponse(postResponse, commentsResponse);
    }
    @PatchMapping("/post/{postId}")
    public UpdatePostResponse updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request){
        Post updatePost = postService.updatePost(postService.findById(postId), request.getTitle(), request.getContent());
        return new UpdatePostResponse(updatePost.getId(), updatePost.getUser().getId());
    }
    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId){
        return postService.deletePost(postId);
    }

}
