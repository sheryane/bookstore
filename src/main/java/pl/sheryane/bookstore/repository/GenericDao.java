package pl.sheryane.bookstore.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.ParameterizedType;

public abstract class GenericDao<T, K> {

    protected final EntityManagerFactory managerFactory;
    protected final EntityManager em;
    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    protected GenericDao() {
        managerFactory = Persistence.createEntityManagerFactory("examplePersistenceUnit");
        this.em = managerFactory.createEntityManager();

        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public T create(T entity) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("Record successfully added!");
        return entity;
    }

    public T read(K id) {
        return em.find(entityClass, id);
    }

    public T update(T entity) {
        EntityTransaction transaction = null;
        T updatedEntity = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            updatedEntity = em.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("Record successfully modified!");
        return updatedEntity;
    }

    public void delete(K id) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            em.remove(read(id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("Record successfully removed!");
    }

    public void cleanUp() {
        System.out.println("Goodbye!");
        em.close();
        managerFactory.close();
    }
}
