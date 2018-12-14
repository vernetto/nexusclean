package org.pierre.nexusclean;

import java.util.Comparator;

import org.apache.maven.artifact.versioning.ComparableVersion;

public class ArtifactsComparator implements Comparator<Artifact> {

	@Override
	public int compare(Artifact o1, Artifact o2) {
		int compareTo = 0;
		if (o1.getGroupId() != null) {
			if (o2.getGroupId() != null) {
				int groupCompare = (o1.getGroupId().compareTo(o2.getGroupId()));
				if (groupCompare != 0) {
					return groupCompare;
				}
			}
			else {
				return -1;
			}
		}
		else {
			return 1;
		}
		
		if (o1.getArtifactId() != null) {
			if (o2.getArtifactId() != null) {
				int artifactCompare = (o1.getArtifactId().compareTo(o2.getArtifactId()));
				if (artifactCompare != 0) {
					return artifactCompare;
				}
			}
			else {
				return -1;
			}
		}
		else {
			return 1;
		}
		
		
		try {
			ComparableVersion cv1 = new ComparableVersion(o1.getVersion());
			ComparableVersion cv2 = new ComparableVersion(o2.getVersion());
			compareTo = cv2.compareTo(cv1);
		} catch (NullPointerException e) {
			System.out.println("ERROR NPE with versions " + o1.getVersion() + " " + o2.getVersion());
		}
		return compareTo;

	}

}
