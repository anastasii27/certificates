package com.epam.esm.repository;

import com.epam.esm.model.Pagination;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    /**
     * This method is used to return T by id.
     *
     * @param id the id of T to be returned.
     * @return T.
     */
    Optional<T> findById(long id);
    /**
     * This method is used to return List of all T.
     *
     * @return List of T.
     */
    List<T>  findAll(Pagination pagination);
    /**
     * This method is used to create T.
     *
     * @param t the T to be created.
     * @return an Optional with the value of created T or empty Optional
     *          if no T was created.
     */
    Optional<T> create(T t);
    /**
     * This method is used to update T.
     *
     * @param t the T to be updated.
     * @return an Optional with the value of updated T or empty Optional
     *          if no T was created.
     */
    Optional<T> update(T t);
    /**
     * This method is used to delete T by id.
     *
     * @param id the id of T to be deleted.
     */
    void delete(long id);
}
