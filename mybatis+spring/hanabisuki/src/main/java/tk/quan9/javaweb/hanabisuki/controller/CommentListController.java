package tk.quan9.javaweb.hanabisuki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tk.quan9.javaweb.hanabisuki.entity.Comment;
import tk.quan9.javaweb.hanabisuki.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentListController {
    @Autowired
    CommentService commentService;

    private void redirect(String url, HttpServletResponse response){
        try {
            response.sendRedirect(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(method = RequestMethod.GET,value = {"/CommentGetter"})
    public String commentGetter(HttpServletRequest request, HttpSession session){

        int pagesIndex=0;
        if(request.getParameter("commentPage")!=null){
            pagesIndex=Integer.parseInt(request.getParameter("commentPage"))-1;
        }

        int commentCounts=commentService.getCommentCounts();
        int pageListLength=1;
        if(commentCounts%10==0){
            pageListLength=commentCounts/10;
        }else {
            pageListLength=commentCounts/10+1;
        }

        List<Comment> commentList=commentService.getCommentListWithPages(pagesIndex);

        session.setAttribute("commentList",commentList);
        session.setAttribute("pageListLength",pageListLength);

        return "commentList";
    }

    @RequestMapping(method = RequestMethod.GET,value = {"/editcomment"})
    public String editComment(HttpSession session,HttpServletRequest request){
        String commentId=request.getParameter("commentId");
        if(!commentId.equals("new")) {
            Comment comment = commentService.getCommentById(commentId);
            session.setAttribute("commentToEdit", comment);
        }else {
            session.setAttribute("commentToEdit", null);
        }
        return "editcomment";
    }

    @RequestMapping(method = RequestMethod.POST,value = {"/editcomment"})
    public void updateOrInsertComment(HttpSession session,
                                      HttpServletRequest request,HttpServletResponse response){
        if((boolean)session.getAttribute("isEdit")){
            commentService.updateComment(request,session);
        }else {
            commentService.insertComment(request,session);
        }
        session.setAttribute("commentToEdit",null);

        redirect("/CommentGetter",response);
    }

}