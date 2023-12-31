package example.post.service;

import example.post.domain.Post;
import example.post.domain.User;
import example.post.dto.post.request.SavePostRequest;
import example.post.dto.post.request.UpdatePostRequest;
import example.post.dto.post.response.PostOnlyPostResponse;
import example.post.repository.PostRepository;
import example.post.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    @Transactional
    public Post savePost(SavePostRequest savePostRequest){
        User user = userService.findById(SecurityUtil.getCurrentUserId());
        return postRepository.save(new Post(savePostRequest.getTitle(), savePostRequest.getContent(), user));
    }
    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));
    }
    public List<PostOnlyPostResponse> findAllPosts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest).stream().map(findPost -> new PostOnlyPostResponse(findPost.getId(), findPost.getTitle(), findPost.getContent(), findPost.getCreateAt(), findPost.getUser().getId())).toList();
    }
    public Long countPost(){
        return postRepository.count();
    }
    @Transactional
    public Post updatePost(Post post, String title, String content){
        return post.updatePost(title, content);
    }
    @Transactional
    public String deletePost(Long id){
        Post findPost = findById(id);
        postRepository.delete(findPost);
        return "ok";
    }
}
