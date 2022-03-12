package models;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Ring {

	private List<Process> processes = new LinkedList<>();
	private boolean election;
	private Process coordinator;
	private long processId = 1;

	public Ring() {
	}

	public Ring(List<Process> processes, Process coordinator) {
		this.setProcesses(processes);
		this.setCoordinator(coordinator);
	}

	public void createProcess() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					Process p;

					if (processes.isEmpty()) {
						p = new Process(processId, true);
						setCoordinator(p);
					} else {
						p = new Process(processId, false);
					}

					processes.add(p);
					processId++;
					System.out.println("Process " + p.getId() + " created.");

					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}) {
		}.start();
	}

	public void disableProcess() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					Process p = getCommonProcess();

					processes.remove(p);
					System.out.println("Process " + p.getId() + " disabled.");

					try {
						Thread.sleep(80000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}) {
		}.start();
	}

	public void disableCoordinator() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					if (getCoordinator() != null) {
						long id = getCoordinator().getId();
						processes.remove(getCoordinator());
						setCoordinator(null);

						System.out.println("Coordinator disabled. (id " + id);
					}
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}) {
		}.start();
	}

	public void sendRequest() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					Process p = getCommonProcess();

					boolean successful = p.sendRequisition(getCoordinator());

					if (!successful) {
						startElection(p.getId());
					}

					try {
						Thread.sleep(25000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}) {
		}.start();
	}

	public void startElection(long processId) {
		System.out.println("Election started by process id " + processId);

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = (int) processId; i < processes.size(); i++) {
					/*
					 * verificar como percorrer
					 */
				}
			}
		}) {
		}.start();

	}

	public void announceNewCoordinator(Process newCoordinator) {

	}

	private Process getCommonProcess() {
		Random rd = new Random();
		int random;
		Process p;

		do {
			random = rd.nextInt(processes.size() - 1);
			p = processes.get(random);

		} while (p.equals(getCoordinator()));

		return p;
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
