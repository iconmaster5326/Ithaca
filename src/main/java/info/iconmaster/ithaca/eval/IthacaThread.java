package info.iconmaster.ithaca.eval;

import java.util.Stack;

import info.iconmaster.ithaca.object.IthacaObject;

public class IthacaThread {
	public Stack<StackFrame> frames = new Stack<>();
	public IthacaObject recieved;
	public FuncEnv globalFenv;
	
	public IthacaThread(FuncEnv fenv) {
		this.globalFenv = fenv;
	}
	
	public IthacaThread(StackFrame init) {
		this.globalFenv = init.fenv;
		frames.push(init);
	}
	
	public IthacaThread(StackFrame init, FuncEnv fenv) {
		this.globalFenv = fenv;
		frames.push(init);
	}
	
	public IthacaThread(IthacaObject form, FuncEnv fenv) {
		this.globalFenv = fenv;
		frames.push(new EvalStackFrame(this, fenv, form));
	}
	
	public void step() {
		if (done()) throw new RuntimeException("Attempted to run a stopped Ithaca thread");
		frames.peek().step();
	}
	
	public boolean done() {
		return frames.isEmpty();
	}
	
	public void eval(IthacaObject form) {
		frames.push(new EvalStackFrame(this, form));
	}
	
	public IthacaObject run() {
		if (done()) throw new RuntimeException("Attempted to run a stopped Ithaca thread");
		
		while (!done()) {
			step();
		}
		
		return recieved;
	}
	
	public FuncEnv fenv() {
		return frames.isEmpty() ? globalFenv : frames.peek().fenv;
	}
}
