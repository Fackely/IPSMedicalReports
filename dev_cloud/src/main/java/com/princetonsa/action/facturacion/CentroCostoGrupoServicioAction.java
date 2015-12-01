/*
 * Creado en May 10, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.CentroCostoGrupoServicioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CentroCostoGrupoServicio;

public class CentroCostoGrupoServicioAction  extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CentroCostoGrupoServicioAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
																ActionForm form,
																HttpServletRequest request,
																HttpServletResponse response ) throws Exception
																{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning

			if (form instanceof CentroCostoGrupoServicioForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				CentroCostoGrupoServicioForm centroCostoGrupoServicioForm = (CentroCostoGrupoServicioForm) form;
				String estado=centroCostoGrupoServicioForm.getEstado();

				logger.warn("Estado CentroCostoGrupoServicioAction [" + estado + "]");

				if(estado.equals("empezar"))
				{
					centroCostoGrupoServicioForm.reset();
					centroCostoGrupoServicioForm.setEstado("empezar");	
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");	
				}
				else if (estado.equals("cargarCentroCostoGrupoServicio"))
				{
					return accionCargarCentrosCostoGrupoServicio(con, mapping, centroCostoGrupoServicioForm);
				}
				else if (estado.equals("agregarRegistro"))
				{
					return accionAgregarCentroCostoGrupoServicio(con, mapping, centroCostoGrupoServicioForm);
				}
				else if (estado.equals("guardar"))
				{
					centroCostoGrupoServicioForm.setPatronOrdenar("");
					centroCostoGrupoServicioForm.setUltimoPatron("");
					return this.accionGuardar(con, mapping, centroCostoGrupoServicioForm);
				}
				else if (estado.equals("eliminarCCostoGrupoServicio"))
				{
					return accionEliminar (con, mapping, centroCostoGrupoServicioForm, usuario);
				}
				else if (estado.equals("ordenar"))  //-Ordenar el listado de centros de costo X grupos servicio
				{			    
					return accionOrdenar(centroCostoGrupoServicioForm, mapping, con);
				}

			}//if
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	
	}
	
	/**
	 * Método que agrega al mapa la información del grupo de servicio y 
	 * centro de costo seleccionado
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionAgregarCentroCostoGrupoServicio(Connection con, ActionMapping mapping, CentroCostoGrupoServicioForm forma)
	{
		//----------Número de registros en el mapa --------------------//
		int numRegistros=(Integer)forma.getMapa("numRegistros");
				
		int codigoGrupoServicio=forma.getCodigoGrupoServicio();
		String nombreGrupoServicio=forma.getNombreGrupoServicio();
		int codigoCentroCosto=forma.getCodigoCentroCosto();
		String identificadorCentroCosto = forma.getIdentificador();
		String nombreCentroCosto=forma.getNombreCentroCosto();
		String codigoCentroAtencion=forma.getCodigoCentroAtencion();
		String nombreCentroAtencion=forma.getNombreCentroAtencion();

		int consecutivo = forma.getFrmConsecutivo();	//61826
		
		//---------Se agrega al mapa el nuevo registro de centros de costo x grupo servicio ------//
		forma.setMapa("codigo_gservicio_"+numRegistros, codigoGrupoServicio);
		forma.setMapa("nombre_gservicio_"+numRegistros, nombreGrupoServicio);
		forma.setMapa("codigo_ccosto_"+numRegistros, codigoCentroCosto);
		forma.setMapa("identificador_"+numRegistros, identificadorCentroCosto);
		forma.setMapa("nombre_ccosto_"+numRegistros, nombreCentroCosto);
		forma.setMapa("codigo_catencion_"+numRegistros, codigoCentroAtencion);
		forma.setMapa("nombre_catencion_"+numRegistros, nombreCentroAtencion);
		forma.setMapa("consecutivo_" + numRegistros, consecutivo);	//61826
		
		forma.setMapa("esta_grabado_"+numRegistros, "0");
		forma.setPatronOrdenar("");
		forma.setUltimoPatron("");
		
		numRegistros++;
		
		forma.setMapa("numRegistros", numRegistros);
		forma.setMensaje(new ResultadoBoolean(false, ""));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");	
	}

	/**
	 * Método que cargar los centros de costo x grupos de servicio parametrizados en la institución 
	 * para el centro de atención seleccionado
	 * @param con
	 * @param mapping
	 * @param centroCostoGrupoServicioForm
	 * @return 
	 */
	private ActionForward accionCargarCentrosCostoGrupoServicio (Connection con, ActionMapping  mapping, CentroCostoGrupoServicioForm centroCostoGrupoServicioForm)
	{
		CentroCostoGrupoServicio mundoCentroCostoGrupoServicio = new  CentroCostoGrupoServicio();
		int centroAtencion=centroCostoGrupoServicioForm.getCentroAtencion();
		
		centroCostoGrupoServicioForm.setMapa(mundoCentroCostoGrupoServicio.consultarCentrosCostoGrupoServicio (con, centroAtencion));
		
		centroCostoGrupoServicioForm.setPatronOrdenar("");
		centroCostoGrupoServicioForm.setUltimoPatron("");
		
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que guarda los nuevos centros de costo x grupo servicio 
	 * insertados 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, CentroCostoGrupoServicioForm forma) throws SQLException
	{
		boolean exito;
		
		CentroCostoGrupoServicio mundoCentroCostoServicio = new CentroCostoGrupoServicio();
		this.llenarMundo (forma, mundoCentroCostoServicio);
		
		//-----------Se insertan los registros agregados al mapa --------------------//
		exito = mundoCentroCostoServicio.insertarCentroCostoGrupoServicio (con);
		
		if(exito)
			forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));
		else
			forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTARÓN INCONVENIENTES EN EL ALMACENAMIENTO DE LOS DATOS."));
			
		//mundoCentroCostoServicio.reset();
		//forma.reset();
		
		return accionCargarCentrosCostoGrupoServicio(con, mapping, forma);
	}
	
	/**
	 * Método que elimina el registro seleccionado del mapa, en el caso que ya esté grabado se borra
	 * de la base de datos sino simplemete se borra del mapa
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, CentroCostoGrupoServicioForm forma, UsuarioBasico usuario) throws SQLException
	{
		int indiceEliminado=Integer.parseInt(forma.getMapa("indiceRegEliminar")+"");
		
		/*Se verifica si el registro está grabado para borrarlo de la base de datos, 
		sino se borra el registro del mapa*/ 
		if (forma.getMapa("esta_grabado_"+indiceEliminado).equals("1"))
		{
			CentroCostoGrupoServicio mundoCentroCostoGrupoServicio = new  CentroCostoGrupoServicio();
			int centroAtencion = forma.getCentroAtencion();
			int grupoServicio = Integer.parseInt(forma.getMapa("codigo_gservicio_"+indiceEliminado)+"");
			int centroCosto = Integer.parseInt(forma.getMapa("codigo_ccosto_"+indiceEliminado)+"");
			int consecutivo = Integer.parseInt(forma.getMapa("consecutivo_"+indiceEliminado)+"");	//61826
			
			//-----------Se elimina el registro de la base de datos --------------------//
			if (mundoCentroCostoGrupoServicio.eliminarCentroCostoGrupoServicio (con, centroAtencion, grupoServicio, centroCosto))
			{
				//-----------------------------------------------GENERACION DEL LOG AL ELIMINAR --------------------------------------------------//
				StringBuffer log=new StringBuffer();
				log.append("\n===============ELIMINACIÓN DE CENTROS COSTO X GRUPOS SERVICIO================");
				String nomGrupoServicio=(String)forma.getMapa("nombre_gservicio_"+indiceEliminado);
				String nomCentroCosto=(String)forma.getMapa("nombre_ccosto_"+indiceEliminado);
				
				log.append("\n CENTRO DE ATENCIÓN :"+Utilidades.obtenerNombreCentroAtencion(con, centroAtencion));
				log.append("\n GRUPO DE SERVICIO :"+nomGrupoServicio);
				log.append("\n CENTRO DE COSTO :"+nomCentroCosto);
				log.append("\n CONSECUTIVO :" + consecutivo);	//61826
				
				log.append("\n======================================================================");
				   //-Generar el log 
				LogsAxioma.enviarLog(ConstantesBD.logCentroCostoXgrupoServicioElimCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
			}
			
			//-------Se elimina el registro del mapa -----------//
			forma.eliminarRegistroMapa ();
		}
		//----------Cuando el registro no ha sido guardado se borra del mapa ----------------//
		else
		{
			forma.eliminarRegistroMapa ();
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que ordena el mapa que el listado de centros de costo
	 * por grupos de servicio
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionOrdenar (CentroCostoGrupoServicioForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"codigo_gservicio_", "nombre_gservicio_", "codigo_ccosto_", "identificador_", "nombre_ccosto_", "codigo_catencion_", "nombre_catencion_", "consecutivo_catencion_", "esta_grabado_", "consecutivo_"};

		Integer num = (Integer) forma.getMapa("numRegistros");
		
		forma.setMapa(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapa(),
									num.intValue() ));
        
        forma.getMapa().put("numRegistros", new Integer(num.intValue()));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("principal");
	}
	
	/**
	 * Método para pasar los datos de la forma al mundo
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo (CentroCostoGrupoServicioForm forma, CentroCostoGrupoServicio mundo)
	{
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setMapa(forma.getMapa());
	}

}
