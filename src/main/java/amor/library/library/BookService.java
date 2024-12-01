package amor.library.library;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Base64;
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
                byte[] decodePdf = Base64.getDecoder().decode(bookDto.getPdfBookFile());
                Book book = Book.builder()
                        .title(bookDto.getName())
                        .author(bookDto.getAuthor())
                        .description(bookDto.getDescription())
                        .pdfBook(decodePdf)
                        .build();
                bookRepository.save(book);
                return "Kitob salandi :)";
            } else {
                return "Kitob mavjud";
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
