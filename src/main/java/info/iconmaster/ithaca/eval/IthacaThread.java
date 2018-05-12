package info.iconmaster.ithaca.eval;

import java.util.Stack;

import info.iconmaster.ithaca.object.IthacaObject;

public class IthacaThread {
	public Stack<StackFrame> frames = new Stack<>();
	public IthacaObject recieved;
	public Scope globalScope;
	
	public IthacaThread(Scope scope) {
		this.globalScope = scope;
	}
	
	public IthacaThread(StackFrame init) {
		this.globalScope = init.scope;
		frames.push(init);
	}
	
	public IthacaThread(StackFrame init, Scope scope) {
		this.globalScope = scope;
		frames.push(init);
	}
	
	public IthacaThread(IthacaObject form, Scope scope) {
		this.globalScope = scope;
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
		return frames.isEmpty() ? globalScope : frames.peek().scope;
	}
	
	public IthacaThread clone() {
		IthacaThread result = new IthacaThread(globalScope);
		for (StackFrame frame : frames) {
			result.frames.push(frame.clone(result));
		}
		return result;
	}
	
	public void replaceWith(IthacaThread other) {
		this.frames.clear();
		this.globalScope = other.globalScope;
		
		for (StackFrame frame : other.frames) {
			frames.push(frame.clone(this));
		}
	}
}
