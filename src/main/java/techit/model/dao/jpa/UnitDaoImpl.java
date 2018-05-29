package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import techit.model.Unit;
import techit.model.dao.UnitDao;

@Repository
public class UnitDaoImpl implements UnitDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Unit> getUnits() {
		
		return entityManager.createQuery( "from Unit", Unit.class )
	            .getResultList();
	}

	@Override
	public Unit getUnit(Long id) {
		Unit unit=entityManager.find(Unit.class, id);
		return unit;
	}

	@Override
	public Unit saveUnit(Unit unit) {
		return entityManager.merge(unit);
	}

}
