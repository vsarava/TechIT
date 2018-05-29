package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;							// User's unique id.

	@Column(nullable = false, unique = true)
	private String username;					// Username of the user.

	@JsonProperty(access = Access.WRITE_ONLY)
	@Transient
	private String password;					// Password of the user.

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String hash;
	
	@Column(name = "first_name", nullable = false)
	private String firstName; 				// User's first name

	@Column(name = "last_name", nullable = false)
	private String lastName; 				// User's last name

	private String email;					// Email of the user.
	
	private boolean enabled = true;
	
	private String phone;					// Phone number of the user.
	
	@Enumerated(EnumType.STRING)
	private Position post;					// Position of the user.
	
    private String department;
	
    @JsonIgnore
    @OneToMany(mappedBy="requester")
	private List<Ticket> ticketsRequested;	// Tickets created by the user.
	
    @JsonIgnore
	@ManyToMany(mappedBy="technicians")
	private List<Ticket> ticketsAssigned;	// Tickets assigned to the technicians
	
    @JsonIgnore
	@ManyToOne
	private Unit unit;				// Unit that this user belongs to.
	
	// Types of users on the system.
	public enum Position {
		SYS_ADMIN, SUPERVISING_TECHNICIAN, TECHNICIAN, USER
	}

	// Default Constructor.
	public User() {
		post = Position.USER;
		ticketsRequested = new ArrayList<Ticket>();
		ticketsAssigned = new ArrayList<Ticket>();
	} 
	
	// Simple constructor for regular users ( students )
	public User(String firstname, String lastname, String username, String password) {
		this();
		this.firstName = firstname;
		this.lastName = lastname;
		this.username = username;
		this.password = password;
	}

	// Use unitId instead of unit in serialization
    public Long getUUnitId()
    {
        return unit != null ? unit.getId() : null;
    }
    
    public void setUUnitId(Long id) {
    	unit = new Unit();
    	unit.setId(id);
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Position getPost() {
		return post;
	}

	public void setPost(Position post) {
		this.post = post;
	}

	public List<Ticket> getTicketsRequested() {
		return ticketsRequested;
	}

	public void setTicketsRequested(List<Ticket> ticketsRequested) {
		this.ticketsRequested = ticketsRequested;
	}

	public List<Ticket> getTicketsAssigned() {
		return ticketsAssigned;
	}

	public void setTicketsAssigned(List<Ticket> ticketsAssigned) {
		this.ticketsAssigned = ticketsAssigned;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(getClass() != obj.getClass())
			return false;
		
		return id.equals(((User)obj).id);
	}

	@Override
	public String toString() {
		return "First name: " + getFirstName() + "\n" + "Last name:" + getLastName() + "\n" + "User name:"
				+ getUsername() + "\n" + "Phone number:" + getPhone() + "\n" + "Email:" + getEmail() + "\n";
	}

}
