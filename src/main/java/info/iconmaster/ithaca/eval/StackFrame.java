package info.iconmaster.ithaca.eval;

public abstract class StackFrame {
	public IthacaThread thread;
	public FuncEnv fenv;
	
	public StackFrame(IthacaThread thread) {
		super();
		this.thread = thread;
		if (thread.frames.isEmpty()) {
			fenv = thread.globalFenv;
		} else {
			fenv = thread.frames.peek().fenv;
		}
	}
	
	public StackFrame(IthacaThread thread, FuncEnv fenv) {
		super();
		this.thread = thread;
		this.fenv = fenv;
	}
	
	public abstract void step();
}
