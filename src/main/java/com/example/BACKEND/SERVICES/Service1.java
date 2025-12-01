package com.example.BACKEND.SERVICES;

import com.example.BACKEND.ENTITY.Mytopcs;
import com.example.BACKEND.ENTITY.Mywork;
import com.example.BACKEND.ENTITY.Questions;
import com.example.BACKEND.ENTITY.Topics;
import com.example.BACKEND.REPOSOTORIES.Mytopicsrepo;
import com.example.BACKEND.REPOSOTORIES.Myworkrepo;
import com.example.BACKEND.REPOSOTORIES.Questionsrepo;
import com.example.BACKEND.REPOSOTORIES.Topicsrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class Service1 {
    @Autowired
    private Topicsrepo topicsrepo;

    @Autowired
    private Questionsrepo questionsrepo;

    @Autowired
    private Myworkrepo myworkrepo;

    @Autowired
    private Mytopicsrepo mytopicsrepo;

    public List<Mytopcs> gettopics(String topic,String username){
        List<Mytopcs> topics = mytopicsrepo.findByTopicAndUsername(topic,username);
        return topics;
    }

    public List<Questions> getquestions(String username){
        List<Questions> questions = questionsrepo.findByUsername(username);
        return questions;
    }

    public ResponseEntity<?> add_dsa_work(Mywork mywork){
        mywork.setDate(java.sql.Date.valueOf(LocalDate.now()));
        mywork.setWork(true);
        mywork.setTopic("DSA");
        myworkrepo.save(mywork);
        if(mytopicsrepo.existsByUsernameAndTopicAndSubtopic(mywork.getUsername(),mywork.getTopic(),mywork.getSubtopic())){
            Mytopcs mytopcs = mytopicsrepo.findByUsernameAndTopicAndSubtopic(mywork.getUsername(),mywork.getTopic(),mywork.getSubtopic());
            int num = mytopcs.getQues();
            mytopcs.setQues(num+1);
            mytopicsrepo.save(mytopcs);
        }
        else{
            Mytopcs mytopcs = new Mytopcs();
            mytopcs.setSubtopic(mywork.getSubtopic());
            mytopcs.setTopic(mywork.getTopic());
            mytopcs.setQues(1);
            mytopcs.setUsername(mywork.getUsername());
            mytopicsrepo.save(mytopcs);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Work Added Successfully"));
    }

    public ResponseEntity<?> add_other_work(Mywork mywork) {
        mywork.setCode("");
        mywork.setAttempted(true);
        mywork.setWork(true);
        mywork.setSubtopic("");
        mywork.setDate(java.sql.Date.valueOf(LocalDate.now()));
        mywork.setLink("");
        myworkrepo.save(mywork);
        if(mytopicsrepo.existsByTopicAndUsername(mywork.getTopic(),mywork.getUsername())){
            List<Mytopcs> mytopcsList = mytopicsrepo.findByTopicAndUsername(mywork.getTopic(), mywork.getUsername());
            for (Mytopcs mytopcs : mytopcsList) {
                int num = mytopcs.getQues();
                mytopcs.setQues(num + 1);
                mytopicsrepo.save(mytopcs);
            }
        }
        else{
            Mytopcs mytopcs = new Mytopcs();
            mytopcs.setSubtopic("");
            mytopcs.setTopic(mywork.getTopic());
            mytopcs.setQues(1);
            mytopcs.setUsername(mywork.getUsername());
            mytopicsrepo.save(mytopcs);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Work Added Successfully"));
    }

    public ResponseEntity<?> get_dsa_work(String username) {
        List<Mywork> myworks = myworkrepo.findByUsernameAndTopic(username,"DSA");
        return new ResponseEntity<>(myworks,HttpStatus.OK);
    }

    public ResponseEntity<?> get_other_work(String username, String topic) {
        List<Mywork> myworks = myworkrepo.findByUsernameAndTopic(username,topic);
        return  new ResponseEntity<>(myworks,HttpStatus.OK);
    }

    public ResponseEntity<?> toggleattempt(String username, Long id) {
        List<Mywork> myworks = myworkrepo.findByUsernameAndId(username,id);
        myworks.get(0).setAttempted(true);
        return ResponseEntity.ok(Collections.singletonMap("message","attempted"));
    }

    public ResponseEntity<?> modifydata(String username, Mywork mywork) {
        List<Mywork> myworks = myworkrepo.findByUsernameAndId(username,mywork.getId());
        myworks.get(0).setQuestion(mywork.getQuestion());
        myworks.get(0).setQuestioninfo(mywork.getQuestioninfo());
        myworks.get(0).setImportant(mywork.isImportant());
        myworks.get(0).setLogic(mywork.getLogic());
//        myworks.get(0).setDate(mywork.getDate());
        myworks.get(0).setCode(mywork.getCode());
        myworks.get(0).setAttempted(mywork.isAttempted());
//        myworks.get(0).setWork(mywork.isWork());
//        myworks.get(0).setTopic(mywork.getTopic());
        myworks.get(0).setSubtopic(mywork.getSubtopic());
//        myworks.get(0).setUsername(username);
        myworks.get(0).setLink(mywork.getLink());
        myworkrepo.save(myworks.get(0));
        return ResponseEntity.ok(Collections.singletonMap("message","updated and saved in db"));
    }
}
