package org.pierre.nexusclean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NexuscleanApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NexuscleanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File baseDir = new File(Configuration.NEXUS_STORAGE);
		navigate(baseDir);

	}

	private void navigate(File baseDir) throws Exception {
		File[] files = baseDir.listFiles();
		for (File file : files) {
			if (file.isDirectory() && !file.getName().equals(".nexus")) {
				navigate(file);
			} else {
				System.out.println(file.getAbsolutePath());
				if (file.getName().endsWith(".pom")) {
					MavenXpp3Reader reader = new MavenXpp3Reader();
					Model model = reader.read(new FileReader(file));
					System.out.println("groupId=" + model.getGroupId() + " artifactId=" + model.getArtifactId() + " version=" + model.getVersion());
				}

			}
		}

	}

}
