package com.nokia.filestore.resource;

import com.nokia.filestore.entity.TarFile;
import com.nokia.filestore.service.FilesStorageService;
import com.nokia.filestore.util.FileStorageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/store")
@Api(description = "It will contain all apis related to FileStore")
public class FilesController {

    @Autowired
    FilesStorageService filesStorageService;

    @RequestMapping(path = "/file", method = RequestMethod.POST)
    @ApiOperation(value = "Store file information", response = FileStorageResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully stored the file"),
            @ApiResponse(code = 500, message = "Server error") })
    public FileStorageResponse uploadFile(@RequestParam("file") MultipartFile file) {
        TarFile tarFile = filesStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/")
                .path(tarFile.getId())
                .toUriString();

        return new FileStorageResponse(tarFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @RequestMapping(path = "/file/{fileId}",method = RequestMethod.GET)
    @ApiOperation(value = "Return file information with given fileId", response = TarFile.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully able to get the file data with given fileId"),
            @ApiResponse(code = 404, message = "file not found with given fileId"),
            @ApiResponse(code = 500, message = "Server error") })
    public TarFile downloadFile(@PathVariable String fileId) {
        // Load tar file from database
        TarFile dbFile = filesStorageService.getFile(fileId);

        return dbFile;
    }

    @RequestMapping(path = "/files",method = RequestMethod.GET)
    @ApiOperation(value = "Return files information", response = TarFile.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully able to get the all files"),
            @ApiResponse(code = 404, message = "files not found"),
            @ApiResponse(code = 500, message = "Server error") })

    public List<TarFile> downloadFiles() {
        // Load file from database
        List<TarFile> dbFiles = filesStorageService.getFiles();

        return dbFiles;
    }
}
