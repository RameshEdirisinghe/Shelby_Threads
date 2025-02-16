package service.custom;

import service.SuperService;

public interface UserService extends SuperService {
    String LoginAuthenticate(String email,String password);
}
