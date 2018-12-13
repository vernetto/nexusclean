package org.pierre.nexusclean;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ArtifactRepository {
	List<Artifact> artifacts = new ArrayList<Artifact>();
	
	public void printAllArtifacts(PrintStream out) {
		printAllArtifacts(artifacts, out);
	}

	public void printAllArtifacts(List<Artifact> theArtifacts, PrintStream out) {
		for (Artifact artifact : theArtifacts) out.println(artifact);
	}

	public void add(Artifact artifact) {
		artifacts.add(artifact);
	}
	
	public List<Artifact> findArtifactsWithSameGA(Artifact artifactModel) {
		return findArtifactsWithSameGA(artifactModel, artifacts);
	}
	
	private List<Artifact> findArtifactsWithSameGA(Artifact artifactModel, List<Artifact> repo) {
		List<Artifact> result = new ArrayList<Artifact>();
		for (Artifact artifact : repo) {
			if (artifactModel.getArtifactId().equals(artifact.getArtifactId()) && artifactModel.getGroupId().equals(artifact.getGroupId())) {
				result.add(artifact);
			}
		}
		return result;
	}
	
	public List<Artifact> findArtifactsWithUniqueGA() {
		List<Artifact> result = new ArrayList<Artifact>();
		for (Artifact artifact : artifacts) {
			List<Artifact> set = findArtifactsWithSameGA(artifact, result);
			if (set.isEmpty()) {
				result.add(artifact);
			}
			
		}
		return result;
	}

	public List<Artifact> findArtifactsYoungerThanMinimumVersions(Artifact item, String dateAfter, int minimumVersions) {
		List<Artifact> itemsWithSameGA = findArtifactsWithSameGA(item);
		List<Artifact> survivors = new ArrayList<Artifact>();
		itemsWithSameGA.sort(new ArtifactsComparator());
		int order = 1;
		for (Artifact art : itemsWithSameGA) {
			if ( (art.getModifiedDate().compareTo(dateAfter) >= 0) || (order <= minimumVersions) ) {
				survivors.add(art);
			}
			order++;
		}
		
		return survivors;
	}
	
}
