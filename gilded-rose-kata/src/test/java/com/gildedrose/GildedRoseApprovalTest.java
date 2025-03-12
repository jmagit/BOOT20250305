package com.gildedrose;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.List;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

//@UseReporter(DiffReporter.class)
public class GildedRoseApprovalTest {

//	@Test
//	public void foo() {
//
//		Item[] items = new Item[] { new Item("foo", 0, 0) };
//		GildedRose app = new GildedRose(items);
//		app.updateQuality();
//		// assertEquals("foo", items[0].name);
//		assertEquals("foo, -1, 0", items[0].toString());
//		assertAll("Producto", 
//				() -> assertEquals("foo", items[0].name),
//				() -> assertEquals(-1, items[0].sellIn, "SellIn"), 
//				() -> assertEquals(0, items[0].quality, "Quality")
//				);
//		// Approvals.verifyAll("Items", items);
//	}
//	@Test
//	void fullCoverageFake() {
//		Item[] items = new Item[] { 
//				new Item("+5 Dexterity Vest", 10, 20), 
//				new Item("Aged Brie", 2, 0), 
//				new Item("Sulfuras, Hand of Ragnaros", 0, 80), 
//				new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49), 
//				new Item("Conjured Mana Cake", 3, 6) 
//				};
//		GildedRose app = new GildedRose(items);
//		for (int i = 0; i < 30; i++)
//			assertDoesNotThrow(() -> app.updateQuality());
//	}

	@ParameterizedTest(name = "{index} => sellIn: {0} quality: {1} –> sellIn: {2} quality: {3}")
	@CsvSource({ 
		"11, 10, 10, 9", 
		"7, 1, 6, 0",
//		"5, -5, 4, 0",
		"0, 3, -1, 1", 
		})
	void product_Standard_Test(int sellIn, int quality, int sellInResult, int qualityResult) {
		String name = "Standard Product";
		Item product = new Item(name, sellIn, quality);
		GildedRose app = new GildedRose(new Item[] { product });
		app.updateQuality();
		assertAll(name, 
				() -> assertEquals(name, product.name, "name"),
				() -> assertEquals(sellInResult, product.sellIn, "sellIn"),
				() -> assertEquals(qualityResult, product.quality, "quality")
				);
	}

    @ParameterizedTest(name = "{index} => sellIn: {0} quality: {1} –> sellIn: {2} quality: {3}")
	@CsvSource({
		"2, 0, 1, 1",
		"-1, 48, -2, 50",
		"2, 50, 1, 50",
		"-2, 49, -3, 50",
		"1, 1, 0, 2",
		})
	void product_Aged_Brie_Test(int sellIn, int quality, int sellInResult, int qualityResult) {
		String name = "Aged Brie";
		Item product = new Item(name, sellIn, quality);
        GildedRose app = new GildedRose(new Item[] { 
        		product
        });
        app.updateQuality();
        assertAll(name,
        		() -> assertEquals(name, product.name, "name"),
        		() -> assertEquals(sellInResult, product.sellIn, "sellIn"),
        		() -> assertEquals(qualityResult, product.quality, "quality")
        		);
	}

	@ParameterizedTest(name = "{index} => sellIn: {0} quality: {1} –> sellIn: {2} quality: {3}")
	@CsvSource({
		"1, 0, 1, 0",
		"0, 1, 0, 1",
		"-1, 1, -1, 1",
		})
	void product_Sulfuras_Test(int sellIn, int quality, int sellInResult, int qualityResult) {
		String name = "Sulfuras, Hand of Ragnaros";
		Item product = new Item(name, sellIn, quality);
        GildedRose app = new GildedRose(new Item[] { 
        		product
        });
        app.updateQuality();
        assertAll(name,
        		() -> assertEquals(name, product.name, "name"),
        		() -> assertEquals(sellInResult, product.sellIn, "sellIn"),
        		() -> assertEquals(qualityResult, product.quality, "quality")
        		);
	}

