package controller.web;

import beans.Cart;
import model.*;
import service.IntroService;
import service.OrderService;
import service.PostService;
import service.ProductService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "AddOrderSuccess", value = "/add_order_success")
public class AddOrderSuccess extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderService oderService = new OrderService();
        int orderid = oderService.getMaxMHD();
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        UserModel user = (UserModel) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getTotal() == 0) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        String phone = request.getParameter("phone");
        String paymentMethod = request.getParameter("paymentMethod");
        String address = request.getParameter("address");
        System.out.println(address);
        String transport = "API";
        String message = request.getParameter("message");
        long totalAmount = cart.getTotal();
        Date orderDate = Date.valueOf(LocalDate.now());
        OrderService orderService = new OrderService();
        try {
            Order order = new Order(orderid, user.getUserName(), totalAmount, 0, orderDate, paymentMethod, transport, 0, address, message, phone);
            orderService.addOder(order);

            order.setOder_id(orderid);

            for (ProductInCart product : cart.getListProductInCart()) {
                Order_detail orderDetail = new Order_detail(0, order, product.getProduct().getProduct_id(), product.getProduct().getPrice_sell(), product.getQuantity(), 0, (product.getProduct().getPrice_sell() * product.getQuantity()));
                orderService.addOrderDetail(orderDetail);
            }

            session.removeAttribute("cart");
            cart = null;
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        response.sendRedirect("/success");
//        RequestDispatcher rd = request.getRequestDispatcher("/views/web/order-success.jsp");
//        rd.forward(request, response);
    }

}