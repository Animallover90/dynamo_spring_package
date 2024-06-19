package dynamo_spring_package.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {
    
    private final TestRepository testRepository;

    @GetMapping
    public List<TestDTO> getAllTests() {
        return testRepository.getAllTests();
    }

    @GetMapping("/{id}")
    public TestDTO getTestById(@PathVariable String id) {
        return testRepository.getTestById(id);
    }

    @PostMapping
    public TestDTO createTest(@RequestBody TestDTO testDTO) {
        return testRepository.createNewTest(testDTO);
    }

    @PutMapping()
    public TestDTO updateTest(@RequestBody TestDTO testDTO) {
        return testRepository.updateTest(testDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTest(@PathVariable String id) {
        testRepository.deleteTest(id);
    }
}
