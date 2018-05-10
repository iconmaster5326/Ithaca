package info.iconmaster.ithaca.eval;

public abstract class StackFrame {
	public IthacaThread thread;
	
	public StackFrame(IthacaThread thread) {
		super();
		this.thread = thread;
	}
	
	public abstract void step();
}
