package com.ddr.penerimaandocument.service;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

@Service
public class GeneratePDFService  {

    @Autowired
    private TemplateEngine templateEngine;

    private String pdfDirectory = "C:\\Users\\YudiSabri\\Desktop\\docReceive\\";

    public void generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfDirectory + pdfFileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
    }
}