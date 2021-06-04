package korIT.kiosk.service;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.mapper.MemberMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberServicerTest {

    @Autowired
    private MemberMapper memberMapper;


    @Test
    @Transactional
    @Rollback(value=true)
    public void insertMember() {

        MemberDTO member = new MemberDTO("ramen_shop", "1234", "1234", "ROLE_MANAGER" , "2021-06-03");
        int insertedMember = memberMapper.insertMember(member);

        System.out.println("[ Inserted memberID : " + member.getId() + " ]");

        MemberDTO findOne = memberMapper.findByUsername("ramen_shop");
        System.out.println("[ findMember : " + findOne + " ]");

        Assertions.assertThat(findOne.getUsername()).isEqualTo(member.getUsername());
        System.out.println("[ Member추가 성공! ]");

    }

}
