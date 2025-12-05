import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductStockTest {

    ProductStock stock;

    @BeforeAll
    static void initAll() {
        System.out.println("=== Starting ProductStock Test Suite ===");
    }

    @BeforeEach
    void init() {
        stock = new ProductStock("P100", "A1", 20, 5, 50);
    }

    @AfterEach
    void cleanup() {
        System.out.println("Test finished.");
    }

    @AfterAll
    static void cleanupAll() {
        System.out.println("=== All tests completed ===");
    }

    @Test
    @Order(1)
    @Tag("sanity")
    @DisplayName("Constructor: valid parameters")
    void testValidConstructor() {
        assertEquals("P100", stock.getProductId());
        assertEquals("A1", stock.getLocation());
        assertEquals(20, stock.getOnHand());
        assertEquals(0, stock.getReserved());
    }

    @Test
    @Order(2)
    @DisplayName("Constructor: invalid parameters should throw error")
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("", "A1", 5, 1, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "", 5, 1, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "A1", -1, 1, 10));
    }

    @Test
    @Order(3)
    @Tag("regression")
    @DisplayName("Add stock: normal case")
    void testAddStock() {
        stock.addStock(10);
        assertEquals(30, stock.getOnHand());
    }

    @Test
    @Order(4)
    @DisplayName("Add stock: exceeding max capacity should fail")
    void testAddStockExceed() {
        assertThrows(IllegalStateException.class, () -> stock.addStock(100));
    }

    @Test
    @Order(5)
    @DisplayName("Reserve stock: valid amount")
    void testReserve() {
        stock.reserve(5);
        assertEquals(5, stock.getReserved());
    }

    @Test
    @Order(6)
    @DisplayName("Reserve stock: more than available should fail")
    void testReserveFail() {
        assertThrows(IllegalStateException.class, () -> stock.reserve(100));
    }

    @Test
    @Order(7)
    @DisplayName("Release reservation: normal case")
    void testRelease() {
        stock.reserve(10);
        stock.releaseReservation(5);
        assertEquals(5, stock.getReserved());
    }

    @Test
    @Order(8)
    @DisplayName("Ship reserved stock")
    void testShipReserved() {
        stock.reserve(5);
        stock.shipReserved(5);
        assertEquals(15, stock.getOnHand());
        assertEquals(0, stock.getReserved());
    }

    @Test
    @Disabled("Future feature - not implemented yet")
    @DisplayName("Bulk update feature (Disabled Example)")
    void testFutureFeature() {
        // Future implementation
    }

    @Test
    @Order(9)
    @DisplayName("Check reorder logic")
    void testReorderNeeded() {
        stock.reserve(18); // leaves available = 2
        assertTrue(stock.isReorderNeeded());
    }
}
