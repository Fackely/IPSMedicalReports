package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadValidacion;
import util.Utilidades;

public class ValoresTipoReporteForm extends ValidatorForm 
{
	
	
	private String estado;
	
	private String codigoModulo;
	
	private String codigoFuncionalidad;
	
	private String codigoReporte;
	
	private HashMap mapaInfoReporte;
	
	private HashMap mapaConsultaParametrizado;
	
	private HashMap mapaEliminado;
	
	private int indiceEliminado;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.estado="";
		this.codigoModulo="";
		this.codigoFuncionalidad="";
		this.codigoReporte="";
		this.mapaInfoReporte= new HashMap();
		this.mapaInfoReporte.put("numRegistros", "0");
		this.mapaConsultaParametrizado= new HashMap();
		this.mapaConsultaParametrizado.put("numRegistros", "0");
		this.mapaEliminado= new HashMap();
		this.mapaEliminado.put("numRegistros", "0");
		this.indiceEliminado= ConstantesBD.codigoNuncaValido;
	}
	
	
	public void resetMensaje()
	{
		this.mensaje= new ResultadoBoolean(false);
	}
	
	
	
	/**
	 *  Validate the properties that have been set from this HTTP request, and
	 *  return an <code>ActionErrors</code> object that encapsulates any 
	 *  validation errors that have been found. If no errors are found, return
	 *  <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 *  error messages.
	 *  @param mapping The mapping used to select this instance
	 *  @param request The servlet request we are processing
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.mapaConsultaParametrizado.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.mapaConsultaParametrizado.get("orden_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Orden del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.mapaConsultaParametrizado.get("orden_"+i)+"").equalsIgnoreCase(this.mapaConsultaParametrizado.get("orden_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El Orden "+this.mapaConsultaParametrizado.get("orden_"+i)));
						}
					}
				}
				if((this.mapaConsultaParametrizado.get("nombreetiqueta_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","La Etiqueta del registro "+(i+1)));
				}
				if((this.mapaConsultaParametrizado.get("rangoinicial_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Rango Inicial del registro "+(i+1)));
				}
				if((this.mapaConsultaParametrizado.get("rangofinal_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Rango Final del registro "+(i+1)));
				}
				if((this.mapaInfoReporte.get("tiporango_0")+"").equals(ConstantesIntegridadDominio.acronimoTipoRangoEdad)&&Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangofinal_"+i)+"")>42830)
				{
					errores.add("", new ActionMessage("errors.notEspecific","El rango final del registro "+(i+1)+" no puede ser mayor a 42830"));
				}
				if((this.mapaInfoReporte.get("tiporango_0")+"").equals(ConstantesIntegridadDominio.acronimoTipoRangoTiempo)&&Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangofinal_"+i)+"")>10080)
				{
					errores.add("", new ActionMessage("errors.notEspecific","El rango final del registro "+(i+1)+" no puede ser mayor a 10080"));
				}
				if(Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangoinicial_"+i)+"")>Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangofinal_"+i)+""))
				{
					errores.add("codigo", new ActionMessage("errors.notEspecific","El rango inicial del registro "+(i+1)+ " debe ser menor/igual que el rango final"));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						int rangoInicialA= Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangoinicial_"+i)+"");
						int rangoFinalA= Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangofinal_"+i)+"");
						int rangoInicialB= Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangoinicial_"+j)+"");
						int rangoFinalB= Utilidades.convertirAEntero(this.mapaConsultaParametrizado.get("rangofinal_"+j)+"");
						if(UtilidadValidacion.hayCruceNumeros(rangoInicialA, rangoFinalA, rangoInicialB, rangoFinalB))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Existen cruces de valores en rangos ingresados, entre el Nro. "+(i+1)+" y el Nro. "+(j+1)+". Favor Verificar."));
						}
						
					}
				}
			}
		}
		return errores;
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoModulo() {
		return codigoModulo;
	}

	/**
	 * 
	 * @param codigoModulo
	 */
	public void setCodigoModulo(String codigoModulo) {
		this.codigoModulo = codigoModulo;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoFuncionalidad() {
		return codigoFuncionalidad;
	}

	/**
	 * 
	 * @param codigoFuncionalidad
	 */
	public void setCodigoFuncionalidad(String codigoFuncionalidad) {
		this.codigoFuncionalidad = codigoFuncionalidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoReporte() {
		return codigoReporte;
	}

	/**
	 * 
	 * @param codigoReporte
	 */
	public void setCodigoReporte(String codigoReporte) {
		this.codigoReporte = codigoReporte;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaInfoReporte() {
		return mapaInfoReporte;
	}

	/**
	 * 
	 * @param mapaInfoReporte
	 */
	public void setMapaInfoReporte(HashMap mapaInfoReporte) {
		this.mapaInfoReporte = mapaInfoReporte;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultaParametrizado() {
		return mapaConsultaParametrizado;
	}

	/**
	 * 
	 * @param mapaConsultaParametrizado
	 */
	public void setMapaConsultaParametrizado(HashMap mapaConsultaParametrizado) {
		this.mapaConsultaParametrizado = mapaConsultaParametrizado;
	}
	
	/**
	 * 
	 * @param Key
	 * @return
	 */
	public Object getMapaConsultaParametrizado(String Key)
	{
		return mapaConsultaParametrizado.get(Key);
	}
	
	/**
	 * 
	 * @param Key
	 * @param value
	 */
	public void setMapaConsultaParametrizado(String Key,Object value)
	{
		this.mapaConsultaParametrizado.put(Key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaEliminado() {
		return mapaEliminado;
	}

	/**
	 * 
	 * @param mapaEliminado
	 */
	public void setMapaEliminado(HashMap mapaEliminado) {
		this.mapaEliminado = mapaEliminado;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * 
	 * @param indiceEliminado
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}

	/**
	 * 	
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
}
