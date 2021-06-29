package com.crm.servicebackend.service;

import com.crm.servicebackend.model.Storage;
import com.crm.servicebackend.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    private final StorageRepository repository;

    @Autowired
    public StorageService(StorageRepository repository) {
        this.repository = repository;
    }

    public Storage get(Long productId, Long serviceCenterId) {
        return repository.findByProductIdAndServiceCenterId(productId, serviceCenterId);
    }

    public Boolean existsByProductIdAndServiceCenterId(Long productId, Long serviceCenterId) {
        return repository.existsByProductIdAndServiceCenterId(productId, serviceCenterId);
    }

    public Storage updateStorageCount(long productId, long serviceCenterId, int count){
        Storage storage = get(productId, serviceCenterId);
        storage.setQuantity(storage.getQuantity()+count);
        return save(storage);
    }

    public void delete(Storage storage) {
        repository.delete(storage);
    }

    public Storage save(Storage storage) {
        return repository.save(storage);
    }

}
