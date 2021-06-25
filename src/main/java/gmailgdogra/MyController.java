package gmailgdogra;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class MyController {

    private List<SwipeRecord> allSwipes;

    @RequestMapping("/")
    public String uploadPage(Model model) {
        return "uploadView";
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam("file") MultipartFile file) {
        System.out.println("Controller.upload");

        if (ReadXlsxService.hasExcelFormat(file)) {
            try {
                model.addAttribute("msg", "Successfully Uploaded: " + file.getOriginalFilename());

                allSwipes = ReadXlsxService.readAllRows(file.getInputStream());
                Set<Officer> officers = ExtractOfficers.from(allSwipes);
                List<ShiftDto> shiftDtos = new ArrayList<>();
                for (Officer officer : officers) {
                    shiftDtos.add(new ShiftDto(officer.toString(), 0));
                }
                DtoWrapper dtoWrapper = new DtoWrapper();
                dtoWrapper.setShifts(shiftDtos);
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

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(Model model) {
        System.out.println("Controller.download");
        try {
            String filename = createFileName();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            List<Shift> shifts = getShiftsDetails();
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

    private List<Shift> getShiftsDetails() {
        Officer anthony = new Officer("Anthony", "Capes");
        Officer gaurav = new Officer("GAURAV", "DOGRA");
        Officer zubair = new Officer("Zubair", "Patel");
        Officer dean = new Officer("Dean", "Colquhoun");
        Officer omo = new Officer("Omoogbolahan", "Adeola");
        Officer derek = new Officer("Derek", "Devlin");
        Officer mark = new Officer("Mark", "Seth");
        Officer reehad = new Officer("Reehad", "Ali");
        Officer kayode = new Officer("Kayode", "Dairo");
        return List.of(
                new Shift(anthony, Location.TL_PLAISTOW, false),
                new Shift(gaurav, Location.MAIN_GATE, false),
                new Shift(zubair, Location.VISITORS_RECEPTION, true),
                new Shift(dean, Location.MAIN_GATE, true),
                new Shift(omo, Location.EP_WEIGHBRIDGE, false),
                new Shift(derek, Location.TL_PLAISTOW, true),
                new Shift(mark, Location.MAIN_GATE, true),
                new Shift(reehad, Location.EP_WEIGHBRIDGE, true),
                new Shift(kayode, Location.MAIN_GATE, false)
        );
    }
}
