package amor.library.library;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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
                    .pdfBookFileName(book.getBookFileName())
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
                        .bookFileName(bookDto.getPdfBookFileName())
                        .build();
                bookRepository.save(book);
                return "Kitob saqlandi :)";
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
                        bookRepository.save(book);
                        return "Kitob aftori taxrirlandi";
                    }
                    case "description" -> {
                        book.setDescription(bookDto.getAuthor());
                        bookRepository.save(book);
                        return "Kitob tavsifi taxrirlandi";
                    }
                    case "pdf" -> {
                        book.setBookFileName(bookDto.getPdfBookFileName());
                        bookRepository.save(book);
                        return "Kitob fayli taxrirlandi";
                    }
                    default -> {
                        return "Bunaqa yo'nalish yo'q";
                    }
                }
            } else {
                return "Nimani taxrirlash buyrug'i kelmadi?";
            }
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
