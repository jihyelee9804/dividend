package com.jihye.dividend.persist;

import com.jihye.dividend.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // id를 전달인자로 받고 해당하는 회원정보를 반환한다.
    Optional<MemberEntity> findByUsername(String username);
    // 존재하는 id인지 확인
    boolean existsByUsername(String username);
}
