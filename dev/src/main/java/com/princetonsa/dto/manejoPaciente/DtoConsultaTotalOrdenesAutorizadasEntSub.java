package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;

/**
 * Esta clase se encarga de contener los datos del resultado de la consulta  
 * de las ordenes autorizadas de ent Subcontratadas 
 * 
 * @author Camilo Gómez
 */
public class DtoConsultaTotalOrdenesAutorizadasEntSub implements Serializable,Comparable<DtoConsultaTotalOrdenesAutorizadasEntSub>{

	private static final long serialVersionUID = 1L;
	/**Atributo que almacena el estado de la autorizacion*/
	private String estadoAutorizacion;
	/**Atributo que almacena el codigo de la entidad sub*/
	private Long codigoEntidadSub;
	/**Atributo que almacena el nombre de la entidad sub*/
	private String 	nombreEntidadSub;
	/**Atributo que almacena el codigo del convenio*/
	private Integer codigoConvenio;
	/**Atributo que almacena el nombre del convenio*/
	private String 	nombreConvenio;
	/**Atributo que almacena la fecha de la autorizacion*/
	private Date fechaAutorizacion;
	/**Atributo que almacena la hora de la autorizacion*/
	private String horaAutorizacion;
	/**Atributo que almacena el consecutivo de la autorizacion*/
	private Long consecutivo;
	

	/**Atributo que almacena el codigo del articulo*/
	private Integer codigoArticulo;
	/**Atributo que almacena el codigo del servicio*/
	private Integer codigoServicio;
	/**Atributo que almacena el nombre del articulo*/
	private String nombreArticulo;
	/**Atributo que almacena el nombre del servicio*/
	private String nombreServicio;
	/**Atributo que almacena la cantidad del articulo*/
	private Integer cantidadArticulo;
	/**Atributo que almacena la cantidad del serivicio*/
	private Integer cantidadServicio;
	/**Atributo que almacena el valor del articulo*/
	private BigDecimal  valorArticulo;
	/**Atributo que almacena el valor del Servicio*/
	private BigDecimal  valorServicio;
	/**Atributo que almacena la descripcion grupoServicio*/
	private String  grupoServicio;
	/**Atributo que almacena el codigo subGrupoInvantario del articulo*/
	private Integer subGrupoInventario;	
	/**Atributo que almacena la descripcion de la claseInvantario del articulo*/
	private String claseInventario;
	/**Atributo que almacena el nivel de atencion del servicio o articulo*/
	private String 	nivelAtencion;
	/**Atributo que almacena el nivel de atencion del articulo*/
	private String 	nivelAtencionArticulo;
	/**Atributo que almacena el nivel de atencion del servicio */
	private String 	nivelAtencionServicio;
	
	/**Atributo que almacena el valor del articulo o servicio */
	private BigDecimal 	valorUnitario;
	
	/**Mapa que almacena los totales*/
	HashMap<String, DtoTotalesOrdenesEntidadesSub>sumNiveles;
	/**Lista que almacena la informacion de servicios y articulos*/
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>consolidado;
	/**Lista que almacena la informacion de servicios y articulos*/
	private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>consolidadoDefinitivo;
	
	
	/**Atributo que almacena la lista del dtoConsultaTotalOrdenesAutorizadasEntSub por valores y cantidades de serivicios*/	
	//private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>listaGrupoServicios;
	/**Atributo que almacena la lista del dtoConsultaTotalOrdenesAutorizadasEntSub por valores y cantidades de articulos*/
	///private ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>listaCalseInventarios;
	

