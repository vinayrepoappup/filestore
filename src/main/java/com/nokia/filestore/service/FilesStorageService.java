package com.nokia.filestore.service;

import com.nokia.filestore.entity.TarFile;
import com.nokia.filestore.repository.FileStorageRepository;
import com.nokia.filestore.util.exception.FileStorageException;
import com.nokia.filestore.util.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FilesStorageService {

    @Autowired
    FileStorageRepository fileStorageRepository;

    public TarFile storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            TarFile dbFile = new TarFile(fileName, file.getContentType(), file.getBytes());
            return fileStorageRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public TarFile getFile(String fileId) {
        return fileStorageRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public List<TarFile> getFiles(){
        try {
            return fileStorageRepository.findAll();
        }catch (Exception ex){
            throw new FileStorageException("Could not get the files", ex);
        }
    }

}
