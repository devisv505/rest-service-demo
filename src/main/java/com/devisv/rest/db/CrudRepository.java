package com.devisv.rest.db;

/**
 * create, read, update, and delete repository
 *
 * @param <E>  entity
 * @param <ID> id of entity
 */
public interface CrudRepository<E, ID> {

    /**
     * create a new entity in database
     *
     * @param entity
     * @return
     */
    E create(E entity);

    /**
     * find an entity by certain ID
     *
     * @param id
     * @return
     */
    E getById(ID id);

    /**
     * update entity
     *
     * @param entity
     * @return
     */
    E update(E entity);

    /**
     * delete an entity by certain ID
     *
     * @param id
     * @return
     */
    boolean delete(ID id);

}
