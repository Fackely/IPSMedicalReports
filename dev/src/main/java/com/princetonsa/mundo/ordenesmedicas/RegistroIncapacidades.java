package com.princetonsa.mundo.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.InfoDatos;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ordenesmedicas.RegistroIncapacidadesDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.SqlBaseRegistroIncapacidadDao;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.ordenesmedicas.DtoIncapacidad;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Jairo Gï¿½mez Fecha Septiembre de 2009
 */

public class RegistroIncapacidades {

	Logger logger = Logger.getLogger(RegistroIncapacidades.class);
	
	private DtoRegistroIncapacidades dtoRegistroIncapacidades;
	
	
	public void clean()
	{
		this.dtoRegistroIncapacidades = new DtoRegistroIncapacidades();
	}
	
	/**
	 * Constructor de la Clase
	 */
	private static RegistroIncapacidadesDao getRegistroIncapacidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroIncapacidadesDao();
	}
	
	public DtoRegistroIncapacidades consultarIncapacidadPorIngreso (Connection connection, int ingreso)
	{
		return getRegistroIncapacidadesDao().consultarIncapacidadPorIngreso(connection, ingreso);
	}
	
	public HashMap consultarTiposIncapacidad(Connection connection)
	{
		return getRegistroIncapacidadesDao().consultarTiposIncapacidad(connection);
	}
	
	public String insertarIncapacidad(Connection connection, DtoInfoFechaUsuario fechaUsuario)
	{
		return getRegistroIncapacidadesDao().insertarIncapacidad(connection, this.dtoRegistroIncapacidades, fechaUsuario);
	}
	
	public String actualizarIncapacidad(Connection connection, DtoInfoFechaUsuario fechaUsuario)
	{
		return getRegistroIncapacidadesDao().actualizarIncapacidad(connection, this.dtoRegistroIncapacidades, fechaUsuario);
	}
	
	public String insertarLogIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario)
	{
		return getRegistroIncapacidadesDao().insertarLogIncapacidad(connection, incapacidad, fechaUsuario);
	}
	
	public int egresoPosteriorConReversion(Connection connection, DtoInfoFechaUsuario fechaUsuario, int cuenta)
	{
		return getRegistroIncapacidadesDao().egresoPosteriorConReversion(connection, fechaUsuario, cuenta);
	}
	
	public String actualizarEstadoIncapacidad(Connection connection, String estado, int codigoPK, DtoInfoFechaUsuario fechaHoraUsuarioAnulacion)
	{
		return getRegistroIncapacidadesDao().actualizarEstadoIncapacidad(connection, estado, codigoPK, fechaHoraUsuarioAnulacion);
	}
	
	public ArrayList<DtoIncapacidad> consultarIncapacidadesPaciente (Connection connection, int codigoPaciente)
	{
		return getRegistroIncapacidadesDao().consultarIncapacidadesPaciente(connection, codigoPaciente);
	}
	
	public ArrayList<DtoIncapacidad> consultarPacientesIncapacidad (Connection connection, DtoIncapacidad parametros)
	{
		return getRegistroIncapacidadesDao().consultarPacientesIncapacidad(connection, parametros);
	}
	
	public ArrayList<DtoIncapacidad> consultaConveniosIngreso (Connection connection, int codigoPk)
	{
		return getRegistroIncapacidadesDao().consultaConveniosIngreso(connection, codigoPk);
	}
	
	public InfoDatos consultarDiagnosticoEvoluciones (Connection connection, int solicitud)
	{
		return getRegistroIncapacidadesDao().consultarDiagnosticoEvoluciones(connection, solicitud);
	}
	
	public InfoDatos consultarDiagnosticoValoraciones (Connection connection, int solicitud)
	{
		return getRegistroIncapacidadesDao().consultarDiagnosticoValoraciones(connection, solicitud);
	}
	
	public String eliminarIncapacidadesInactivasXRegistro (Connection connection, int noIngreso)
	{
		return getRegistroIncapacidadesDao().eliminarIncapacidadesInactivasXRegistro(connection, noIngreso);
	}
	
	public String consultarIncapacidadesCambios(Connection connection, int noIngreso){
		return getRegistroIncapacidadesDao().consultarIncapacidadesCambios(connection, noIngreso);
	}

	/**
	 * @return the dtoRegistroIncapacidades
	 */
	public DtoRegistroIncapacidades getDtoRegistroIncapacidades() {
		return dtoRegistroIncapacidades;
	}

	/**
	 * @param dtoRegistroIncapacidades the dtoRegistroIncapacidades to set
	 */
	public void setDtoRegistroIncapacidades(
			DtoRegistroIncapacidades dtoRegistroIncapacidades) {
		this.dtoRegistroIncapacidades = dtoRegistroIncapacidades;
	}
	
	/**
	 * Método implementado para activar los registros de incapacidades de un ingreso
	 */
	public static ResultadoBoolean activarRegistrosIncapacidades(Connection con,DtoRegistroIncapacidades incapacidad)
	{
		return getRegistroIncapacidadesDao().activarRegistrosIncapacidades(con, incapacidad);
	}
	
	/**
	 * Método que realiza la impresión de incapacidades desde otras funcionalidades
	 */
	public HttpServletRequest imprimirIncapacidad(Connection connection, HttpServletRequest request,
			UsuarioBasico usuario, int noIngreso) {

		DtoRegistroIncapacidades incapacidad = consultarIncapacidadPorIngreso(connection, noIngreso);
		
		int	flagCodigo = incapacidad.getCodigoPk();
		int	flagPrioridad = 1;
		
		String nombreRptDesign = "registroIncapacidades.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"ordenes/",nombreRptDesign);
		
		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		
		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
		v=new Vector();
		v.add(""+ins.getRazonSocial());
		v.add("NIT: "+ins.getNit()+"\n"+ins.getDireccion()+"\nTels: "+ins.getTelefono());
		comp.insertLabelInGridOfMasterPage(0,1,v);
		
		// Parametros de Generación
		comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
		v=new Vector();
		
		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
		
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport+"");
			
		if(!newPathReport.equals("")){
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport+"&codigoPK="+flagCodigo+"&prioridad="+flagPrioridad);
		}

		UtilidadBD.closeConnection(connection);
		return request;
		//return mapping.findForward("ingresarModificar");
	}
	
	/**
	 * Método implementado para verificar si ya fueron facturados todos los servicios de una solicitud
	 */
	public String verificaSolicitudFacturada(Connection connection,
			int solicitud) {
		return getRegistroIncapacidadesDao().verificaSolicitudFacturada(connection, solicitud);
	}
}