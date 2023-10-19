package org.kyi.solution.service;

import org.kyi.solution.model.Writer;

import java.util.List;

public interface WriterService {
    Writer save(Writer post);
    List<Writer> findAll();
    Writer findById(long id);
    void delete(long id);
}
