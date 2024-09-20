package pos.system.project.service;

import org.hibernate.Session;
import pos.system.project.entity.Badge;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface BadgeService {
    void addBadge(Badge badge) throws IOException;
    void updateBadge(Badge badge);
    void deleteBadge(int badgeId);
    List<Badge> getAllBadges() throws IOException;
}
