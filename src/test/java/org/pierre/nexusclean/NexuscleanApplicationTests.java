package org.pierre.nexusclean;

import java.util.List;

import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NexuscleanApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testUnique() {
		ArtifactRepository artifactRepository = createTestRepo();
		
		List<Artifact> unique = artifactRepository.findArtifactsWithUniqueGA();
		System.out.println("\nall unique:");
		artifactRepository.printAllArtifacts(unique, System.out);
		for (Artifact af : unique) {
			List<Artifact> item = artifactRepository.findArtifactsWithSameGA(af);
			System.out.println("\nartifacts with GA " + af.getGroupId() + " " + af.getArtifactId());
			artifactRepository.printAllArtifacts(item, System.out);
		}
		List<Artifact> survivors = artifactRepository.findArtifactsYoungerThanMinimumVersions(new Artifact("artifactId1", "groupId1"), "20171209", 1);
		System.out.println("\nsurvivors:");
		artifactRepository.printAllArtifacts(survivors, System.out);
		
	}
	
	
	@Test
	public void testSort() {
		ArtifactRepository artifactRepository = createTestRepo();
		artifactRepository.sort();
		System.out.println("\nsort");
		artifactRepository.printAllArtifacts(System.out);
	}
		
	@Test
	public void testCompareto() {
		ArtifactRepository artifactRepository = createTestRepo();
		artifactRepository.sort();
		System.out.println("\ncompareto");
		System.out.println("Group1".compareTo("Group1"));
		System.out.println("Group2".compareTo("Group1"));
		System.out.println("Group1".compareTo("Group2"));
		System.out.println("Group2".compareTo("Group2"));
	}
			
	
	

	private ArtifactRepository createTestRepo() {
		ArtifactRepository artifactRepository = new ArtifactRepository();
		artifactRepository.add(new Artifact("artifactId1", "groupId1", "version1", "20181209"));
		artifactRepository.add(new Artifact("artifactId1", "groupId1", "version2", "20181208"));
		artifactRepository.add(new Artifact("artifactId1", "groupId2", "version2", "20181207"));
		artifactRepository.add(new Artifact("artifactId2", "groupId1", "version2", "20181206"));
		return artifactRepository;
	}
}

