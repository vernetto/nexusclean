package org.pierre.nexusclean;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ArtifactRepository {
	List<Artifact> artifacts = new ArrayList<Artifact>();
	
	public void printAllArtifacts(PrintStream out) {
		for (Artifact artifact : artifacts) out.println(artifact);
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
				result.add(artifactModel);
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
	
}
