package com.example.categoryproduct.service.Impl;

import com.example.categoryproduct.model.Category;
import com.example.categoryproduct.service.DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class DAOCategooryServiceImpl implements DAO<Category> {
    EntityManager entityManager = Persistence.createEntityManagerFactory("connexionDB").createEntityManager();

    @Override
    public Optional<Category> get(Long id) {
        Category category = null;
        try{
            category = entityManager.find(Category.class, id);
        }catch(Exception e){
            System.out.println("Error while adding category: " + e.getMessage());
        }
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> getAll() {
        List<Category> listCategory = null;
        try{
            listCategory = entityManager.createQuery("SELECT c FROM Category c").getResultList();
        }catch(Exception e){
            System.out.println("Error while getting all categories: " + e.getMessage());
        }
        return listCategory;
    }

    @Override
    public int save(Category object) {
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(object);
            entityManager.getTransaction().commit();
            return 1;
        }catch(Exception e){
            System.out.println("Error while adding category: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return 0;
    }

    @Override
    public Optional<Category> update(long id, Category object) {
        Optional<Category> category = Optional.empty();
        try {
            Optional<Category> existingCategory = get(id);
            if (existingCategory.isPresent()) {
                entityManager.getTransaction().begin();
                existingCategory.get().setNom(object.getNom());
                existingCategory.get().setDescription(object.getDescription());
                entityManager.merge(existingCategory.get());
                entityManager.getTransaction().commit();
                category = Optional.of(existingCategory.get());
            }
        }catch(Exception e){
            System.out.println("Error while updating category: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return category;
    }

    @Override
    public int delete(long id) {
        try{
            Optional<Category> category = get(id);
            if(category.isPresent()){
                entityManager.getTransaction().begin();
                entityManager.remove(category.get());
                entityManager.getTransaction().commit();
                return 1;
            }
        }catch (Exception e){
            System.out.println("Error while deleting category: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return 0;
    }
}
