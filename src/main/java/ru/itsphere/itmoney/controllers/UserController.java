package ru.itsphere.itmoney.controllers;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.itsphere.itmoney.domain.User;
import ru.itsphere.itmoney.services.ServiceException;
import ru.itsphere.itmoney.services.UserService;

import java.io.Serializable;
import java.util.Map;

/**
 * Это класс контроллер для работы с пользователями он обрабатывает запросы DispatcherServlet
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
//add
@Controller
public class UserController extends AbstractController {
    /**
     * Подключили логгер к текущему классу
     */
    private static final Logger logger = LogManager.getLogger(UserController.class);
    //add
    @Autowired
    private UserService userService;

    // task 16
    public Serializable findUsersByQuery(Map<String, String> params) {
        try {
//            if (params.get("query").isEmpty()) {
//                return null;
//            }
            String query = String.valueOf(params.get("query"));
            return wrap(userService.findUsersByQuery(query));
        } catch (ServiceException e) {
            // TODO add code
            throw new ApplicationException(String.format("Action findUsersByQuery with params (%s) has thrown an exception", params), e);
        }
    };

    // task 15
    public Serializable getCount(Map<String, String> params) {
        try {
            return wrap(userService.getCount());
        } catch (Exception e) {
            throw new ApplicationException("Action getCount has thrown exception", e);
        }
    }

    public Serializable getById(Map<String, String> params) {
        try {
            if (params.get("id") == null) {
                logger.warn("Action {} incoming param id is null", "getById");
                return null;
            }
            int id = Integer.parseInt(params.get("id"));
            User user = userService.getById(id);
            return wrap(user);
        } catch (ServiceException e) {
            throw new ApplicationException(String.format("Action getById with params (%s) has thrown an exception", params), e);
        }
    }

    public Serializable save(Map<String, String> params) {
        try {
            User newUser = convertMapToUser(params);
            if (params.get("id") == null) {
                logger.warn("Action {} incoming param id is null", "save");
                userService.save(newUser);
                return wrap(newUser);
            } else {
                userService.update(newUser);
                return wrap(newUser);
            }
        } catch (ServiceException e) {
            // TODO add code
            throw new ApplicationException(String.format("Action save with params (%s) has thrown an exception", params), e);
        }
    };


    public Serializable getAll(Map<String, String> params) {
        try {
            return wrap(userService.getAll());
        } catch (ServiceException e) {
            // TODO add code
            throw new ApplicationException("Action getAll has thrown an exception", e); // можно так
        }
    }

    public Serializable deleteById(Map<String, String> params) {
        try {
            if (params.get("id") == null) {
                return null;
            }
            int id = Integer.parseInt(params.get("id"));
            userService.deleteById(id);
            return null;
        } catch (ServiceException e) {
            // TODO add code
        }
        return null;
    };

    private String wrap(Object object) {
        return new Gson().toJson(object);
    }

    private User convertMapToUser(Map<String, String> params) {
        String name = params.get("name");
        if (params.get("id") == null) {
            return new User(name);
        }
        int id = Integer.parseInt(params.get("id"));
        return new User(id, name);
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
