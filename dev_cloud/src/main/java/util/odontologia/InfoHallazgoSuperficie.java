package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.servinte.axioma.orm.ValoresPorDefecto;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.General.InfoDatosBD;

/**
 * 
 * @author axioma
 *
 */
public class InfoHallazgoSuperficie implements Serializable
{
	
	private static final String APLICA_PARA_TODAS_SUPERFICIES="Todas las superficies";
	
	
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Aplica para la sección de Plan Tratamiento 
	 **/
	private BigDecimal codigoPkDetalle;  //ESTE ES EL CODIGO DEL DETALLE DEL PLAN DE TRATAMIENTO
	
	/**
	 * hallazgo ligado a la pieza dental,
	 * (CASO 1) 
	 * puede ocurrir que la pieza solo tenga un hallazgo 
	 * (CASO 2)
	 * una pieza con un hallazgo ligado a una superficie
	 * 
	 */
	private InfoDatosInt hallazgoREQUERIDO; 
	private InfoDatosInt hallazgoREQUERIDOOld;
	
	
	/**
	 * Ayudante PARA CARGAR EL NOMBRE DEL SUPERFICIE 
	 * SI APLICA PARA TODAS O NO.
	 */
	private String nombreSuperficeAyudante;
	/**
	 * 
	 */
	private String nombreClasificacionAyudante;
	
	
	
	/**
	 * solo se llena para el caso 2, o también es vacía cuando los hallazgos son en boca
	 */
	private InfoDatosInt superficieOPCIONAL;
	private InfoDatosInt superficieOPCIONALOld;
	
	
	/**
	 * carga los programas o los servicios dependiendo de lo
	 * que tenga el parametro general "Institucion maneja programas"
	 */
	private ArrayList<InfoProgramaServicioPlan> programasOservicios;
	
	/**
	 * value = existe en base de datos (S/N)
	 * activo = fue eliminado por el usuario y debe ser eliminado de la BD (si esta en false)
	 * */
	private InfoDatosBD existeBD;
	
	/**
	 * 
	 * */
	private InfoDatosString clasificacion;

	/**
	 * 
	 * */
	private String porConfirmar;
	
	/**
	 * 
	 * */
	private String codigoConvencion;
	
	/**
	 * Esta campo es usado para almacenar la fecha del programa/servicio
	 * con convención mas reciente
	 * */
	private String fechaUltimaModProgServ;
	
	/**
	 * Esta campo es usado para almacenar la hora del programa/servicio
	 * con convención mas reciente
	 * */
	private String horaUltimaModProgServ;
	
	
	/**
	 * 
	 */
	private boolean existenServicios;
	
	/**
	 * 
	 * */
	private DtoInfoFechaUsuario infoRegistroHallazgo;
	
	/**
	 * Pieza a la cual está asociado el hallazgo
	 */
	private InfoDetallePlanTramiento piezaAsociada;
		
	public InfoHallazgoSuperficie() {
		super();
		this.hallazgoREQUERIDO = new InfoDatosInt();
		this.superficieOPCIONAL = new InfoDatosInt();
		this.programasOservicios = new ArrayList<InfoProgramaServicioPlan>();
		this.setCodigoPkDetalle(new BigDecimal(ConstantesBD.codigoNuncaValidoDouble));
		this.existeBD = new InfoDatosBD();
		this.infoRegistroHallazgo = new DtoInfoFechaUsuario();
		this.clasificacion = new InfoDatosString();
		this.porConfirmar = ConstantesBD.acronimoSi;
		this.codigoConvencion = "";
		this.hallazgoREQUERIDOOld = new InfoDatosInt();
		this.superficieOPCIONALOld = new InfoDatosInt();
		this.fechaUltimaModProgServ = "";
		this.horaUltimaModProgServ = "";
		this.piezaAsociada=new InfoDetallePlanTramiento();
		this.nombreSuperficeAyudante="";
		this.nombreClasificacionAyudante="";
		this.setExistenServicios(Boolean.FALSE);
	}

	/**
	 * @return the hallazgoREQUERIDO
	 */
	public InfoDatosInt getHallazgoREQUERIDO() {
		return hallazgoREQUERIDO;
	}

	/**
	 * @param hallazgoREQUERIDO the hallazgoREQUERIDO to set
	 */
	public void setHallazgoREQUERIDO(InfoDatosInt hallazgoREQUERIDO) {
		this.hallazgoREQUERIDO = hallazgoREQUERIDO;
	}

	/**
	 * @return the superficieOPCIONAL
	 */
	public InfoDatosInt getSuperficieOPCIONAL() {
		return superficieOPCIONAL;
	}

