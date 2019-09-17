package com.github.nut077.lotto.service;

import com.github.nut077.lotto.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Log4j2
public abstract class BaseService<E, I> {

  private JpaRepository<E, I> repository;

  protected BaseService(JpaRepository<E, I> repository) {
    this.repository = repository;
  }

  public List<E> findAll() {
    return repository.findAll();
  }

  public E findById(I id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("Id: " + id + " -->> not found"));
  }

  public E create(E entity) {
    return repository.save(entity);
  }

  public E update(I id, E entity) {
    findById(id);
    return create(entity);
  }

  public void delete(I id) {
    findById(id);
    repository.deleteById(id);
    log.info("id:{} -->> is deleted", id);
  }
}
