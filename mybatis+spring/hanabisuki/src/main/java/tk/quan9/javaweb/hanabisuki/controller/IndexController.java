package tk.quan9.javaweb.hanabisuki.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tk.quan9.javaweb.hanabisuki.entity.User;
import tk.quan9.javaweb.hanabisuki.service.RightsCheck;
import tk.quan9.javaweb.hanabisuki.service.UserGroupService;
import tk.quan9.javaweb.hanabisuki.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RightsCheck rightsCheck;
    @Autowired
    private UserGroupService userGroupService;

    private void redirect(String url,HttpServletResponse response){
        try {
            response.sendRedirect(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = {"/"})
    public String index(HttpSession session){
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST,value = {"/Login"})
    public void Login(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        int id=Integer.parseInt(request.getParameter("id"));
        String passWord=request.getParameter("passWord");
        User user=userService.getUserById(id);
        if(user!=null) {
            if (rightsCheck.loginCheck(id, passWord)) {
                System.out.println("id：" + id + "  登录成功!");
                session.setAttribute("LoginUser", user);
                session.setAttribute("UserGroupName", userGroupService.getGroupNameById(user.getGroupId()));
                redirect("/CommentGetter?commentPage=1", response);
            }else {
                session.setAttribute("indexWarning", "用户名或密码有误");
                redirect("/", response);
            }
        }else {
            session.setAttribute("indexWarning", "用户名或密码有误");
            redirect("/", response);
        }
    }
}
