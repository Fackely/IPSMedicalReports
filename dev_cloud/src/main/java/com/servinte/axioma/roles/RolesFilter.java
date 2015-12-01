package com.servinte.axioma.roles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AccesoIlimitadoRoles;
import com.servinte.axioma.orm.Funcionalidades;
import com.servinte.axioma.orm.delegate.AccesoIlimitadoRolesDelegate;
import com.servinte.axioma.orm.delegate.FuncionalidadesDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.persistencia.impl.hibernate.TransaccionHibernate;

public class RolesFilter implements Filter{

	private static List<AccesoIlimitadoRoles> listaRoles=null;
	
	@Override
	public void destroy() {
		// No se hace necesario hacer nada
	}
	
	private static ArrayList<String> pathsFuncionalidades;

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		UsuarioBasico usuarioBasico = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		String urlCompleto=request.getRequestURI();
		String carpeta=request.getRequestURI();

		while(carpeta.indexOf('/')==0)
		{
			carpeta=carpeta.substring(1, carpeta.length());
		}
		if(carpeta.indexOf('/')>0)
		{
			carpeta=carpeta.substring(carpeta.indexOf('/'), carpeta.length());
		}
		while(carpeta.indexOf('/')==0)
		{
			carpeta=carpeta.substring(1, carpeta.length());
		}
		if(carpeta.indexOf('/')>0)
		{
			carpeta=carpeta.substring(0, carpeta.indexOf('/'));
		}
		else
		{
			carpeta="";
		}
		while(carpeta.indexOf('/')==0)
		{
			carpeta=carpeta.substring(1, carpeta.length());
		}
		
		/*
		System.out.println("urlCompleto "+urlCompleto);
		System.out.println("carpeta -->"+carpeta+"<--");
		System.out.println("usuario "+usuarioBasico);
		*/
		
		try {
			if(listaRoles == null)
			{
				try{
					// Primero verifico las carpetas que no deben tener restricciones
					AccesoIlimitadoRolesDelegate accesoIlimitadoRolesDelegate=new AccesoIlimitadoRolesDelegate();		
					UtilidadTransaccion.getTransaccion().begin();
					listaRoles=accesoIlimitadoRolesDelegate.listar();
					UtilidadTransaccion.getTransaccion().commit();
				}
				catch (Exception e) {
					UtilidadTransaccion.getTransaccion().rollback();
					Log4JManager.info("Hubo un error en una consulta de la BD, verifique el log", e);
					chain.doFilter(request, response);
					return;
				}
			}
			
			if(!carpeta.equals(""))
			{
				for (Iterator<AccesoIlimitadoRoles> iterator = listaRoles.iterator(); iterator.hasNext();)
				{
					AccesoIlimitadoRoles accesoIlimitadoRoles = (AccesoIlimitadoRoles) iterator.next();
					if(accesoIlimitadoRoles.getCarpeta().contains(carpeta))
					{
						chain.doFilter(request, response);
						return;
					}
				}
			}
			
			boolean urlsPermitidos=urlCompleto.contains("login.jsp") ||
								urlCompleto.contains("logout.jsp") ||
								urlCompleto.contains("login.do") ||
								urlCompleto.contains("accesoDenegado.jsp") ||
								urlCompleto.contains("loginCentroCosto.jsp")||urlCompleto.contains("loginPasswordInactivo.jsp");
			
			// Verifico las páginas de login
			if(usuarioBasico==null && !urlsPermitidos)
			{
				if(carpeta.equals("/") || carpeta.equals(""))
				{
					//System.out.println("hace redireccion a login");
					((HttpServletResponse)response).sendRedirect(request.getContextPath()+"/login.jsp");
					return;
				}
				reenviar(request, response);
				return;
			}
			
			if(!urlsPermitidos)
			{
				/*
				 * Verifico si existe el rol para el usuario según la carpeta en la que entra
				 */
				if(!carpeta.equals(""))
				{
					if(pathsFuncionalidades.contains(carpeta))
					{
						if(usuarioBasico==null)
						{
							request.getSession().invalidate();
							try{
								((HttpServletResponse)response).sendRedirect(request.getContextPath()+"/login.jsp");
								return;
							}
							catch (IllegalStateException e) {
								Log4JManager.error("Verificando existe rol " + e);
							}
						}
					}
					/* Por el momento estamos de afán!! más adelante bloqueo todas las carpetas
					else
					{
						logger.info("----------------------------CARPETA CANCELADA >"+carpeta+"<");
						reenviar(request, response);
						HibernateUtil.endTransaction();
						return;
					}*/
				}
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			Log4JManager.error("Error RolesFilter --------- ", e);
			e.printStackTrace();
		}	
	}

	private void reenviar(HttpServletRequest request, ServletResponse response) {
		try {
			((HttpServletResponse)response).sendRedirect(request.getContextPath()+"/accesoDenegado.jsp");
		} catch (IOException e) {
			Log4JManager.error("Error redireccionando a la página de login", e);
		}
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		pathsFuncionalidades=new ArrayList<String>();
		HibernateUtil.getSessionFactory(true);
		FuncionalidadesDelegate funcionalidadesDelegate=new FuncionalidadesDelegate();
		List<Funcionalidades> funcionalidades=funcionalidadesDelegate.listarTodas();
		for (Iterator<Funcionalidades> iterator = funcionalidades.iterator(); iterator.hasNext();) {
			Funcionalidades funcionalidad = (Funcionalidades) iterator.next();
			String path=funcionalidad.getArchivoFunc();
			while(path.indexOf('/')==0)
			{
				path=path.substring(1, path.length());
			}
			if(path.indexOf('/')!=-1)
			{
				path=path.substring(0, path.indexOf('/'));
			}
			if(!pathsFuncionalidades.contains(path) && !path.equals(""))
			{
				pathsFuncionalidades.add(path);
			}
		}
		HibernateUtil.endTransaction();
	}

}
