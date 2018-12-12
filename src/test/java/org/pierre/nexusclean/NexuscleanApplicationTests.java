package org.pierre.nexusclean;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NexuscleanApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testUnique() {
		ArtifactRepository artifactRepository = new ArtifactRepository();
		artifactRepository.add(new Artifact("artifactId1", "groupId1", "version1", "20181209"));
		artifactRepository.add(new Artifact("artifactId1", "groupId1", "version2", "20181208"));
		artifactRepository.add(new Artifact("artifactId1", "groupId2", "version2", "20181207"));
		artifactRepository.add(new Artifact("artifactId2", "groupId1", "version2", "20181207"));
		
		List<Artifact> unique = artifactRepository.findArtifactsWithUniqueGA();
		System.out.println("\nall unique:");
		artifactRepository.printAllArtifacts(unique, System.out);
		for (Artifact af : unique) {
			List<Artifact> item = artifactRepository.findArtifactsWithSameGA(af);
			System.out.println("\nartifacts with GA " + af.getGroupId() + " " + af.getArtifactId());
			artifactRepository.printAllArtifacts(item, System.out);
		}
		
	}
}

