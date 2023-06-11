package io.github.debuggyteam.tablesaw.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Represents a one-input, one-output recipe for the Tablesaw.
 */
public class TableSawRecipe {
	/**
	 * The input Item
	 * @see #quantity
	 */
	protected Item input;
	
	/**
	 * The number of input items required per-craft
	 */
	protected int quantity;
	
	/**
	 * The result of crafting
	 */
	protected ItemStack result;
	
	/**
	 * Creates a new TableSawRecipe
	 * @param input The input item
	 * @param quantity How many of the input item is required per craft
	 * @param result The result of crafting.
	 */
	public TableSawRecipe(Item input, int quantity, ItemStack result) {
		this.input = input;
		this.quantity = quantity;
		this.result = result;
	}
	
	/**
	 * Gets the input Item that this Recipe uses / consumes
	 * @return this Recipe's input Item
	 */
	public Item getInput() {
		return input;
	}
	
	/**
	 * Gets the number of input items consumed by this Recipe
	 * @return the number of input items that will be consumed by this Recipe
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Returns an ItemStack representing the output result of this Recipe. <em>Do not modify this ItemStack!</em> Make a
	 * copy instead!
	 * @return The result of crafting this Recipe
	 */
	public ItemStack getResult() {
		return result;
	}
}
