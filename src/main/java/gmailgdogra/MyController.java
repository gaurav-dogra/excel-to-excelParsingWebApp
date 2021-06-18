package gmailgdogra;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Controller
public class MyController {

    private List<SwipeRecord> allSwipes;

    @RequestMapping("/")
    public String uploadPage() {
        return "uploadView";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        System.out.println("Controller.upload");

        if (ReadXlsxService.hasExcelFormat(file)) {
            try {
                allSwipes = ReadXlsxService.readAllRows(file.getInputStream());
                return "redirect:/shift-info";
            } catch (Exception e) {
                return "failedUploadView";
            }
        } else {
            return "failedUploadView";
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
            List<Shift> shifts = UserInput.getShiftDetails(officers);
            List<OutputRow> outputData = SwipeProcessor.getOutputDataFrom(allSwipes, shifts);
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
