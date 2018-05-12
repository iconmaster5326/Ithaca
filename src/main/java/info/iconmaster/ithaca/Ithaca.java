package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaRuntime;
import info.iconmaster.ithaca.library.GlobalScope;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaRuntime runtime = new IthacaRuntime(new GlobalScope());
		runtime.spawn("(print #t) (print #f) (print #v)");
		runtime.run();
	}
}
