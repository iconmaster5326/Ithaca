package info.iconmaster.ithaca.object;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.EvalStackFrame;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.eval.StackFrame;

public class IthacaUserFunc extends IthacaFunc {
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

	public ParameterList params;
	public List<IthacaObject> body;
	public Scope scope;
	
	public IthacaUserFunc(ParameterList params, List<IthacaObject> body, Scope scope) {
		super();
		this.params = params;
		this.body = body;
		this.scope = scope;
	}
	
	@Override
	public IthacaObject callFunc(IthacaThread thread, IthacaObject argList) {
		if (body.isEmpty()) {
			return IthacaVoid.VOID;
		} else {
			Scope newScope = new Scope(scope);
			newScope.defineBindings(params.parseArgList(argList));
			thread.frames.push(new Frame(thread, newScope, body));
			return null;
		}
	}
}
