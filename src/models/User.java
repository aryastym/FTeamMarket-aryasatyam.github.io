package models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectors.Connector;

public class User {
	private String id;
	private String username;
	private String password;
	private String email;
	private String gender;
	private String phone;
	private int age;
	private String role;
	
	public User(String id, String username, String password, String email, String gender, String phone, int age,
			String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
		this.age = age;
		this.role = role;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean insert() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("INSERT INTO user (userID, username, password, gender, email, phoneNumber, age, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			s.setString(1, this.id);
			s.setString(2, this.username);
			s.setString(3,  this.password);
			s.setString(4, this.gender);
			s.setString(5,  this.email);
			s.setString(6,  this.phone);
			s.setInt(7, this.age);
			s.setString(8, this.role);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public boolean update() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("UPDATE user SET email = ?, phoneNumber = ?, gender = ?, age = ? WHERE userID = ?");
			s.setString(1, this.email);
			s.setString(2, this.phone);
			s.setString(3, this.gender);
			s.setInt(4, this.age);
			s.setString(5, this.id);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public boolean delete() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("DELETE FROM user WHERE userID = ?");
			s.setString(1, this.id);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public static String getNextId() {
		int idNum = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("SELECT userID FROM user WHERE userID IN (SELECT MAX(userID) FROM user)");
			
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				idNum = Integer.parseInt(rs.getString("userID").substring(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String id = String.format("US%03d", idNum+1);
		
		return id;
	}
	
	public static User auth(String uname, String pwd){
		User u = null;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
			s.setString(1, uname);
			s.setString(2, pwd);
			
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				String userid = rs.getString("userID");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String gender = rs.getString("gender");
				String role = rs.getString("role");
				String email = rs.getString("email");
				String phone = rs.getString("phoneNumber");
				int age = rs.getInt("age");
				
				u = new User(userid, username, password, email, gender, phone, age, role);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return u;
	}
	
	public static ArrayList<User> getAll(boolean includeAdmin){
		ArrayList<User> arr = new ArrayList<>();
		
		try {
			
			PreparedStatement s = Connector.conn.prepareStatement("SELECT * FROM user" + (!includeAdmin ? " WHERE role = 'user'" : ""));
			
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				String userid = rs.getString("userID");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String gender = rs.getString("gender");
				String role = rs.getString("role");
				String email = rs.getString("email");
				String phone = rs.getString("phoneNumber");
				int age = rs.getInt("age");
				
				arr.add(new User(userid, username, password, email, gender, phone, age, role));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arr;
	}
	
}
