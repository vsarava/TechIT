package techit.model.dao;

import java.util.List;

import techit.model.Ticket;

public interface TicketDao {

	Ticket getTicket(Long id);
	
	List<Ticket> getTickets();
	
	Ticket saveTicket(Ticket ticket);
	
}
