package models;

import java.util.Objects;

public class Process {

	private long id;
	private boolean coordinator;

	public Process(long id, boolean coordinator) {
		this.setId(id);
		this.setCoordinator(coordinator);
	}

	public boolean sendRequisition(Process coordinator) {
		try {
			System.out.println("Request sent by process " + this.getId());
			coordinator.getRequisition(this.getId());

		} catch (NullPointerException npe) {
			System.out.println("Coordinator timed out");
			return false;
		}
		return true;
	}

	public void getRequisition(long id) {
		System.out.println("Request from process id " + id + " received by process id " + this.getId());
	}

	public boolean isCoordinator() {
		return coordinator;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Process other = (Process) obj;
		return id == other.id;
	}

	public void setCoordinator(boolean coordinator) {
		this.coordinator = coordinator;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
