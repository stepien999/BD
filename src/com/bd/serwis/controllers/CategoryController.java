package com.bd.serwis.controllers;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.bd.serwis.model.Category;
import com.bd.serwis.services.CategoryService;

@ManagedBean(name = "categoryController")
@SessionScoped
public class CategoryController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CategoryService categoryService = new CategoryService();
	private Category category;
	private Integer categoryId;

	public String createCategory() {
		category = new Category();
		return "redirect_category_creation";
	}

	public String saveCategory() {
		Category parent = new Category();
		parent.setCategoryId(categoryId);
		category.setParent(parent);
		categoryService.createCategory(category);
		return "success";
	}

	public String redirectCategoriesPage() {
		return "redirect_categories_page";
	}

	public String redirectManageCategories() {
		return "redirect_manage_categories";
	}

	public String editCategory(Category category) {
		this.category = category;
		return "redirect_edit_category";
	}

	public String updateCategory() {
		Category parent = new Category();
		parent.setCategoryId(categoryId);
		category.setParent(parent);
		categoryService.updateCategory(category);
		return "success";
	}

	public String deleteCategory(Category toDelete) {
		categoryService.deleteCategory(toDelete.getCategoryId());
		return "success";
	}

	public List<Category> categoriesByParent(Category category) {
		return categoryService.findCategoriesByParentCategory(category);
	}

	public List<Category> getAllCategories() {

		return categoryService.findAllCategories();
	}

	public List<Category> getParentCategories() {
		return categoryService.findParentCategories();
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public CategoryController() {

	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		if(categoryId == 1000000000){
			this.categoryId = null;
			return;
		}
		else if (categoryId == category.getCategoryId()){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Wybierz innego rodzica. Nie mo¿esz nim byæ dla siebie :)"));
			return;
		}
		this.categoryId = categoryId;
	}
}
