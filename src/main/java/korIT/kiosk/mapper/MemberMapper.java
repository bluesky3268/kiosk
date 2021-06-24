package korIT.kiosk.mapper;

import korIT.kiosk.dto.Criteria;
import korIT.kiosk.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

     int insertMember(MemberDTO member);

     MemberDTO findByMemberId(int id);

     MemberDTO findByUsername(String username);

     List<MemberDTO> findByRole(String role);

     List<MemberDTO> findMembers();

     // 페이징처리
     List<MemberDTO> findManagersWithPaging(Criteria criteria);

     int countManagers(String role);

}
