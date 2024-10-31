package com.example.categoryproduct.controller;

import com.example.categoryproduct.formbean.formCategory;
import com.example.categoryproduct.model.Category;
import com.example.categoryproduct.service.Impl.DAOCategooryServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "categoryServlet", value = "/category-servlet")
public class CategoryController extends HttpServlet {
    private final DAOCategooryServiceImpl daoCategoryService = new DAOCategooryServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("actionKey");

        if (action == null || action.equals("list")) {
            request.getSession().setAttribute("categories", daoCategoryService.getAll());
            request.getRequestDispatcher("/WEB-INF/category.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            long id = Long.parseLong(request.getParameter("id"));
            Optional<Category> category = daoCategoryService.get(id);
            if (category.isPresent()) {
                formCategory form = new formCategory();
                form.setId(category.get().getId());
                form.setNom(category.get().getNom());
                form.setDescription(category.get().getDescription());
                request.getSession().setAttribute("category", form);
                request.getRequestDispatcher("/WEB-INF/category.jsp").forward(request, response);
            }
        } else if(action.equals("filter")) {
            String filter = request.getParameter("filter");
            request.getSession().setAttribute("categories",
                    daoCategoryService.getAll().stream()
                            .filter(category -> category.getNom().toLowerCase().contains(filter.toLowerCase()))
                            .toList());
            request.getRequestDispatcher("/WEB-INF/category.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("actionKey");
        formCategory form = new formCategory();
        form.setId(request.getParameter("id") != null && !request.getParameter("id").isEmpty() ?
                Long.parseLong(request.getParameter("id")) : 0);
        form.setNom(request.getParameter("nom"));
        form.setDescription(request.getParameter("description"));

        Category category = new Category();
        category.setNom(form.getNom());
        category.setDescription(form.getDescription());

        if (action.equals("add")) {
            daoCategoryService.save(category);
        } else if (action.equals("update")) {
            daoCategoryService.update(form.getId(), category);
        } else if (action.equals("delete")) {
            daoCategoryService.delete(form.getId());
        }

        response.sendRedirect(request.getContextPath() + "/category-servlet");
    }
}