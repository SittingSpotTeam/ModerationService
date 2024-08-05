package com.sittingspot.moderationservice.controller;

import org.springframework.web.bind.annotation.*;

import org.apache.commons.csv.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.net.URL;

@RestController
@RequestMapping("moderation/api/v1")
public class ModerationServiceController {
    private Set<String> wordSet = new HashSet<>();
    private String wordsUrl = "https://www.cs.cmu.edu/~biglou/resources/bad-words.txt";


    @PostMapping
    private String censorReview(@RequestBody String corpus){
        // System.out.println(wordSet.size());
        System.out.println("Received:"+corpus);
        if(this.wordSet.size()==0){
            loadWords();
            // System.out.println(wordSet.size());
        }
        for (String str : wordSet) {    
            str="(?i)"+str;
            corpus = corpus.replaceAll(str, "*".repeat(str.length()-4));
        }
        return corpus;
    }

    private void loadWords(){
        // System.out.println(wordsUrl);
        try (InputStream inputStream = new URL(wordsUrl).openStream();
            Reader reader = new InputStreamReader(inputStream);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                // Assuming the word is in the first column
                String word = record.get(0);
                wordSet.add(word.trim().toLowerCase()); // Normalize to lower case
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
