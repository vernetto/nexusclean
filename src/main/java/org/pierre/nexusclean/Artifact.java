package org.pierre.nexusclean;

public class Artifact {
	String artifactId;
	String groupId;
	String version;
	String modifiedDate;
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	@Override
	public String toString() {
		return "Artifact [groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version
				+ ", modifiedDate=" + modifiedDate + "]";
	}
	public Artifact(String artifactId, String groupId, String version, String modifiedDate) {
		super();
		this.artifactId = artifactId;
		this.groupId = groupId;
		this.version = version;
		this.modifiedDate = modifiedDate;
	}
	
}
