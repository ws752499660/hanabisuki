package hanabiMVC.service.impl;

import hanabiMVC.entity.Role;
import hanabiMVC.entity.User;
import hanabiMVC.repository.RoleRightsRepository;
import hanabiMVC.repository.UserRepository;
import hanabiMVC.service.UserService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRightsRepository roleRightsRepository;

    private String codeTrans(String a){
        try {
            String b=new String(a.getBytes("ISO8859-1"),"UTF-8");
            return b;
        }catch (Exception e){
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getUserList() {
        return userRepository.getUserList();
    }

    @Override
    public int getCommentCount(int id) {
        return userRepository.getCommentCount(id);
    }

    @Override
    public void updateUser(HttpServletRequest request, HttpSession session) {
        User user=(User) session.getAttribute("userForProfile");
        user.setUserName(StringEscapeUtils.escapeHtml4(request.getParameter("userName")));
        if(request.getParameter("passWord").equals("")){
            user.setPassWord(((User)session.getAttribute("LoginUser")).getPassWord());
        }else {
            user.setPassWord(Security.MD5(user.getId()+request.getParameter("passWord")));
        }
        user.setPhoneNum(StringEscapeUtils.escapeHtml4(request.getParameter("phoneNum")));
        user.setEmail(StringEscapeUtils.escapeHtml4(request.getParameter("email")));
        userRepository.updateUser(user);
    }

    @Override
    public List<User> getUserListByGroupId(int groupId) {
        return userRepository.getUserListByGroupId(groupId);
    }

    @Override
    public void updateGroup(int userId, int groupId) {
        userRepository.updateGroup(userId,groupId);
    }

    @Override
    public void updateRole(int userId, String roleName) {
        userRepository.updateRole(userId,roleName);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRightsRepository.getAllRole();
    }
}
