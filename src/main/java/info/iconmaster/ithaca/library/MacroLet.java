package info.iconmaster.ithaca.library;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.EvalBodyStackFrame;
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
	private class BindingsFrame extends StackFrame {
		List<Pair<IthacaSymbol, IthacaObject>> parsedBindings = new ArrayList<>();
		List<IthacaObject> body;

		private BindingsFrame(IthacaThread thread, Scope scope, List<Pair<IthacaSymbol, IthacaObject>> parsedBindings, List<IthacaObject> body) {
			super(thread, scope);
			this.parsedBindings.addAll(parsedBindings);
			this.body = body;
		}

		@Override
		public void step() {
			if (thread.recieved != null) {
				// a binding value has finished evaluation, set the binding
				Pair<IthacaSymbol, IthacaObject> binding = parsedBindings.remove(0);
				scope.setBinding(binding.getKey(), thread.recieved);
				thread.recieved = null;
			}
			
			if (parsedBindings.isEmpty()) {
				// run the body
				thread.frames.pop();
				
				thread.frames.push(new EvalBodyStackFrame(thread, scope, body));
			} else {
				// run the next binding value
				Pair<IthacaSymbol, IthacaObject> binding = parsedBindings.get(0);
				thread.frames.push(new EvalStackFrame(thread, scope, binding.getValue()));
			}
		}

		@Override
		public StackFrame clone(IthacaThread thread) {
			return new BindingsFrame(thread, scope, parsedBindings, body);
		}
	}

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
		
		thread.frames.push(new BindingsFrame(thread, newScope, parsedBindings, body));
		
		return null;
	}
}
