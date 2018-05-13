package info.iconmaster.ithaca.library.core;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaUserMacro;
import info.iconmaster.ithaca.object.ParameterList;
import info.iconmaster.ithaca.util.ListUtils;

public class MacroMacro extends IthacaMacro {
	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		ParameterList params = new ParameterList(args.remove(0));
		thread.recieved = new IthacaUserMacro(params, args, thread.scope());
		return null;
	}
}
