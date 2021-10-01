package net.gdogra.controller;

import net.gdogra.dto.DtoWrapper;
import net.gdogra.dto.UserInputDto;
import net.gdogra.pojo.Location;
import net.gdogra.pojo.Officer;
import net.gdogra.pojo.OutputRow;
import net.gdogra.pojo.Shift;
import net.gdogra.pojo.Swipe;
import net.gdogra.service.DailyReportService;
import net.gdogra.service.DailyReportGeneratingService;
import net.gdogra.service.EmailSenderService;
import net.gdogra.service.ExtractOfficersService;
import net.gdogra.service.ReadXlsxService;
import net.gdogra.service.ShiftReportService;
import net.gdogra.service.SwipeProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AppController {

    private List<Swipe> allSwipes;

    private File uploadedFile;
    private File dailyReportFile;
    private File shiftReportFile;

    private final ReadXlsxService readXlsxService;
    private final SwipeProcessorService swipeProcessorService;
    private final EmailSenderService emailService;

    @GetMapping("/")
    public String uploadPage() {
        log.info("AppController.uploadPage");
        return "uploadView";
    }

    @PostMapping("/")
    public String upload(Model model, @RequestParam("file") MultipartFile file) {
        log.info("AppController.upload");

        try {

            uploadedFile = new File(System.getProperty("java.io.tmpdir") + File.separator +
                    file.getOriginalFilename());
            file.transferTo(uploadedFile);

            allSwipes = readXlsxService.readAllRows(new FileInputStream(uploadedFile));
            DtoWrapper dtoWrapper = createUserInputDtoWrapper(allSwipes);
            model.addAttribute("dtoWrapper", dtoWrapper);

            return "shift-infoFormView";

        } catch (Exception e) {
            model.addAttribute("msg", "Please upload an xlsx in mandatory format only");
        }

        return "messageView";
    }

    private DtoWrapper createUserInputDtoWrapper(List<Swipe> allSwipes) {

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
        log.info("AppController.preview");

        List<Shift> shifts = getShiftsListFromWrapper(dtoWrapper);

        swipeProcessorService.prepareData(allSwipes, shifts);
        List<OutputRow> inOutSwipesPrevTwoShifts = swipeProcessorService.getInOutSwipesPrevTwoShifts();
        List<OutputRow> inSwipesCurrentShift = swipeProcessorService.getInSwipesCurrentShift();

        XSSFWorkbook dailyReport = DailyReportGeneratingService.write(inOutSwipesPrevTwoShifts);
        XSSFWorkbook formattedDailyReport = DailyReportService.format(dailyReport);
        XSSFWorkbook shiftReport = ShiftReportService.generate(inSwipesCurrentShift);

        dailyReportFile = convertWorkbookToFile(formattedDailyReport, createDailyReportFileName());
        shiftReportFile = convertWorkbookToFile(shiftReport, createShiftReportFileName());

        model.addAttribute("dailyReportData", inOutSwipesPrevTwoShifts);
        model.addAttribute("shiftReportData", inSwipesCurrentShift);
        return "report_preview";
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public void download(HttpServletResponse response) throws IOException {
        log.info("AppController.download");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\"reports.zip\"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

        List<File> files = new ArrayList<>();
        files.add(dailyReportFile);
        files.add(shiftReportFile);

        for (File file : files) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                IOUtils.copy(fileInputStream, zipOutputStream);
            }
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
        Shift shift = new Shift(officer);

        switch (dtoObj.getShiftCode()) {
            case 1:
                shift.setLocation(Location.MAIN_GATE);
                shift.setDayShift(true);
                break;
            case 2:
                shift.setLocation(Location.MAIN_GATE);
                shift.setDayShift(false);
                break;
            case 3:
                shift.setLocation(Location.EP_WEIGHBRIDGE);
                shift.setDayShift(true);
                break;
            case 4:
                shift.setLocation(Location.EP_WEIGHBRIDGE);
                shift.setDayShift(false);
                break;
            case 5:
                shift.setLocation(Location.VISITORS_RECEPTION);
                shift.setDayShift(true);
                break;
            case 6:
                shift.setLocation(Location.TL_PLAISTOW);
                shift.setDayShift(true);
                break;
            case 7:
            default:
                shift.setLocation(Location.TL_PLAISTOW);
                shift.setDayShift(false);
                break;
        }
        return shift;
    }

    @GetMapping("/emailFiles")
    public String emailFiles(Model model) throws MessagingException {
        log.info("AppController.email Files");
        File[] files = {uploadedFile, dailyReportFile, shiftReportFile};
        emailService.sendEmailWithAttachment(files);
        model.addAttribute("msg", "Thanks for reporting an error in the reports, " +
                "it would help us improve the app");
        return "messageView";
    }

    @GetMapping("/about")
    public String about() {
        return "aboutView";
    }
}
