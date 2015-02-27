package com.bd.serwis.services;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.bd.serwis.connection.ConnectionManager;
import com.bd.serwis.model.User;
import com.bd.serwis.security.PasswordEncryptor;

public class UserService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2133527622703996277L;
	private ConnectionManager conn = new ConnectionManager();
	private List<User> usersList;
	private User user;

	private final static String AUTH_KEY_USER = "user.logged";
	private final static String AUTH_KEY_ADMIN = "admin.logged";

	private final static String CREATE_USER_QUERY = "INSERT INTO User (Email,Password,FirstName,LastName,City,PhoneNumber,IsAdmin)"
			+ "VALUES (?,?,?,?,?,?,?)";

	private final static String FIND_USER_BY_ID_QUERY = "SELECT * FROM User WHERE UserId=?";

	private final static String FIND_ALL_USERS_QUERY = "SELECT * FROM User";

	private final static String FIND_USER_BY_EMAIL_QUERY = "SELECT * FROM User WHERE Email=?";

	private final static String DELETE_USER_QUERY = "DELETE FROM User WHERE UserId=?";

	private final static String UPDATE_USER_QUERY = "UPDATE User SET Password=?, FirstName=?, LastName=?, City=?, PhoneNumber=?, isAdmin=?  WHERE UserId=?";

	public List<User> findAllUsers() {
		usersList = new ArrayList<User>();
		Statement stmt;
		ResultSet rs;
		conn.connect();
		try {
			stmt = conn.getConnection().createStatement();
			rs = stmt.executeQuery(FIND_ALL_USERS_QUERY);

			while (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("UserId"));
				user.setEmail(rs.getString("Email"));
				user.setPassword(rs.getString("Password"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setCity(rs.getString("City"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setAdmin(rs.getByte("IsAdmin"));
				usersList.add(user);
			}
			stmt.close();
			rs.close();
			conn.close();

			return usersList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usersList;
	}

	public User findUserById(int userId) {
		User user = new User();
		ResultSet rs;
		PreparedStatement pstmt;
		conn.connect();
		try {
			pstmt = conn.getConnection()
					.prepareStatement(FIND_USER_BY_ID_QUERY);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("UserId"));
				user.setEmail(rs.getString("Email"));
				user.setPassword(rs.getString("Password"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setCity(rs.getString("City"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setAdmin(rs.getByte("IsAdmin"));
			}
			pstmt.close();
			rs.close();
			return user;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}

	public User findUserByEmail(String email) {
		User user = new User();
		PreparedStatement pstmt;
		ResultSet rs;
		conn.connect();

		try {
			pstmt = conn.getConnection().prepareStatement(
					FIND_USER_BY_EMAIL_QUERY);

			pstmt.setString(1, email);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("UserId"));
				user.setEmail(rs.getString("Email"));
				user.setPassword(rs.getString("Password"));
				user.setFirstName(rs.getString("FirstName"));
				user.setLastName(rs.getString("LastName"));
				user.setCity(rs.getString("City"));
				user.setPhoneNumber(rs.getString("PhoneNumber"));
				user.setAdmin(rs.getByte("IsAdmin"));
				return user;
			}

			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return user;
	}

	public boolean isUserLoggedIn() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(AUTH_KEY_USER) != null
				|| FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().get(AUTH_KEY_ADMIN) != null)
			return true;
		else
			return false;
	}

	public boolean isAdminLoggedIn() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(AUTH_KEY_ADMIN) != null;
	}

	private boolean isUserRegistered(User user) {

		if (findUserByEmail(user.getEmail()).getUserId() == null)
			return false;
		else
			return true;
	}

	public boolean logIn(User user) throws NoSuchAlgorithmException {
		User verify = findUserByEmail(user.getEmail());
		if (verify.getPassword() == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Nieprawid³owe dane do logowania. "));
			return false;
		} else {
			if (verify.getPassword().equals(
					PasswordEncryptor.encrypt(user.getPassword()))) {

				if (verify.isAdmin() == 1) {
					FacesContext.getCurrentInstance().getExternalContext()
							.getSessionMap()
							.put(AUTH_KEY_ADMIN, user.getEmail());
					FacesContext.getCurrentInstance().getExternalContext()
							.getSessionMap()
							.put(AUTH_KEY_USER, user.getEmail());

				} else {
					FacesContext.getCurrentInstance().getExternalContext()
							.getSessionMap()
							.put(AUTH_KEY_USER, user.getEmail());
				}

				return true;
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						"Nieprawid³owe dane do logowania."));
				return false;
			}
		}
	}

	public boolean logOut(User user) {

		if (user.isAdmin() == 1) {
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(AUTH_KEY_ADMIN);
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(AUTH_KEY_USER);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession httpSession = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			httpSession.invalidate();

			return true;
		} else {
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(AUTH_KEY_USER);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession httpSession = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			httpSession.invalidate();

			return true;
		}

	}

	public void createUser(User user) throws NoSuchAlgorithmException {

		if (isUserRegistered(user)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Podany email jest ju¿ u¿ywany przez innego u¿ytkownika"));
			return;
		}

		PreparedStatement pstmt;
		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(CREATE_USER_QUERY);
			pstmt.setString(1, user.getEmail());
			pstmt.setString(2, PasswordEncryptor.encrypt(user.getPassword()));
			pstmt.setString(3, user.getFirstName());
			pstmt.setString(4, user.getLastName());
			pstmt.setString(5, user.getCity());
			pstmt.setString(6, user.getPhoneNumber());
			pstmt.setByte(7, user.isAdmin());

			pstmt.executeUpdate();

			pstmt.close();
			conn.getConnection().close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Konto zosta³o zarejestrowane. Mo¿esz siê ju¿ zalogowaæ"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(User user) throws NoSuchAlgorithmException {
		PreparedStatement pstmt;
		conn.connect();
		try {
			pstmt = conn.getConnection().prepareStatement(UPDATE_USER_QUERY);
			pstmt.setString(1, PasswordEncryptor.encrypt(user.getPassword()));
			pstmt.setString(2, user.getFirstName());
			pstmt.setString(3, user.getLastName());
			pstmt.setString(4, user.getCity());
			pstmt.setString(5, user.getPhoneNumber());
			pstmt.setByte(6, user.isAdmin());
			pstmt.setInt(7, user.getUserId());

			pstmt.executeUpdate();

			pstmt.close();
			conn.getConnection().close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"Dane zosta³y zaktualizowane"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteUser(User user) {
		PreparedStatement pstmt;

		try {
			conn.connect();

			pstmt = conn.getConnection().prepareStatement(DELETE_USER_QUERY);

			pstmt.setInt(1, user.getUserId());
			pstmt.execute();

			pstmt.close();
			conn.close();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"U¿ytkownik zosta³ usuniêty"));

		} catch (SQLException e) {
			System.out.println("B³¹d sql");
			e.printStackTrace();
		}
	}
}
