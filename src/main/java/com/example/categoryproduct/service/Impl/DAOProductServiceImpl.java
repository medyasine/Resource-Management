package com.example.categoryproduct.service.Impl;

import com.example.categoryproduct.model.Category;
import com.example.categoryproduct.model.Product;
import com.example.categoryproduct.service.DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class DAOProductServiceImpl  implements DAO<Product> {
    EntityManager entityManager = Persistence.createEntityManagerFactory("connexionDB").createEntityManager();
    DAOCategooryServiceImpl categoryService = new DAOCategooryServiceImpl();

    @Override
    public Optional<Product> get(Long id) {
        Product Product = null;
        try{
            Product = entityManager.find(Product.class, id);
        }catch(Exception e){
            System.out.println("Error while adding Product: " + e.getMessage());
        }
        return Optional.ofNullable(Product);
    }

    @Override
    public List<Product> getAll() {
        List<Product> listProduct = null;
        try{
            listProduct = entityManager.createQuery("SELECT p FROM Product p").getResultList();
        }catch(Exception e){
            System.out.println("Error while getting all Products: " + e.getMessage());
        }
        return listProduct;
    }

    @Override
    public int save(Product object) {
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(object);
            entityManager.getTransaction().commit();
            return 1;
        }catch(Exception e){
            System.out.println("Error while adding Product: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return 0;
    }

    @Override
    public Optional<Product> update(long id, Product object) {
        Optional<Product> Product = Optional.empty();
        try {
            Optional<Product> existingProduct = get(id);
            if (existingProduct.isPresent()) {
                entityManager.getTransaction().begin();
                existingProduct.get().setNom(object.getNom());
                existingProduct.get().setDescription(object.getDescription());
                existingProduct.get().setPrix(object.getPrix());
                existingProduct.get().setQuantite(object.getQuantite());
                existingProduct.get().setCategory(object.getCategory());
                existingProduct.get().setSelected(object.isSelected());
                Category ct = categoryService.get(object.getCategory().getId()).get();
                existingProduct.get().setCategory(ct);
                entityManager.merge(existingProduct.get());
                entityManager.getTransaction().commit();
                Product = Optional.of(existingProduct.get());
            }
        }catch(Exception e){
            System.out.println("Error while updating category: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return Product;
    }

    @Override
    public int delete(long id) {
        try{
            Optional<Product> Product = get(id);
            if(Product.isPresent()){
                entityManager.getTransaction().begin();
                entityManager.remove(Product.get());
                entityManager.getTransaction().commit();
                return 1;
            }
        }catch (Exception e){
            System.out.println("Error while deleting Product: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return 0;
    }
}
