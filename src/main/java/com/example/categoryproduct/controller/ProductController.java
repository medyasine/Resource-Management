package com.example.categoryproduct.controller;

import com.example.categoryproduct.formbean.formCategory;
import com.example.categoryproduct.formbean.formProduct;
import com.example.categoryproduct.model.Product;
import com.example.categoryproduct.service.Impl.DAOCategooryServiceImpl;
import com.example.categoryproduct.service.Impl.DAOProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@WebServlet(name = "productServlet", value = "/product-servlet")
@MultipartConfig
public class ProductController extends HttpServlet {
    private final DAOProductServiceImpl daoProductService = new DAOProductServiceImpl();
    private final DAOCategooryServiceImpl daoCategooryService = new DAOCategooryServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("actionKey");
        if (action == null || action.equals("list")) {
            System.out.println(request.getSession().getAttribute(""));
            formProduct form = new formProduct();
            form.setProducts(daoProductService.getAll());
            form.setCategories(daoCategooryService.getAll());
            request.getSession().setAttribute("bean", form);
            request.getRequestDispatcher("/WEB-INF/product.jsp").forward(request, response);
        }else if (action.equals("edit")) {
            long id = Long.parseLong(request.getParameter("id"));
            Optional<Product> product = daoProductService.get(id);
            if (product.isPresent()) {
                formProduct form = new formProduct();
                form.setId(product.get().getId());
                form.setNom(product.get().getNom());
                form.setDescription(product.get().getDescription());
                form.setPrix(product.get().getPrix());
                form.setQuantite(product.get().getQuantite());
                form.setSelected(product.get().isSelected());
                form.setCategory(product.get().getCategory());
                form.setProducts(daoProductService.getAll());
                form.setCategories(daoCategooryService.getAll());
                request.getSession().setAttribute("bean", form);
                request.getRequestDispatcher("/WEB-INF/product.jsp").forward(request, response);
            }
        }
        else if(action.equals("filter")){
            String filter = request.getParameter("filter");
            request.getSession().setAttribute("products", daoProductService.getAll().stream().filter(product -> product.getNom().equals(filter)).toList());
            request.getSession().setAttribute("categories", daoCategooryService.getAll());
            request.getRequestDispatcher("/WEB-INF/product.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("actionKey");
        formProduct form = new formProduct();
        form.setId(request.getParameter("id") != null && !request.getParameter("id").isEmpty() ? Long.parseLong(request.getParameter("id")) : 0);
        form.setNom(request.getParameter("nom"));
        form.setDescription(request.getParameter("description"));
        form.setPrix(Double.parseDouble(request.getParameter("prix")));
        form.setQuantite(Integer.parseInt(request.getParameter("quantite")));
        form.setSelected(request.getParameter("selected") != null);
        if (request.getParameter("categoryId") != null) {
            Long categoryId = Long.parseLong(request.getParameter("categoryId"));
            form.setCategory(daoCategooryService.get(categoryId).orElse(null));
        }

        // Handle the image upload
        Part filePart = request.getPart("image");
        String imagePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String imageFolder = getServletContext().getRealPath("/images");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            imagePath = imageFolder + File.separator + fileName;
            File imageFile = new File(imagePath);
            if (!imageFile.getParentFile().exists()) {
                imageFile.getParentFile().mkdirs();
            }
            Files.copy(filePart.getInputStream(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        Product product = new Product();
        product.setNom(form.getNom());
        product.setDescription(form.getDescription());
        product.setPrix(form.getPrix());
        product.setQuantite(form.getQuantite());
        product.setSelected(form.isSelected());
        product.setCategory(form.getCategory());
        product.setImagePath(imagePath);

        if (action.equals("add")) {
            daoProductService.save(product);
        } else if (action.equals("update")) {
            daoProductService.update(form.getId(), product);
        } else if (action.equals("delete")) {
            daoProductService.delete(form.getId());
        }
        form.setProducts(daoProductService.getAll());
        form.setCategories(daoCategooryService.getAll());
        request.getSession().setAttribute("bean", form);
        request.getRequestDispatcher("/WEB-INF/product.jsp").forward(request, response);
    }

}