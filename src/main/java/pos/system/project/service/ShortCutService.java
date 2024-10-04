package pos.system.project.service;

import pos.system.project.entity.ShortCut;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface ShortCutService {
    String getItemBarCodeById(int id) throws IOException;
    void saveShortCut(int id, String text) throws IOException;
    List<ShortCut> getAllShortCuts() throws IOException;
}
