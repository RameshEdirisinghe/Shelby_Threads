package repository.custom.impl;

import Entity.UserEntity;
import repository.custom.UserDao;

import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {


    @Override
    public boolean save(UserEntity entity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Integer integer, UserEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public List getAll() {
        return List.of();
    }
}
