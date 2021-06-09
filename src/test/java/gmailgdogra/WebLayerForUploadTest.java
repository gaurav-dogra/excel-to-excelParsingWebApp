package gmailgdogra;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WebLayerForUploadTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("GD Welcomes you to Excel -> Excel Parsing Web App")));
    }

    @Test
    public void shouldReturnStatusOk() throws Exception {
        File file = new File("src/main/resources/testFile.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(multipartFile))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequest() throws Exception {
        File file = new File("file.txt");

        if (file.createNewFile()) {
            FileInputStream inputStream = new FileInputStream(file);
            MockMultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(),
                    "text/plain",
                    IOUtils.toByteArray(inputStream));

            mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                    .file(multipartFile))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    public void shouldReturnExpectationFailed() throws Exception {
        File nonParseableFile = new File("NonParseableFile.xlsx");
        nonParseableFile.createNewFile();
        FileInputStream inputStream = new FileInputStream(nonParseableFile);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                nonParseableFile.getName(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                IOUtils.toByteArray(inputStream));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(multipartFile))
                .andExpect(status().isExpectationFailed());
    }
}
