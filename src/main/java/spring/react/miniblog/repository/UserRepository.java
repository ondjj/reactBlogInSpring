package spring.react.miniblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.react.miniblog.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User save(User user);

//    User update(User user);

//    @Query("SELECT m.auth FROM MemberAuth m WHERE m.userNo = :userNo")
//    List<MemberAuth> findMemberAuthByUserNo(Long userNo);

    Optional<User> findById(Long id);

    Optional<User> findMemberByRefreshToken(String refreshToken);
//    @Query("SELECT new com.shop.domain.codelabel.CodeLabelValue(clv.code, clv.label) " +
//            "FROM CodeLabelValue clv " +
//            "WHERE clv.groupCode = :groupCode")
//
//    List<CodeLabelValue> getCodeLabelValueList(String groupCode);

}
