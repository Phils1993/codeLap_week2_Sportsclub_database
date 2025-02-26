package DTO;

import java.util.List;
public class MemberAndSportsDTO {
    private int memberId;
    private String name;
    private String sport;
    private List<String> sports;

    public MemberAndSportsDTO(int memberId, String name, String sport, List<String> sports) {
        this.memberId = memberId;
        this.name = name;
        this.sport = sport;
        this.sports = sports;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }
}

