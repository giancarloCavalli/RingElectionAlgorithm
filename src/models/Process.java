/*
 * Authors: Eduarda Engels, Giancarlo Cavalli & Gustavo Soares
 */

package models;

import java.util.Objects;

public class Process implements Comparable<Process> {

	private long id;

	public Process(long id) {
		this.setId(id);
	}

	public boolean sendRequisition(Process coordinator) {
		try {
			System.out.println("Request sent by process " + this.getId() + ".");
			coordinator.getRequisition(this.getId());

		} catch (NullPointerException npe) {
			System.out.println("Coordinator timed out.");
			return false;
		}
		return true;
	}

	public void getRequisition(long id) {
		System.out.println("Request from process id " + id + " received by process id " + this.getId() + ".");
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Process other = (Process) obj;
		return id == other.id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Process p) {
		if (this.id > p.id) {
			return 1;
		}

		if (this.id < p.id) {
			return -1;
		}

		return 0;
	}

	public static Process higherOf(Process process1, Process process2) {
		if (process1.compareTo(process2) == 1) {
			return process1;
		}

		if (process1.compareTo(process2) == -1) {
			return process2;
		}

		return process1;
	}

}
