package repository.custom.impl;

import repository.custom.LoginDao;

import java.sql.SQLException;
import java.util.List;

public class LoginDaoImpl implements LoginDao {
    @Override
    public boolean save(Object entity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Object o, Object entity) {
        return false;
    }

    @Override
    public boolean delete(Object o) {
        return false;
    }

    @Override
    public List getAll() {
        return List.of();
    }
}