	@ParameterizedTest(name = "{index} => sellIn: {0} quality: {1} –> sellIn: {2} quality: {3}")
	@CsvSource({
		"11, 0, 10, 1",
		"7, 1, 6, 3",
		"7, 49, 6, 50",
		"5, 3, 4, 6",
		"0, 3, -1, 0",
		"-1, 3, -2, 0",
		})
	void product_Passes_Test(int sellIn, int quality, int sellInResult, int qualityResult) {
		String name = "Backstage passes to a TAFKAL80ETC concert";
		Item product = new Item(name, sellIn, quality);
        GildedRose app = new GildedRose(new Item[] { 
        		product
        });
        app.updateQuality();
        assertAll(name,
        		() -> assertEquals(name, product.name, "name"),
        		() -> assertEquals(sellInResult, product.sellIn, "sellIn"),
        		() -> assertEquals(qualityResult, product.quality, "quality")
        		);
	}

//	@Disabled
	@ParameterizedTest(name = "{index} => sellIn: {0} quality: {1} –> sellIn: {2} quality: {3}")
	@CsvSource({
		"11, 10, 10, 8",
		"7, 1, 6, 0",
		"-5, 10, -6, 6",
		"0, 3, -1, 0",
		})
	void product_Conjured_Test(int sellIn, int quality, int sellInResult, int qualityResult) {
		String name = "Conjured Mana Cake";
		Item product = new Item(name, sellIn, quality);
        GildedRose app = new GildedRose(new Item[] { 
        		product
        });
        app.updateQuality();
        assertAll(name,
        		() -> assertEquals(name, product.name, "name"),
        		() -> assertEquals(sellInResult, product.sellIn, "sellIn"),
        		() -> assertEquals(qualityResult, product.quality, "quality")
        		);
	}

////	@Disabled
//	@ParameterizedTest(name = "{0} => sellIn: {1} quality: {2} –> sellIn: {3} quality: {4}")
//	@CsvFileSource(resources = "/casos-de-prueba.csv", numLinesToSkip = 1)
//	void datasourceTest(String producto, int sellIn, int quality, int sellInResult, int qualityResult) {
//		String name = producto.replace("\'", "");
////		assumeFalse("Conjured Mana Cake".equals(name));
//		Item product = new Item(name, sellIn, quality);
//        GildedRose app = new GildedRose(new Item[] { 
//        		product
//        });
//        app.updateQuality();
//        assertAll(name,
//        		() -> assertEquals(name, product.name, "name"),
//        		() -> assertEquals(sellInResult, product.sellIn, "sellIn"),
//        		() -> assertEquals(qualityResult, product.quality, "quality")
//        		);
//	}
	
//	@Test
////	@Disabled
//	void instantanea() {
//		Item[] items = new Item[] { 
//				new Item("+5 Dexterity Vest", 10, 20), 
//				new Item("Aged Brie", 2, 0),
//				new Item("Elixir of the Mongoose", 5, 7), 
//				new Item("Sulfuras, Hand of Ragnaros", 0, 80),
//				new Item("Sulfuras, Hand of Ragnaros", -1, 80),
//				new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
//				new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
//				new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
//				// this conjured item does not work properly yet
//				new Item("Conjured Mana Cake", 3, 6) 
//				};
//
//		GildedRose app = new GildedRose(items);
//		var output = new StringBuilder();
//		output.append("day,name, sellIn, quality\n");
//		List.of(items).forEach(item -> output.append("0," + item + "\n"));
//		for (int i = 1; i <= 31; i++) {
//			app.updateQuality();
//			for(Item item: items)
//				output.append(i + "," + item + "\n");
//		}
//		Approvals.verify(output);
//	}

//    @Test
//    public void thirtyDays() {
//
//        ByteArrayOutputStream fakeoutput = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(fakeoutput));
//        System.setIn(new ByteArrayInputStream("a\n".getBytes()));
//
//        Program.main();
//        String output = fakeoutput.toString();
//
//        Approvals.verify(output);
//    }
}
