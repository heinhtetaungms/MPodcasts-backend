package org.kyi.solution.service.impl;

import lombok.AllArgsConstructor;
import org.kyi.solution.model.Writer;
import org.kyi.solution.repository.WriterRepository;
import org.kyi.solution.service.WriterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WriterServiceImpl implements WriterService {
    private final WriterRepository writerRepository;

    @Override
    public Writer save(Writer writer) {
        return writerRepository.save(writer);
    }

    @Override
    public List<Writer> findAll() {
        return writerRepository.findAll();
    }

    @Override
    public Writer findById(long id) {
        return writerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find writer by id " + id));
    }

    @Override
    public void delete(long id) {
        if (writerRepository.existsById(id)) {
            writerRepository.deleteById(id);
        }
    }
}
