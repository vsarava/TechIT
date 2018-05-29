package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Unit;
import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.service.*;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User getUser(Long id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public User getUser(String username) {
		List<User> users = entityManager.createQuery("from User where username = :username", User.class)
				.setParameter("username", username).getResultList();
		return users.size() == 0 ? null : users.get(0);
	}

	@Override
	public List<User> getUsers() {
		return entityManager.createQuery("from User", User.class).getResultList();
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		
		return entityManager.merge(user);
	}

	@Override
	public List<User> getSupervisors(Unit unit) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from User where post = :post and unit = :unit", User.class)
				.setParameter("post", User.Position.SUPERVISING_TECHNICIAN)
				.setParameter("unit", unit).getResultList();
	}

}
