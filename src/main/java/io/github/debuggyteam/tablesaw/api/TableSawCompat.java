package io.github.debuggyteam.tablesaw.api;

/**
 * Use this class as a "tablesaw" entrypoint in your quilt.mod.json or fabric.mod.json, and you can dynamically register
 * recipes any time the recipe table is rebuilt.
 */
public interface TableSawCompat {
	/**
	 * Override this method to be notified when TableSaw is ready to accept recipes.
	 * @param api The TableSawAPI that can accept recipes and other integrations.
	 */
	public void run(TableSawAPI api);
}
