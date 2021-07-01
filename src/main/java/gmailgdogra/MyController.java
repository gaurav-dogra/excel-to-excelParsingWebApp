package gmailgdogra;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MyController {

    private List<SwipeRecord> allSwipes;

    @RequestMapping("/")
    public String uploadPage() {
        return "uploadView";
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam("file") MultipartFile file) {
        System.out.println("Controller.upload");
        if (ReadXlsxService.hasExcelFormat(file)) {
            try {
                allSwipes = ReadXlsxService.readAllRows(file.getInputStream());
                DtoWrapper dtoWrapper = createUserInputDtoWrapper(allSwipes);
                model.addAttribute("dtoWrapper", dtoWrapper);
                return "shift-info";
            } catch (Exception e) {
                model.addAttribute("msg", "Uploaded file is not in expected format");
            }
        } else {
            model.addAttribute("msg", "The uploaded file is not a xlsx file");
        }
        return "failedUploadView";
    }

    private DtoWrapper createUserInputDtoWrapper(List<SwipeRecord> allSwipes) {
        Set<Officer> officers = ExtractOfficers.from(allSwipes);
        DtoWrapper dtoWrapper = new DtoWrapper();
        List<UserInputDto> dtoList = officers.stream()
                .map(officer -> new UserInputDto(officer.getFirstName(), officer.getLastName(), 0))
                .collect(Collectors.toList());
        dtoWrapper.setUserInputDtoList(dtoList);
        return dtoWrapper;
    }

    @PostMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@ModelAttribute DtoWrapper dtoWrapper) {
        System.out.println("Controller.download");
        try {
            String downloadFileName = createFileName();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFileName);
            List<Shift> shifts = getShiftsListFromWrapper(dtoWrapper);
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

    private List<Shift> getShiftsListFromWrapper(DtoWrapper dtoWrapper) {
        return dtoWrapper.getUserInputDtoList().stream()
                .filter(dtoObj -> dtoObj.getShiftCode() != 0)
                .map(this::convertDtoToShift)
                .collect(Collectors.toList());
    }

    private Shift convertDtoToShift(UserInputDto dtoObj) {
        Officer officer = new Officer(dtoObj.getFirstName(), dtoObj.getLastName());
        switch (dtoObj.getShiftCode()) {
            case 1:
                return new Shift(officer, Location.MAIN_GATE, true);
            case 2:
                return new Shift(officer, Location.MAIN_GATE, false);
            case 3:
                return new Shift(officer, Location.EP_WEIGHBRIDGE, true);
            case 4:
                return new Shift(officer, Location.EP_WEIGHBRIDGE, false);
            case 5:
                return new Shift(officer, Location.VISITORS_RECEPTION, true);
            case 6:
                return new Shift(officer, Location.TL_PLAISTOW, true);
            case 7:
                return new Shift(officer, Location.TL_PLAISTOW, false);
            default:
                throw new RuntimeException("Unable to understand user Input: " + dtoObj);
        }
    }
}
