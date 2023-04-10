package com.dot.filereader;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//user_access
@SpringBootTest(args = {"--accessFile=/Users/chinonsoharrison/IdeaProjects/fileReader/src/test/resources/test_user_access.txt","--start=2022-01-01 00:00:01","--duration=hourly", "--limit=3" })
class FileReaderApplicationTests {

	@Test
	void contextLoads() {
	}

}
