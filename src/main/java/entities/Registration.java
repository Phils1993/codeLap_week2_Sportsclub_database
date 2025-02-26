package entities;

public class Registration {
    private int member_id;
    private String team_id;
    private int price;
    private String sport;
    private String member_name;

    public Registration(int member_id, String team_id, int price) {
        this.member_id = member_id;
        this.team_id = team_id;
        this.price = price;
    }

    public Registration(int member_id, String team_id, int price, String sport, String member_name) {
        this.member_id = member_id;
        this.team_id = team_id;
        this.price = price;
        this.sport = sport;
        this.member_name = member_name;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "member_id=" + member_id +
                ", team_id='" + team_id + '\'' +
                ", price=" + price +
                ", sport='" + sport + '\'' +
                ", member_name='" + member_name + '\'' +
                '}';
    }
}
