package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.reportes.InfoEncabezadoReporte;

import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoOdontograma;

/**
 * Forma de la consulta de odontograma
 * 
 * @author Ronald Angel 
 * @author Edgar Carvajal 
 *
 * Apr 27, 2010 - 4:23:32 PM
 */
public class ConsultaOdontogramaForm extends ValidatorForm
{
	/**
	 * serial
	 */
	private static final long serialVersionUID = -8516716703783975628L;

	/**
	 * estado de la forma
	 */
	private String estado;
	
	/**
	 * lista de ingresos pagina inicial (ingreso, centro atencion, fecha, hora, estado ingreso)
	 */
	private ArrayList<DtoOtrosIngresosPaciente> arrayIngresos;
	
	/**
	 * lista de odontogramas x ingreso seleccionado, contiene 
	 */
	private ArrayList<DtoOdontograma> arrayOdontogramas;
	
	/**
	 * Carga el plan de tratamiento inicial ligado al odontograma seleccionado
	 */
	private InfoPlanTratamiento dtoPlanTratamiento;
	
	/**
	 * Carga el odontolograma seleccionado de la lista 
	 */
	private DtoOdontograma dtoOdontograma;
	
	/**
	 * posicion seleccionada del ingreso
	 */
	private int posArray;
	
	/**
	 * posicion seleccionada odontologram 
	 */
	private int posArrayDetalle;
	
    /**
     * tipo relacion "Programa" - "Servicio" 
     */
	private String tipoRelacion;
	
	
	private boolean utilizaProgramas;
	
	
	
	private ArrayList<InfoProgramaServicioPlan> listaProgramasProximaCita = new ArrayList<InfoProgramaServicioPlan>();
	
	
	/**
	 * FACHADA QUE TIENE TODOS LOS DATOS GENERALES DEL ENCACABEZADO DEL REPORTE
	 */
	private InfoEncabezadoReporte infoEncabezadoReporte;
	
	
	/**
	 * LISTA DE SERVICIOS  PARA MOSTRAR LOS SERVICIO DEL PLAN DE TRATAMIENTO EVOLUCIONADO
	 */
	private ArrayList<InfoProgramaServicioPlan> listaProgramasServicios;
	
	
	
	/**
	 *ATRIBUTO AYUDANTE CARA CARGA LA FECHA DE LA CITA 
	 */
	private String fechaCita;
	
	
	
	
	//////////////////////////////// INFORMACION PROFESIONAL 	
	/**
	 * NOMBRE COMPLETO PROFESIONAL 
	 */
	private String nombreCompletoProfesional;
	
	/**
	 * LISTA ESPECIALIDES PROFESIONAL 
	 */
	private String listaEspecialidadesProfesional;
	
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private String fechaProceso;
	
	
	
	
	
	
	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() 
	{
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the fechaProceso
	 */
	public String getFechaProceso() 
	{
		this.fechaProceso=UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual());
		return fechaProceso;
	}

	/**
	 * @param fechaProceso the fechaProceso to set
	 */
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	/**
	 * 
	 * Metodo para resetar
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 */
	public void  reset()
	{
		this.arrayOdontogramas = new ArrayList<DtoOdontograma>();	
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
		this.dtoOdontograma = new DtoOdontograma();
		this.arrayIngresos = new ArrayList<DtoOtrosIngresosPaciente>();
		this.dtoPlanTratamiento = new InfoPlanTratamiento();
		this.tipoRelacion = "";
		this.setUtilizaProgramas(Boolean.TRUE);
		this.setListaProgramasProximaCita(new ArrayList<InfoProgramaServicioPlan>());
		this.setInfoEncabezadoReporte(new InfoEncabezadoReporte());
		this.listaProgramasServicios= new ArrayList<InfoProgramaServicioPlan>();
		this.fechaCita="";
		this.nombreCompletoProfesional="";
		this.listaEspecialidadesProfesional="";
		this.usuarioModifica="";
		this.fechaProceso="";
		
	}

	/**
	 * @return the estado
	 */
	public String getEstado() 
	{
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) 
	{
		this.estado = estado;
	}

	/**
	 * @return the arrayOdontogramas
	 */
	public ArrayList<DtoOdontograma> getArrayOdontogramas() {
		return arrayOdontogramas;
	}

