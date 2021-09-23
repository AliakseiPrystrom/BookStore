package by.bookstore.servlets;

import by.bookstore.entity.Comment;
import by.bookstore.service.FacadeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet()
public class BookCommentServlet extends HttpServlet {
    private final FacadeService facadeService = new FacadeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long bookId = Long.parseLong(req.getParameter("bookId"));
        List<Comment> list = facadeService.getCommentByBookBySorted(bookId);
        req.setAttribute("listCommentsByBookId", list);
        getServletContext().getRequestDispatcher("").forward(req, resp);
    }

}