	/**Atributo que almacena la inconsistencia del nivel de atencion del servicio o el medicamento*/
	private DtoInconsistenciasProcesoPresupuestoCapitado inconsistencia; 
	/**Atributo que almacena el tipo de consulta*/
	private String tipoConsulta;
	
	
	/**Metodo de ordenamiento para entidad-convenio-nivel*/
	public int compareTo(DtoConsultaTotalOrdenesAutorizadasEntSub o) 
    {
    	DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsulta = o;    
        
    	if(o.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaNivelAtencion))
    	{
	        if(this.codigoEntidadSub.compareTo(dtoConsulta.codigoEntidadSub) == 0)
	        {
	        	if(this.codigoConvenio.compareTo(dtoConsulta.codigoConvenio) == 0)
	        	{
	        		return this.nivelAtencion.compareToIgnoreCase(dtoConsulta.nivelAtencion);
	        	}else
	        	{
	        		return this.codigoConvenio.compareTo(dtoConsulta.codigoConvenio);
	        	}        	
	        }else{
	        	return this.codigoEntidadSub.compareTo(dtoConsulta.codigoEntidadSub);
	        }
	        
    	}else
    		if(o.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario))
    		{    		        
	        	if(this.codigoConvenio.compareTo(dtoConsulta.codigoConvenio) == 0)
	        	{
	        		if(this.nivelAtencion.compareToIgnoreCase(dtoConsulta.nivelAtencion) == 0)
	        		{
	        			if(this.codigoServicio!=null)
	        				return this.grupoServicio.compareToIgnoreCase(dtoConsulta.grupoServicio);
	        			else
	        				return this.claseInventario.compareToIgnoreCase(dtoConsulta.claseInventario);
	        		}else
	        		{
	        			return this.nivelAtencion.compareToIgnoreCase(dtoConsulta.nivelAtencion);
	        		}	        		
	        	}else
	        	{
	        		return this.codigoConvenio.compareTo(dtoConsulta.codigoConvenio);
	        	}        	
    			
    		}else
    			if(o.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaDetallado))
    			{
    				if(this.nivelAtencion.compareToIgnoreCase(dtoConsulta.nivelAtencion) == 0)
	        		{
	        			if(this.codigoServicio!=null)
	        				return this.codigoServicio.compareTo(dtoConsulta.codigoServicio);
	        			else
	        				return this.codigoArticulo.compareTo(dtoConsulta.codigoArticulo);
	        		}else
	        		{
	        			return this.nivelAtencion.compareToIgnoreCase(dtoConsulta.nivelAtencion);
	        		}
    			}else{
    				return ConstantesBD.codigoNuncaValido;
    			}
    }
	
	public String toString() {
        return this.codigoEntidadSub + "\t" + this.codigoConvenio + "\t" + this.nivelAtencion + "  \t"+
        this.codigoArticulo+"\t"+this.codigoServicio+"\t"+this.estadoAutorizacion+"\t"+this.grupoServicio+"\t "+
        this.claseInventario+"\t \t \t"+this.nombreArticulo+"\t \t"+this.nombreServicio;
        
    }
	
	/**Metodo constructor de la clase*/
	public DtoConsultaTotalOrdenesAutorizadasEntSub(){
		this.reset();
	}
	
	/** Metodo que se encarga de inicializar los atributos de la clase*/
	private void reset(){
		this.estadoAutorizacion="";
		this.codigoEntidadSub=ConstantesBD.codigoNuncaValidoLong;
		this.nombreEntidadSub="";
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.nombreConvenio="";
		this.fechaAutorizacion=new Date();
		this.horaAutorizacion="";
		this.consecutivo=new Long(0);
		
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.nombreArticulo="";
		this.nombreServicio="";		
		this.cantidadArticulo=ConstantesBD.codigoNuncaValido;
		this.cantidadServicio=ConstantesBD.codigoNuncaValido;		
		this.valorArticulo=new BigDecimal(0.0); 
		this.valorServicio=new BigDecimal(0.0);
		this.valorUnitario=new BigDecimal(0.0);
		this.grupoServicio="";
		this.subGrupoInventario=ConstantesBD.codigoNuncaValido;
		this.claseInventario="";
		this.nivelAtencion="";		
		this.nivelAtencionArticulo="";
		this.nivelAtencionServicio="";
		
		this.tipoConsulta="";
		//this.listaGrupoServicios=new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
		//this.listaCalseInventarios=new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
		
		this.sumNiveles=new HashMap<String, DtoTotalesOrdenesEntidadesSub>();
		this.consolidado=new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
		this.consolidadoDefinitivo=new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
		
	}

	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public Long getCodigoEntidadSub() {
		return codigoEntidadSub;
	}

	public void setCodigoEntidadSub(Long codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}

	public String getNombreEntidadSub() {
		return nombreEntidadSub;
	}

	public void setNombreEntidadSub(String nombreEntidadSub) {
		this.nombreEntidadSub = nombreEntidadSub;
	}

	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public String getNombreConvenio() {
		return nombreConvenio;
	}

	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	public String getNombreArticulo() {
		return nombreArticulo;
	}

	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public Integer getCantidadArticulo() {
		return cantidadArticulo;
	}

	public void setCantidadArticulo(Integer cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}

	public Integer getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(Integer cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	public BigDecimal getValorArticulo() {
		return valorArticulo;
	}

	public void setValorArticulo(BigDecimal valorArticulo) {
		this.valorArticulo = valorArticulo;
	}

	public BigDecimal getValorServicio() {
		return valorServicio;
	}

	public void setValorServicio(BigDecimal valorServicio) {
		this.valorServicio = valorServicio;
	}

	public String getGrupoServicio() {
		return grupoServicio;
	}

	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	public Integer getSubGrupoInventario() {
		return subGrupoInventario;
	}

	public void setSubGrupoInventario(Integer subGrupoInventario) {
		this.subGrupoInventario = subGrupoInventario;
	}

	public String getNivelAtencion() {
		return nivelAtencion;
	}

	public void setNivelAtencion(String nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	public String getNivelAtencionArticulo() {
		return nivelAtencionArticulo;
	}

	public void setNivelAtencionArticulo(String nivelAtencionArticulo) {
		this.nivelAtencionArticulo = nivelAtencionArticulo;
	}

	public String getNivelAtencionServicio() {
		return nivelAtencionServicio;
	}

	public void setNivelAtencionServicio(String nivelAtencionServicio) {
		this.nivelAtencionServicio = nivelAtencionServicio;
	}

	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}

	public String getClaseInventario() {
		return claseInventario;
	}

	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public Integer getCodigoServicio() {
		return codigoServicio;
	}

	public void setInconsistencia(DtoInconsistenciasProcesoPresupuestoCapitado inconsistencia) {
		this.inconsistencia = inconsistencia;
	}

	public DtoInconsistenciasProcesoPresupuestoCapitado getInconsistencia() {
		return inconsistencia;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public String getTipoConsulta() {
		return tipoConsulta;
	}

	public HashMap<String, DtoTotalesOrdenesEntidadesSub> getSumNiveles() {
		return sumNiveles;
	}

	public void setSumNiveles(
			HashMap<String, DtoTotalesOrdenesEntidadesSub> sumNiveles) {
		this.sumNiveles = sumNiveles;
	}

	public void setConsolidado(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> consolidado) {
		this.consolidado = consolidado;
	}

	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> getConsolidado() {
		return consolidado;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Long getConsecutivo() {
		return consecutivo;
	}

	public void setHoraAutorizacion(String horaAutorizacion) {
		this.horaAutorizacion = horaAutorizacion;
	}

	public String getHoraAutorizacion() {
		return horaAutorizacion;
	}

	public void setConsolidadoDefinitivo(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> consolidadoDefinitivo) {
		this.consolidadoDefinitivo = consolidadoDefinitivo;
	}

	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> getConsolidadoDefinitivo() {
		return consolidadoDefinitivo;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	

	/*public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> getListaGrupoServicios() {
		return listaGrupoServicios;
	}

	public void setListaGrupoServicios(
			ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaGrupoServicios) {
		this.listaGrupoServicios = listaGrupoServicios;
	}

	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> getListaCalseInventarios() {
		return listaCalseInventarios;
	}

	public void setListaCalseInventarios(
			ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaCalseInventarios) {
		this.listaCalseInventarios = listaCalseInventarios;
	}*/

	
}
