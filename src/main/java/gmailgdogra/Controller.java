package gmailgdogra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class Controller {

    private List<SwipeRecord> allData;

    @GetMapping("/")
    public String home() {
        return "GD Welcomes you to Excel -> Excel Parsing Web App";
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> upload(@RequestParam MultipartFile file) {
        String message = "";

        if (ReadXlsxService.hasExcelFormat(file)) {
            try {
                allData = ReadXlsxService.parse(file.getInputStream());
                message = "Successfully uploaded: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(message));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(new ResponseMessage("Unable to parse file: " + file.getName()));
            }
        } else {
            message = "Please upload an excel file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping("/download")
    public boolean download() {

        return true;
    }

}
