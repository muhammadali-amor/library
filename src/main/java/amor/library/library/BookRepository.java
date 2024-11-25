package amor.library.library;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsBookByTitleEqualsIgnoreCase(String title);
}
