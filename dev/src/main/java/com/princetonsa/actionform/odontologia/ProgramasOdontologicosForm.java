package com.princetonsa.actionform.odontologia;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.sqlbase.glosas.SqlBaseConceptosEspecificosDao;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class ProgramasOdontologicosForm extends ValidatorForm  
{
	private String estado;
	
	private ArrayList<HashMap> especialidadesOdontologia;
	
	private DtoPrograma dtoProgramaFiltro;
	
	private ArrayList<DtoPrograma> listadoProgramas;
	
	private int posPrograma;
	
	private ArrayList<DtoDetalleProgramas> listadoDetalleProgramas;
	
	private HashMap serviciosMap;
	
	private String codigosServiciosInsertados;
	
	private DtoDetalleProgramas filtrosDetallePO;
	
	private DtoPrograma logPrograma;
	
	private String mensaje;
	
	private DtoDetalleProgramas logDetalle;
	
	private int posDetalle;
	
	private boolean esConsulta;
	
	private ArrayList<DtoDetalleProgramas> copiaListadoDetalle;
	
	private String archImagen;
	
	private int convencion;
	
	private double codioProgramaTmp;
	
	/**
	 * para el ordenamiento
	 */
	private String patronOrdenar;
	
	private String estadoAnterior;
	
	/**
	 *Mensajes de error 
	 */
	//static Logger logger = Logger.getLogger(SqlBaseConceptosEspecificosDao.class);
	static Logger logger = Logger.getLogger(ProgramasOdontologicosForm.class);
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();

    	if (this.estado.equals("guardarPrograma")||this.estado.equals("modificarPrograma"))
    	{
    		int numeroListadoProgramas = listadoProgramas.size();
    		logger.info(">>> El tamaño de la lista de programas es: "+numeroListadoProgramas);
    		String nombreComparar = "";
    		String codigoProgramaComparar = "";
    		boolean salirForCodigo = false, salirForNombre = false;
    		logger.info(">>> Código ingresado = <"+dtoProgramaFiltro.getCodigoPrograma()+">");
    		logger.info(">>> Nombre ingresado = <"+dtoProgramaFiltro.getNombre()+">");
    		
    		if (this.dtoProgramaFiltro.getCodigoPrograma().equals(""))
    			errores.add("", new ActionMessage("errors.required","El Código del Programa "));
    		if (this.dtoProgramaFiltro.getNombre().equals(""))
    			errores.add("", new ActionMessage("errors.required","El Nombre del Programa "));
    		
    	
//    		if(!this.dtoProgramaFiltro.getCodigoPrograma().equals(""))
//    		{
//    			for(int i=0; i<numeroListadoProgramas && salirForCodigo==false;i++)
//        		{
//        			logger.info(">>> Ingresé al For de Código"+i+" veces");
//        			codigoProgramaComparar = this.listadoProgramas.get(i).getCodigoPrograma()+"";
//        			logger.info(">>> Código a comparar = <"+codigoProgramaComparar+">");
//        			if(codigoProgramaComparar.equals(this.dtoProgramaFiltro.getCodigoPrograma()))
//        			{
//        				logger.info(">>> Ingresé al condicional del código!!!");
//        				errores.add("", new ActionMessage("errors.notEspecific","El código "+codigoProgramaComparar+
//        						" Ya se encuentra registrado, por favor verifique"));
//        				salirForCodigo = true;
//        			}
//        		}
//    		}
//    		
//    		
//    		if(!this.dtoProgramaFiltro.getNombre().equals(""))
//    		{
//    			for(int j=0; j<numeroListadoProgramas && salirForNombre == false;j++)
//        		{
//        			logger.info(">>> Ingresé al For de Nombre"+j+" veces");
//        			nombreComparar = this.listadoProgramas.get(j).getNombre()+"";
//        			logger.info(">>> Nombre a comparar = <"+nombreComparar+">");
//        			if(nombreComparar.equals(this.dtoProgramaFiltro.getNombre()))
//        			{
//        				logger.info(">>> Ingresé al condicional del nombre!!!");
//        				errores.add("", new ActionMessage("errors.notEspecific","El nombre "+nombreComparar+
//        						" Ya se encuentra registrado, por favor verifique"));
//        				salirForNombre = true;
//        			}
//        		}
//    		}
    	}
		return errores;
	}
	/**
	 * 
	 */
	public void reset()
	{
		this.estado="";
		this.especialidadesOdontologia= new ArrayList<HashMap>();
		this.dtoProgramaFiltro= new DtoPrograma();
		this.listadoProgramas=new ArrayList<DtoPrograma>();
		this.posPrograma=ConstantesBD.codigoNuncaValido;
		this.listadoDetalleProgramas=new ArrayList<DtoDetalleProgramas>();
		this.serviciosMap= new HashMap();
		this.codigosServiciosInsertados="";
		this.filtrosDetallePO=new DtoDetalleProgramas();
		this.logPrograma=new DtoPrograma();
		this.mensaje="";
		this.logDetalle=new DtoDetalleProgramas();
		this.posDetalle=ConstantesBD.codigoNuncaValido;
		this.copiaListadoDetalle=new ArrayList<DtoDetalleProgramas>();
		this.esConsulta=false;
		this.archImagen="";
		this.convencion=ConstantesBD.codigoNuncaValido;
		this.codioProgramaTmp=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.estadoAnterior="";
		this.patronOrdenar="";
	}
	
	public void resetConsulta()
	{
		this.estado="";
		this.especialidadesOdontologia= new ArrayList<HashMap>();
		this.dtoProgramaFiltro= new DtoPrograma();
		this.listadoProgramas=new ArrayList<DtoPrograma>();
		this.posPrograma=ConstantesBD.codigoNuncaValido;
		this.listadoDetalleProgramas=new ArrayList<DtoDetalleProgramas>();
		this.serviciosMap= new HashMap();
		this.codigosServiciosInsertados="";
		this.filtrosDetallePO=new DtoDetalleProgramas();
		this.logPrograma=new DtoPrograma();
		this.mensaje="";
		this.logDetalle=new DtoDetalleProgramas();
		this.posDetalle=ConstantesBD.codigoNuncaValido;
		this.copiaListadoDetalle=new ArrayList<DtoDetalleProgramas>();
		this.archImagen="";
		this.convencion=ConstantesBD.codigoNuncaValido;
	}
	
	public void resetMsj()
	{
		this.mensaje="";
	}
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<HashMap> getEspecialidadesOdontologia() {
		return especialidadesOdontologia;
	}

	public void setEspecialidadesOdontologia(
			ArrayList<HashMap> especialidadesOdontologia) {
		this.especialidadesOdontologia = especialidadesOdontologia;
	}

	public DtoPrograma getDtoProgramaFiltro() {
		return dtoProgramaFiltro;
	}

	public void setDtoProgramaFiltro(DtoPrograma dtoProgramaFiltro) {
		this.dtoProgramaFiltro = dtoProgramaFiltro;
	}

	public ArrayList<DtoPrograma> getListadoProgramas() {
		return listadoProgramas;
	}

	public void setListadoProgramas(ArrayList<DtoPrograma> listadoProgramas) {
		this.listadoProgramas = listadoProgramas;
	}

	public int getPosPrograma() {
		return posPrograma;
	}

	public void setPosPrograma(int posPrograma) {
		this.posPrograma = posPrograma;
	}

	public ArrayList<DtoDetalleProgramas> getListadoDetalleProgramas() {
		return listadoDetalleProgramas;
	}

	public void setListadoDetalleProgramas(
			ArrayList<DtoDetalleProgramas> listadoDetalleProgramas) {
		this.listadoDetalleProgramas = listadoDetalleProgramas;
	}

	public HashMap getServiciosMap() {
		return serviciosMap;
	}

	public void setServiciosMap(HashMap serviciosMap) {
		this.serviciosMap = serviciosMap;
	}

	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	public DtoDetalleProgramas getFiltrosDetallePO() {
		return filtrosDetallePO;
	}

	public void setFiltrosDetallePO(DtoDetalleProgramas filtrosDetallePO) {
		this.filtrosDetallePO = filtrosDetallePO;
	}

	public DtoPrograma getLogPrograma() {
		return logPrograma;
	}

	public void setLogPrograma(DtoPrograma logPrograma) {
		this.logPrograma = logPrograma;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public DtoDetalleProgramas getLogDetalle() {
		return logDetalle;
	}

	public void setLogDetalle(DtoDetalleProgramas logDetalle) {
		this.logDetalle = logDetalle;
	}

	public int getPosDetalle() {
		return posDetalle;
	}

	public void setPosDetalle(int posDetalle) {
		this.posDetalle = posDetalle;
	}

	public ArrayList<DtoDetalleProgramas> getCopiaListadoDetalle() {
		return copiaListadoDetalle;
	}

	public void setCopiaListadoDetalle(
			ArrayList<DtoDetalleProgramas> copiaListadoDetalle) {
		this.copiaListadoDetalle = copiaListadoDetalle;
	}

	public boolean isEsConsulta() {
		return esConsulta;
	}

	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	public String getArchImagen() {
		return archImagen;
	}

	public void setArchImagen(String archImagen) {
		this.archImagen = archImagen;
	}

	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public void setCodioProgramaTmp(double codioProgramaTmp) {
		this.codioProgramaTmp = codioProgramaTmp;
	}

	public double getCodioProgramaTmp() {
		return codioProgramaTmp;
	}
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	public String getEstadoAnterior() {
		return estadoAnterior;
	}
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
	
}