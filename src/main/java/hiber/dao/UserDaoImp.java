package hiber.dao;

import hiber.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
@Transactional
public class UserDaoImp implements UserDao {
    public static Logger logger = Logger.getLogger(UserDaoImp.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }

    @Override
    public List<User> getUsers(String model, int series) {
        List<User> users = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String hqlQuery = "Select user FROM User user WHERE user.car.model = :model AND user.car.series = :series";

            Query query = session.createQuery(hqlQuery, User.class);
            query.setParameter("model", model);
            query.setParameter("series", series);

            users = query.getResultList();
            System.out.println(users);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Произошла ошибка в getUser: " + e);
        }

        return users;
    }
}