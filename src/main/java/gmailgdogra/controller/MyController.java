package gmailgdogra.controller;

import gmailgdogra.pojo.*;
import gmailgdogra.service.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MyController {

    private List<SwipeRecord> allSwipes;
    private List<OutputRow> inOutSwipesPrevTwoShifts;
    private List<OutputRow> inSwipesCurrentShift;
    private final ReadXlsxService readXlsxService;
    private final SwipeProcessorService swipeProcessorService;

    @Autowired
    public MyController(ReadXlsxService readXlsxService, SwipeProcessorService swipeProcessorService) {
        this.readXlsxService = readXlsxService;
        this.swipeProcessorService = swipeProcessorService;
    }

    @GetMapping("/")
    public String uploadPage() {
        System.out.println("MyController.uploadPage");
        return "uploadView";
    }

    @PostMapping("/")
    public String upload(Model model, @RequestParam("file") MultipartFile file) {
        System.out.println("Controller.upload");
        if (readXlsxService.hasExcelFormat(file)) {
            try {
                allSwipes = readXlsxService.readAllRows(file.getInputStream());
                DtoWrapper dtoWrapper = createUserInputDtoWrapper(allSwipes);
                model.addAttribute("dtoWrapper", dtoWrapper);
                return "shift-infoFormView";
            } catch (Exception e) {
                model.addAttribute("msg", "Uploaded file is not in expected format");
            }
        } else {
            model.addAttribute("msg", "The uploaded file is not an xlsx");
        }
        return "messageView";
    }

    private DtoWrapper createUserInputDtoWrapper(List<SwipeRecord> allSwipes) {
        Set<Officer> officers = ExtractOfficersService.from(allSwipes);
        DtoWrapper dtoWrapper = new DtoWrapper();
        List<UserInputDto> dtoList = officers.stream()
                .map(officer -> new UserInputDto(officer.getFirstName(), officer.getLastName(), 0))
                .collect(Collectors.toList());
        dtoWrapper.setUserInputDtoList(dtoList);
        return dtoWrapper;
    }

    @PostMapping("/preview")
    public String preview(@ModelAttribute DtoWrapper dtoWrapper, Model model) {
        System.out.println("Controller.download");
        List<Shift> shifts = getShiftsListFromWrapper(dtoWrapper);
        swipeProcessorService.prepareData(allSwipes, shifts);
        inOutSwipesPrevTwoShifts = swipeProcessorService.getInOutSwipesPrevTwoShifts();
        inSwipesCurrentShift = swipeProcessorService.getInSwipesCurrentShift();
        model.addAttribute("dailyReportData", inOutSwipesPrevTwoShifts);
        model.addAttribute("shiftReportData", inSwipesCurrentShift);
        return "report_preview";
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download() throws IOException {
        System.out.println("MyController.download");
        XSSFWorkbook plainXlsx = WriteToXlsxService.write(inOutSwipesPrevTwoShifts);
        XSSFWorkbook formattedXlsx = FormatXlsxService.of(plainXlsx);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + createFileName());

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            formattedXlsx.write(outputStream);
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            return new ResponseEntity<>(resource, header, HttpStatus.CREATED);
        } catch (IOException e) {
            throw new IOException("Failed to write output to file");
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

    @GetMapping("/restart")
    public String restart() {
        System.out.println("MyController.restart");
        Restarter.getInstance().restart();
        return "uploadView";
    }
}
