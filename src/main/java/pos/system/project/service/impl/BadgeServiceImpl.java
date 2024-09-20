package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
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
    public void deleteBadge(int badgeId) {

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
}
