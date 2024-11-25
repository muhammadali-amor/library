package amor.library.library;

import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public HttpEntity<?> getBook() {
        return ResponseEntity.ok(bookService.getBook());
    }

    @PostMapping
    public HttpEntity<?> addBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.addBook(bookDto));
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editBook(@PathVariable int id, @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.editBook(id, bookDto));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteBook(@PathVariable int id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    @GetMapping("/{id}/download")
    public HttpEntity<?> downloadPdf(@PathVariable int id) {
        return ResponseEntity.ok(bookService.downloadPdf(id));
    }

}
