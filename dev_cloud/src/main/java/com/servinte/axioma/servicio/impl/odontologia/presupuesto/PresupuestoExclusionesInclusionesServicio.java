/**
 * 
 */
package com.servinte.axioma.servicio.impl.odontologia.presupuesto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionErrors;

import util.ResultadoBoolean;
import util.odontologia.InfoInclusionExclusionBoca;
import util.odontologia.InfoInclusionExclusionPiezaSuperficie;

import com.princetonsa.dto.odontologia.DtoContratarInclusion;
import com.princetonsa.dto.odontologia.DtoEncabezadoInclusion;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoRegistroContratarInclusion;
import com.princetonsa.dto.odontologia.DtoRegistroGuardarExclusion;
import com.princetonsa.dto.odontologia.DtoTotalesContratarInclusion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo;
import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio;


/**
 * @author Juan David Ramírez
 * @since Dec 29, 2010
 */
public class PresupuestoExclusionesInclusionesServicio implements IPresupuestoExclusionesInclusionesServicio
{
	@Override
	public void seleccionarPrograma(
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,
			int indexRegInclusion, int indexConvenio, boolean checkContratado)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		presupuestoExclusionesInclusionesMundo.seleccionarPrograma(registrosContratarInclusion, indexRegInclusion, indexConvenio, checkContratado);
	}

	@Override
	public void seleccionarPromocion(
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,
			int indexRegInclusion, int indexConvenio,
			boolean checkPromocion)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		presupuestoExclusionesInclusionesMundo.seleccionarPromocion(registrosContratarInclusion, indexRegInclusion, indexConvenio, checkPromocion);
		
	}

	@Override
	public void calcularTotalesConvenios(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios, DtoTotalesContratarInclusion totalesContratarInclusion) throws IPSException
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		presupuestoExclusionesInclusionesMundo.calcularTotalesConvenios(registrosContratarInclusion, listaSumatoriaConvenios, totalesContratarInclusion);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#verificarSonIgualesValoresConvenios(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public boolean verificarSonIgualesValoresConvenios(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusionClon)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		return presupuestoExclusionesInclusionesMundo.verificarSonIgualesValoresConvenios(registrosContratarInclusion, registrosContratarInclusionClon);
	}

	@Override
	public void seleccionarTodosProgramasConvenio(ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, int indexConvenio, boolean checkSelectTodosConvenio)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		presupuestoExclusionesInclusionesMundo.seleccionarTodosProgramasConvenio(registrosContratarInclusion, indexConvenio, checkSelectTodosConvenio);
	}

