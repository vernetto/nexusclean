package org.pierre.nexusclean;

import java.util.Comparator;

import org.apache.maven.artifact.versioning.ComparableVersion;

public class ArtifactsComparator implements Comparator<Artifact> {

	@Override
	public int compare(Artifact o1, Artifact o2) {
		ComparableVersion cv1 = new ComparableVersion(o1.getVersion());
		ComparableVersion cv2 = new ComparableVersion(o2.getVersion());
		int compareTo = cv1.compareTo(cv2);
		return compareTo;
	}

}
