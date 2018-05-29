package techit.model.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import techit.model.UpdateDetails;
import techit.model.dao.UpdateDetailsDao;

@Repository
public class UpdateDetailsDaoImpl implements UpdateDetailsDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public UpdateDetails saveUpdate(UpdateDetails update) {
		return entityManager.merge(update);
	}

	@Override
	public UpdateDetails getUpdate(Long id) {
		return entityManager.find(UpdateDetails.class, id);
	}

}
