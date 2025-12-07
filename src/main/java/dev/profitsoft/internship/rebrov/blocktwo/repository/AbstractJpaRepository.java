package dev.profitsoft.internship.rebrov.blocktwo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractJpaRepository<T> {

    private final Class<T> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractJpaRepository() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    public Optional<T> getById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    public List<T> getAll() {
        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(query, entityClass).getResultList();
    }

    @Transactional
    public void save(T entity) {
        entityManager.persist(entity);

    }

    @Transactional
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    public void delete(@NotNull Long id) {
        T objToDelete = getById(id).orElseThrow();
        entityManager.remove(objToDelete);
    }

    @Transactional
    public void saveBatch(@NotNull List<T> batch) {
        for (T entity : batch) {
            entityManager.persist(entity);
        }
        entityManager.flush();
        entityManager.clear();
    }

    public List<T> getAllByIdSet(@NotNull Set<Long> ids){
        String query = "SELECT t FROM "+entityClass.getSimpleName()+" t WHERE t.id IN :ids";
        return entityManager.createQuery(query, entityClass)
                .setParameter("ids", ids)
                .getResultList();
    }
}
