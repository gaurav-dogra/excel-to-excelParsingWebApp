package gmailgdogra.controller;

import gmailgdogra.pojo.DtoWrapper;
import gmailgdogra.pojo.Location;
import gmailgdogra.pojo.Officer;
import gmailgdogra.pojo.OutputRow;
import gmailgdogra.pojo.Shift;
import gmailgdogra.pojo.SwipeRecord;
import gmailgdogra.pojo.UserInputDto;
import gmailgdogra.service.DailyReportFormattingService;
import gmailgdogra.service.DailyReportGeneratingService;
import gmailgdogra.service.EmailService;
import gmailgdogra.service.ExtractOfficersService;
import gmailgdogra.service.ReadXlsxService;
import gmailgdogra.service.ShiftReportGeneratingService;
import gmailgdogra.service.SwipeProcessorService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class AppController {

    private List<SwipeRecord> allSwipes;

    private File convertedFile;
    private File dailyReportFile;
    private File shiftReportFile;

    private final ReadXlsxService readXlsxService;
    private final SwipeProcessorService swipeProcessorService;

    @Autowired
    public AppController(ReadXlsxService readXlsxService, SwipeProcessorService swipeProcessorService) {
        this.readXlsxService = readXlsxService;
        this.swipeProcessorService = swipeProcessorService;
    }

    @GetMapping("/")
    public String uploadPage() {
        System.out.println("AppController.uploadPage");
        return "uploadView";
    }

    @PostMapping("/")
    public String upload(Model model, @RequestParam("file") MultipartFile file) {
        System.out.println("AppController.upload");

        try {

            convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" +
                    file.getOriginalFilename());
            file.transferTo(convertedFile);

            allSwipes = readXlsxService.readAllRows(new FileInputStream(convertedFile));
            DtoWrapper dtoWrapper = createUserInputDtoWrapper(allSwipes);
            model.addAttribute("dtoWrapper", dtoWrapper);

            return "shift-infoFormView";

        } catch (Exception e) {
            model.addAttribute("msg", "Please upload an xlsx in mandatory format only");
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
    public String preview(@ModelAttribute DtoWrapper dtoWrapper, Model model) throws IOException {
        System.out.println("AppController.preview");

        List<Shift> shifts = getShiftsListFromWrapper(dtoWrapper);

        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> inOutSwipesPrevTwoShifts = swipeProcessorService.getInOutSwipesPrevTwoShifts();
        List<OutputRow> inSwipesCurrentShift = swipeProcessorService.getInSwipesCurrentShift();

        XSSFWorkbook dailyReport = DailyReportGeneratingService.write(inOutSwipesPrevTwoShifts);
        XSSFWorkbook formattedDailyReport = DailyReportFormattingService.of(dailyReport);
        XSSFWorkbook shiftReport = ShiftReportGeneratingService.write(inSwipesCurrentShift);

        dailyReportFile = convertWorkbookToFile(formattedDailyReport, createDailyReportFileName());
        shiftReportFile = convertWorkbookToFile(shiftReport, createShiftReportFileName());

        model.addAttribute("dailyReportData", inOutSwipesPrevTwoShifts);
        model.addAttribute("shiftReportData", inSwipesCurrentShift);
        return "report_preview";
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public void download(HttpServletResponse response) throws IOException {
        System.out.println("AppController.download");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"reports.zip\"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        List<File> files = new ArrayList<>();
        files.add(dailyReportFile);
        files.add(shiftReportFile);

        for (File file : files) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);
            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        zipOutputStream.close();
    }

    private String createShiftReportFileName() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return String.format("Shift Report Day %s.xlsx", date);
    }

    private File convertWorkbookToFile(XSSFWorkbook formattedDailyReport, String fileName) throws IOException {
        File file = new File(fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        formattedDailyReport.write(outputStream);
        return file;
    }

    private String createDailyReportFileName() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return String.format("Daily Report %s.xlsx", date);
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

    @GetMapping("/reportError")
    public String error(Model model) throws MessagingException {
        System.out.println("AppController.error");

        EmailService emailService = new EmailService();
        emailService.sendmail(Arrays.asList(convertedFile, dailyReportFile, shiftReportFile));
        model.addAttribute("msg", "Thanks for reporting.\nIt helps us improve the app.");

        return "messageView";
    }

    @GetMapping("/about")
    public String about() {
        return "aboutView";
    }
}
