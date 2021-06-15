package gmailgdogra;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
public class Controller {

    private List<SwipeRecord> allSwipes;

    @GetMapping("/")
    public String home() {
        return "GD Welcomes you to Excel -> Excel Parsing Web App";
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> upload(@RequestParam MultipartFile file) {
        System.out.println("Controller.upload");
        String message;

        if (ReadXlsxService.hasExcelFormat(file)) {
            try {
                allSwipes = ReadXlsxService.readAllRows(file.getInputStream());
                message = "Successfully uploaded: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(message));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(new ResponseMessage("Unable to readAllRows file: " + file.getName()));
            }
        } else {
            message = "Please upload an excel file!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage(message));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download() {
        System.out.println("Controller.download");
        try {
            if (allSwipes == null) {
                throw new Exception();
            }
            String filename = createFileName();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            Set<Officer> officers = ExtractOfficers.from(allSwipes);
            System.out.println(officers);
            List<Shift> shifts = UserInput.getShiftDetails(officers);
            System.out.println("shifts.size() = " + shifts.size());
            List<OutputRow> outputData = SwipeProcessor.getOutputDataFrom(allSwipes, shifts);
            System.out.println("outputData.size() = " + outputData.size());
            outputData.forEach(System.out::println);
            ByteArrayResource resource = new ByteArrayResource(WriteOutputToXlsx.write(outputData));
            return new ResponseEntity<>(resource, header, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

    }

    private String createFileName() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return String.format("Securitas Report %s.xlsx", date);
    }
}
