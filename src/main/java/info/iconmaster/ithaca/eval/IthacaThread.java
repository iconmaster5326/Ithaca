package info.iconmaster.ithaca.eval;

import java.io.IOException;
import java.util.Stack;

import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.parse.IthacaReader;
import info.iconmaster.ithaca.parse.TokenStream;

public class IthacaThread {
	public Stack<StackFrame> frames = new Stack<>();
	public IthacaObject recieved;
	public Scope threadScope;
	public IthacaRuntime runtime;
	
	public IthacaThread(IthacaRuntime runtime) {
		this.runtime = runtime;
		this.threadScope = runtime.globalScope;
	}
	public IthacaThread(IthacaRuntime runtime, Scope scope) {
		this.runtime = runtime;
		this.threadScope = scope;
	}
	
	public IthacaThread(IthacaRuntime runtime, IthacaObject form) {
		this(runtime);
		eval(form);
	}
	public IthacaThread(IthacaRuntime runtime, IthacaObject form, Scope scope) {
		this(runtime, scope);
		eval(form, scope);
	}
	
	public IthacaThread(IthacaRuntime runtime, String s) throws IOException {
		this(runtime);
		eval(s);
	}
	public IthacaThread(IthacaRuntime runtime, String s, Scope scope) throws IOException {
		this(runtime, scope);
		eval(s, scope);
	}
	
	public void step() {
		if (done()) throw new RuntimeException("Attempted to run a stopped Ithaca thread");
		frames.peek().step();
	}
	
	public boolean done() {
		return frames.isEmpty();
	}
	
	public void eval(IthacaObject form) {
		eval(form, scope());
	}
	public void eval(IthacaObject form, Scope scope) {
		frames.push(new EvalStackFrame(this, scope, form));
	}
	
	public void eval(String s) throws IOException {
		eval(s, scope());
	}
	public void eval(String s, Scope scope) throws IOException {
		frames.push(new EvalBodyStackFrame(this, scope, new IthacaReader(new TokenStream(s), this, scope).readAll()));
	}
	
	public IthacaObject run() {
		if (done()) throw new RuntimeException("Attempted to run a stopped Ithaca thread");
		
		while (!done()) {
			step();
		}
		
		return recieved;
	}
	
	public Scope scope() {
		return frames.isEmpty() ? threadScope : frames.peek().scope;
	}
	
	public IthacaThread clone() {
		IthacaThread result = new IthacaThread(runtime, threadScope);
		for (StackFrame frame : frames) {
			result.frames.push(frame.clone(result));
		}
		return result;
	}
	
	public void replaceWith(IthacaThread other) {
		this.frames.clear();
		this.threadScope = other.threadScope;
		
		for (StackFrame frame : other.frames) {
			frames.push(frame.clone(this));
		}
	}
}
