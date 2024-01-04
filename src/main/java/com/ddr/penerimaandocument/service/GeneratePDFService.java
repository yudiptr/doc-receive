package com.ddr.penerimaandocument.service;

import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class GeneratePDFService  {

    @Autowired
    private TemplateEngine templateEngine;

    // private String pdfDirectory = "C:\\Users\\YudiSabri\\Desktop\\docReceive\\";

    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName, HttpServletResponse response) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream, false);
            renderer.finishPDF();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
            response.setContentLength(byteArrayOutputStream.size());

            byteArrayOutputStream.writeTo(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}