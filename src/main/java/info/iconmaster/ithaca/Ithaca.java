package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaRuntime;
import info.iconmaster.ithaca.library.GlobalScope;
import info.iconmaster.ithaca.parse.IthacaReader;
import info.iconmaster.ithaca.parse.TokenStream;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaRuntime runtime = new IthacaRuntime(new GlobalScope());
		runtime.spawn(IthacaReader.read(new TokenStream("(begin (define ret (begin/return ret ret)) (print ret) (ret 5))")));
		runtime.run();
	}
}
