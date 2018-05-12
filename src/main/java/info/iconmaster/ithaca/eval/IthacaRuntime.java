package info.iconmaster.ithaca.eval;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.iconmaster.ithaca.object.IthacaObject;

public class IthacaRuntime {
	public List<IthacaThread> threads = new ArrayList<>();
	public Scope globalScope;
	
	private Random scheduler = new Random();
	
	public IthacaRuntime(Scope globalScope) {
		this.globalScope = globalScope;
	}
	
	public void step() {
		if (done()) throw new RuntimeException("Attempted to run a stopped Ithaca runtime");
		
		IthacaThread thread = threads.get(scheduler.nextInt(threads.size()));
		thread.step();
		if (thread.done()) threads.remove(thread);
	}
	
	public boolean done() {
		return threads.isEmpty();
	}
	
	public void run() {
		if (done()) throw new RuntimeException("Attempted to run a stopped Ithaca runtime");
		
		while (!done()) {
			step();
		}
	}
	
	public void spawn(IthacaThread thread) {
		threads.add(thread);
	}
	
	public void spawn(IthacaObject form) {
		threads.add(new IthacaThread(this, form));
	}
	public void spawn(IthacaObject form, Scope scope) {
		threads.add(new IthacaThread(this, form, scope));
	}
	
	public void spawn(String form) throws IOException {
		threads.add(new IthacaThread(this, form));
	}
	public void spawn(String form, Scope scope) throws IOException {
		threads.add(new IthacaThread(this, form, scope));
	}
	
	public void spawn(File form) throws IOException {
		threads.add(new IthacaThread(this, form));
	}
	public void spawn(File form, Scope scope) throws IOException {
		threads.add(new IthacaThread(this, form, scope));
	}
}
