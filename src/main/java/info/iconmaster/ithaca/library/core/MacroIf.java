package info.iconmaster.ithaca.library.core;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.StackFrame;
import info.iconmaster.ithaca.object.IthacaMacro;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.util.ListUtils;

public class MacroIf extends IthacaMacro {
	private class Frame extends StackFrame {
		IthacaObject ifT, ifF;
		
		public Frame(IthacaThread thread, IthacaObject ifT, IthacaObject ifF) {
			super(thread);
			this.ifF = ifF;
			this.ifT = ifT;
		}
		
		@Override
		public void step() {
			boolean truthy = thread.recieved.isTruthy();
			thread.recieved = null;
			
			thread.frames.pop();
			thread.eval(truthy ? ifT : ifF);
		}
		
		@Override
		public StackFrame clone(IthacaThread thread) {
			return new Frame(thread, ifT, ifF);
		}
	}
	
	@Override
	public IthacaObject callMacro(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		
		thread.frames.push(new Frame(thread, args.get(1), args.get(2)));
		thread.eval(args.get(0));
		return null;
	}
}
