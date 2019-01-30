package cn.hellohao.pojo;

public class User {
	private Integer id;
	private String username;
	private String password;
	private String email;
	private String birthder;
	private Integer level;
	
	
	public User() {
		super();
	}


	public User(Integer id, String username, String password, String email, String birthder, Integer level,
			Integer keyId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.birthder = birthder;
		this.level = level;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
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


	public String getBirthder() {
		return birthder;
	}


	public void setBirthder(String birthder) {
		this.birthder = birthder;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}

	
	
	
}
