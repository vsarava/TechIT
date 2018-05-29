package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    private Long id;						// Ticket's unique id.
	
	private String subject;				// Subject of the ticket.
	
	private String details;				// Text concerning the project.
	
	@Column(name = "start_date")
	private Date startDate;				// Project's starting date.
	
	@Column(name = "end_date")
	private Date endDate; 				// When the project was completed.
	
	@Column(name = "update_date")
	private Date lastUpdated;			// last date where changes were made to the ticket (edits, technician updates, etc..)
	
	private String location;				// Location where the project is.
	
	@JsonIgnore
	@OneToMany(mappedBy="ticket")
	private List<UpdateDetails> updates;	// List of all updates that was made to the ticket.
	
	@Column(name = "completion_details")
	private String completionDetails;	// Information pertaining vendors, cost, materials used.
	
	@Enumerated(EnumType.STRING)
	private Progress progress;			// Current progress of the ticket
	
	@Enumerated(EnumType.STRING)
	private Priority priority;			// Importance or level of urgency of the ticket
	
	@JsonIgnore
	@ManyToOne
	private Unit unit;
	
	@JsonIgnore
	@ManyToOne
	private User requester;
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "assignments",
		joinColumns = @JoinColumn(name = "ticket_id"),
		inverseJoinColumns = @JoinColumn(name = "technician_id"))
	private List<User> technicians;
	
	public enum Progress { OPEN, IN_PROGRESS, ON_HOLD, COMPLETED, CLOSED }
	public enum Priority { NOT_ASSIGNED, LOW, MEDIUM, HIGH }
	
	// Default Constructor.
	public Ticket() {
		technicians =  new ArrayList<User>();
		updates = new ArrayList<UpdateDetails>();
		progress = Progress.OPEN;
		priority = Priority.NOT_ASSIGNED;
		startDate = new Date();
	}

	// Constructor with all the fields.
	public Ticket(String subject, String details, String location,Unit unit, User requesterDetails) {
		this();
		this.subject = subject;
		this.details = details;
		this.location = location;
		this.unit = unit;
		this.requester = requesterDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<UpdateDetails> getUpdates() {
		return updates;
	}

	public void setUpdates(List<UpdateDetails> updates) {
		this.updates = updates;
	}

	public String getCompletionDetails() {
		return completionDetails;
	}

	public void setCompletionDetails(String completionDetails) {
		this.completionDetails = completionDetails;
	}

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requesterDetails) {
		this.requester = requesterDetails;
	}

	public List<User> getTechnicians() {
		return technicians;
	}

	public void setTechnicians(List<User> technicians) {
		this.technicians = technicians;
	}

	@Override
	public String toString() {
		return "Ticket [subject=" + subject + ", details=" + details + ", startDate=" + startDate + ", endDate="
				+ endDate + ", lastUpdated=" + lastUpdated + ", location=" + location + ", completionDetails="
				+ completionDetails + ", progress=" + progress + ", priority=" + priority + ", unit=" + unit
				+ ", requesterDetails=" + requester + "]";
	}

	
}
