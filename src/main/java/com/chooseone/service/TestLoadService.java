package com.chooseone.service;

import com.chooseone.data.redis.model.Imdb;
import com.chooseone.data.redis.model.test.QuestionDocument;
import com.chooseone.data.redis.model.test.TestDocument;
import com.chooseone.data.redis.repository.QuestionRepository;
import com.chooseone.data.redis.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestLoadService {

    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    private final ImdbService imdbService;

//    @PostConstruct
    public void loadTest() {

        try {
            List<TestDocument> tests = new ArrayList<TestDocument>() {{
                add(new TestDocument("movie", "Action"));
                add(new TestDocument("movie", "Animation"));
                add(new TestDocument("movie", "Adventure"));
                add(new TestDocument("movie", "Family"));
                add(new TestDocument("movie", "Fantasy"));
                add(new TestDocument("series", "Action"));
                add(new TestDocument("series", "Animation"));
                add(new TestDocument("series", "Adventure"));
                add(new TestDocument("series", "Family"));
                add(new TestDocument("series", "Fantasy"));
            }};

            for (int i = 0; i < tests.size(); ++i) {
                setupTest(tests.get(i), i + 1);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setupTest(TestDocument testDocument, int index) {

        testRepository.save(testDocument, index);

        try {
            Thread.sleep(10000);
        }catch (Exception ex){

        }

        List<Imdb> items = imdbService.getAllItems().stream()
                .filter(e -> !ObjectUtils.isEmpty(e.getType()) && !ObjectUtils.isEmpty(e.getGenre()))
                .filter(e -> testDocument.getType().equals(e.getType()))
                .filter(e -> e.getGenre().toLowerCase().contains(testDocument.getCategory().toLowerCase()))
                .collect(Collectors.toList());



        HashMap<String, QuestionDocument> questionsMap = new HashMap<>();

        for (int j = 0; j < 10; ++j) {
            Collections.shuffle(items);
            for (int i = 0; i < items.size() - 1; i += 2) {
                String item1 = items.get(i).getImdbID();
                String item2 = items.get(i + 1).getImdbID();
                if (questionsMap.containsKey(item1 + item2) || questionsMap.containsKey(item2 + item1)) {
                    continue;
                }
                questionsMap.put(item1 + item2, new QuestionDocument().setItem1(item1).setItem2(item2));
            }
        }

        int count = 1;
        for (String key : questionsMap.keySet()) {
            questionRepository.save(testDocument.toString(), UUID.randomUUID().toString().replaceAll("-", ""), questionsMap.get(key), count++);
        }
    }


}
