package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.entity.ShortCut;
import pos.system.project.service.ShortCutService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class ShortCutServiceImpl implements ShortCutService {

    @Override
    public String getItemBarCodeById(int id) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT itemBarcode FROM shortcut WHERE shortcutId = :id");
        nativeQuery.setParameter("id", id);
        List<String> list = nativeQuery.list();
        session.close();
        return list.get(0);
    }

    @Override
    public void saveShortCut(int id, String text) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        ShortCut shortCut = new ShortCut(id, text);
        session.saveOrUpdate(shortCut);
        transaction.commit();
        session.close();
    }

    @Override
    public List<ShortCut> getAllShortCuts() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        NativeQuery<ShortCut> nativeQuery = session.createNativeQuery("SELECT * FROM shortcut", ShortCut.class);
        List<ShortCut> list = nativeQuery.list();
        session.close();
        return list;
    }
}
