package me.ecology.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import me.ecology.app.ecology.service.EcologyService;
import me.ecology.vo.ecology.EcologyCode;
import me.ecology.vo.ecology.EcologyParam;
import me.ecology.vo.ecology.EcologyProgram;
import me.ecology.vo.ecology.EcologyResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EcologyTest {
	@Resource
	private EcologyService ecologyService;

	@Test
	public void 생태_관광_정보_조회() throws Exception {
		String regionId = "reg1455";

		EcologyCode ecologyCode = ecologyService.search(regionId);
		System.out.println("ecologyCode : " + ecologyCode);
		assertNotNull(ecologyCode);
	}

	@Test
	public void 생태_관광_정보_추가() throws Exception {
		EcologyParam ecologyParam = new EcologyParam();
		ecologyParam.setProgramName("testPrgmName992");
		ecologyParam.setTheme("testTheme999");
		ecologyParam.setRegionName("testRegionName991");
		//optional
		ecologyParam.setProgramInfo("testProgramInfo999");
		ecologyParam.setProgramDetail("detail, detail");
		System.out.println("req ecologyParam : " + ecologyParam);

		EcologyProgram resPrgm = ecologyService.create(ecologyParam);
		System.out.println("resPrgm : " + resPrgm);
		assertNotNull(resPrgm);
	}

	@Test
	public void 생태_관광_정보_수정() throws Exception {
		EcologyParam ecologyParam = new EcologyParam();
		ecologyParam.setProgramId("prgm0112");
		ecologyParam.setRegionName("testRegionName999");

		ecologyParam.setTheme("testTheme999 modify");
		ecologyParam.setProgramInfo("testProgramInfo999 modify");
		ecologyParam.setProgramDetail("detail3, detail modify");

		System.out.println("req ecologyParam : " + ecologyParam);

		EcologyProgram resPrgm = ecologyService.update(ecologyParam);
		System.out.println("resPrgm : " + resPrgm);
		assertNotNull(resPrgm);
	}

	@Test
	public void 특정지역명_검색() throws Exception {
		String regionName = "강원도 속초";

		EcologyCode resCode = ecologyService.searchForRegion(regionName);
		System.out.println("resCode : " + resCode);
		assertNotNull(resCode);

		List<EcologyProgram> list = resCode.getEcologyPrograms();
		for(EcologyProgram prgm : list) {
			System.out.println("prgm : " + prgm);
		}
		assertFalse(list.isEmpty());
	}

	@Transactional
	@Test
	public void 프로그램_소개_키워드로_카운트_조회() throws Exception {
		String keyword = "주왕산";

		EcologyResult result = ecologyService.searchForInfo(keyword);
		System.out.println("result : " + result);
		assertNotNull(result);
	}

	@Test
	public void 프로그램_상세_정보_키워드로_카운트_조회() throws Exception {
		String keyword = "생";

		EcologyResult result = ecologyService.searchForDetail(keyword);
		System.out.println("result : " + result);
		assertNotNull(result);
	}

	@Transactional
	@Test
	public void 지역_및_키워드를_통한_프로그램_추천() throws Exception {
		EcologyParam param = new EcologyParam();
		param.setRegionName("경기도");
		param.setKeyword("문화");
		System.out.println("req param : " + param);

		EcologyResult result = ecologyService.searchForWeight(param);
		System.out.println("result : " + result);
		assertNotNull(result);
		assertNotNull(result.getProgram());
	}
}
