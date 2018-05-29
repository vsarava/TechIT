package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.dao.UnitDao;
import techit.rest.error.RestException;

@RestController
public class UnitController {

	@Autowired
	UnitDao unitDao;
	
	@RequestMapping(value = "/units", method = RequestMethod.GET)
	public List<Unit> getUnits( @ModelAttribute("currentUser") User currentUser ){
		if(currentUser.getPost() == User.Position.SYS_ADMIN)
			return unitDao.getUnits();
		
		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/units", method = RequestMethod.POST)
	public Unit addUnit( @RequestBody Unit unit, @ModelAttribute("currentUser") User currentUser ){
		if(currentUser.getPost() == User.Position.SYS_ADMIN)
			return unitDao.saveUnit(unit);
		
		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/units/{unitId}/technicians", method = RequestMethod.GET)
	public List<User> getUnitTechnicians( @PathVariable Long unitId, @ModelAttribute("currentUser") User currentUser ){
		
		if( ( currentUser.getPost() == User.Position.SUPERVISING_TECHNICIAN && currentUser.getUUnitId() == unitId)
				|| currentUser.getPost() == User.Position.SYS_ADMIN)
			return unitDao.getUnit(unitId).getTechnicians();
		
		throw new RestException(403, "Unauthorized Access");
	}
	
	@RequestMapping(value = "/units/{unitId}/tickets", method = RequestMethod.GET)
	public List<Ticket> getUnitTickets( @PathVariable Long unitId, @ModelAttribute("currentUser") User currentUser ){
		
		if( currentUser.getPost() == User.Position.SUPERVISING_TECHNICIAN
				|| currentUser.getPost() == User.Position.SYS_ADMIN)
			return unitDao.getUnit(unitId).getTickets();
		
		throw new RestException(403, "Unauthorized Access");
	}
}
