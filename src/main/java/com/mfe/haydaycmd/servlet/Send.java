package com.mfe.haydaycmd.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Send extends HttpServlet
{
	private static Logger log = LogManager.getLogger( Send.class );
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
		resp.setHeader("Cache-Control","no-cache");

		req.setCharacterEncoding("utf-8");

		try {
			log.info(req.getParameterMap().toString());
			String id=req.getParameter("id");
			String cmd=req.getParameter("cmd");
            if (null==id || "".equals(id) || null==cmd || "".equals(cmd)){
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                PrintWriter out = resp.getWriter();
                out.println("Wrong Paramters");
            }else{
                String baseDir=System.getProperty("baseDir", System.getProperty("user.home"));
                Path p= Paths.get(baseDir+File.separator+id);
                if(Files.notExists(p)){
                    resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                    PrintWriter out = resp.getWriter();
                    out.println("ID does not exist");
                }else{
                    Files.write(Paths.get(baseDir+File.separator+id+File.separator+"cmd.txt"), cmd.getBytes());
                    resp.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter out = resp.getWriter();
                    out.println("OK");
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