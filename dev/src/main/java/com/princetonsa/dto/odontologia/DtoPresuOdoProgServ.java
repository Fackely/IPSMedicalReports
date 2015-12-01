package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.princetonsa.enu.general.EnumTipoModificacion;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class DtoPresuOdoProgServ  implements Serializable, Comparable<DtoPresuOdoProgServ>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4485430451707746663L;
	
	/**
	 * 
	 */
	private	BigDecimal  codigoPk;
	private InfoDatosDouble programa;
	private InfoDatosInt servicio;
	private DtoInfoFechaUsuario usuarioModifica;
	private int cantidad;
	private BigDecimal presupuesto;
	private EnumTipoModificacion tipoModificacion;
	private DtoListaCalculoCantidadesPresupuesto listaCalculoCantidades;
	private String especialidad;
	private String contratado;
	
	/**
	 * Atributo que contiene el valor de la tarifa
	 * usado en el momento de guardar las inclusiones y exclusiones
	 */
	private BigDecimal valorTarifa;
	
	/**
	 * DETALLE PRESUPUESTO ODONTOLOGICO CONVENIO 
	 */
	private ArrayList<DtoPresupuestoOdoConvenio> listPresupuestoOdoConvenio = new ArrayList<DtoPresupuestoOdoConvenio>();  
	
	/**
	 * DETALLE PRESUPUESTO PIEZAS
	 */
	private ArrayList<DtoPresupuestoPiezas> listPresupuestoPiezas = new ArrayList<DtoPresupuestoPiezas>();
	
	/**
	 * inclusion
	 */
	private BigDecimal inclusion;
	
	/**
	 * 
	 */
	private boolean eliminar;
	
	
	/**
	 * Lista de recomendaicones asociadas los programas del presupuesto
	 */
	private DtoRecomendaciones recomendaciones;
	
	/**
	 * Codigo del Programa - Hallazgo - Pieza
	 */
	private BigDecimal codigoProgramaHallazgoPieza;
	
	
	/**
	 * 
	 * 
	 */
	public DtoPresuOdoProgServ() {
		reset();
	}
	
	/**
	 * 
	 * Metodo para reset
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public void reset()
	{
		this.codigoPk = new BigDecimal(0);
		this.programa = new InfoDatosDouble();
		this.servicio = new InfoDatosInt();
		this.usuarioModifica = new DtoInfoFechaUsuario();
		this.cantidad = ConstantesBD.codigoNuncaValido;
		this.listPresupuestoOdoConvenio = new ArrayList<DtoPresupuestoOdoConvenio>();
		this.listPresupuestoPiezas = new ArrayList<DtoPresupuestoPiezas>();
		this.presupuesto = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.tipoModificacion= EnumTipoModificacion.NUEVO;
		this.listaCalculoCantidades= new DtoListaCalculoCantidadesPresupuesto();
		this.especialidad= "";
		this.inclusion= BigDecimal.ZERO;
		this.eliminar= false;
		this.contratado="";
		this.recomendaciones = new DtoRecomendaciones();
		
		this.valorTarifa = BigDecimal.ZERO;
		
		this.setCodigoProgramaHallazgoPieza(new BigDecimal(ConstantesBD.codigoNuncaValido));
		
	}
	

	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	public InfoDatosDouble getPrograma() {
		return programa;
	}

	public void setPrograma(InfoDatosDouble programa) {
		this.programa = programa;
	}

	public InfoDatosInt getServicio() {
		return servicio;
	}

	public double getProgramaOServicio(boolean utilizaPrograma)
	{
		if(utilizaPrograma)
			return this.getPrograma().getCodigo();
		else
			return this.getServicio().getCodigo();
	}
	
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}

	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public int getCantidad() {
		return (cantidad-this.getCantidadPlanTratamientoNoPendiente());
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public ArrayList<DtoPresupuestoOdoConvenio> getListPresupuestoOdoConvenio() {
		return listPresupuestoOdoConvenio;
	}

	public void setListPresupuestoOdoConvenio(
			ArrayList<DtoPresupuestoOdoConvenio> listPresupuestoOdoConvenio) {
		this.listPresupuestoOdoConvenio = listPresupuestoOdoConvenio;
	}

	public ArrayList<DtoPresupuestoPiezas> getListPresupuestoPiezas() {
		return listPresupuestoPiezas;
	}

	public void setListPresupuestoPiezas(
			ArrayList<DtoPresupuestoPiezas> listPresupuestoPiezas) {
		this.listPresupuestoPiezas = listPresupuestoPiezas;
	}

	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}

	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	/**
	 * @return the tipoModificacion
	 */
	public EnumTipoModificacion getTipoModificacion()
	{
		return tipoModificacion;
	}

	/**
	 * @param tipoModificacion the tipoModificacion to set
	 */
	public void setTipoModificacion(EnumTipoModificacion tipoModificacion)
	{
		this.tipoModificacion = tipoModificacion;
	}

	/**
	 * @return the cantidadPlanTratamientoNoPendiente
	 */
	public int getCantidadPlanTratamientoNoPendiente()
	{
		int contador=0;
		ArrayList<String> arrayKey= new ArrayList<String>();
		boolean utilizaProgramas= this.getPrograma().getCodigo()>0;
		
		for(DtoPresupuestoPiezas dtoPieza: this.getListPresupuestoPiezas())
		{
			if(!dtoPieza.getActivo() && !arrayKey.contains(dtoPieza.getHallazgo()+"_"+this.getProgramaOServicio(utilizaProgramas)))
			{
				contador+=obtenerCantidadInactivaXHallazgoProgServ(dtoPieza.getHallazgo(), this.getProgramaOServicio(utilizaProgramas), utilizaProgramas, dtoPieza.getNumSuperficies());
				arrayKey.add(dtoPieza.getHallazgo()+"_"+this.getProgramaOServicio(utilizaProgramas));
			}
		}
		return contador;
	}

	/**
	 * 
	 * @param hallazgo
	 * @param programaOServicio
	 * @return
	 */
	private int obtenerCantidadInactivaXHallazgoProgServ(BigDecimal hallazgo,double programaOServicio, boolean utilizaProgramas, int numeroSuperficies) 
	{
		int retorna=0;
		int contador=0;
		for(DtoPresupuestoPiezas dtoPieza: this.getListPresupuestoPiezas())
		{
			if(!dtoPieza.getActivo() && dtoPieza.getHallazgo().doubleValue()== hallazgo.doubleValue() && this.getProgramaOServicio(utilizaProgramas)==programaOServicio)
			{
				contador++;
			}
		}
		if(numeroSuperficies>1)
		{
			if(contador==numeroSuperficies)
			{
				return 1;
			}
			else if(contador>numeroSuperficies) 	
			{	
				retorna= new BigDecimal( contador ).divide( new BigDecimal(numeroSuperficies), 0, RoundingMode.DOWN).intValue();
			}
		}
		else
		{
			retorna= contador;
		}
		
		return retorna;
	}

	/**
	 * @return the listaCalculoCantidades
	 */
	public DtoListaCalculoCantidadesPresupuesto getListaCalculoCantidades() {
		return listaCalculoCantidades;
	}

	/**
	 * @param listaCalculoCantidades the listaCalculoCantidades to set
	 */
	public void setListaCalculoCantidades(
			DtoListaCalculoCantidadesPresupuesto listaCalculoCantidades) {
		this.listaCalculoCantidades = listaCalculoCantidades;
	}

	/**
	 * 
	 * @return
	 */
	public int calcularCantidadModificada(boolean utilizaPrograma) 
	{
		int cantidadXServicio=0;
		for(DtoAgrupacionHallazgoSuperficie dto: this.getListaCalculoCantidades().getListaCalculoCantidades())
		{
			if(dto.getProgramaServicio().doubleValue()==this.getProgramaOServicio(utilizaPrograma) && !this.getPaquetizado())
			{
				cantidadXServicio+= dto.getCantidadCALCULADA();
			}
		}
		return cantidadXServicio;
	}

	/**
	 * 
	 * @return
	 */
	public int calcularCantidad(boolean utilizaPrograma) 
	{
		int cantidadXServicio=0;
		for(DtoAgrupacionHallazgoSuperficie dto: this.getListaCalculoCantidades().getListaCalculoCantidades())
		{
			if(dto.getProgramaServicio().doubleValue()==this.getProgramaOServicio(utilizaPrograma))
			{
				cantidadXServicio+= dto.getCantidadCALCULADA();
			}
		}
		return cantidadXServicio;
	}
	
	
	/**
	 * @return the especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	@Override
	public int compareTo(DtoPresuOdoProgServ o) 
	{
		return this.getEspecialidad().compareTo(o.getEspecialidad());
	}

	/**
	 * @return the inclusion
	 */
	public BigDecimal getInclusion() {
		return inclusion;
	}

	/**
	 * @param inclusion the inclusion to set
	 */
	public void setInclusion(BigDecimal inclusion) {
		this.inclusion = inclusion;
	}

	/**
	 * 
	 * Metodo para validar la eliminacion de un programa que pertenezca a un paquete(s)
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String getErrorEliminarXPaquetes()
	{
		ArrayList<String> codigosPaquetes= new ArrayList<String>();
		String retorna="";
		for(DtoPresupuestoOdoConvenio dto: this.getListPresupuestoOdoConvenio())
		{
			if(!dto.getDtoPresupuestoPaquete().getCodigoPaqueteMostrar().isEmpty() 
				&& !codigosPaquetes.contains(dto.getDtoPresupuestoPaquete().getCodigoPaqueteMostrar()))
			{
				codigosPaquetes.add(dto.getDtoPresupuestoPaquete().getCodigoPaqueteMostrar());
			}
		}
		if(codigosPaquetes.size()>0)
		{
			retorna= "El programa a eliminar pertenece al paquete(s) "+UtilidadTexto.convertirArrayStringACodigosSeparadosXComasSinComillas(codigosPaquetes)+" asignado al presupuesto. Debe eliminar primero el paquete del presupuesto para permitir eliminar el programa";
		}
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para validar si fue o no paquetizado 
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean getPaquetizado()
	{
		boolean retorna= false;
		for(DtoPresupuestoOdoConvenio dto: this.getListPresupuestoOdoConvenio())
		{
			if(dto.getDtoPresupuestoPaquete().getDetallePaqueteOdontologicoConvenio()>0)
			{
				retorna= true;
				break;
			}
		}
		return retorna;
	}

	
	/**
	 * 
	 * Metodo para validar si fue o no paquetizado 
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public void resetPaquetizado()
	{
		for(DtoPresupuestoOdoConvenio dto: this.getListPresupuestoOdoConvenio())
		{
			dto.setDtoPresupuestoPaquete(new DtoPresupuestoPaquetes());
		}
	}

	
	/**
	 * 
	 * Metodo para verificar si el programa ya fue paquetizado para un convenio especifico
	 * @param convenio
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean getPaquetizadoXConvenio(int convenio) 
	{
		boolean retorna= false;
		for(DtoPresupuestoOdoConvenio dto: this.getListPresupuestoOdoConvenio())
		{
			if(dto.getConvenio().getCodigo()==convenio)
			{	
				if(dto.getDtoPresupuestoPaquete().getDetallePaqueteOdontologicoConvenio()>0)
				{
					retorna= true;
					break;
				}
			}	
		}
		return retorna;
	}

	/**
	 * @return the eliminar
	 */
	public boolean isEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(boolean eliminar) {
		this.eliminar = eliminar;
	}

	public String getContratado() {
		return contratado;
	}

	public void setContratado(String contratado) {
		this.contratado = contratado;
	}

	/**
	 * @return the recomendaciones
	 */
	public DtoRecomendaciones getRecomendaciones() {
		return recomendaciones;
	}

	/**
	 * @param recomendaciones the recomendaciones to set
	 */
	public void setRecomendaciones(DtoRecomendaciones recomendaciones) {
		this.recomendaciones = recomendaciones;
	}

	/**
	 * @param valorTarifa the valorTarifa to set
	 */
	public void setValorTarifa(BigDecimal valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	/**
	 * @return the valorTarifa
	 */
	public BigDecimal getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * @param codigoProgramaHallazgoPieza the codigoProgramaHallazgoPieza to set
	 */
	public void setCodigoProgramaHallazgoPieza(
			BigDecimal codigoProgramaHallazgoPieza) {
		this.codigoProgramaHallazgoPieza = codigoProgramaHallazgoPieza;
	}

	/**
	 * @return the codigoProgramaHallazgoPieza
	 */
	public BigDecimal getCodigoProgramaHallazgoPieza() {
		return codigoProgramaHallazgoPieza;
	}
	
	
}