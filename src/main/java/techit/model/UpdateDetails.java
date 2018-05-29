package techit.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "updates")
public class UpdateDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    private Long id;				// Updates unique id.
	
	@ManyToOne
	private User modifier; 			// modifier's username.
	
	@Column(name = "details")
	private String updateDetails;	// Details of updates.
	
	@Column(name = "date")
	private Date modifiedDate;		// Date on which the update was made.
	
	@ManyToOne
	private Ticket ticket;			// Ticket to which the update has been made.
	
	//Default Constructor.
	public UpdateDetails() {
		
	}
	
	// Constructor with all the fields.
	public UpdateDetails(Long id, User modifier, String updateDetails, String modifiedDate, Ticket ticket) {
		super();
		this.modifier = modifier;
		this.updateDetails = updateDetails;
		this.modifiedDate = new Date();
		this.ticket = ticket;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	public String getUpdateDetails() {
		return updateDetails;
	}

	public void setUpdateDetails(String updateDetails) {
		this.updateDetails = updateDetails;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UpdateDetails [id=" + id + ", modifier=" + modifier + ", updateDetails=" + updateDetails
				+ ", modifiedDate=" + modifiedDate + ", ticket=" + ticket + "]";
	}
	
}
