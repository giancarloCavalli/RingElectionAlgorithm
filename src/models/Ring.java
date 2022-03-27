/*
 * Authors: Eduarda Engels, Giancarlo Cavalli & Gustavo Soares
 */

package models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Ring {

	private List<Process> processes = Collections.synchronizedList(new LinkedList<Process>());
	private boolean election;
	private Process coordinator;
	private long processId = 1;

	private final int CREATE_INTERVAL_SECONDS = 15;
	private final int DISABLE_INTERVAL_SECONDS = 40;
	private final int DISABLE_COORDINATOR_INTERVAL_SECONDS = 50;
	private final int SEND_REQUEST_INTERVAL_SECONDS = 12;

	public Ring() {
	}

	public Ring(List<Process> processes, Process coordinator) {
		this.setProcesses(processes);
		this.setCoordinator(coordinator);
	}

	public synchronized void createProcess() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					Process process;

					if (getProcesses().isEmpty()) {
						process = new Process(processId);
						setCoordinator(process);
					} else {
						process = new Process(processId);
					}

					getProcesses().add(process);
					processId++;
					System.out.println("Process " + process.getId() + " created.");

					try {
						Thread.sleep(CREATE_INTERVAL_SECONDS * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}) {
		}.start();
	}

	public synchronized void disableProcess() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(DISABLE_INTERVAL_SECONDS * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Process process = getRandomProcess();

					getProcesses().remove(process);

					if (getCoordinator() != null && isCoordinator(process)) {
						setCoordinator(null);
					}

					System.out.println("Process " + process.getId() + " disabled.");
				}
			}
		}) {
		}.start();
	}

	public synchronized void disableCoordinator() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(DISABLE_COORDINATOR_INTERVAL_SECONDS * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (getCoordinator() != null) {
						long id = getCoordinator().getId();
						getProcesses().remove(getCoordinator());
						setCoordinator(null);

						System.out.println("Coordinator disabled (id " + id + ").");
					}
				}
			}
		}) {
		}.start();
	}

	public synchronized void sendRequest() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(SEND_REQUEST_INTERVAL_SECONDS * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Process process = getCommonProcess();

					boolean successful = process.sendRequisition(getCoordinator());

					if (!successful) {
						startElection(process.getId());
					}
				}
			}
		}) {
		}.start();
	}

	public synchronized void startElection(long processId) {
		System.out.println("Election started by process " + processId + ".");

		new Thread(new Runnable() {

			@Override
			public void run() {

				Process coordinator = getProcessById(processId);

				for (Process process : getProcesses()) {
					coordinator = Process.higherOf(coordinator, process);
				}

				setCoordinator(coordinator);
				announceNewCoordinator(coordinator);
			}
		}) {
		}.start();

	}

	public void announceNewCoordinator(Process newCoordinator) {
		System.out.println("Process id " + newCoordinator.getId() + " is the new coordinator.");
	}

	private boolean isCoordinator(Process p) {
		if (getCoordinator().equals(p)) {
			return true;
		}

		return false;
	}

	public Process getProcessById(long processId) {
		for (Process process : getProcesses()) {
			if (process.getId() == processId) {
				return process;
			}
		}

		return null;
	}

	private Process getCommonProcess() {
		Process process;

		do {
			process = getRandomProcess();

		} while (process.equals(getCoordinator()));

		return process;
	}

	private Process getRandomProcess() {
		Random rd = new Random();
		int random = rd.nextInt(getProcesses().size());
		Process process = getProcesses().get(random);

		return process;
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}

	public boolean isElection() {
		return election;
	}

	public void setElection(boolean election) {
		this.election = election;
	}

	public Process getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(Process coordinator) {
		this.coordinator = coordinator;
	}

}
