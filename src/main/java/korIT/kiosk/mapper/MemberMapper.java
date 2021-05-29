package korIT.kiosk.mapper;

import korIT.kiosk.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

     int insertMember(MemberDTO member);

     MemberDTO findByMemberId(int id);

     MemberDTO findByShop(String shopName);

     List<MemberDTO> findMembers();

     MemberDTO findByPwd(String pwd);
}
