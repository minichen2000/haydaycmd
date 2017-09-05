package com.mfe.haydaycmd.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Get extends HttpServlet
{
	private static Logger log = LogManager.getLogger( Get.class );
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
		resp.setHeader("Cache-Control","no-cache");

		req.setCharacterEncoding("utf-8");

		try {
			log.info(req.getParameterMap().toString());
			String id=req.getParameter("id");
            if (null==id || "".equals(id) ){
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                PrintWriter out = resp.getWriter();
                out.println("Wrong Paramters");
            }else{
                String baseDir=System.getProperty("baseDir", System.getProperty("user.home"));
                Path p=Paths.get(baseDir+File.separator+id+File.separator+"cmd.txt");
                if(Files.notExists(p)){
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }else{
                    String rlt= new String(Files.readAllBytes(p));
                    Files.delete(p);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = resp.getWriter();
                    out.println(rlt);
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println("KO:"+e.toString());
		}
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
    
    
}