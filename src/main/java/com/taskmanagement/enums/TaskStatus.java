package com.taskmanagement.enums;

public enum TaskStatus {
	PENDING("Pending"), IN_PROGRESS("In Progress"), COMPLETED("Completed");

	private final String displayName;

	TaskStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}