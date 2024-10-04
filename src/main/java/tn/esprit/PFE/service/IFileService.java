package tn.esprit.PFE.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    public String saveFile(MultipartFile file, String subPath);
    public String saveFileProduct(MultipartFile file, String subPath);

    public void deleteFile(String subPath);

    public Resource downloadFile(String fileName);


}
