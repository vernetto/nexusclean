package org.pierre.nexusclean;

import org.springframework.beans.factory.annotation.Value;

public class Configuration {
	@Value("${folderToScan}")
	public static String NEXUS_STORAGE;

}
