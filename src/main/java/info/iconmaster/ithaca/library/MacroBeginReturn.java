package info.iconmaster.ithaca.library;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.EvalStackFrame;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.eval.StackFrame;
import info.iconmaster.ithaca.object.IthacaFunc;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.util.ListUtils;

public class MacroBeginReturn extends IthacaMacro {
	private class Frame extends StackFrame {
		List<IthacaObject> toEval = new ArrayList<>();

		private Frame(IthacaThread thread, Scope scope, List<IthacaObject> body) {
			super(thread, scope);
			toEval.addAll(body);
		}

		@Override
		public void step() {
			if (toEval.isEmpty()) {
				// let thread.recieved pass through to our caller
				thread.frames.pop();
			} else {
				// discard thread.recieved and run the next expression
				thread.recieved = null;
				IthacaObject next = toEval.remove(0);
				thread.frames.push(new EvalStackFrame(thread, scope, next));
			}
		}

		@Override
		public StackFrame clone(IthacaThread thread) {
			return new Frame(thread, scope, toEval);
		}
	}

	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> body = ListUtils.unwrapProperList(argList);
		if (!(body.get(0) instanceof IthacaSymbol)) throw new IllegalArgumentException("begin/return: expected argument 1 to be a symbol, got: "+body.get(0));
		IthacaSymbol returnFuncSymbol = (IthacaSymbol) body.remove(0);
		
		// define the return function
		IthacaThread threadState = thread.clone();
		Scope newScope = new Scope(thread.scope());
		newScope.defineBinding(returnFuncSymbol, new IthacaFunc() {
			@Override
			public IthacaObject callFunc(IthacaThread thread, IthacaObject argList) {
				// replace the thread state with the saved one. thread.recieved is set to the argument.
				thread.replaceWith(threadState);
				return ListUtils.unwrapProperList(argList).get(0);
			}
		});
		
		// run the body
		thread.frames.push(new Frame(thread, newScope, body));
		
		return null;
	}
}
