package techit.rest.controller;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.UpdateDetails;
import techit.model.User;
import techit.model.dao.TicketDao;
import techit.model.dao.UserDao;
import techit.model.dao.UpdateDetailsDao;
import techit.rest.error.RestException;


@RestController
public class TicketController {

	@Autowired
	private TicketDao ticketDao;
	
  
  @Autowired
  private UpdateDetailsDao updateDao;
	
  @Autowired
	private UserDao userDao;
	
	@RequestMapping(value = "/tickets", method = RequestMethod.GET)
	public List<Ticket> getTickets(@ModelAttribute("currentUser") User currentUser) {
		if (currentUser.getPost() == User.Position.SYS_ADMIN || currentUser.getPost() == User.Position.SUPERVISING_TECHNICIAN)
			return ticketDao.getTickets();

		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/tickets", method = RequestMethod.POST)
	public Ticket addTicket(@RequestBody Ticket ticket,@ModelAttribute("currentUser") User currentUser) {
		
		ticket.setRequester(currentUser);
		
		return ticketDao.saveTicket(ticket);
	}
	
	@RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.GET)
	public Ticket getTicket(@ModelAttribute("currentUser") User currentUser,@PathVariable Long ticketId) {
		Ticket ticket =ticketDao.getTicket(ticketId);
		if(ticket==null)
			throw new RestException(400, "No such Ticket found");
		
		if(ticket.getRequester().getId()==currentUser.getId() 
				|| currentUser.getPost() == User.Position.SYS_ADMIN 
				|| currentUser.getPost() == User.Position.SUPERVISING_TECHNICIAN
				|| (currentUser.getPost() == User.Position.TECHNICIAN && ticket.getTechnicians().contains(currentUser)))
			return ticket;

		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.PUT)
	public Ticket editTicket(@PathVariable Long ticketId, @RequestBody Ticket ticket,
			@ModelAttribute("currentUser") User currentUser) {
		
		Ticket originalTicket = ticketDao.getTicket(ticketId);
		
		if(originalTicket==null)
			throw new RestException(400, "No such Ticket found");
		
		if(originalTicket.getRequester().getId() == currentUser.getId()
				|| currentUser.getPost() == User.Position.SYS_ADMIN 
				|| currentUser.getPost() == User.Position.SUPERVISING_TECHNICIAN) {
			
			ticket.setId(ticketId);
			ticket.setRequester(currentUser);
			return ticketDao.saveTicket(ticket);
		}
			
		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/tickets/{ticketId}/technicians", method = RequestMethod.GET)
	public List<User> getTicketTechnicians(@PathVariable Long ticketId,@ModelAttribute("currentUser") User currentUser) {
		
		Ticket ticket=ticketDao.getTicket(ticketId);
		
		if(ticket==null)
			throw new RestException(400, "No such Ticket found");
		
		if(currentUser.getPost() == User.Position.SYS_ADMIN 
				|| (currentUser.getPost() == User.Position.SUPERVISING_TECHNICIAN && ticket.getUnit().getId() == currentUser.getUUnitId()))
			return ticket.getTechnicians();

		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/tickets/{ticketId}/technicians/{userId}", method = RequestMethod.PUT)
	public Ticket assignTechnician(@PathVariable Long ticketId,@PathVariable Long userId,@ModelAttribute("currentUser") User currentUser) {
		User user=userDao.getUser(userId);
		if((currentUser.getPost() != User.Position.SYS_ADMIN
				&& (currentUser.getPost() != User.Position.SUPERVISING_TECHNICIAN || currentUser.getUnit().getId() != user.getUnit().getId()))
				|| (user.getPost()!=User.Position.TECHNICIAN && user.getPost()!=User.Position.SUPERVISING_TECHNICIAN)) {
			
			throw new RestException(403, "Unauthorized Access");
		}
		Ticket ticket=ticketDao.getTicket(ticketId);
		

			if(ticket.getTechnicians().contains(user)) {
				throw new RestException(400, "Bad Request");

			}
		ticket.getTechnicians().add(user);
		ticket.setId(ticketId);
		return ticketDao.saveTicket(ticket);
		
	}
  @RequestMapping(value= "/tickets/{ticketId}/status/{status}", method = RequestMethod.PUT)
    public Ticket setStatus(@ModelAttribute("currentUser") User user, @PathVariable Long ticketId, @RequestBody String description, @PathVariable String status) {
        if (user.getPost()!= User.Position.SYS_ADMIN && user.getPost()!= User.Position.SUPERVISING_TECHNICIAN
                && user.getPost()!= User.Position.TECHNICIAN){
            throw new RestException(403, "Unauthorized Access");
        }
        Ticket ticket = ticketDao.getTicket(ticketId);
        if (user.getPost() == User.Position.TECHNICIAN && !user.getTicketsAssigned().contains(ticket)) {
            throw new RestException(403, "Unauthorized Access, You have no permissions for this action");
        }
        ticket.setProgress(Ticket.Progress.valueOf(status));
        UpdateDetails update = new UpdateDetails();
        update.setModifier(user);
        update.setModifiedDate(new Date());
        update.setTicket(ticket);
        update.setUpdateDetails(description);
        update = updateDao.saveUpdate(update);

        if (update != null)
        return ticketDao.saveTicket(ticket);
        else
            throw new RestException(400, "An error occured");
    }

    @RequestMapping(value= "/tickets/{ticketId}/priority/{priority}", method = RequestMethod.PUT)
    public Ticket setPriority(@ModelAttribute("currentUser") User user, @PathVariable Long ticketId, @PathVariable String priority) {
        if (user.getPost()!= User.Position.SYS_ADMIN && user.getPost()!= User.Position.SUPERVISING_TECHNICIAN
                && user.getPost()!= User.Position.TECHNICIAN){
            throw new RestException(403, "Unauthorized Access");
        }
        Ticket ticket = ticketDao.getTicket(ticketId);
        if (user.getPost() == User.Position.TECHNICIAN && !user.getTicketsAssigned().contains(ticket)) {
            throw new RestException(403, "Unauthorized Access, You have no permissions for this action");
        }
        ticket.setPriority(Ticket.Priority.valueOf(priority));
        return ticketDao.saveTicket(ticket);

    }

    @RequestMapping(value = "/tickets/{ticketid}/updates", method = RequestMethod.POST)
    public UpdateDetails createTicketUpdate(@ModelAttribute("currentUser") User user,
                                            @PathVariable Long ticketid, @RequestBody UpdateDetails update){
        if (user.getPost()!= User.Position.SYS_ADMIN && user.getPost()!= User.Position.SUPERVISING_TECHNICIAN
                && user.getPost()!= User.Position.TECHNICIAN){
            throw new RestException(403, "Unauthorized Access");
        }
        Ticket ticket = ticketDao.getTicket(ticketid);
        if (user.getPost() == User.Position.TECHNICIAN && !user.getTicketsAssigned().contains(ticket)) {
            throw new RestException(403, "Unauthorized Access, You Have no permissions for this action");
        }
        update.setModifier(user);
        update.setModifiedDate(new Date());
        update.setTicket(ticket);

        return updateDao.saveUpdate(update);

    }
}
