package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class DevolucionInventariosPacienteForm extends ValidatorForm {
	
	/**
	 * Variable para manejar los estados posibles dentro de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa para manejar los ingreso/cuenta del paciente cargado
	 */
	private HashMap<String, Object> ingresoCuentaPaciente;
	
	/**
	 * Mapa para manejar los pedidos del paciente cargado
	 */
	private HashMap<String, Object> pedidos;
	
	/**
	 * Mapa para manejar el detalle de los pedidos del paciente
	 */
	private HashMap<String, Object> detallePedidos;
	
	/**
	 * Mapa para manejar las solicitudes del paciente cargado
	 */
	private HashMap<String, Object> solicitudes;
	
	/**
	 * Mapa para manejar el detalle de las solicitudes
	 */
	private HashMap<String, Object> detalleSolicitudes;
	
	/**
	 * Variable para manejar si la solicitud es facturada o no
	 */
	private String esFacturado;
	
	/**
	 * Variable para manejar si la solicitud tiene administracion o no
	 */
	private int administracion;
	
	/**
	 * Variable que habilita la casilla de cantidad a devolver
	 */
	private boolean devolver;
	
	/**
	 * Variable que maneja la cantidad a devolver del detalle de las solicitudes
	 */
	private String cantidad;
	
	/**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
	
	/**
	 * Variable para mostrar los mensajes
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Indice seleccionado del ingreso.
	 */
	private int indiceIngresoSeleccionado;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 *
	 */
	private String fecha;
	
	/**
	 * 
	 *
	 */
	private String hora;
	
	/**
	 * 
	 *
	 */
	private String motivo;
	
	/**
	 * 
	 *
	 */
	private String usuario;
	
	/**
	 * 
	 *
	 */
	private String despacho;
	
	/**
	 * 
	 *
	 */
	private String devolver_check;
	
	/**
	 * 
	 *
	 */
	private HashMap<String, Object> fechaHoraDespacho;
	
	/**
	 * 
	 *
	 */
	private int devolucion;
	
	/**
	 * 
	 */
	private HashMap<String, Object> devolucionMap;
	
	/**
	 * 
	 */
	private int indiceDetalle;
	/**
	 * 
	 */
	private HashMap<String, Object> detalleDevolucionMap;
	
	public void reset()
	{
		this.administracion=ConstantesBD.codigoNuncaValido;
		this.esFacturado="";
		this.devolver=false;
		this.cantidad="";
		this.indiceIngresoSeleccionado=ConstantesBD.codigoNuncaValido;
		this.observaciones="";
		this.fecha="";
		this.hora="";
		this.motivo="";
		this.usuario="";
		this.despacho="";
		this.devolver_check="";
		this.devolucion=ConstantesBD.codigoNuncaValido;
		
		this.detallePedidos=new HashMap<String, Object>();
		this.detallePedidos.put("numRegistros", "0");
		
		this.detalleSolicitudes=new HashMap<String, Object>();
		this.detalleSolicitudes.put("numRegistros", "0");
		
		this.ingresoCuentaPaciente=new HashMap<String, Object>();
		this.ingresoCuentaPaciente.put("numRegistros", "0");		
		
		this.pedidos=new HashMap<String, Object>();
		this.pedidos.put("numRegistros", "0");
		
		this.solicitudes=new HashMap<String, Object>();
		this.solicitudes.put("numRegistros", "0");
		
		this.fechaHoraDespacho=new HashMap<String, Object>();
		
		this.devolucionMap=new HashMap<String, Object>();
		this.devolucionMap.put("numRegistros", "0");
		
		this.detalleDevolucionMap=new HashMap<String, Object>();
		this.detalleDevolucionMap.put("numRegistros", "0");
		this.indiceDetalle=ConstantesBD.codigoNuncaValido;
	}
	
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("guardarDevolucion"))
		{
			if(this.getFecha().equals(""))
			{
				errores.add("fecha",new ActionMessage("errors.required","La fecha de devolución "));
			}
			if(this.getHora().equals(""))
			{
				errores.add("hora",new ActionMessage("errors.required","La hora de devolución "));
			}
			if(this.getMotivo().equals(""))
			{
				errores.add("motivo",new ActionMessage("errors.required","El motivo de devolución "));
			}
			if(this.getObservaciones().equals(""))
			{
				errores.add("observaciones",new ActionMessage("errors.required","La observacion de devolución "));
			}
			int numReg=Utilidades.convertirAEntero(this.detalleSolicitudes.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(this.detalleSolicitudes.get("devolver_"+i).equals("true"))
				{
					if(this.detalleSolicitudes.get("cantidaddevolver_"+i).equals(""))
					{
						errores.add("cantidad",new ActionMessage("errors.required","La cantidad de devolución"));
					}
					else if(Utilidades.convertirAEntero(this.detalleSolicitudes.get("cantidaddevolver_"+i)+"") <= 0)
					{
			    			errores.add("Numero invalido",new ActionMessage("errors.caracterEspecial",this.detalleSolicitudes.get("cantidaddevolver_"+i)));
					}
					else if(Utilidades.convertirAEntero(this.detalleSolicitudes.get("cantidaddevolver_"+i)+"")>Utilidades.convertirAEntero(this.detalleSolicitudes.get("cantidad_"+i)+""))
					{
						errores.add("cantidad_menor_saldo",new ActionMessage("errors.numeroMenor",this.detalleSolicitudes.get("cantidad_"+i)));
					}
				}
			}
		}
		return errores;
	}

	public int getAdministracion() {
		return administracion;
	}

	public void setAdministracion(int administracion) {
		this.administracion = administracion;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public HashMap<String, Object> getDetallePedidos() {
		return detallePedidos;
	}

	public void setDetallePedidos(HashMap<String, Object> detallePedidos) {
		this.detallePedidos = detallePedidos;
	}

	public HashMap<String, Object> getDetalleSolicitudes() {
		return detalleSolicitudes;
	}

	public void setDetalleSolicitudes(HashMap<String, Object> detalleSolicitudes) {
		this.detalleSolicitudes = detalleSolicitudes;
	}

	public boolean isDevolver() {
		return devolver;
	}

	public void setDevolver(boolean devolver) {
		this.devolver = devolver;
	}

	public String getEsFacturado() {
		return esFacturado;
	}

	public void setEsFacturado(String esFacturado) {
		this.esFacturado = esFacturado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap<String, Object> getIngresoCuentaPaciente() {
		return ingresoCuentaPaciente;
	}

	public void setIngresoCuentaPaciente(
			HashMap<String, Object> ingresoCuentaPaciente) {
		this.ingresoCuentaPaciente = ingresoCuentaPaciente;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public HashMap<String, Object> getPedidos() {
		return pedidos;
	}

	public void setPedidos(HashMap<String, Object> pedidos) {
		this.pedidos = pedidos;
	}

	public HashMap<String, Object> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(HashMap<String, Object> solicitudes) {
		this.solicitudes = solicitudes;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public int getIndiceIngresoSeleccionado() {
		return indiceIngresoSeleccionado;
	}

	public void setIndiceIngresoSeleccionado(int indiceIngresoSeleccionado) {
		this.indiceIngresoSeleccionado = indiceIngresoSeleccionado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDespacho() {
		return despacho;
	}

	public void setDespacho(String despacho) {
		this.despacho = despacho;
	}

	public String getDevolver_check() {
		return devolver_check;
	}

	public void setDevolver_check(String devolver_check) {
		this.devolver_check = devolver_check;
	}

	public HashMap<String, Object> getFechaHoraDespacho() {
		return fechaHoraDespacho;
	}

	public void setFechaHoraDespacho(HashMap<String, Object> fechaHoraDespacho) {
		this.fechaHoraDespacho = fechaHoraDespacho;
	}

	public int getDevolucion() {
		return devolucion;
	}

	public void setDevolucion(int devolucion) {
		this.devolucion = devolucion;
	}

	public HashMap<String, Object> getDetalleDevolucionMap() {
		return detalleDevolucionMap;
	}

	public void setDetalleDevolucionMap(HashMap<String, Object> detalleDevolucionMap) {
		this.detalleDevolucionMap = detalleDevolucionMap;
	}

	public HashMap<String, Object> getDevolucionMap() {
		return devolucionMap;
	}

	public void setDevolucionMap(HashMap<String, Object> devolucionMap) {
		this.devolucionMap = devolucionMap;
	}

	public int getIndiceDetalle() {
		return indiceDetalle;
	}

	public void setIndiceDetalle(int indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}

}
