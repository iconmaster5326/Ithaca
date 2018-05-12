package info.iconmaster.ithaca.object;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.eval.EvalStackFrame;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.eval.StackFrame;

public class IthacaUserMacro extends IthacaMacro {
	private class Frame extends StackFrame {
		List<IthacaObject> toEval = new ArrayList<>();

		private Frame(IthacaThread thread, Scope scope, List<IthacaObject> body) {
			super(thread, scope);
			toEval.addAll(body);
		}

		@Override
		public void step() {
			if (toEval.isEmpty()) {
				// push the resulting form
				thread.frames.pop();
				thread.frames.push(new EvalStackFrame(thread, thread.recieved));
				thread.recieved = null;
			} else {
				// discard thread.recieved and run the next expression
				thread.recieved = null;
				IthacaObject next = toEval.remove(0);
				thread.frames.push(new EvalStackFrame(thread, scope, next));
			}
		}

		@Override
		public StackFrame clone(IthacaThread thread) {
			return new Frame(thread, scope, body);
		}
	}

	public ParameterList params;
	public List<IthacaObject> body;
	public Scope scope;
	
	public IthacaUserMacro(ParameterList params, List<IthacaObject> body, Scope scope) {
		super();
		this.params = params;
		this.body = body;
		this.scope = scope;
	}
	
	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		if (body.isEmpty()) {
			thread.recieved = IthacaVoid.VOID;
			return null;
		} else {
			Scope newScope = new Scope(scope);
			newScope.defineBindings(params.parseArgList(argList));
			
			thread.frames.push(new Frame(thread, newScope, body));
			
			return null;
		}
	}
}
