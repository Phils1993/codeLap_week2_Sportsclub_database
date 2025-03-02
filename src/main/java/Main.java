import entities.Member;
import entities.Registration;
import exceptions.DatabaseException;
import persistence.Database;
import persistence.MemberMapper;
import persistence.RegistrationMapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/sportsclub_Phils?currentSchema=public";

    public static void main(String[] args) throws DatabaseException, SQLException {

        Database db = new Database(USER, PASSWORD, URL);
        MemberMapper memberMapper = new MemberMapper(db);
        RegistrationMapper registrationMapper = new RegistrationMapper(db);
        List<Member> members = memberMapper.getAllMembers();

        showMembers(members);
        showMemberById(memberMapper, 13);

        // task 7:

        System.out.println("Members on each teams: " + MemberMapper.findNoOfEachParticipantsOnEachTeam());
        System.out.println("*******************");
        //task 8
        System.out.println("All members on each sport: " + MemberMapper.findNoOfEachParticipantsOnSport());
        System.out.println("***********");
        // task 9
        System.out.println("Genders in the club: " + MemberMapper.memberGender());
        System.out.println("********************");
        // task 10
        System.out.println("Total income for teams: kr. " + MemberMapper.sumOfIncome());
        System.out.println("*************");
        // task 11
        System.out.println("Total income for For Each Team: " + MemberMapper.sumOfIncomeForEachTeam());
        System.out.println("*************");
        // task 12
        System.out.println("Average income for each team: " + MemberMapper.averageIncomeForEachTeam());

       // registrationMapper.addToTeam(new Registration(10,"gym01",200));
        // task 13 + 14 + 15 + 16
        //registrationMapper.addToTeam(new Registration(50,"gym01",180));
        registrationMapper.getAllRegistations().forEach(System.out::println);







        /*  
            int newMemberId = insertMember(memberMapper);
            deleteMember(newMemberId, memberMapper);
            showMembers(members);
            updateMember(13, memberMapper);
        */
    }


    private static void deleteMember(int memberId, MemberMapper memberMapper) throws DatabaseException {
        if (memberMapper.deleteMember(memberId)){
            System.out.println("Member with id = " + memberId + " is removed from DB");
        }
    }

    private static int insertMember(MemberMapper memberMapper)throws DatabaseException {
        Member m1 = new Member("Ole Olsen", "Banegade 2", 3700, "Rønne", "m", 1967);
        Member m2 = memberMapper.insertMember(m1);
        showMemberById(memberMapper, m2.getMemberId());
        return m2.getMemberId();
    }

    private static void updateMember(int memberId, MemberMapper memberMapper) throws DatabaseException {
        Member m1 = memberMapper.getMemberById(memberId);
        m1.setYear(1970);
        if(memberMapper.updateMember(m1)){
            showMemberById(memberMapper, memberId);
        }
    }

    private static void showMemberById(MemberMapper memberMapper, int memberId) throws DatabaseException {
        System.out.println("***** Vis medlem nr. 13: *******");
        System.out.println(memberMapper.getMemberById(memberId).toString());
    }

    private static void showMembers(List<Member> members) throws DatabaseException {
        System.out.println("***** Vis alle medlemmer *******");
        for (Member member : members) {
            System.out.println(member.toString());
        }
    }


}
