package amor.library.library;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDto {
    private int id;
    private String name;
    private String author;
    private String description;
    private String enter;
    private String pdfBookFileName;
}
