package be.stepnote.member.repository;

import be.stepnote.member.entity.Member;
import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findAllUsers();

    List<Member> findBlockedMembers(Long blockerId);
}



