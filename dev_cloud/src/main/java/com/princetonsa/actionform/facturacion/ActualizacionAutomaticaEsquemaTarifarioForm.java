package com.princetonsa.actionform.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.action.facturacion.ActualizacionAutomaticaEsquemaTarifarioAction;
import com.princetonsa.mundo.facturacion.ActualizacionAutomaticaEsquemaTarifario;

public class ActualizacionAutomaticaEsquemaTarifarioForm extends ValidatorForm 
{
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ActualizacionAutomaticaEsquemaTarifarioForm.class);
	
	private String estado;
	
	private HashMap<String, Object> mapaConvenio;
	
	private HashMap<String, Object> mapaEsquemaInventario;
	
	private HashMap<String, Object> mapaEsquemaServicio;
	
	private HashMap<String, Object> mapaConsultaInventarios;
	
	private HashMap<String, Object> mapaConsultaServicios;
	
	private ResultadoBoolean mostrarMensaje;
	
	private int indiceEliminar;
	
	private int indiceInventario;
	
	private int indiceServicio;
	
	
	private String convenio;
	
	private String contrato;
	
	private String empresa;
	
	private String esquemaServicios;
	
	private String esquemaInventarios;
	
	private String tipoConvenio;
	
	
	
	/**
	 *
	 */
	public void reset()
	{
		this.estado="";
		
		this.mapaConvenio=new HashMap<String, Object>();
		this.mapaConvenio.put("numRegistros", "0");
		this.mapaEsquemaInventario=new HashMap<String, Object>();
		this.mapaEsquemaInventario.put("numRegistros", "0");
		this.mapaEsquemaServicio=new HashMap<String, Object>();
		this.mapaEsquemaServicio.put("numRegistros", "0");
		this.mapaConsultaInventarios=new HashMap<String, Object>();
		this.mapaConsultaInventarios.put("numRegistros", "0");
		this.mapaConsultaServicios=new HashMap<String, Object>();
		this.mapaConsultaServicios.put("numRegistros", "0");
		this.indiceEliminar=ConstantesBD.codigoNuncaValido;
		this.indiceInventario=ConstantesBD.codigoNuncaValido;
		this.indiceServicio=ConstantesBD.codigoNuncaValido;
		
		this.convenio="";
		this.contrato="";
		this.empresa="";
		this.esquemaServicios="";
		this.esquemaInventarios="";
		this.tipoConvenio="";
	}
	
	/**
	 *
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje=new ResultadoBoolean(false,"");
	}
	
	
	/**
	 * 
	 */
	public void resetBusqueda()
	{
		this.mapaConvenio=new HashMap<String, Object>();
		this.mapaConvenio.put("numRegistros", "0");
		this.convenio="";
		this.contrato="";
		this.empresa="";
		this.esquemaServicios="";
		this.esquemaInventarios="";
		this.tipoConvenio="";
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		ActualizacionAutomaticaEsquemaTarifario mundo= new ActualizacionAutomaticaEsquemaTarifario();
		
		Connection con=UtilidadBD.abrirConexion();
		
		if(this.estado.equals("guardar"))
		{
			int numRegCon=Integer.parseInt(this.mapaConvenio.get("numRegistros")+"");
			
			int numRegInv=Integer.parseInt(this.mapaEsquemaInventario.get("numRegistros")+"");
			
			int numRegSer=Integer.parseInt(this.mapaEsquemaServicio.get("numRegistros")+"");
			
			this.setMapaConsultaServicios(mundo.consultaServicios(con));
			
			this.setMapaConsultaInventarios(mundo.consultaInventarios(con));
			
			for(int t=0;t<numRegCon;t++)
			{
				if((this.mapaConvenio.get("seleccionado_"+t)+"").equals(ConstantesBD.acronimoSi))
				{	
					for(int j=0;j<numRegSer;j++)
					{
				
						for(int k=0;k<Integer.parseInt(this.getMapaConsultaServicios("numRegistros")+"");k++)
						{
							
							if((this.getMapaConsultaServicios("contrato_"+k)+"").equals(this.getMapaConvenio("contrato_"+t)+"")&&(this.getMapaConsultaServicios("gruposervicio_"+k)+"").equals(this.getMapaEsquemaServicio("gruposervicio_"+j)+"")&&(this.getMapaConsultaServicios("esquematarifario_"+k)+"").equals(this.getMapaEsquemaServicio("esquemaservicio_"+j)+"")&&(this.getMapaConsultaServicios("fechavigencia_"+k)+"").equals(UtilidadFecha.conversionFormatoFechaABD(this.getMapaEsquemaServicio("fechaservicio_"+j)+"")))
							{
								
								errores.add("",new ActionMessage("errors.notEspecific","Para el Convenio "+this.getMapaConsultaServicios("nombreconvenio_"+k)+" existen Esquemas Tarifarios con la misma Fecha de Vigencia y Grupo de Servicios, por favor verifique."));
								
							}	
						}
					}
					for(int j=0;j<numRegInv;j++)
					{
						
						for(int k=0;k<Integer.parseInt(this.getMapaConsultaInventarios("numRegistros")+"");k++)
						{
							
							if((this.getMapaConsultaInventarios("contrato_"+k)+"").equals(this.getMapaConvenio("contrato_"+t)+"")&&(this.getMapaConsultaInventarios("claseinventario_"+k)+"").equals(this.getMapaEsquemaInventario("claseinventario_"+j)+"")&&(this.getMapaConsultaInventarios("esquematarifario_"+k)+"").equals(this.getMapaEsquemaInventario("esquemainventario_"+j)+"")&&(this.getMapaConsultaInventarios("fechavigencia_"+k)+"").equals(UtilidadFecha.conversionFormatoFechaABD(this.getMapaEsquemaInventario("fechainventario_"+j)+"")))
							{
								
								errores.add("",new ActionMessage("errors.notEspecific","Para el Convenio "+this.getMapaConsultaInventarios("nombreconvenio_"+k)+" existen Esquemas Tarifarios con la misma Fecha de Vigencia y Clase de Inventarios, por favor verifique."));
								
							}	
						}
					}
				}
			}	
			for(int i=0;i<numRegCon;i++)
			{
				
				if((this.mapaConvenio.get("convenio_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Convenio del registro "+(i+1)));
				}
				
				if((this.mapaConvenio.get("contrato_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Contrato Asociado del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if(((this.mapaConvenio.get("convenio_"+i)+"").equalsIgnoreCase(this.mapaConvenio.get("convenio_"+j)+""))&&((this.mapaConvenio.get("contrato_"+i)+"").equalsIgnoreCase(this.mapaConvenio.get("contrato_"+j)+"")))
						{
							errores.add("", new ActionMessage("error.facturacion.conveniosDuplicados"));
						}
					}
				}
			}
			for(int i=0;i<numRegInv;i++)
			{
				
				if((this.mapaEsquemaInventario.get("esquemainventario_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Esquema Tarifario de Inventarios del registro "+(i+1)));
				}
				
				if((this.mapaEsquemaInventario.get("fechainventario_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del Esquema Tarifario de Inventarios del registro "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getMapaEsquemaInventario("fechainventario_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getMapaEsquemaInventario("fechainventario_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia del Esquema Tarifario de Inventarios "+this.getMapaEsquemaInventario("fechainventario_"+i)));
						centinelaErrorFechas=true;
					}
					//Se comentarea la siguiente validación, según la Tarea 65222
					/*if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getMapaEsquemaInventario("fechainventario_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia del Esquema Tarifario de Inventarios "+this.getMapaEsquemaInventario("fechainventario_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
					}*/
				}
			}
			for(int i=0;i<numRegSer;i++)
			{
				
				if((this.mapaEsquemaServicio.get("esquemaservicio_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Esquema Tarifario de Servicios del registro "+(i+1)));
				}
				
				if((this.mapaEsquemaServicio.get("fechaservicio_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del Esquema Tarifario de Servicios del registro "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getMapaEsquemaServicio("fechaservicio_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getMapaEsquemaServicio("fechaservicio_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "de Vigencia del Esquema Tarifario de Servicios "+this.getMapaEsquemaServicio("fechaservicio_"+i)));
						centinelaErrorFechas=true;
					}
					//Se comentarea la siguiente validación, según la Tarea 65222
					/*if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getMapaEsquemaServicio("fechaservicio_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia del Esquema Tarifario de Servicios "+this.getMapaEsquemaServicio("fechaservicio_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
					}*/
				}
			}
			logger.info(">>> Convenio = " + convenio +
					"\n>>> Contrato = " + contrato +
					"\n>>> Num Registros Servicios = " +numRegSer+
					"\n>>> Num Registros Artículos = "+numRegInv+
					"\n>>> Num registros convenio = "+numRegCon);
			/*
			 * para poder realizar actualización debe por lo menos seleccionar un esquema tariafario de servicios o de invesntairius
			 */
			if(numRegSer == 0 && numRegInv == 0)
			{
				errores.add("contrato", new ActionMessage("errors.required", "La selección de un esquema tarifario"));
			}
			
			int tmpInt= Integer.parseInt(mapaConvenio.get("numRegistros")+"");
			if(tmpInt<=0)
			{
				errores.add("contrato", new ActionMessage("errors.required", "El Convenio/Contrato"));
			}
		}
		
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public HashMap<String, Object> getMapaConvenio() {
		return mapaConvenio;
	}

	/**
	 * 
	 * @param mapaConvenio
	 */
	public void setMapaConvenio(HashMap<String, Object> mapaConvenio) {
		this.mapaConvenio = mapaConvenio;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaEsquemaInventario() {
		return mapaEsquemaInventario;
	}

	/**
	 * 
	 * @param mapaEsquemaInventario
	 */
	public void setMapaEsquemaInventario(
			HashMap<String, Object> mapaEsquemaInventario) {
		this.mapaEsquemaInventario = mapaEsquemaInventario;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaEsquemaServicio() {
		return mapaEsquemaServicio;
	}

	/**
	 * 
	 * @param mapaEsquemaServicio
	 */
	public void setMapaEsquemaServicio(HashMap<String, Object> mapaEsquemaServicio) {
		this.mapaEsquemaServicio = mapaEsquemaServicio;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConvenio(String key) {
		return mapaConvenio.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConvenio(String key,Object value) {
		this.mapaConvenio.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaEsquemaInventario(String key) {
		return mapaEsquemaInventario.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaEsquemaInventario(String key,Object value) {
		this.mapaEsquemaInventario.put(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaEsquemaServicio(String key) {
		return mapaEsquemaServicio.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaEsquemaServicio(String key,Object value) {
		this.mapaEsquemaServicio.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaInventarios() {
		return mapaConsultaInventarios;
	}

	/**
	 * 
	 * @param mapaConsultaInventarios
	 */
	public void setMapaConsultaInventarios(
			HashMap<String, Object> mapaConsultaInventarios) {
		this.mapaConsultaInventarios = mapaConsultaInventarios;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaInventarios(String key) {
		return mapaConsultaInventarios.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaInventarios(String key,Object value) {
		this.mapaConsultaInventarios.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaServicios() {
		return mapaConsultaServicios;
	}

	/**
	 * 
	 * @param mapaConsultaServicios
	 */
	public void setMapaConsultaServicios(
			HashMap<String, Object> mapaConsultaServicios) {
		this.mapaConsultaServicios = mapaConsultaServicios;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaServicios(String key) {
		return mapaConsultaServicios.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaServicios(String key,Object value) {
		this.mapaConsultaServicios.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceEliminar() {
		return indiceEliminar;
	}

	/**
	 * 
	 * @param indiceEliminar
	 */
	public void setIndiceEliminar(int indiceEliminar) {
		this.indiceEliminar = indiceEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceInventario() {
		return indiceInventario;
	}

	/**
	 * 
	 * @param indiceInventario
	 */
	public void setIndiceInventario(int indiceInventario) {
		this.indiceInventario = indiceInventario;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceServicio() {
		return indiceServicio;
	}

	/**
	 * 
	 * @param indiceServicio
	 */
	public void setIndiceServicio(int indiceServicio) {
		this.indiceServicio = indiceServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * 
	 * @param convenio
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * 
	 * @return
	 */
	public String getContrato() {
		return contrato;
	}

	/**
	 * 
	 * @param contrato
	 */
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEmpresa() {
		return empresa;
	}
	
	/**
	 * 
	 * @param empresa
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getEsquemaServicios() {
		return esquemaServicios;
	}
	
	/**
	 * 
	 * @param esquemaServicios
	 */
	public void setEsquemaServicios(String esquemaServicios) {
		this.esquemaServicios = esquemaServicios;
	}

	/**
	 * 
	 * @return
	 */
	public String getEsquemaInventarios() {
		return esquemaInventarios;
	}
	
	/**
	 * 
	 * @param esquemaInventarios
	 */
	public void setEsquemaInventarios(String esquemaInventarios) {
		this.esquemaInventarios = esquemaInventarios;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipoConvenio() {
		return tipoConvenio;
	}
	
	/**
	 * 
	 * @param tipoConvenio
	 */
	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}
	
	
}
