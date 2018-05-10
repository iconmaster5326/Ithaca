package info.iconmaster.ithaca.eval;

import java.util.Stack;

import info.iconmaster.ithaca.object.IthacaObject;

public class IthacaThread {
	public Stack<StackFrame> frames = new Stack<>();
	public IthacaObject recieved;
	public Scope globalFenv;
	
	public IthacaThread(Scope scope) {
		this.globalFenv = scope;
	}
	
	public IthacaThread(StackFrame init) {
		this.globalFenv = init.scope;
		frames.push(init);
	}
	
	public IthacaThread(StackFrame init, Scope scope) {
		this.globalFenv = scope;
		frames.push(init);
	}
	
	public IthacaThread(IthacaObject form, Scope scope) {
		this.globalFenv = scope;
		frames.push(new EvalStackFrame(this, scope, form));
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
	
	public Scope scope() {
		return frames.isEmpty() ? globalFenv : frames.peek().scope;
	}
}
