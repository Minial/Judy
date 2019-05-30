package pl.wroc.pwr.judy.work;

public class MutationSummary {
	private int killed;
	private int alive;
	private double objectives[];

	public MutationSummary() {
	}

	public MutationSummary(int killed, int alive) {
		this.killed = killed;
		this.alive = alive;
	}

	public int getKilled() {
		return killed;
	}

	public void setKilled(int killed) {
		this.killed = killed;
	}

	public int getAlive() {
		return alive;
	}

	public void setAlive(int alive) {
		this.alive = alive;
	}

	public int getAll() {
		return killed + alive;
	}

	public double[] getObjectiveValues() {
		return objectives;
	}

	public void setObjectiveValues(double... objectives) {
		this.objectives = objectives;
	}

}
