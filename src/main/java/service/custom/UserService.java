package service.custom;

import service.SuperService;

public interface LoginService extends SuperService {
    String LoginAuthenticate(String email,String password);
}
