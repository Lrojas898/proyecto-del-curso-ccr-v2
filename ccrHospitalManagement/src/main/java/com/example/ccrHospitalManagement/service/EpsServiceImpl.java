package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.repository.EPSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EpsServiceImpl implements EpsService {

    private final EPSRepository epsRepository;

    @Override
    public EPS registerEPS(EPS eps) {
        if (epsRepository.existsById(eps.getNit())) {
            throw new IllegalArgumentException("Ya existe una EPS con ese NIT.");
        }
        validateEPS(eps, true);
        return epsRepository.save(eps);
    }

    @Override
    public EPS UpdateEPS(EPS eps) {
        if (!epsRepository.existsById(eps.getNit())) {
            throw new IllegalArgumentException("No se puede actualizar una EPS que no existe.");
        }
        validateEPS(eps, false);
        return epsRepository.save(eps);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EPS> getAllEPS() {
        return epsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EPS> getEPSById(String id) {
        return epsRepository.findById(id);
    }

    @Override
    public void removeEPSById(String id) {
        if (!epsRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar una EPS que no existe.");
        }
        epsRepository.deleteById(id);
    }

    private void validateEPS(EPS eps, boolean isCreate) {
        if (isCreate && (eps.getNit() == null || eps.getNit().isBlank())) {
            throw new IllegalArgumentException("El NIT de la EPS es obligatorio.");
        }

        if (eps.getName() == null || eps.getName().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre de la EPS debe tener al menos 3 caracteres.");
        }
    }
}
