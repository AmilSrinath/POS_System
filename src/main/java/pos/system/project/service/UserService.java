package pos.system.project.service;

import pos.system.project.dto.UserDTO;
import pos.system.project.entity.User;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface UserService {
    int Add(UserDTO userDTO) throws IOException;
    void Edit(UserDTO userDTO, int currentSelectedId) throws IOException;
    void Delete(int currentSelectedId) throws IOException;
    List<User> getAllUser() throws IOException;
    User getUserByUsername(String username) throws IOException;
}
