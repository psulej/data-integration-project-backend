package dev.psulej.dataintegrationprojectbackend.service;

import dev.psulej.dataintegrationprojectbackend.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.repository.MortalityDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MortalityDataService {

    private final MortalityDataRepository mortalityDataRepository;

    public Page<MortalityData> getMortalityData(Pageable pageable) {
        return mortalityDataRepository.findAll(pageable);
    }
}
