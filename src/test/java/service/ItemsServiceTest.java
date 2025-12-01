package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Items;
import domain.libraryType;
import repository.ItemsRepository;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ItemsServiceTest {
    private ItemsRepository itemsRepository;
    private ItemsService itemsService;

    @BeforeEach
    void setUp() {
        itemsRepository = mock(ItemsRepository.class);
        itemsService = new ItemsService(itemsRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addNewItem() {
        String name = "Sara's Test";
        String author = "Sara Taha";
        int quantity = 10;
        libraryType type = libraryType.Book;
        when(itemsRepository.addItem(any(Items.class)))
                .thenReturn(true);
        boolean result = itemsService.addNewItem(name, author, quantity, type);
        assertTrue(result);
        verify(itemsRepository, times(1)).addItem(any(Items.class));
    }

    @Test
    void addItemWithBlankNameExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.addNewItem("   ", "Author", 5, libraryType.Book)
        );

        assertEquals("Item name cannot be empty.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void addItemWithBlankAuthorExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.addNewItem("Sara's Test", " ", 5, libraryType.Book)
        );
        assertEquals("Author cannot be empty.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void addItemWithBlankQuantityExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.addNewItem("Sara's Test", "Author", 0, libraryType.Book)
        );
        assertEquals("Quantity must be greater than 0.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void addItemWithBlankTypeExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.addNewItem("Title", "Author", 5, null)
        );
        assertEquals("Type (BOOK / CD) is required.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void increaseQuantityByISBN() {
        String isbnInput = "123";
        int isbn = 123;
        Items existingItem = new Items("Author", "Title", libraryType.Book, 5, "");
        when(itemsRepository.findByISBN(isbn)).thenReturn(Optional.of(existingItem));
        when(itemsRepository.increaseQuantity(isbn)).thenReturn(true);
        boolean result = itemsService.increaseQuantityByISBN(isbnInput);
        assertTrue(result);
        verify(itemsRepository, times(1)).findByISBN(isbn);
        verify(itemsRepository, times(1)).increaseQuantity(isbn);
    }

    @Test
    void increaseQuantityByNullIsbnExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.increaseQuantityByISBN(null)
        );
        assertEquals("ISBN cannot be empty.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void increaseQuantityByNonNumberIsbnExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.increaseQuantityByISBN("abc")
        );
        assertEquals("ISBN must be a number.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void increaseQuantityByNotFoundIsbnExceptionWith() {
        String isbnInput = "5000";
        int isbn = 5000;

        when(itemsRepository.findByISBN(isbn)).thenReturn(Optional.empty());
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.increaseQuantityByISBN(isbnInput)
        );
        assertEquals("No item found with this ISBN.", ex.getMessage());
        verify(itemsRepository, times(1)).findByISBN(isbn);
        verify(itemsRepository, never()).increaseQuantity(anyInt());
    }

    @Test
    void searchByISBN() {
        String isbnInput = "123";
        int isbn = 123;
        Items mockItem = new Items("Author", "Title", libraryType.Book, 10, isbnInput);
        when(itemsRepository.findByISBN(isbn)).thenReturn(Optional.of(mockItem));
        Items result = itemsService.searchByISBN(isbnInput);
        assertEquals(mockItem, result);
        verify(itemsRepository, times(1)).findByISBN(isbn);
    }

    @Test
    void searchByNullISBNExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.searchByISBN(null)
        );
        assertEquals("ISBN cannot be empty.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void searchByBlankISBNExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.searchByISBN("   ")
        );
        assertEquals("ISBN cannot be empty.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void searchByNonNumberISBNExceptionWith() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.searchByISBN("abc")
        );
        assertEquals("ISBN must be a number.", ex.getMessage());
        verifyNoInteractions(itemsRepository);
    }

    @Test
    void searchByNotFoundISBNExceptionWith() {
        String isbnInput = "5000";
        int isbn = 5000;
        when(itemsRepository.findByISBN(isbn)).thenReturn(Optional.empty());
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> itemsService.searchByISBN(isbnInput)
        );
        assertEquals("No item found with this ISBN.", ex.getMessage());
        verify(itemsRepository, times(1)).findByISBN(isbn);
    }

    @Test
    void searchBooksByName() {
        String name = "Sara";
        List<Items> mockList = List.of(
                new Items("A1", "Sara", libraryType.Book, 3, "1")
        );
        when(itemsRepository.findByName(name)).thenReturn(mockList);
        List<Items> result = itemsService.searchBooksByName(name);
        assertEquals(1, result.size());
        assertEquals(libraryType.Book, result.get(0).getType());
        verify(itemsRepository, times(1)).findByName(name);
    }

    @Test
    void searchCDsByName() {
        String name = "Sara";
        List<Items> mockList = List.of(
                new Items("A1", "Sara", libraryType.CD, 3, "1")
        );
        when(itemsRepository.findByName(name)).thenReturn(mockList);
        List<Items> result = itemsService.searchCDsByName(name);
        assertEquals(1, result.size());
        assertEquals(libraryType.CD, result.get(0).getType());
        verify(itemsRepository, times(1)).findByName(name);
    }

    @Test
    void searchBooksByAuthor() {
        String author = "Sara";
        List<Items> mockList = List.of(
                new Items("A1", "Sara", libraryType.Book, 3, "1")
        );
        when(itemsRepository.findByAuthor(author)).thenReturn(mockList);
        List<Items> result = itemsService.searchBooksByAuthor(author);
        assertEquals(1, result.size());
        assertEquals(libraryType.Book, result.get(0).getType());
        verify(itemsRepository, times(1)).findByAuthor(author);
    }

    @Test
    void searchCDsByAuthor() {
        String author = "Sara";
        List<Items> mockList = List.of(
                new Items("A1", "Sara", libraryType.CD, 3, "1")
        );
        when(itemsRepository.findByAuthor(author)).thenReturn(mockList);
        List<Items> result = itemsService.searchCDsByAuthor(author);
        assertEquals(1, result.size());
        assertEquals(libraryType.CD, result.get(0).getType());
        verify(itemsRepository, times(1)).findByAuthor(author);
    }
}