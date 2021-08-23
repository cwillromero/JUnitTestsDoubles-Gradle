/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modifications 
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to 
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

/**
 * Unit tests for CoffeeMaker class.
 * 
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {
	
	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		coffeeMaker = new CoffeeMaker();
		
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
	}
	
	/* ----------------------------- Add Recipe ----------------------------- */
	/**
	 * Given a coffee maker with no recipes
	 * When we add 3 recipes
	 * Then we do not get an exception trying to so 
	 * 
	 * @throws InventoryException  if try to add more than 3 recipes.
	 */
	@Test
	public void testAddRecipes() throws Exception {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
	}
	/**
	 * Given a coffee maker with no recipes
	 * When we add 4 or more recipes
	 * Then get an exception trying to so 
	**/
	@Test
	public void testAddMoreThan3Recipes(){
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
		boolean added;
		try {
			added = coffeeMaker.addRecipe(recipe4);
		}catch(Exception e) {
			added= false;
		}
		assertEquals(false, added);

	}
	
	
	/* ----------------------------- Add Inventory ----------------------------- */
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","1","9");
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}
	
	/* ----------------------------- Make Coffee ----------------------------- */
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than 
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying less than 
	 * 		the coffee costs
	 * Then we get a the same amount we put.
	 */
	@Test
	public void testMakeCoffeeLessMoney() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(1, coffeeMaker.makeCoffee(0, 1));
	}
	
	/**
	 * Given a coffee maker with one invalid recipe less than 0
	 * When we make coffee, selecting the recipe and paying
	 * Then we get an exception.
	 */
	@Test
	public void testMakeCoffeeNegativeRecipe() {
		int change;
		try {
			change = coffeeMaker.makeCoffee(-1, 56);
		}catch(Exception e) {
			change = 0;
		}
		assertEquals(56, change);
	}
	/**
	 * Given a coffee maker with one invalid recipe less than 0
	 * When we make coffee, selecting the recipe and paying
	 * Then we get an exception.
	 */
	@Test
	public void testMakeCoffeeOutOfIndexRecipe() {
		int change;
		try {
			change = coffeeMaker.makeCoffee(100, 56);
		}catch(Exception e) {
			change = 0;
		}
		assertEquals(56, change);
	}
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying with non-number value
	 * Then we get a random integer as change.
	 */
	@Test
	public void testMakeCoffeeInvalidPayment() {
		assertEquals(115, coffeeMaker.makeCoffee(0, 's'));
	}

	/* ----------------------------- Check Inventory ----------------------------- */
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying with a valid value
	 * And  we check the inventory, it should be less than 15 units per ingredient 
	 * Then we get an exception.
	 */
	@Test
	public void testCheckInventory() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.makeCoffee(0, 75);
		assertEquals("Coffee: 12\n"
				+ "Milk: 14\n"
				+ "Sugar: 14\n"
				+ "Chocolate: 15\n", 
				coffeeMaker.checkInventory());
	}
	
	/* ----------------------------- Edit Recipe ----------------------------- */
	
	/**
	 * Given a valid recipe
	 * When we edit it with a valid recipe
	 * Then we get the name of recipe we edited.
	 */
	@Test
	public void testEditRecipe() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals("Coffee", coffeeMaker.editRecipe(0, recipe4));
	}
	/**
	 * Given an invalid recipe
	 * When we edit it with a valid recipe
	 * Then we get a null value.
	 */
	@Test
	public void testEditNegativeRecipe() throws Exception{
		coffeeMaker.addRecipe(recipe1);
		assertEquals(null, coffeeMaker.editRecipe(-1, recipe2));
	}
	
	/**
	 * Given an invalid recipe
	 * When we edit it with a valid recipe
	 * Then we get a null value.
	 */
	@Test
	public void testEditOutOfIndexRecipe() throws Exception{
		coffeeMaker.addRecipe(recipe1);
		assertEquals(null, coffeeMaker.editRecipe(5, recipe2));
	}
	
	/* ----------------------------- Delete Recipe ----------------------------- */
	
	/**
	 * Given an invalid recipe
	 * When we try to delete it
	 * Then we get a null value.
	 */
	@Test
	public void testDeleteNegativeRecipe() throws Exception {
		assertEquals(null, coffeeMaker.deleteRecipe(-1));
	}
	
	/**
	 * Given an invalid recipe
	 * When we trye to delete it
	 * Then we get a null value.
	 */
	@Test
	public void testDeleteOutOfIndexRecipe() throws Exception {
		assertEquals(null, coffeeMaker.deleteRecipe(5));
	}

}
