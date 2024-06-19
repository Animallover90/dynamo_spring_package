package dynamo_spring_package.sns;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sns")
@RequiredArgsConstructor
public class SnsController {
    
    private final SnsRepository snsRepository;

    @GetMapping("/{id}")
    public Sns getUserInfoByUserID(@PathVariable String id) {
        return snsRepository.getUserInfoByUserID(id);
    }

    @GetMapping("/followers/{id}")
    public List<Sns> getFollowerListByUserID(@PathVariable String id) {
        return snsRepository.getFollowerListByUserID(id);
    }

    @GetMapping("/followings/{id}")
    public List<Sns> getFollowingListByUserID(@PathVariable String id) {
        return snsRepository.getFollowingListByUserID(id);
    }

    @GetMapping("/posts/{id}")
    public List<Sns> getPostListByUserID(@PathVariable String id) {
        return snsRepository.getPostListByUserID(id);
    }

    @GetMapping("/post/likes/{id}")
    public List<Sns> getUserLikesByPostID(@PathVariable String id) {
        return snsRepository.getUserLikesByPostID(id);
    }

    @GetMapping("/post/count/{id}")
    public Long getLikeCountByPostID(@PathVariable String id) {
        return snsRepository.getLikeCountByPostID(id);
    }

    @GetMapping("/timelines/{id}")
    public List<Sns> getTimelineByUserID(@PathVariable String id) {
        return snsRepository.getTimelineByUserID(id);
    }

}
