package bk.controller;

import bk.model.*;
import bk.repository.AuthorRepository;
import bk.repository.CandidateRepository;
import bk.ulti.CandidateService;
import bk.ulti.CoAuthorShipSimplyService;
import bk.ulti.StreamGobbler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@Controller
public class CandidateController {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    CoAuthorShipSimplyService coAuthorShipSimplyService;
    @Autowired
    CandidateService candidateService;
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 50;
    private static final int[] PAGE_SIZES = {50, 100, 200};

    @GetMapping("/candidates")
    public String candidate(
            @RequestParam(value = "page", required = false, defaultValue = "0") Optional<Integer> page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Optional<Integer> pageSize,
            Model model) {
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Pageable pageable = new PageRequest(evalPage, evalPageSize);
        Page<Candidate> candidates = candidateRepository.findAll(pageable);
        Pager pager = new Pager(candidates.getTotalPages(), candidates.getNumber(), BUTTONS_TO_SHOW);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Candidate candidate : candidates.getContent()) {
            Map<String, Object> miniMap = new HashMap<>();
            miniMap.put("id1", candidate.getAuthorId1());
            miniMap.put("id2", candidate.getAuthorId2());
            Author author1 = authorRepository.findById(candidate.getAuthorId1());
            Author author2 = authorRepository.findById(candidate.getAuthorId2());
            miniMap.put("author1", author1.getGivenName() + " " + author1.getSurname());
            miniMap.put("author2", author2.getGivenName() + " " + author2.getSurname());
            miniMap.put("cn", candidate.getCN());
            miniMap.put("jc", candidate.getJC());
            miniMap.put("aa", candidate.getAA());
            miniMap.put("wcn", candidate.getWCN());
            miniMap.put("wjc", candidate.getWJC());
            miniMap.put("waa", candidate.getWAA());
            miniMap.put("label", candidate.isLabel());
            mapList.add(miniMap);
        }
        model.addAttribute("maps", mapList);
        model.addAttribute("candidates", candidates);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);

        return "candidate";
    }

    @GetMapping("/candidates/train")
    public String train() {
        exportFileResult("result.json", "/home/quangminh/anaconda2/bin/python2.7 /home/quangminh/IdeaProjects/testpython/F1AndLogisticRegression.py");
        final String redirectUrl = "redirect:http://localhost:8078/statics";
        return redirectUrl;
    }

    @GetMapping("/candidates/after-predict")
    public String afterPredict(
            @RequestParam(value = "page", required = false, defaultValue = "0") Optional<Integer> page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Optional<Integer> pageSize,
            @RequestParam(value = "flag", required = false, defaultValue = "false") boolean flag,
            Model model
    ) throws FileNotFoundException {
        JSONArray jsonArray = null;
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Pageable pageable = new PageRequest(evalPage, evalPageSize);
        Page<Candidate> candidates = candidateRepository.findByYearStartGreaterThanEqualAndYearEndLessThanEqual(2000, 2005, pageable);
        List<Map<String, Object>> mapList = new ArrayList<>();
        Pager pager = new Pager(candidates.getTotalPages(), candidates.getNumber(), BUTTONS_TO_SHOW);
        if (flag) {
            //doc file
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader("/home/quangminh/IdeaProjects/Journal (copy)/result-predict.json"));
                jsonArray = (JSONArray) obj;
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < candidates.getContent().size(); i++) {
            Candidate candidate = candidates.getContent().get(i);
            Map<String, Object> miniMap = new HashMap<>();
            miniMap.put("id1", candidate.getAuthorId1());
            miniMap.put("id2", candidate.getAuthorId2());
            Author author1 = authorRepository.findById(candidate.getAuthorId1());
            Author author2 = authorRepository.findById(candidate.getAuthorId2());
            miniMap.put("author1", author1.getGivenName() + " " + author1.getSurname());
            miniMap.put("author2", author2.getGivenName() + " " + author2.getSurname());
            miniMap.put("label", candidate.isLabel());
            if (flag) {
                miniMap.put("labelByNormal", (Boolean) jsonArray.get(i));
                miniMap.put("labelByWeight", (Boolean) jsonArray.get(i));

            } else {
                miniMap.put("labelByNormal", candidate.isPredictByNormalMeasure());
                miniMap.put("labelByWeight", candidate.isPredictByWeightedMeasure());
            }
            mapList.add(miniMap);
        }
        model.addAttribute("maps", mapList);
        model.addAttribute("candidates", candidates);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "predict";
    }


    @RequestMapping(value = "/candidates/get-list-candidate", method = RequestMethod.GET)
    public String getlist(@RequestParam(value = "id", required = false, defaultValue = " ") String id,
                          Model model) {
        if (id.compareTo(" ") != 0) {
            ObjectMapper mapper = new ObjectMapper();
            List<Candidate> listCandidate = new ArrayList<>();
            //get list candidate
            List<String> candidatesById = coAuthorShipSimplyService.getCandidateAuthorBetweenYear(id, Var.yearStart, Var.yearEnd);
            //measure this
            for (String idByEachCandidate : candidatesById) {
                Candidate candidate = new Candidate();
                candidate.setAuthorId1(id);
                candidate.setAuthorId2(idByEachCandidate);
                candidate.setYearStart(Var.yearStart);
                candidate.setYearEnd(Var.yearEnd);
                List<String> listCommon = coAuthorShipSimplyService.getCommonNeighbours(candidate.getAuthorId1(), candidate.getAuthorId2(), candidate.getYearStart(), candidate.getYearEnd());
                candidate.setAA(candidateService.AAMeasure(candidate, listCommon, candidate.getYearStart(), candidate.getYearEnd()));
                candidate.setCN(listCommon.size());
                candidate.setJC(candidateService.JCMeasure(candidate, listCommon, candidate.getYearStart(), candidate.getYearEnd()));
                candidate.setWAA(candidateService.WAAMeasure(candidate, listCommon, candidate.getYearStart(), candidate.getYearEnd()));
                candidate.setWCN(candidateService.WCNMeasure(candidate, listCommon, candidate.getYearStart(), candidate.getYearEnd()));
                candidate.setWJC(candidateService.WJCMeasure(candidate, listCommon, candidate.getYearStart(), candidate.getYearEnd()));
                listCandidate.add(candidate);
            }
            try (FileWriter file = new FileWriter("./candidates.json")) {
                String jsonInString = mapper.writeValueAsString(listCandidate);
                file.write(jsonInString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //print file
            exportFileResult("result_predict.json", "/home/quangminh/anaconda2/bin/python2.7 /home/quangminh/IdeaProjects/testpython/UseFileForClassifier.py");
            List<Map<String, String>> results = new ArrayList<>();
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader("/home/quangminh/IdeaProjects/Journal (copy)/result_predict.json"));
                JSONArray msg = (JSONArray) obj;
                for (int i = 0; i < msg.size(); i++) {
                    Map<String, String> result = new HashMap<>();
                    String authorId1 = ((JSONObject) msg.get(i)).get("authorId1").toString();
                    String authorId2 = ((JSONObject) msg.get(i)).get("authorId2").toString();
                    String authorName1 = authorRepository.findById(authorId1).getGivenName() + " " + authorRepository.findById(authorId1).getSurname();
                    String authorName2 = authorRepository.findById(authorId2).getGivenName() + " " + authorRepository.findById(authorId2).getSurname();
                    result.put("authorId1", authorId1);
                    result.put("authorId2", authorId2);
                    result.put("author1", authorName1);
                    result.put("author2", authorName2);
                    String normalPredict = ((JSONObject) msg.get(i)).get("normal_predict").toString();
                    String weightedPredict = ((JSONObject) msg.get(i)).get("weighted_predict").toString();
                    if (normalPredict.compareTo("0") == 0 && weightedPredict.compareTo("0") == 0) {
                        continue;
                    } else {
                        if (normalPredict.compareTo("1") == 0) {
                            result.put("normal_predict", "true");
                        } else {
                            result.put("normal_predict", "false");
                        }
                        if (weightedPredict.compareTo("1") == 0) {
                            result.put("weighted_predict", "true");
                        } else {
                            result.put("weighted_predict", "false");
                        }
                    }
                    results.add(result);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.addAttribute("results", results);
        }
        return "user-predict";
    }

    private List<Tag> simulateSearchResult(String tagName) {
        List<Tag> data = new ArrayList<Tag>();
        data.add(new Tag(1, "ruby"));
        data.add(new Tag(2, "rails"));
        data.add(new Tag(3, "c / c++"));
        data.add(new Tag(4, ".net"));
        data.add(new Tag(5, "python"));
        data.add(new Tag(6, "java"));
        data.add(new Tag(7, "javascript"));
        data.add(new Tag(8, "jscript"));
        List<Tag> result = new ArrayList<Tag>();
        // iterate a list and filter by tagName
        for (Tag tag : data) {
            if (tag.getTagName().contains(tagName)) {
                result.add(tag);
            }
        }
        return result;
    }

    @GetMapping("/candidates/test")
    public String test() {
        exportFileResult("result-predict.json", "/home/quangminh/anaconda2/bin/python2.7 /home/quangminh/IdeaProjects/testpython/TestData.py");
        final String redirectUrl = "redirect:http://localhost:8078/candidates/after-predict?flag=false";
        return redirectUrl;
    }

    @GetMapping("/candidates/predict")
    public String predict() {
        return "test";
    }

    public void exportFileResult(String name, String command) {
        try {
            FileOutputStream fos = new FileOutputStream(name);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            // any error message?
            StreamGobbler errorGobbler = new
                    StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new
                    StreamGobbler(proc.getInputStream(), "OUTPUT", fos);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
            fos.flush();
            fos.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}