//	/* (non-Javadoc)
//	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#contratarInclusiones(org.apache.struts.action.ActionErrors, com.princetonsa.mundo.PersonaBasica, java.util.ArrayList, java.util.ArrayList, int, java.math.BigDecimal, java.lang.String, java.sql.Connection, java.math.BigDecimal)
//	 */
//	@Override
//	public ResultadoBoolean contratarInclusiones(
//			ActionErrors errores,
//			PersonaBasica paciente,
//			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion,
//			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
//			int codigoInstitucion, BigDecimal codigoPlanTratamiento,
//			String loginUsuario, Connection con, BigDecimal codigoPresupuesto) {
//		
//		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
//			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
//		
//		return presupuestoExclusionesInclusionesMundo.contratarInclusiones(errores, paciente, registrosContratarInclusion, listaSumatoriaConvenios, codigoInstitucion, codigoPlanTratamiento, loginUsuario, con, codigoPresupuesto);
//		
//	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#guardarExclusiones(org.apache.struts.action.ActionErrors, java.util.ArrayList, java.sql.Connection, java.lang.String, java.math.BigDecimal, int, int)
	 */
	@Override
	public ResultadoBoolean guardarExclusiones(
			ActionErrors errores,
			ArrayList<DtoRegistroGuardarExclusion> listadoRegistroGuardarExclusion,
			Connection con, String loginUsuario, BigDecimal codigoPresupuesto, int codigoInstitucion,
			int codigoCentroAtencion) {
		
		
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo=
			PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.guardarExclusiones(errores, listadoRegistroGuardarExclusion, con, loginUsuario, codigoPresupuesto, codigoInstitucion, codigoCentroAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#cargarRegistrosExclusion(long)
	 */
	@Override
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion(long codigoPresupuesto) {
		
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo =	PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.cargarRegistrosExclusion(codigoPresupuesto);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#cargarRegistrosInclusion(long)
	 */
	@Override
	public ArrayList<DtoEncabezadoInclusion> cargarRegistrosInclusion(long codigoPresupuesto) {
		
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.cargarRegistrosInclusion(codigoPresupuesto);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#cargarDetalleRegistroInclusion(long, java.util.ArrayList, java.util.ArrayList, com.princetonsa.mundo.UsuarioBasico)
	 */
	@Override
	public ArrayList<DtoRegistroContratarInclusion> cargarDetalleRegistroInclusion(
			long codigoIncluPresuEncabezado,
			ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas,
			ArrayList<InfoInclusionExclusionBoca> inclusionesBoca,
			UsuarioBasico usuario) {
		
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		return presupuestoExclusionesInclusionesMundo.cargarDetalleRegistroInclusion(codigoIncluPresuEncabezado, inclusionesPiezas, inclusionesBoca, usuario);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#obtenerListadoExclusiones(java.util.ArrayList, java.util.ArrayList, java.lang.String, int)
	 */
	@Override
	public ArrayList<DtoRegistroGuardarExclusion> obtenerListadoExclusiones(
			ArrayList<InfoInclusionExclusionPiezaSuperficie> exclusionesPiezas,
			ArrayList<InfoInclusionExclusionBoca> exclusionesBoca,
			String loginUsuario, int codigoInstitucion) {
		
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.obtenerListadoExclusiones(exclusionesPiezas, exclusionesBoca, loginUsuario, codigoInstitucion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#obtenerListadoInclusiones(java.util.ArrayList, java.util.ArrayList, java.lang.String, int)
	 */
	@Override
	public ArrayList<DtoPresuOdoProgServ> obtenerListadoInclusiones(
			ArrayList<InfoInclusionExclusionPiezaSuperficie> inclusionesPiezas,
			ArrayList<InfoInclusionExclusionBoca> inclusionesBoca,
			String loginUsuario, int codigoInstitucion) {
		
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.obtenerListadoInclusiones(inclusionesPiezas, inclusionesBoca, loginUsuario, codigoInstitucion);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#generarSolicitudDescuento(com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento)
	 */
	@Override
	public boolean generarSolicitudDescuento(DtoPresupuestoOdontologicoDescuento dtoDcto)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.generarSolicitudDescuento(dtoDcto);
	}

	@Override
	public boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		return presupuestoExclusionesInclusionesMundo.relacionarConInclusiones(dtoDcto, encabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#actualizarIncluPresuEncabezado(com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public boolean actualizarIncluPresuEncabezado(
			IncluPresuEncabezado incluPresuEncabezado) {
	
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.actualizarIncluPresuEncabezado(incluPresuEncabezado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#cargarEncabezadoInclusion(long)
	 */
	@Override 
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado) {
	
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.cargarEncabezadoInclusion(codigoIncluPresuEncabezado);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#calcularTotalInclusion(int)
	 */
	@Override 
	public DtoTotalesContratarInclusion calcularTotalInclusion(long codigoPkEncabezadoInclusion, int codigoIngreso) throws IPSException
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.calcularTotalInclusion(codigoPkEncabezadoInclusion, codigoIngreso);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#obtenerListadoSumatoriaConvenios(java.util.ArrayList, int)
	 */
	@Override
	public ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoSumatoriaConvenios(ArrayList<DtoRegistroContratarInclusion> registrosInclusionDetalle,int codigoIngreso)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.obtenerListadoSumatoriaConvenios(registrosInclusionDetalle, codigoIngreso);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#guardarInclusiones(com.princetonsa.dto.odontologia.DtoContratarInclusion)
	 */
	@Override
	public ResultadoBoolean guardarInclusiones(
			DtoContratarInclusion dtoContratarInclusion) throws IPSException{
	
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.guardarInclusiones(dtoContratarInclusion);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#anularSolicitudDescuentoOdontologico(java.math.BigDecimal, java.lang.String, int)
	 */
	@Override
	public ResultadoBoolean anularSolicitudDescuentoOdontologico(BigDecimal codigoPresupuestoDctoOdon, String loginUsuario, int codigoInstitucion) {
	

		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.anularSolicitudDescuentoOdontologico(codigoPresupuestoDctoOdon, loginUsuario, codigoInstitucion);
	}
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio#eliminarDetalleInclusiones(long)
	 */
	@Override
	public boolean eliminarDetalleInclusiones(long encabezadoInclusionPresupuesto)
	{
		IPresupuestoExclusionesInclusionesMundo presupuestoExclusionesInclusionesMundo = PresupuestoFabricaMundo.crearPresupuestoExclusionesInclusionesMundo();
		
		return presupuestoExclusionesInclusionesMundo.eliminarDetalleInclusiones(encabezadoInclusionPresupuesto);
	}
	
}
