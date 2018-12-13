package org.pierre.nexusclean;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NexuscleanApplication implements CommandLineRunner {
	ArtifactRepository artifactRepository = new ArtifactRepository();

	public static void main(String[] args) {
		SpringApplication.run(NexuscleanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File baseDir = new File(Configuration.NEXUS_STORAGE);
		// build a list of ALL artifacts in nexus2 repository
		navigate(baseDir);
		System.out.println("list of all artifacts");
		artifactRepository.printAllArtifacts(System.out);
		// build a list of unique artifacts - independent of version/date
		List<Artifact> unique = artifactRepository.findArtifactsWithUniqueGA();
		System.out.println("list of unique artifacts");
		artifactRepository.printAllArtifacts(unique, System.out);	
		// build now a list of artifacts who satisfy criteria (like: created in 2018, or belonging to 10 last releases)
		for (Artifact item : unique) {
			List<Artifact> survivors = artifactRepository.findArtifactsYoungerThanMinimumVersions(item, "2018", 10);
		}
		
	}

	
	/**
	 * Scan recursively a folder, identifying all pom files and adding artifact to collection
	 * @param baseDir
	 * @throws Exception
	 */
	private void navigate(File baseDir) throws Exception {
		File[] files = baseDir.listFiles();
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyymmddhhmmss");
		for (File file : files) {
			if (file.isDirectory() && !file.getName().equals(".nexus")) {
				navigate(file);
			} else {
				System.out.println(file.getAbsolutePath());
				if (file.getName().endsWith(".pom")) {
					MavenXpp3Reader reader = new MavenXpp3Reader();
					Model model = reader.read(new FileReader(file));
					Artifact artifact = new Artifact(model.getArtifactId(), model.getGroupId(), model.getVersion(),  dt.format(new Date(file.lastModified())));
					artifactRepository.add(artifact);
				}

			}
		}
		

	}

}
