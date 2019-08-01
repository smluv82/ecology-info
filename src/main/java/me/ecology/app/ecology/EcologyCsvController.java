package me.ecology.app.ecology;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.ecology.service.EcologyService;
import me.ecology.common.util.CsvUtil;
import me.ecology.vo.ecology.EcologyGroup;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/ecology/csv")
public class EcologyCsvController {
	private final EcologyService ecologyService;

	@PostMapping(value="/add")
	public ResponseEntity<EcologyGroup> csvAdd(@RequestParam("csvFile") MultipartFile csvFile) throws Exception {
		log.info("csvInfo restCall");

		if(csvFile.isEmpty())
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		return ResponseEntity.ok(ecologyService.saveCsv(CsvUtil.readAll(csvFile)));
	}


}
