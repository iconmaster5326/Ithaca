package info.iconmaster.ithaca.library;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.StackFrame;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.object.IthacaVoid;
import info.iconmaster.ithaca.util.ListUtils;

public class MacroDefine extends IthacaMacro {
	private class Frame extends StackFrame {
		IthacaSymbol sym;
		
		private Frame(IthacaThread thread, IthacaSymbol sym) {
			super(thread);
			this.sym = sym;
		}

		@Override
		public void step() {
			thread.scope().defineBinding(sym, thread.recieved);
			thread.recieved = IthacaVoid.VOID;
			thread.frames.pop();
		}

		@Override
		public StackFrame clone(IthacaThread thread) {
			return new Frame(thread, sym);
		}
	}

	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		thread.frames.push(new Frame(thread, (IthacaSymbol) args.get(0)));
		thread.eval(args.get(1));
		return null;
	}
}
