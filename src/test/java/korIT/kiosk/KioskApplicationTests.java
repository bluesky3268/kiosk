package korIT.kiosk;

import korIT.kiosk.dto.MemberDTO;
import korIT.kiosk.mapper.MemberMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class KioskApplicationTests {
//
//	@Autowired
//	private SqlSessionTemplate sqlSession;

	@Autowired
	private MemberMapper memberMapper;

//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	public void testSqlSession() throws Exception{
//		System.out.println(sqlSession.toString());
//	}


	@Test
	@Transactional
	@Rollback(value=true)
	public void insertMember() {

		MemberDTO member = new MemberDTO("ramen_shop", "1234", "1234");
		int insertedMember = memberMapper.insertMember(member);

		System.out.println("[ Inserted memberID : " + member.getId() + " ]");

		MemberDTO findOne = memberMapper.findByShop("ramen_shop");
		System.out.println("[ findMember : " + findOne + " ]");

		Assertions.assertThat(findOne.getShop()).isEqualTo(member.getShop());
		System.out.println("[ Member추가 성공! ]");

	}

}
