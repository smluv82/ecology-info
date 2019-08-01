package me.ecology.common.util;

import java.io.Reader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;
import me.ecology.vo.ecology.EcologyGroup;

@Slf4j
public class CsvUtil {
	public static List<EcologyGroup> readAll(final MultipartFile csvFile) throws Exception {
		List<EcologyGroup> list = Lists.newArrayList();

		try (
				Reader reader = getReader(csvFile);
				CSVReader csvReader = new CSVReaderBuilder(getReader(csvFile))
						.withSkipLines(1)
						.withCSVParser(getParser())
						.build();
				) {
			String[] strArr;

			while ((strArr = csvReader.readNext()) != null) {
				EcologyGroup ecologyGroup = new EcologyGroup();
				//TODO 레디스로 변경 필요 할 듯
				ecologyGroup.setProgramId(String.join(StringUtils.EMPTY, "prgm", String.format("%04d", Integer.parseInt(strArr[0]))));
				ecologyGroup.setProgramName(strArr[1]);
				ecologyGroup.setTheme(strArr[2]);
				ecologyGroup.setRegionName(strArr[3]);
				ecologyGroup.setProgramInfo(strArr[4]);
				ecologyGroup.setProgramDetail(strArr[5]);
				list.add(ecologyGroup);
			}
		}catch(Exception e) {
			log.error("readAll Exception : ", e);
		}

		return list;
	}

	private static Reader getReader(final MultipartFile csvFile) throws Exception {
		return CharSource.wrap(new String(csvFile.getBytes(), "EUC-KR")).openStream();
	}

	private static CSVParser getParser() throws Exception {
		return new CSVParserBuilder()
				.withSeparator(',')
				.withIgnoreQuotations(false)
				.withQuoteChar('"')
				.build();
	}
}
