package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.repository.PrepaidMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional 
public class PrepaidMedicineServiceImpl implements PrepaidMedicineService {

    private final PrepaidMedicineRepository prepaidMedicineRepository;

    @Override
    public PrepaidMedicine registerPrepaidMedicine(PrepaidMedicine medicine) {
        if (prepaidMedicineRepository.existsById(medicine.getNit())) {
            throw new IllegalArgumentException("Ya existe una medicina prepagada con ese NIT.");
        }
        validatePrepaidMedicine(medicine, true);
        return prepaidMedicineRepository.save(medicine);
    }

    @Override
    public PrepaidMedicine updatePrepaidMedicine(PrepaidMedicine medicine) {
        if (!prepaidMedicineRepository.existsById(medicine.getNit())) {
            throw new IllegalArgumentException("La medicina prepagada no existe.");
        }
        validatePrepaidMedicine(medicine, false);
        return prepaidMedicineRepository.save(medicine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrepaidMedicine> getAllPrepaidMedicines() {
        return prepaidMedicineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaidMedicine> getPrepaidMedicineById(String id) {
        return prepaidMedicineRepository.findById(id);
    }

    @Override
    public void removePrepaidMedicineById(String id) {
        if (!prepaidMedicineRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar una medicina prepagada que no existe.");
        }
        prepaidMedicineRepository.deleteById(id);
    }

    private void validatePrepaidMedicine(PrepaidMedicine medicine, boolean isCreate) {
        if (isCreate && (medicine.getNit() == null || medicine.getNit().isBlank())) {
            throw new IllegalArgumentException("El NIT es obligatorio.");
        }

        if (medicine.getName() == null || medicine.getName().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }
    }
}
