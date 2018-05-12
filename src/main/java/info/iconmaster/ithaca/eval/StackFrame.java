package info.iconmaster.ithaca.eval;

public abstract class StackFrame {
	public IthacaThread thread;
	public Scope scope;
	
	public StackFrame(IthacaThread thread) {
		super();
		this.thread = thread;
		if (thread.frames.isEmpty()) {
			scope = thread.globalScope;
		} else {
			scope = thread.frames.peek().scope;
		}
	}
	
	public StackFrame(IthacaThread thread, Scope scope) {
		super();
		this.thread = thread;
		this.scope = scope;
	}
	
	public abstract StackFrame clone(IthacaThread thread);
	public abstract void step();
}
