package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.library.GlobalScope;
import info.iconmaster.ithaca.parse.IthacaReader;
import info.iconmaster.ithaca.parse.TokenStream;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaThread thread = new IthacaThread(IthacaReader.read(new TokenStream("(begin (define ret (begin/return ret ret)) (print ret) (ret 5))")), new GlobalScope());
		System.out.println(thread.run());
	}
}
