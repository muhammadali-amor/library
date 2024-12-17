package amor.library.library;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
public class FileController {

    @Value("${file.upload-pdf-dir}")
    private String uploadPdfDir;

    private static final long MAX_FILE_SIZE = 104857600L;

//    @Value("${file.upload-image-dir}")
//    private String uploadImageDir;
//
//    @Value("${file.upload-video-dir}")
//    private String uploadVideoDir;

    // Rasmlar va videolarni va filelarni yuklash uchun umumiy metod
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Fayl turi bo'yicha saqlash papkasini tanlang
        String fileType = file.getContentType();
        String uploadDir;

        if ("application/pdf".equals(fileType)) {
            uploadDir = uploadPdfDir; // PDF fayllar uchun papka
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Faqat pdf fayllar yuklash mumkin.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Fayl hajmi juda katta. Maksimal hajm 100MB.");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Files.write(filePath, file.getBytes());
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fayl yuklashda xatolik yuz berdi.");
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadPdfDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{type}/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String type, @PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadPdfDir).resolve(fileName).normalize();
            File file = filePath.toFile();

            if (file.exists() && file.delete()) {
                return ResponseEntity.ok("Fayl muvaffaqiyatli o'chirildi.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fayl topilmadi yoki o'chirib bo'lmadi.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Faylni o'chirishda xatolik yuz berdi.");
        }
    }
}

