package techit.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import techit.model.Ticket;

@Test(groups = "TicketDaoTest")
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TicketDaoTest extends AbstractTransactionalTestNGSpringContextTests{
	@Autowired
	TicketDao ticketDao;
	@Autowired
	UserDao userDao;
	
	@Test
	void getTicket() {
		assert ticketDao.getTicket(1L).getId()!=null;
	}
	
	@Test
	void getTickets() {
		assert ticketDao.getTickets().size()>=2;
	}
	
	@Test
	void saveTicket() {
		Ticket t=new Ticket();
		t.setSubject("PC error");
		t=ticketDao.saveTicket(t);
		assert t.getId()!=null;
	}
}