	/**
	 * @param superficieOPCIONAL the superficieOPCIONAL to set
	 */
	public void setSuperficieOPCIONAL(InfoDatosInt superficieOPCIONAL) {
		this.superficieOPCIONAL = superficieOPCIONAL;
	}

	/**
	 * @return the programasOservicios
	 */
	public ArrayList<InfoProgramaServicioPlan> getProgramasOservicios() {
		return programasOservicios;
	}
	
	public int getNumProgramasServiciosActivos()
	{
		int cont = 0;
		
		for(InfoProgramaServicioPlan prog : this.programasOservicios)
		{
			if(prog.getCodigoPkProgramaServicio().intValue() < 0)
			{
				for(InfoServicios serv : prog.getListaServicios())
				{
					if(serv.getExisteBD().isActivo())
					{
						cont++;
					}
				}
			}
			else if (prog.getExisteBD().isActivo())
			{
				cont++;
			}
		}
		
		return cont;
	}
	
	
	/**
	 * @param programasOservicios the programasOservicios to set
	 */
	public void setProgramasOservicios(
			ArrayList<InfoProgramaServicioPlan> programasOservicios) {
		this.programasOservicios = programasOservicios;
	}

	public void setCodigoPkDetalle(BigDecimal codigoPkDetalle) {
		this.codigoPkDetalle = codigoPkDetalle;
	}

	public BigDecimal getCodigoPkDetalle() {
		return codigoPkDetalle;
	}
	
	public DtoInfoFechaUsuario getInfoRegistroHallazgo() {
		return infoRegistroHallazgo;
	}

	public void setInfoRegistroHallazgo(DtoInfoFechaUsuario infoRegistroHallazgo) {
		this.infoRegistroHallazgo = infoRegistroHallazgo;
	}

	public InfoDatosString getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(InfoDatosString clasificacion) {
		this.clasificacion = clasificacion;
	}
	
	
	
	/**
	 * CONTAR EL NUMERO DE PROGRAMAS DE LAS SUPERFICIE 
	 * @return
	 */
	public int getNumeroProgramasServicios(){
		return this.getProgramasOservicios().size();
	}
	
	public int getNumeroTotalServiciosActivosXsuperficie(boolean validarExcluInclu)
	{
		int cont = 0;
		
		for(int i = 0; i < this.programasOservicios.size(); i++)
		{
			cont += this.programasOservicios.get(i).getNumeroServiciosActivos(validarExcluInclu);
			
		}
		
		return cont;
	}
	
	public int getNumeroTotalServiciosActivosXsuperficie(boolean validarExcluInclu,boolean esContarConPrograma)
	{
		int cont = 0;
		
		for(int i = 0; i < this.programasOservicios.size(); i++)
		{
			cont += this.programasOservicios.get(i).getNumeroServiciosActivos(validarExcluInclu);
			if(esContarConPrograma && this.programasOservicios.get(i).getExisteBD().isActivo())
			{
				/*if(validarExcluInclu&&this.programasOservicios.get(i).getInclusion().equals(ConstantesBD.acronimoSi)||
						!validarExcluInclu&&!this.programasOservicios.get(i).getInclusion().equals(ConstantesBD.acronimoSi))*/
				{
					cont++;
				}
			}
		}
		
		return cont;
	}
	
	public int getNumeroProgramasActivosParaCita(boolean validarExcluInclu)
	{
		/*
		 * Variable para manejar la cantidad de programas activos para mostrar en la cita
		 */
		int cantidadProgramasActivos=0;
		
		for(InfoProgramaServicioPlan programa:getProgramasOservicios())
		{
			int cantidadServiciosActivos=programa.getNumeroServiciosActivosParaCita(validarExcluInclu);
			/*
			 * Solamente si tiene uno o mas servicios activos se muestra el programa, de lo contrario
			 * no se tiene en cuenta
			 */
			if(cantidadServiciosActivos>0)
			{
				cantidadProgramasActivos++;
			}
		}
		return cantidadProgramasActivos;
	}
	
	public int getNumeroTotalServiciosActivosXsuperficieParaCita(boolean validarExcluInclu)
	{
		int cont = 0;
		
		for(int i = 0; i < this.programasOservicios.size(); i++)
			cont += this.programasOservicios.get(i).getNumeroServiciosActivosParaCita(validarExcluInclu);
		
		return cont;
	}
	
	/**
	 * CONTAR EL NUMERO DE PROGRAMAS DE LAS SUPERFICIE 
	 * @return
	 */
	public int getNumeroProgramasServiciosRowSpan(){
		return (this.getProgramasOservicios().size()>0)?this.getProgramasOservicios().size():1;
	}

