/*
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software
 * except in compliance with the terms of the license at:
 * http://developer.sun.com/berkeley_license.html
 */

package controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import session.CbLogsFacade;

/**
 *
 * @author tgiunipero
 */
@WebServlet(name = "Controller",
            loadOnStartup = 0,
            urlPatterns = { "/logs",
                            "/critical_info",
                            "/critical_errs",
                            "/logs_all"})
public class ControllerServlet extends HttpServlet {

    @EJB
    private CbLogsFacade cbLogsFacade;


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

        super.init(servletConfig);

        // store category list in servlet context
        getServletContext().setAttribute("logs", cbLogsFacade.findAll());
    }


    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        HttpSession session = request.getSession();
        
        
        

        if (userPath.equals("/logs")) {
            
            /*Тестовые инструменты
            */
            String uvcode =  request.getParameter("uvc");
            if(uvcode!=null)    {
                if(uvcode.length()>0)    {
                    cbLogsFacade.updProductByVendorCode(uvcode);
                }
            }
            /*Тестовые инструменты
            */

            short sh=1;
            session.setAttribute("logs", cbLogsFacade.getLast200Logs());

        } else if (userPath.equals("/logs_all")) {

            userPath="/logs";
            session.setAttribute("logs", cbLogsFacade.findAll());

        } else if (userPath.equals("/critical_info")) {
            session.setAttribute("logs2", cbLogsFacade.getCriticalInfo());
        } else if (userPath.equals("/critical_errs")) {
            session.setAttribute("logs3", cbLogsFacade.getCriticalErrs());
        } 

        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");  // ensures that user input is interpreted as
                                                // 8-bit Unicode (e.g., for Czech characters)

        String userPath = request.getServletPath();
        HttpSession session = request.getSession();
        
        if (userPath.equals("/logs")) {
            short sh=1;
            session.setAttribute("logs", cbLogsFacade.getLast200Logs());
        // if cart page is requested
        }

        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}