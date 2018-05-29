package techit.model.dao;

import techit.model.UpdateDetails;

public interface UpdateDetailsDao {
	
	UpdateDetails saveUpdate(UpdateDetails update);
	
	UpdateDetails getUpdate(Long id);

}
