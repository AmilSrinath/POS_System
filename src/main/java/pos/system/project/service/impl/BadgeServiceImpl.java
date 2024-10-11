package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import pos.system.project.entity.Badge;
import pos.system.project.service.BadgeService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class BadgeServiceImpl implements BadgeService {
    @Override
    public void addBadge(Badge badge) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(badge);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateBadge(Badge badge) {

    }

    @Override
    public void deleteBadge(int badgeId) throws IOException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // Use parameterized query to prevent SQL injection
            Query query = session.createNativeQuery("UPDATE badge SET status = 0 WHERE badgeId = :badgeId");
            query.setParameter("badgeId", badgeId);
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of failure
            }
            throw new IOException("Failed to delete badge", e);
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed
            }
        }
    }

    @Override
    public List<Badge> getAllBadges() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM badge");
        nativeQuery.addEntity(Badge.class);
        List<Badge> badgeList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return badgeList;
    }

    @Override
    public void deleteBadgeByItemId(int currentItemSelectedId) throws IOException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // Use parameterized query to prevent SQL injection
            Query query = session.createNativeQuery("UPDATE badge SET status = 0 WHERE itemId = :currentItemSelectedId");
            query.setParameter("currentItemSelectedId", currentItemSelectedId);
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of failure
            }
            throw new IOException("Failed to delete badge", e);
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed
            }
        }
    }
}
