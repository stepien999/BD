package com.bd.serwis.services;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.bd.serwis.connection.ConnectionManager;
import com.bd.serwis.model.Category;

public class CategoryService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5237146449932629675L;
	private ConnectionManager conn = new ConnectionManager();
	private Category category;
	private List<Category> categoriesList;

	private final static String CREATE_CATEGORY_QUERY = "INSERT INTO Category (ParentId,CategoryName,Description)"
			+ "VALUES (?,?,?)";

	private final static String FIND_ALL_CATEGORIES_QUERY = "SELECT * FROM Category";

	private final static String FIND_PARENT_CATEGORIES_QUERY = "SELECT * FROM Category "
			+ "WHERE ParentId IS NULL" + " ORDER BY CategoryName";

	private final static String FIND_CATEGORIES_BY_PARENT_QUERY = "SELECT * FROM Category "
			+ "WHERE ParentId=?" + " ORDER BY CategoryName";

	private final static String UPDATE_CATEGORY_QUERY = "UPDATE Category"
			+ " SET ParentId=?, CategoryName=?, Description= ?"
			+ " WHERE CategoryId=?";

	private final static String FIND_CATEGORY_BY_NAME = "SELECT * FROM Category WHERE CategoryName = ?";

	private final static String DELETE_CATEGORY_AND_CHILD_CATEGORIES_QUERY = "DELETE FROM Category "
			+ "WHERE CategoryId=? OR ParentId=CategoryId";

	private final static String DELETE_ANNOUNCEMENT_QUERY = "DELETE FROM Announcement WHERE CategoryId = ?";

	private final static String FIND_CATEGORY_BY_ID_QUERY = "SELECT * FROM Category WHERE CategoryId = ?";

	public List<Category> findAllCategories() {
		categoriesList = new ArrayList<Category>();
		Statement stmt;
		ResultSet rs;

		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();

			rs = stmt.executeQuery(FIND_ALL_CATEGORIES_QUERY);

			while (rs.next()) {
				category = new Category();
				category.setCategoryId(rs.getInt("CategoryId"));
				category.setParent(findCategoryById(rs.getInt("ParentId")));
				category.setCategoryName(rs.getString("CategoryName"));
				category.setDescription(rs.getString("Description"));
				categoriesList.add(category);
			}
			stmt.close();
			rs.close();
			conn.close();

			return categoriesList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoriesList;
	}

	public Category findCategoryById(int categoryId) {
		Category category = new Category();
		PreparedStatement pstmt;
		ResultSet rs;
		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_CATEGORY_BY_ID_QUERY);
			pstmt.setInt(1, categoryId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				category = new Category();
				category.setCategoryId(rs.getInt("CategoryId"));
				category.setParent(findCategoryById(rs.getInt("ParentId")));
				category.setCategoryName(rs.getString("CategoryName"));
				category.setDescription(rs.getString("Description"));
			}
			pstmt.close();
			rs.close();
			return category;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return category;
	}

	public List<Category> findCategoriesByParentCategory(Category parentCategory) {
		if (parentCategory == null) {
			return new ArrayList<Category>();
		}

		categoriesList = new ArrayList<Category>();
		PreparedStatement pstmt;
		ResultSet rs;
		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_CATEGORIES_BY_PARENT_QUERY);
			pstmt.setInt(1, parentCategory.getCategoryId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				category = new Category();
				category.setCategoryId(rs.getInt("CategoryId"));
				category.setParent(findCategoryById(rs.getInt("ParentId")));
				category.setCategoryName(rs.getString("CategoryName"));
				category.setDescription(rs.getString("Description"));
				categoriesList.add(category);
			}
			pstmt.close();
			rs.close();
			conn.close();

			return categoriesList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoriesList;
	}

	public List<Category> findParentCategories() {
		Statement stmt;
		ResultSet rs;
		categoriesList = new ArrayList<Category>();
		Category cat;
		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();

			rs = stmt.executeQuery(FIND_PARENT_CATEGORIES_QUERY);

			while (rs.next()) {
				category = new Category();
				category.setCategoryId(rs.getInt("CategoryId"));
				cat = findCategoryById(rs.getInt("ParentId"));
				if (cat == null) {
					category.setParent(null);
				} else {
					category.setParent(cat);
				}
				category.setCategoryName(rs.getString("CategoryName"));
				category.setDescription(rs.getString("Description"));
				categoriesList.add(category);
			}
			stmt.close();
			rs.close();
			conn.close();

			return categoriesList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoriesList;
	}

	public void createCategory(Category category) {

		if (categoryExists(category)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Kategoria o podanej nazwie ju¿ istnieje."));
			return;
		}

		PreparedStatement pstmt;
		conn.connect();
		try {

			/*
			 * if (category.getParent().getCategoryId() != null) { FacesContext
			 * context = FacesContext.getCurrentInstance();
			 * context.addMessage(null, new FacesMessage(
			 * "Podkategoria nie mo¿e mieæ kategorii")); return; }
			 */
			pstmt = conn.getConnection()
					.prepareStatement(CREATE_CATEGORY_QUERY);

			if (category.getParent().getCategoryId() == null) {
				pstmt.setNull(1, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(1, category.getParent().getCategoryId());
			}
			pstmt.setString(2, category.getCategoryName());
			pstmt.setString(3, category.getDescription());

			pstmt.executeUpdate();

			pstmt.close();
			conn.getConnection().close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Kategoria zosta³a dodana"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean categoryExists(Category category) {
		PreparedStatement pstmt;
		ResultSet rs;
		conn.connect();
		try {
			pstmt = conn.getConnection()
					.prepareStatement(FIND_CATEGORY_BY_NAME);
			pstmt.setString(1, category.getCategoryName());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (category.getCategoryId() == rs.getInt("CategoryId")) {
					pstmt.close();
					rs.close();
					conn.close();
					return false;
				}
				pstmt.close();
				rs.close();
				conn.close();
				return true;
			} else {
				pstmt.close();
				rs.close();
				conn.close();
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void updateCategory(Category category) {

		if (categoryExists(category)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Kategoria o podanej nazwie ju¿ istnieje."));
			return;
		}

		PreparedStatement pstmt;
		try {
			conn.connect();

			pstmt = conn.getConnection()
					.prepareStatement(UPDATE_CATEGORY_QUERY);
			if (category.getParent().getCategoryId() == null) {
				pstmt.setNull(1, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(1, category.getParent().getCategoryId());
			}

			pstmt.setString(2, category.getCategoryName());
			pstmt.setString(3, category.getDescription());
			pstmt.setInt(4, category.getCategoryId());
			pstmt.execute();

			pstmt.close();
			conn.getConnection().close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Kategoria zosta³a zaktualizowana "));

		} catch (SQLException e) {
			System.out.println("B³¹d sql");
			e.printStackTrace();
		}
	}

	public void deleteCategory(int categoryId) {
		PreparedStatement pstmt;
		try {
			conn.connect();

			pstmt = conn.getConnection().prepareStatement(
					DELETE_ANNOUNCEMENT_QUERY);

			pstmt.setInt(1, categoryId);
			pstmt.execute();

			pstmt = conn.getConnection().prepareStatement(
					DELETE_CATEGORY_AND_CHILD_CATEGORIES_QUERY);

			pstmt.setInt(1, categoryId);
			pstmt.execute();

			pstmt.close();
			conn.getConnection().close();

		} catch (SQLException e) {
			System.out.println("B³¹d sql");
			e.printStackTrace();
		}
	}
}
