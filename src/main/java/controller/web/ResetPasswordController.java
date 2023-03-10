package controller.web;

import domain.Email;
import model.Article_Category;
import model.Introduce;
import model.Product_type;
import model.UserModel;
import service.ArticleService;
import service.IntroService;
import service.ProductService;
import service.UserService;
import util.EmailUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ResetPasswordController", urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Lay ra danh sach loai bai viet
        ArticleService service = new ArticleService();
        ProductService productService = new ProductService();
        List<Article_Category> list = service.getListArCategory();
        request.setAttribute("listAr", list);
        //Lay ra danh sach loai sp de chen vao header
        List<Product_type> listType = productService.getAllProduct_type();
        request.setAttribute("listType",listType);
        //Lay ra thong tin de chen vao footer
        IntroService intr = new IntroService();
        Introduce intro = intr.getIntro();
        request.setAttribute("info", intro);
        RequestDispatcher rd = request.getRequestDispatcher("views/web/user-reset-password.jsp");
        rd.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String emailAddress = request.getParameter("email");
        String username = request.getParameter("username");
        UserModel user = UserService.findByUserAndEmail(username,emailAddress);
        if(user == null){
            request.setAttribute("error","T??n t??i kho???n ho???c email kh??ng ????ng");
        } else {
            int id = user.getId();
            String token = UserService.createToken();
            if(UserService.checkToken(token)){
                token = UserService.createToken();
            }
            UserService.addToken(id,token);
            Email email = new Email();
            email.setFrom("happyhomenoithat@gmail.com");
            email.setTo(emailAddress);
            email.setFromPassword("smckqxzmhsecmqld");
            email.setSubject("HappyHome - ?????i m???t kh???u");
            StringBuilder sb = new StringBuilder();
            sb.append("<div style=\"font-size:16px;color:black;\">");
            sb.append("<p style=\"font-size:24px;\">Thi???t l???p m???t kh???u <p>");
            sb.append("<span>Xin ch??o </span>").append(user.getFullName()).append("<br><br>");
            sb.append("<span>Click v??o ???????ng d???n d?????i ????y ????? thi???t l???p m???t kh???u t??i kho???n c???a qu?? kh??ch h??ng t???i").append("<strong> N???i Th???t HappyHome</strong></span>").append("<br>");
            sb.append("<span>Qu?? kh??ch h??ng c?? 10 ph??t ????? thay ?????i m???t kh???u, sau 10 ph??t ???????ng d???n s??? kh??ng c??n t???n t???i.</span>").append("<br>");
            sb.append("<span>N???u qu?? kh??ch h??ng kh??ng c?? y??u c???u thay ?????i m???t kh???u, xin h??y x??a email n??y ????? b???o m???t th??ng tin.</span>").append("<br><br>");
            sb.append("<button style=\"padding:20px 15px;color:#fff;background-color:#343a40;border-radius:4px;\"><a href=http://localhost:8080/").append(request.getContextPath()).append("/change-password?token=").append(token).append(" style=\"font-size:16px;text-decoration: none;color:#fff\">Thi???t l???p l???i m???t kh???u</a></button>").append("<br><br>");
            sb.append("<span>Tr??n tr???ng!</span>").append("<br>");
            sb.append("<span>C???m ??n</span>");
            email.setContent(sb.toString());
            EmailUtil.send(email);
            request.setAttribute("message","Link thi???t l???p l???i m???t kh???u ???? ???????c g???i v??o email c???a b???n."
                    + "Vui l??ng check email c???a b???n");
        }
        ArticleService service = new ArticleService();
        ProductService productService = new ProductService();
        List<Article_Category> list = service.getListArCategory();
        request.setAttribute("listAr", list);
        //Lay ra danh sach loai sp de chen vao header
        List<Product_type> listType = productService.getAllProduct_type();
        request.setAttribute("listType",listType);
        //Lay ra thong tin de chen vao footer
        IntroService intr = new IntroService();
        Introduce intro = intr.getIntro();
        request.setAttribute("info", intro);
        request.getRequestDispatcher("views/web/user-reset-password.jsp").forward(request,response);
    }
}
