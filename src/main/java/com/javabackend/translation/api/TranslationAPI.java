package com.javabackend.translation.api;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class TranslationAPI {
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public ResponseEntity uploadFile(@RequestPart("file") MultipartFile file){
        Map<String, Object> response = new HashMap();
        try {
            byte[] bytesData = file.getBytes();
            StringBuilder data = new StringBuilder();
            if(file.getOriginalFilename().contains(".pdf")){
                PDDocument document = PDDocument.load(bytesData);
                if (!document.isEncrypted()) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(document);
                    String lines[] = text.replaceAll("\\\\","")
                            .split("\\r?\\n");
                    for (String line : lines) {
                        data.append(line);
                    }
                }
                document.close();
            }else{
                data.append(new String(bytesData));
            }
            response.put("status", true);
            response.put("data", data.toString());
        }catch (Exception e){
            response.put("status", false);
            response.put("data", null);
        }finally {
            return ResponseEntity.ok(response);
        }
    }
}
