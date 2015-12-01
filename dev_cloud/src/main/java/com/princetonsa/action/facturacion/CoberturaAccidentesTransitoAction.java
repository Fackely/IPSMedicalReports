/*
 * CoberturaAccidentesTransitoAction.java 
 * Autor			:  mdiaz
 * Creado el	:  24-nov-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.facturacion.CoberturaAccidentesTransitoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CoberturaAccidentesTransito;


/**
 * descripcion de esta clase
 *
 * @version 1.0, 24-nov-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class CoberturaAccidentesTransitoAction  extends Action{

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CoberturaAccidentesTransitoAction.class);
	
	/**
	 * Manejo del paginador en el resumen
	 */
	private boolean res=false;
	
	/**
	 * Mapa para almacenar los datos cargados de la
	 * BD para se comparados en el momento de la inserción
	 */
	private HashMap mapaBD;

	/**
	 * Metodo para cerrar la conexión con la BD
	 * @param con
	 */
	private void cerrarConexion(Connection con)
	{
		try
		{
			if( con != null && !con.isClosed() )
			{
				UtilidadBD.closeConnection(con);
			}
		}
		catch(SQLException e)
		{
			logger.warn("no se pudo cerrar la conexion con la base de datos: "+e );
		}
	}

	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con = null;
		try{
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			CoberturaAccidentesTransito mundo=new CoberturaAccidentesTransito();

			// intentamos abrir una conexion con la fuente de datos
			try
			{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con=myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("Problemas con la base de datos al abrir la conexion");
			}

			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			if(usuario == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
				cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else
			{
				if( form instanceof CoberturaAccidentesTransitoForm )
				{
					CoberturaAccidentesTransitoForm forma=(CoberturaAccidentesTransitoForm)form;
					String estado=forma.getEstado();
					logger.warn("Estado "+estado);
					if(estado.equals("comenzar"))
					{
						return accionEmpezar(con, mapping, forma, mundo, usuario);
					}
					else if(estado.equals("adicionarOtro"))
					{
						return accionAdicionarOtro(mapping, forma, con);
					}
					else if(estado.equals("guardar"))
					{
						return accionGuardar(mapping, forma, mundo, con, Integer.parseInt(usuario.getCodigoInstitucion()), usuario);
					}
					else if(estado.equals("eliminar"))
					{
						return accionEliminar(mapping, forma, con);
					}
					else if(estado.equals("pager"))
					{
						return accionPager(con, mapping, forma);
					}
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
				}
				else
				{
					logger.error("Error con la forma (Revisar StrutsConfig)");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * Metodo para revisar el paginador
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionPager(Connection con, ActionMapping mapping, CoberturaAccidentesTransitoForm forma)
	{
		if(res)
		{
			forma.setEstado("resumen");
		}
		return hacerForward(mapping, con);
	}

	/**
	 * Método para guardar los cambios
	 * @param mapping
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param string
	 * @return Pagina principal (En estado resumen)
	 */
	private ActionForward accionGuardar(ActionMapping mapping, CoberturaAccidentesTransitoForm forma, CoberturaAccidentesTransito mundo, Connection con, int institucion, UsuarioBasico usuario)
	{
		int numeroRegistros=((Integer)forma.getRegistro("numRegistros")).intValue();
		double[] cobertura = new double[numeroRegistros];
		for(int i=0;i<numeroRegistros; i++)
		{
			String coberturaBD=(String)mapaBD.get("cobertura_"+i);
			double cobMap=Double.parseDouble(forma.getRegistro("cobertura_"+i)+"");
			if(coberturaBD!=null&&!coberturaBD.equals(""))
			{
				double cobBD=Double.parseDouble(coberturaBD);
				if(cobBD!=cobMap)
				{
					String log="\n            ====INFORMACION ORIGINAL=====\n";
					log+="*\tInstitución ["+usuario.getCodigoInstitucion()+"]\n";
					log+="*\tResponsable ["+(i+1)+"]\n";
					log+="*\tCobertura   ["+cobBD+"]\n";
					log+="            ====INFORMACION DESPUES DE LA MODIFICACION=====\n";
					log+="*\tInstitución ["+usuario.getCodigoInstitucion()+"]\n";
					log+="*\tResponsable ["+(i+1)+"]\n";
					log+="*\tCobertura   ["+cobMap+"]";
					LogsAxioma.enviarLog(ConstantesBD.logCoberturaAccidentesTransitoCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				}
			}
			else
			{
				String log="\n            ====INFORMACION DE LOS DATOS INSERTADOS=====\n";
				log+="*\tInstitución ["+usuario.getCodigoInstitucion()+"]\n";
				log+="*\tResponsable ["+(i+1)+"]\n";
				log+="*\tCobertura   ["+cobMap+"]";
				LogsAxioma.enviarLog(ConstantesBD.logCoberturaAccidentesTransitoCodigo, log, ConstantesBD.tipoRegistroLogInsercion, usuario.getLoginUsuario());
			}
			cobertura[i]=cobMap;
		}
		if(mundo.guardar(con, institucion, cobertura)!=ConstantesBD.codigoNuncaValido)
			forma.setIsGuardo(ConstantesBD.acronimoSi);
		else
			forma.setIsGuardo(ConstantesBD.acronimoNo);
		forma.setEstado("resumen");
		forma.setOffset(0);
		this.setRes(true);
		return hacerForward(mapping, con);
	}

	/**
	 * Método que quita un registro de la cobertura de accidentes
	 * @param mapping
	 * @param forma
	 * @param con
	 * @return Página sin el registro el cual se acaba de eliminar
	 */
	private ActionForward accionEliminar(ActionMapping mapping, CoberturaAccidentesTransitoForm forma, Connection con)
	{
		int registroEliminado=forma.getEliminado();
		int numeroRegistros=((Integer)forma.getRegistro("numRegistros")).intValue();
		forma.setIsGuardo(ConstantesBD.acronimoNo);
		for(int i=registroEliminado;i<numeroRegistros-1; i++)
		{
			forma.setRegistro("cobertura_"+i,forma.getRegistro("cobertura_"+(i+1)));
			forma.setRegistro("institucion_"+i,forma.getRegistro("institucion_"+(i+1)));
			forma.setRegistro("responsable_"+i,(i+1)+"");
		}
		numeroRegistros-=1;
		if(numeroRegistros<=forma.getMaxPageItems())
		{
			forma.setOffset(0);
		}
		res=false;
		forma.setRegistro("numRegistros",new Integer(numeroRegistros));
		return hacerForward(mapping, con);
	}

	/**
	 * Método para adicionar otro registro
	 * @param mapping
	 * @param forma
	 * @param con
	 * @return Pagina principal
	 */
	private ActionForward accionAdicionarOtro(ActionMapping mapping, CoberturaAccidentesTransitoForm forma, Connection con)
	{
		int numeroRegistros=((Integer)forma.getRegistro("numRegistros")).intValue()+1;
		forma.setIsGuardo(ConstantesBD.acronimoNo);
		forma.setRegistro("numRegistros", new Integer(numeroRegistros));
		forma.setRegistro("responsable_"+(numeroRegistros-1), numeroRegistros+"");
		forma.setRegistro("cobertura_"+(numeroRegistros-1), "");
		res=false;
		return hacerForward(mapping, con);
	}

	/**
	 * Metodo para empezar el flujo de la funcionalidad
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param mundo
	 * @return Página Principal
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, CoberturaAccidentesTransitoForm forma, CoberturaAccidentesTransito mundo, UsuarioBasico usuario)
	{
		res=false;
		forma.setOffset(0);
		mapaBD=new HashMap();
		forma.reset();
		forma.getRegistros().putAll(mundo.listar(con, Integer.parseInt(usuario.getCodigoInstitucion())));
		mapaBD.putAll(forma.getRegistros());
		return hacerForward(mapping, con);
	}

	/**
	 * Sólo hay una página en toda la funcionalidad, asi que
	 * factoricé el mapping.findForward en esta funcionalidad
	 * (Además cierra la conexión)
	 * @param mapping
	 * @param con
	 * @return Pagina princial
	 */
	private ActionForward hacerForward(ActionMapping mapping, Connection con)
	{
		cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * @return Retorna res.
	 */
	public boolean getRes()
	{
		return res;
	}
	/**
	 * @param res Asigna res.
	 */
	public void setRes(boolean res)
	{
		this.res = res;
	}
}
