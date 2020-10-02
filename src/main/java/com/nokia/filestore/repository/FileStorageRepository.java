package com.nokia.filestore.repository;

import com.nokia.filestore.entity.TarFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageRepository extends JpaRepository<TarFile, String> {
}
