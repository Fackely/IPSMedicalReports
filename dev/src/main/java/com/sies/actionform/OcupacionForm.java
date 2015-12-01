/*
 * Creado 03/04/07
 */

package com.sies.actionform;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.orm.TiposVinculacion;
import com.servinte.axioma.orm.Turno;

/**
 * @author mono 
 */
public class OcupacionForm extends ActionForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código de la ocupación
	 */
	private int codigo;
	
	/**
	 * Nombre de la ocupación
	 */
	private String nombre;
	
	/**
	 * Relacioncon el codigo
	 */
	private int ocupacion;
	
	/**
	 * Valor salario base
	 */
	private float valor;
	
	/**
	 * Fecha Inicio
	 */
	private String fecha_inicio;
	
	/**
	 * Fecha Fin
	 */
	private String fecha_fin;
	
	/**
	 * Tipo de Vinculacion
	 */
	private int tipo_vinculacion;
	
	private HashMap<String, Object> mapa;
	
	private String estado;
	
	private List<OcupacionesMedicas> listadoOcupacionesMedicas;
	
	private List<TiposVinculacion> listadoTiposVinculacion;
	
	private List<Turno> listadoTurnos;
	
	private String mensajeAccion;
	
	
	public void clean()
	{
		codigo=0;
		nombre="";
		ocupacion=0;
		valor=0;
		fecha_inicio="";
		fecha_fin="";
		mapa=new HashMap<String, Object>();
		mapa.put("numeroOtrosTipos", "0");
		this.mensajeAccion="";
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores= new ActionErrors();
		
		//System.out.println("Estado "+estado);

		if (estado.equalsIgnoreCase("guardarNuevo") || estado.equals("guardarModificacion"))
		{
			if (this.nombre.equals(""))
			{
				errores.add("Campo Nombre vacio", new ActionMessage("errors.required", "El campo Nombre"));
			}
			
			if (this.listadoTurnos.equals(null))
			{
				errores.add("Seleccion de Turnos vacio", new ActionMessage("errors.required", "Los turnos"));
			}
			
			int numeroSalarios = Integer.parseInt((String)getMapa().get("numeroOtrosTipos"));
			for(int i=0; i<numeroSalarios ;i++)
			{
				int tv=Integer.parseInt((String)getMapa().get("tipoVinculacion_"+i));
				if (tv==0 )
				{
					errores.add("Tipos de Vinculacion vacio", new ActionMessage("errors.required", "El campo Tipo Vinculacion"));
				}
				
				//BigDecimal salario=new BigDecimal(Double.parseDouble((String)getMapa().get("salario_vinculacion_"+i)));
				Double salario=new Double((String)getMapa().get("salarioBase_"+i));
				try
				{
					if(salario==0)
					{
						errores.add("Campo Salario Base vacio", new ActionMessage("errors.required", "El campo Salario Base"));
					}
								
					SimpleDateFormat dfinicio= new SimpleDateFormat("dd/mm/yyyy");
					Date fechaInicio=new Date(dfinicio.parse((String)getMapa().get("fecha_inicio_"+i)).getTime());
					if (fechaInicio.equals(""))
					{
						errores.add("Fecha de Inicio", new ActionMessage("errors.required", "El campo Fecha Inicio"));
					}
					
					SimpleDateFormat dffin= new SimpleDateFormat("dd/mm/yyyy");
					Date fechaFin=new Date(dffin.parse((String)getMapa().get("fecha_fin_"+i)).getTime());
					if (fechaFin.equals(""))
					{
						errores.add("Fecha de Fin", new ActionMessage("errors.required", "El campo Fecha Fin"));
					}
				}
				catch (Exception e) {
					//handle exception
				}
			}
			
			if(!errores.isEmpty())
			{
				if(estado.equals("guardarNuevo"))
					estado="ingresarNuevo";
				else if(estado.equals("guardarModificacion"))
					estado="modificarEleccion";
				else
					estado="consultar";
			}
		}
		return errores;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the fecha_fin
	 */
	public String getFecha_fin() {
		return fecha_fin;
	}

	/**
	 * @param fecha_fin the fecha_fin to set
	 */
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	/**
	 * @return the fecha_inicio
	 */
	public String getFecha_inicio() {
		return fecha_inicio;
	}

	/**
	 * @param fecha_inicio the fecha_inicio to set
	 */
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the ocupacion
	 */
	public int getOcupacion() {
		return ocupacion;
	}

	/**
	 * @param ocupacion the ocupacion to set
	 */
	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * @return the valor
	 */
	public float getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(int valor) {
		this.valor = valor;
	}


	public int getTipo_vinculacion() {
		return tipo_vinculacion;
	}

	public void setTipo_vinculacion(int tipo_vinculacion) {
		this.tipo_vinculacion = tipo_vinculacion;
	}

	public List<OcupacionesMedicas> getListadoOcupacionesMedicas() {
		return listadoOcupacionesMedicas;
	}

	public void setListadoOcupacionesMedicas(
			List<OcupacionesMedicas> listadoOcupacionesMedicas) 
	{
		this.listadoOcupacionesMedicas = listadoOcupacionesMedicas;
	}

	public List<TiposVinculacion> getListadoTiposVinculacion() {
		return listadoTiposVinculacion;
	}

	public void setListadoTiposVinculacion(
			List<TiposVinculacion> listadoTiposVinculacion) {
		this.listadoTiposVinculacion = listadoTiposVinculacion;
	}

	public HashMap<String, Object> getMapa() {
		return mapa;
	}

	public void setMapa(HashMap<String, Object> mapa) {
		this.mapa = mapa;
	}

	public List<Turno> getListadoTurnos() {
		return listadoTurnos;
	}

	public void setListadoTurnos(List<Turno> listadoTurnos) {
		this.listadoTurnos = listadoTurnos;
	}

	public String getMensajeAccion() {
		return mensajeAccion;
	}

	public void setMensajeAccion(String mensajeAccion) {
		this.mensajeAccion = mensajeAccion;
	}
}
