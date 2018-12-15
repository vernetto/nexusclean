package org.pierre.nexusclean;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class NexuscleanApplication implements CommandLineRunner {
	@Value("${folderToScan}")
	public String NEXUS_STORAGE;
	
	@Value("${urlForDelete}")
	public String URLFORDELETE;
	
	@Value("${dateAfter}")
	public String DATEAFTER;
	
	@Value("${versionsToRetain}")
	public int VERSIONSTORETAIN;
	

    @Autowired
    Environment env;
	
	
	ArtifactRepository artifactRepository = new ArtifactRepository();

	public static void main(String[] args) {
		SpringApplication.run(NexuscleanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("NEXUS_STORAGE=" + NEXUS_STORAGE + " URLFORDELETE=" + URLFORDELETE + " DATEAFTER=" + DATEAFTER + " VERSIONSTORETAIN=" + VERSIONSTORETAIN);
		System.out.println("scanning folder " + NEXUS_STORAGE);
		File baseDir = new File(NEXUS_STORAGE);
		// build a list of ALL artifacts in nexus2 repository
		navigate(baseDir);
		artifactRepository.sort();
		System.out.println("list of all artifacts");
		artifactRepository.printAllArtifacts(System.out);
		// build a list of unique artifacts - independent of version/date
		List<Artifact> unique = artifactRepository.findArtifactsWithUniqueGA();
		System.out.println("list of unique artifacts");
		artifactRepository.printAllArtifacts(unique, System.out);
		// build now a list of artifacts who satisfy criteria (like: created in 2018, or
		// belonging to 10 last releases)
		List<Artifact> allSurvivors = new ArrayList<Artifact>();
		for (Artifact item : unique) {
			List<Artifact> survivors = artifactRepository.findArtifactsYoungerThanMinimumVersions(item, DATEAFTER, VERSIONSTORETAIN);
			allSurvivors.addAll(survivors);
		}
		System.out.println("list of survivor artifacts");
		artifactRepository.printAllArtifacts(allSurvivors, System.out);
		List<Artifact> toBeDeleted = artifactRepository.removeFromSet(allSurvivors);
		
		// verify that all "tobedeleted" have at least VERSIONSTORETAIN survivors
		int checked = 0;
		for (Artifact item : toBeDeleted) {
			List<Artifact> survivors = artifactRepository.findArtifactsWithSameGA(item, allSurvivors);
			if (survivors.size() < VERSIONSTORETAIN) {
				throw new IllegalArgumentException("Artifact to be deleted doesn't have enough survivors " + item);
			}
			checked++;
		}
		System.out.println(checked  + " artifacts to be deleted have been verified");

		System.out.println("\nlist of tobedeleted artifacts");
		
		artifactRepository.printAllArtifacts(toBeDeleted, System.out);
		System.out.println("curl to delete artifacts");
		for (Artifact item : toBeDeleted) {
			System.out.println(URLFORDELETE + item.getGroupId().replaceAll("\\.", "/") + "/" + item.getArtifactId() + "/" + item.getVersion());
		}
		System.out.println("rm to delete artifacts");
		for (Artifact item : toBeDeleted) {
			System.out.println("rm -rf  " + new File(item.getFileLocation()).getParent() + "/");
		}
		
	}

	/**
	 * Scan recursively a folder, identifying all pom files and adding artifact to
	 * collection
	 * 
	 * @param baseDir
	 * @throws Exception
	 */
	private void navigate(File baseDir) throws Exception {
		File[] files = baseDir.listFiles();

		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddhhmmss");
		for (File file : files) {
			if (file.isDirectory() && !file.getName().equals(".nexus")) {
				navigate(file);
			} else {
				System.out.println(file.getAbsolutePath());
				if (file.getName().endsWith(".pom")) {
					MavenXpp3Reader reader = new MavenXpp3Reader();
					Model model = reader.read(new FileReader(file));
					Artifact artifact = new Artifact(model.getArtifactId(), model.getGroupId(), model.getVersion(),
							dt.format(new Date(file.lastModified())), file.getAbsolutePath());
					if (model.getGroupId() == null) {
						System.out.println("WARNING: null groupId for " + artifact);
					} 
					else if (model.getArtifactId() == null) {
						System.out.println("WARNING: null artifactId for " + artifact);
					} 
					else if (model.getVersion() == null) {
						System.out.println("WARNING: null version for " + artifact);
					} else {

						artifactRepository.add(artifact);
					}
				}

			}
		}

	}

}
