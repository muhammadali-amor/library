package amor.library.library;

import jakarta.persistence.Lob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@CrossOrigin
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;

    @GetMapping
    public HttpEntity<?> getBook() {
        return ResponseEntity.ok(bookService.getBook());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> getBookPdf(@PathVariable int id) {
//        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("get Book"));
//        byte[] pdfData = book.getPdfBook();
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_PDF)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"book.pdf\"") // Faylni ko'rsatish uchun 'inline' ishlatiladi
//                .body(pdfData);
//    }


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


}
