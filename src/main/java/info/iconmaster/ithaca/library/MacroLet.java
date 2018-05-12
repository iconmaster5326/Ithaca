package info.iconmaster.ithaca.library;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.EvalStackFrame;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.eval.StackFrame;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.object.IthacaVoid;
import info.iconmaster.ithaca.util.ListUtils;
import javafx.util.Pair;

public class MacroLet extends IthacaMacro {

	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> body = ListUtils.unwrapProperList(argList);
		List<IthacaObject> bindings = ListUtils.unwrapProperList(body.remove(0));
		Scope newScope = new Scope(thread.scope());
		
		IthacaSymbol symbol = null;
		List<Pair<IthacaSymbol, IthacaObject>> parsedBindings = new ArrayList<>();
		for (IthacaObject binding : bindings) {
			if (symbol == null) {
				if (!(binding instanceof IthacaSymbol)) {
					throw new IllegalArgumentException("let: Expected symbol in binding, got: "+binding);
				}
				symbol = (IthacaSymbol) binding;
			} else {
				newScope.defineBinding(symbol, IthacaVoid.VOID);
				parsedBindings.add(new Pair<IthacaSymbol, IthacaObject>(symbol, binding));
				symbol = null;
			}
		}
		if (symbol != null) {
			throw new IllegalArgumentException("let: Binding list too short");
		}
		
		thread.frames.push(new StackFrame(thread, newScope) {
			@Override
			public void step() {
				if (thread.recieved != null) {
					// a binding value has finished evaluation, set the binding
					Pair<IthacaSymbol, IthacaObject> binding = parsedBindings.remove(0);
					newScope.setBinding(binding.getKey(), thread.recieved);
					thread.recieved = null;
				}
				
				if (parsedBindings.isEmpty()) {
					// run the body
					thread.frames.pop();
					
					thread.frames.push(new StackFrame(thread, newScope) {
						List<IthacaObject> toEval = new ArrayList<>(body);
						
						@Override
						public void step() {
							if (toEval.isEmpty()) {
								// let thread.recieved pass through to our caller
								thread.frames.pop();
							} else {
								// discard thread.recieved and run the next expression
								thread.recieved = null;
								IthacaObject next = toEval.remove(0);
								thread.frames.push(new EvalStackFrame(thread, newScope, next));
							}
						}
					});
				} else {
					// run the next binding value
					Pair<IthacaSymbol, IthacaObject> binding = parsedBindings.get(0);
					thread.frames.push(new EvalStackFrame(thread, newScope, binding.getValue()));
				}
			}
		});
		
		return null;
	}
}