	/**
	 * Método que verifica si la superficie tiene todos sus programas terminados
	 * @return true en caso de que la superficie no tenga programas pendientes
	 */
	public boolean getSuperficieTerminada()
	{
		boolean existePendientes=false;
		for(InfoProgramaServicioPlan programa: this.getProgramasOservicios())
		{
			if(
				programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso)
				||
				programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoContratado)
				)
			{
				existePendientes=true;
			}
		}
		return !existePendientes;
	}

	public boolean getSuperficieTerminadaConfirmado()
	{
		boolean existePendientes=false;
		for(InfoProgramaServicioPlan programa: this.getProgramasOservicios())
		{
			if(
				programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso)
				||
				programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoContratado)
				|| (programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoTerminado) && programa.getPorConfirmar().equals(ConstantesBD.acronimoSi))
				)
			{
				existePendientes=true;
			}
		}
		return !existePendientes;
	}

	public InfoDatosBD getExisteBD() {
		return existeBD;
	}

	public void setExisteBD(InfoDatosBD existeBD) {
		this.existeBD = existeBD;
	}

	public String getPorConfirmar() {
		return porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public String getCodigoConvencion() {
		return codigoConvencion;
	}

	public void setCodigoConvencion(String codigoConvencion) {
		this.codigoConvencion = codigoConvencion;
	}

	public InfoDatosInt getHallazgoREQUERIDOOld() {
		return hallazgoREQUERIDOOld;
	}

	public void setHallazgoREQUERIDOOld(InfoDatosInt hallazgoREQUERIDOOld) {
		this.hallazgoREQUERIDOOld = hallazgoREQUERIDOOld;
	}

	public InfoDatosInt getSuperficieOPCIONALOld() {
		return superficieOPCIONALOld;
	}

	public void setSuperficieOPCIONALOld(InfoDatosInt superficieOPCIONALOld) {
		this.superficieOPCIONALOld = superficieOPCIONALOld;
	}
	
	/**
	 * Verifica si existen programas o servicios
	 * @param String esPrograma
	 * @param int codigoProgServ
	 * */
	public boolean existenProgramaOserviciosActivos(String esPrograma,int codigoProgServ)
	{
		for(InfoProgramaServicioPlan progServ:programasOservicios)
		{
			if(esPrograma.equals(ConstantesBD.acronimoSi))
			{
				int codigoProgramaInt=progServ.getCodigoPkProgramaServicio().intValue();
				if(codigoProgramaInt == codigoProgServ && 
						progServ.getExisteBD().isActivo())
					return true;
			}
			else
			{
				for(InfoServicios serv:progServ.getListaServicios())
				{
					if(serv.getCodigoPkProgServ().intValue() == codigoProgServ && 
							serv.getExisteBD().isActivo())
					{
						return true;
					}
				}
			}
		}

		return false;
	}
	
	
	/**
	 * Da la posicion del ultimo programa evolucionado totalmente
	 * */
	public InfoDatosInt getPosUltimoProgramaOServicioTerminado()
	{
		InfoDatosInt info = new InfoDatosInt();
		
		for(int i=0; i<programasOservicios.size(); i++)
		{
			info.setActivo(programasOservicios.get(i).getPorConfirmar().equals(ConstantesBD.acronimoSi)?true:false);
			
			if(programasOservicios.get(i).getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoTerminado))
			{
				info.setCodigo(i);
				info.setDescripcion(programasOservicios.get(i).getArchivoConvencionPrograma());
				return info;
			}
			else 
			{
				boolean todosRealizados = true;
				//Busca si todos sus servicios están realizados externos o realizados internos
				for(int j=0; j<this.programasOservicios.get(i).getListaServicios().size() && todosRealizados; j++)
				{
					if(!(this.programasOservicios.get(i).getListaServicios().get(j).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoExterno) 
						|| this.programasOservicios.get(i).getListaServicios().get(j).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)))
							todosRealizados = false;
				}
				
				if(todosRealizados)
				{
					info.setCodigo(i);
					info.setDescripcion(programasOservicios.get(i).getArchivoConvencionPrograma());
					return info;
				}
			}
		}
		
		return info;
	}
	
	/**
	 * Indica si algún programa esta marcado en proceso o uno de sus servicios
	 * esta en estado realizado
	 * */
	public boolean esAlgunProgramaOServicioEnProceso()
	{
		for(int i=0; i<programasOservicios.size(); i++)
		{
			if(programasOservicios.get(i).getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso))
				return true;
			else 
			{
				boolean algunoRealizado = false;
				//Busca si todos sus servicios están realizados externos o realizados internos
				for(int j=0; j<this.programasOservicios.get(i).getListaServicios().size() && !algunoRealizado; j++)
				{
					if(this.programasOservicios.get(i).getListaServicios().get(j).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado) 
						|| this.programasOservicios.get(i).getListaServicios().get(j).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
						algunoRealizado = true;
				}
				
				if(algunoRealizado)
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Determina y actualiza el estado de modificable
	 * @param boolean modifiSoloNuevos
	 * */
	public boolean determinaYactulizaEsModificable(boolean modifiSoloNuevos)
	{
		this.getExisteBD().setEsModificable(false);
		if(!this.porConfirmar.equals(ConstantesBD.acronimoSi))
		{
			if(modifiSoloNuevos && !this.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
			{
				this.getExisteBD().setEsModificable(true);
			}
		}
		//cuando esta pendiente por confirmar se debe permitir modificar tarea 148979.
		else
		{
			this.getExisteBD().setEsModificable(true);
		}

		return this.getExisteBD().isEsModificable();
	}

	/**
	 * @return Retorna fechaUltimaModProgServ
	 */
	public String getFechaUltimaModProgServ()
	{
		return fechaUltimaModProgServ;
	}

	/**
	 * @param Asigna fechaUltimaModProgServ
	 */
	public void setFechaUltimaModProgServ(String fechaUltimaModProgServ)
	{
		this.fechaUltimaModProgServ = fechaUltimaModProgServ;
	}

	/**
	 * @return Retorna horaUltimaModProgServ
	 */
	public String getHoraUltimaModProgServ()
	{
		return horaUltimaModProgServ;
	}

	/**
	 * @param Asigna horaUltimaModProgServ
	 */
	public void setHoraUltimaModProgServ(String horaUltimaModProgServ)
	{
		this.horaUltimaModProgServ = horaUltimaModProgServ;
	}

	/**
	 * @return Retorna el atributo piezaAsociada
	 */
	public InfoDetallePlanTramiento getPiezaAsociada()
	{
		return piezaAsociada;
	}

	/**
	 * @param piezaAsociada Asigna el atributo piezaAsociada
	 */
	public void setPiezaAsociada(InfoDetallePlanTramiento piezaAsociada)
	{
		this.piezaAsociada = piezaAsociada;
	}

	/**
	 * Cuenta los programas activos del hallazgo
	 * @return cantidad de programas activos del hallazgo
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-22
	 */
	public int getNumeroProgramasActivos()
	{
		int cantidadProgramas=0;
		for(InfoProgramaServicioPlan programa:programasOservicios)
		{
			if(programa.getExisteBD().isActivo())
			{
				cantidadProgramas++;
			}
		}
		return cantidadProgramas;
	}

	public void setNombreSuperficeAyudante(String nombreSuperficeAyudante) {
		this.nombreSuperficeAyudante = nombreSuperficeAyudante;
	}

	
	
	
	/**
	 * ATRIBUTO AYUNDATE PARA PRESENTACION GRAFICA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreSuperficeAyudante() {
		
		
		if(UtilidadTexto.isEmpty(this.superficieOPCIONAL.getNombre()))
		{
			this.nombreSuperficeAyudante=APLICA_PARA_TODAS_SUPERFICIES;
		}
		else
		{
			this.nombreSuperficeAyudante=this.superficieOPCIONAL.getNombre();
		}
	
		
		return this.nombreSuperficeAyudante;
	
	}

	public void setNombreClasificacionAyudante(	String nombreClasificacionAyudante) 
	{
		this.nombreClasificacionAyudante = nombreClasificacionAyudante;
	}

	
	/**
	 * AYUNDATEN PARA CARGAR EL NOMBRE DE LA CLASIFICACION
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreClasificacionAyudante() 
	{
		try{
			
			if(! UtilidadTexto.isEmpty(this.clasificacion.getValue()) )
			{
				this.nombreClasificacionAyudante=(String) util.ValoresPorDefecto.getIntegridadDominio(this.clasificacion.getValue());
			}
		}
		
		catch (Exception e) {
			Log4JManager.info("Error al convertir un Object a String ");
		}
		
		
		return nombreClasificacionAyudante;
	}
	
	
	
	


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public boolean presentaServiciosEvolucionados()
	{
		
		boolean retorno=Boolean.FALSE;
		 
		for( InfoProgramaServicioPlan dtoProgramas:  this.programasOservicios)
		{
			
			if(dtoProgramas.getListaServicios().size()>0)
			{
				retorno=Boolean.TRUE;
			}
			
		}
		
		
		return retorno;
	}

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param existenServicios
	 */
	public void setExistenServicios(boolean existenServicios) {
		this.existenServicios = existenServicios;
	}

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public boolean getExistenServicios() 
	{
		this.existenServicios=presentaServiciosEvolucionados();
		return existenServicios;
	}
	
	
	
	
	
	
	
}
