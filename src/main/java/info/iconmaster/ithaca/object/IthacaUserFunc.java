package info.iconmaster.ithaca.object;

import java.util.List;

import info.iconmaster.ithaca.eval.EvalBodyStackFrame;
import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;

public class IthacaUserFunc extends IthacaFunc {
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
		Scope newScope = new Scope(scope);
		newScope.defineBindings(params.parseArgList(argList));
		thread.frames.push(new EvalBodyStackFrame(thread, newScope, body));
		return null;
	}
}