	/**
	 * @param arrayOdontogramas the arrayOdontogramas to set
	 */
	public void setArrayOdontogramas(ArrayList<DtoOdontograma> arrayOdontogramas) {
		this.arrayOdontogramas = arrayOdontogramas;
	}

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the posArrayDetalle
	 */
	public int getPosArrayDetalle() {
		return posArrayDetalle;
	}

	/**
	 * @param posArrayDetalle the posArrayDetalle to set
	 */
	public void setPosArrayDetalle(int posArrayDetalle) {
		this.posArrayDetalle = posArrayDetalle;
	}

	/**
	 * @return the dtoOdontograma
	 */
	public DtoOdontograma getDtoOdontograma() {
		return dtoOdontograma;
	}

	/**
	 * @param dtoOdontograma the dtoOdontograma to set
	 */
	public void setDtoOdontograma(DtoOdontograma dtoOdontograma) {
		this.dtoOdontograma = dtoOdontograma;
	}

	/**
	 * @return the arrayIngresos
	 */
	public ArrayList<DtoOtrosIngresosPaciente> getArrayIngresos() {
		return arrayIngresos;
	}

	/**
	 * @param arrayIngresos the arrayIngresos to set
	 */
	public void setArrayIngresos(ArrayList<DtoOtrosIngresosPaciente> arrayIngresos) {
		this.arrayIngresos = arrayIngresos;
	}

	/**
	 * @return the dtoPlanTratamiento
	 */
	public InfoPlanTratamiento getDtoPlanTratamiento() {
		return dtoPlanTratamiento;
	}

	/**
	 * @param dtoPlanTratamiento the dtoPlanTratamiento to set
	 */
	public void setDtoPlanTratamiento(InfoPlanTratamiento dtoPlanTratamiento) {
		this.dtoPlanTratamiento = dtoPlanTratamiento;
	}

	/**
	 * @return the tipoRelacion
	 */
	public String getTipoRelacion() {
		return tipoRelacion;
	}

	/**
	 * @param tipoRelacion the tipoRelacion to set
	 */
	public void setTipoRelacion(String tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}

	public void setUtilizaProgramas(boolean utilizaProgramas) {
		this.utilizaProgramas = utilizaProgramas;
	}

	public boolean isUtilizaProgramas() {
		return utilizaProgramas;
	}
	
	
	public boolean getUtilizaProgramas() {
		return utilizaProgramas;
	}
	
	/**
	 * 
	 * @param i
	 * @param y
	 * @return
	 */
	public String nombreSuperficie(int i, int y)
	{
		return this.getDtoPlanTratamiento().getSeccionHallazgosDetalle().get(i).getDetalleSuperficie().get(y).getSuperficieOPCIONAL().getNombre();
	}

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param listaProgramasProximaCita
	 */
	public void setListaProgramasProximaCita(ArrayList<InfoProgramaServicioPlan> listaProgramasProximaCita) {
		this.listaProgramasProximaCita = listaProgramasProximaCita;
	}

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public ArrayList<InfoProgramaServicioPlan> getListaProgramasProximaCita() {
		return listaProgramasProximaCita;
	}

	public void setInfoEncabezadoReporte(InfoEncabezadoReporte infoEncabezadoReporte) {
		this.infoEncabezadoReporte = infoEncabezadoReporte;
	}

	public InfoEncabezadoReporte getInfoEncabezadoReporte() {
		return infoEncabezadoReporte;
	}

	public void setListaProgramasServicios(ArrayList<InfoProgramaServicioPlan> listaProgramasServicios) {
		this.listaProgramasServicios = listaProgramasServicios;
	}

	public ArrayList<InfoProgramaServicioPlan> getListaProgramasServicios() {
		return listaProgramasServicios;
	}

	public void setFechaCita(String fechaCita) {
		this.fechaCita = fechaCita;
	}

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getFechaCita() {
		if(! UtilidadTexto.isEmpty(this.fechaCita) )
		{	
			this.fechaCita=UtilidadFecha.conversionFormatoFechaAAp(this.fechaCita);
		}
		
		return fechaCita;
	}

	public void setNombreCompletoProfesional(String nombreCompletoProfesional) {
		this.nombreCompletoProfesional = nombreCompletoProfesional;
	}

	public String getNombreCompletoProfesional() {
		return nombreCompletoProfesional;
	}

	public void setListaEspecialidadesProfesional(
			String listaEspecialidadesProfesional) {
		this.listaEspecialidadesProfesional = listaEspecialidadesProfesional;
	}

	public String getListaEspecialidadesProfesional() {
		return listaEspecialidadesProfesional;
	}
	

}