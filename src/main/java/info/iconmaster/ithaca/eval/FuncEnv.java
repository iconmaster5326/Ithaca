package info.iconmaster.ithaca.eval;

import java.util.Map;
import java.util.Set;

import info.iconmaster.ithaca.object.IthacaObject;

public class FuncEnv {
	public Set<FuncEnv> parents;
	public Map<String, IthacaObject> bindings;
}
