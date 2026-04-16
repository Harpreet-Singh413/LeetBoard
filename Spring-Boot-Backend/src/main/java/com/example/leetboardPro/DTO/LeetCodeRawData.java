package com.example.leetboardPro.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LeetCodeRawData {

    private Data data;

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        private MatchedUser matchedUser;
        public MatchedUser getMatchedUser() { return matchedUser; }
        public void setMatchedUser(MatchedUser matchedUser) { this.matchedUser = matchedUser; }
    }

    public static class MatchedUser {
        private SubmitStats submitStats;
        public SubmitStats getSubmitStats() { return submitStats; }
        public void setSubmitStats(SubmitStats submitStats) { this.submitStats = submitStats; }
    }

    public static class SubmitStats {
        @JsonProperty("acSubmissionNum")
        private List<ACSubmissionNum> acSubmissionNum;
        public List<ACSubmissionNum> getAcSubmissionNum() { return acSubmissionNum; }
        public void setAcSubmissionNum(List<ACSubmissionNum> acSubmissionNum) { this.acSubmissionNum = acSubmissionNum; }
    }

    public static class ACSubmissionNum {
        private String difficulty;
        private int count;
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}