package amor.library.library;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookDto> getBook() {
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : bookRepository.findAll()) {
            BookDto build = BookDto.builder()
                    .id(book.getId())
                    .name(book.getTitle())
                    .author(book.getAuthor())
                    .description(book.getDescription())
                    .pdfBook(book.getPdfBook())
                    .build();
            bookDtos.add(build);
        }
        return bookDtos;
    }

    public String addBook(BookDto bookDto) {
        try {
            if (!bookRepository.existsBookByTitleEqualsIgnoreCase(bookDto.getName())) {
                Book book = Book.builder()
                        .title(bookDto.getName())
                        .author(bookDto.getAuthor())
                        .description(bookDto.getDescription())
                        .pdfBook(bookDto.getPdfBookFile().getBytes())
                        .build();
                bookRepository.save(book);
                return "Kitob salandi :)";
            } else {
                return "Kitob mavjud";
            }
        } catch (Exception e) {
            return "error :(";
        }
    }

    public String editBook(int id, BookDto bookDto) {
        try {
            Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getBook"));
            if (!bookDto.getEnter().isEmpty()) {
                switch (bookDto.getEnter()) {
                    case "name" -> {
                        if (!bookRepository.existsBookByTitleEqualsIgnoreCase(bookDto.getName())) {
                            book.setTitle(bookDto.getName());
                            return "Kitob nomi taxrirlandi";
                        } else {
                            return "Kitob mavjud";
                        }
                    }
                    case "author" -> {
                        book.setAuthor(bookDto.getAuthor());
                    }
                    case "description" -> {
                        book.setDescription(bookDto.getAuthor());
                    }
                    case "pdf" -> {
                        book.setPdfBook(bookDto.getPdfBookFile().getBytes());
                    }
                    default -> {
                        return "Bunaqa yo'nalish yo'q";
                    }
                }
            } else {
                return "Nimani taxrirlash buyrug'i kelmadi?";
            }
            return "-{}-";
        } catch (Exception e) {
            return "error  :(";
        }
    }

    public String deleteBook(int id) {
        try {
            bookRepository.deleteById(id);
            return "Kitob o'chirildi";
        } catch (Exception e) {
            return "error  :(";
        }
    }

    public ResponseEntity<byte[]> downloadPdf(int id){
        try {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent() && book.get().getPdfBook() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + book.get().getTitle() + ".pdf");
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                        .body(book.get().getPdfBook());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            System.err.println(e.getMessage());
            ResponseEntity<byte[]> ResponseEntity = null;
            return ResponseEntity;
        }
    }
}
