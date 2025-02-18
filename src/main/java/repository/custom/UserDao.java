package repository.custom;

import Entity.UserEntity;
import repository.CrudDao;

import java.sql.SQLException;

public interface UserDao extends CrudDao<UserEntity,Integer> {
    UserEntity LoginAuthenticate(String email,String password) throws SQLException;

    boolean isExist(String email) throws SQLException;

    boolean updatePassword(String email,String newPassword) throws SQLException;

}
