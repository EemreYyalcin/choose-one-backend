package com.chooseone.service;

import com.chooseone.data.redis.model.Imdb;
import com.chooseone.data.redis.repository.ImdbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImdbService {

    private final ImdbRepository imdbRepository;

    private List<Imdb> items = new ArrayList<>();


    //Run Once
//    @PostConstruct
    public void setup() {

        Set<String> titles = new HashSet<>();
        FileInputStream fis = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream("D:/tmp/sample.txt");
            reader = new BufferedReader(new InputStreamReader(fis));

            System.out.println("Reading File line by line using BufferedReader");

            String line = reader.readLine();
            while (line != null) {
                if (line.contains("title/")) {
                    titles.add(line.substring(line.indexOf("title") + 6, line.indexOf("?") - 1));
                }
                line = reader.readLine();
            }
            titles.forEach(title -> {
                imdbRepository.save(new Imdb().setImdbID(title));
            });
            System.out.println(titles);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
            }
        }

    }

    //    @PostConstruct
    public void setup2() {
        imdbRepository.getAllMovies().filter(e -> ObjectUtils.isEmpty(e.getPoster()))
                .map(e -> {
                    Imdb imdb;
                    try {
                        ResponseEntity<Imdb> response = new RestTemplate().getForEntity("http://www.omdbapi.com/?apikey=[API_KEY]&i=" + e.getImdbID(), Imdb.class);
                        imdb = response.getBody();
                        return imdb;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return e;
                    }
                }).filter(e -> !ObjectUtils.isEmpty(e.getPoster()))
                .map(e -> {
                    imdbRepository.save(e);
                    return e;
                }).subscribe();
    }


    @PostConstruct
    public void loadItems() {
        imdbRepository.getAllMovies()
                .filter(e -> !ObjectUtils.isEmpty(e.getPoster()))
                .map(e -> {
                    items.add(e);
                    return e;
                }).subscribe();
    }

    public List<Imdb> getRandomItems(int size) {
        Random random = new Random();
        List<Imdb> result = new ArrayList<>();
        int index;
        for (int i = 0; i < size; ++i) {
            index = random.nextInt(items.size());
            result.add(items.get(index));
        }
        return result;
    }

    public List<Imdb> getAllItems(){
        return items;
    }


}
