package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.General.InfoDatosBD;


/**
 * 
 * @author axioma
 *
 */
public class InfoDetallePlanTramiento implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3182951563030320410L;

	/**
	 * Aplica para las secciones de Otros hallazgos y hallazgos Boca
	 * */
	private BigDecimal codigoPkDetalle;
	
	/**
	 * 
	 */
	private InfoDatosInt pieza;
	
	/**
	 * 
	 * */
	private InfoDatosInt piezaOld;
	
	/**
	 * value = existe en base de datos (S/N)
	 * activo = fue eliminado por el usuario y debe ser eliminado de la BD (si esta en false)
	 * */
	private InfoDatosBD existeBD;
	/**
	 * 
	 */
	private ArrayList<InfoHallazgoSuperficie> detalleSuperficie;
	/**
	 * 
	 */
	private  ArrayList<InfoHallazgoSuperficie> superficiesInclusion;
	/**
	 * 
	 */
	private ArrayList<InfoHallazgoSuperficie> superficiesGarantia;
	
	/**
	 * 
	 */
	private ArrayList<DtoSectorSuperficieCuadrante> arraySuperficiesDiente;
	
	

	/**
	 * Esta campo es usado para almacenar la fecha del programa/servicio
	 * con convencion mas reciente
	 * */
	private String fechaUltimaModProgServ;
	
	/**
	 * Esta campo es usado para almacenar la hora del programa/servicio
	 * con convencion mas reciente
	 * */
	private String horaUltimaModProgServ;
	
	/**
	 * Atribut de control para verificar si se evalúa la inserción de programas de N superficies
	 */
	private boolean evaluado=false;
	
	
	private int ayundateTamanoDetalleSuperficie;
	
	
	/**
	 * 
	 */
	public InfoDetallePlanTramiento() 
	{
		super();		
		this.pieza = new InfoDatosInt();
		this.detalleSuperficie = new ArrayList<InfoHallazgoSuperficie>();
		this.existeBD = new InfoDatosBD();
		this.superficiesInclusion = new ArrayList<InfoHallazgoSuperficie>();
		this.setSuperficiesGarantia(new ArrayList<InfoHallazgoSuperficie>());
		this.piezaOld = new InfoDatosInt();
		this.codigoPkDetalle = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.fechaUltimaModProgServ = "";
		this.horaUltimaModProgServ = "";
		this.evaluado=false;
		this.setAyundateTamanoDetalleSuperficie(0);
	}

	
	public void resetDetalleSuperficie()
	{
		this.detalleSuperficie = new ArrayList<InfoHallazgoSuperficie>();;
	}
	/**
	 * @return the pieza
	 */
	public InfoDatosInt getPieza() {
		return pieza;
	}

	/**
	 * @param pieza the pieza to set
	 */
	public void setPieza(InfoDatosInt pieza) {
		this.pieza = pieza;
	}

	/**
	 * @return the detalleSuperficie
	 */
	public ArrayList<InfoHallazgoSuperficie> getDetalleSuperficie() {
		return detalleSuperficie;
	}

	/**
	 * @param detalleSuperficie the detalleSuperficie to set
	 */
	public void setDetalleSuperficie(
			ArrayList<InfoHallazgoSuperficie> detalleSuperficie) {
		this.detalleSuperficie = detalleSuperficie;
	}
	
	

	/**
	 * @return the detalleSuperficie
	 */
	public int getNumTotalProgramasServiciosActivosXDienteParaCita(boolean esContarConPrograma,boolean validarExcluInclu) 
	{
		int cont = 0;
		
		for(int i = 0; i < detalleSuperficie.size(); i++)
		{
			if(detalleSuperficie.get(i).getExisteBD().isActivo())
			{
				for(int j=0; j<detalleSuperficie.get(i).getProgramasOservicios().size(); j++)
				{
					if(detalleSuperficie.get(i).getProgramasOservicios().get(j).getExisteBD().isActivo() 
						/*&& ((!validarExcluInclu && !detalleSuperficie.get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi)) 
								|| (validarExcluInclu && detalleSuperficie.get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi)))*/
						&& !detalleSuperficie.get(i).getProgramasOservicios().get(j).getProgramasParCita().equals(ConstantesBD.acronimoNo))
					{
						cont += detalleSuperficie.get(i).getProgramasOservicios().get(j).getNumeroServiciosActivosParaCita(validarExcluInclu);
						if(esContarConPrograma)
							cont++;
					}
				}
				
				
			}
		}
		
		return cont;
	}
	
	/**
	 * @return the detalleSuperficie
	 */
	public int getNumTotalProgramasServiciosActivosXDiente(boolean esContarConPrograma,boolean validarExcluInclu) 
	{
		int cont = 0;
		
		for(int i = 0; i < detalleSuperficie.size(); i++)
		{
			if(detalleSuperficie.get(i).getExisteBD().isActivo())
			{
				for(int j=0; j<detalleSuperficie.get(i).getProgramasOservicios().size(); j++)
				{
					if(detalleSuperficie.get(i).getProgramasOservicios().get(j).getExisteBD().isActivo() 
						/*&& ((!validarExcluInclu && !detalleSuperficie.get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi)) 
								|| (validarExcluInclu && detalleSuperficie.get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi)))*/)
					{
						cont += detalleSuperficie.get(i).getProgramasOservicios().get(j).getNumeroServiciosActivos(validarExcluInclu);
						if(esContarConPrograma)
							cont++;
					}
				}
				
				
			}
		}
		
		return cont;
	}
	
	public void setSuperficiesInclusion(ArrayList<InfoHallazgoSuperficie> superficiesInclusion) {
		this.superficiesInclusion = superficiesInclusion;
	}

	public ArrayList<InfoHallazgoSuperficie> getSuperficiesInclusion() {
		return superficiesInclusion;
	}

	public void setSuperficiesGarantia(ArrayList<InfoHallazgoSuperficie> superficiesGarantia) {
		this.superficiesGarantia = superficiesGarantia;
	}

	public ArrayList<InfoHallazgoSuperficie> getSuperficiesGarantia() {
		return superficiesGarantia;
	}

	public InfoDatosBD getExisteBD() {
		return existeBD;
	}

	public void setExisteBD(InfoDatosBD existeBD) {
		this.existeBD = existeBD;
	}

	public InfoDatosInt getPiezaOld() {
		return piezaOld;
	}

	public void setPiezaOld(InfoDatosInt piezaOld) {
		this.piezaOld = piezaOld;
	}

	public BigDecimal getCodigoPkDetalle() {
		return codigoPkDetalle;
	}

	public void setCodigoPkDetalle(BigDecimal codigoPkDetalle) {
		this.codigoPkDetalle = codigoPkDetalle;
	}
	
	/**
	 * Devuelve el numero de superficies que posee el mismo hallazgo
	 * */
	public int getNumeroSuperParaHallazgo(int codigoHallazgo)
	{
		int cont = 0;
		for(InfoHallazgoSuperficie hallazgo :detalleSuperficie)
		{
			if(hallazgo.getHallazgoREQUERIDO().getCodigo() == codigoHallazgo 
					&& hallazgo.getExisteBD().isActivo()
					&& UtilidadTexto.getBoolean(hallazgo.getPorConfirmar()))
				cont++;
		}
		
		return cont;
	}
	
	/**
	 * @return the detalleSuperficie
	 */
	public int getNumeroSuperficiesActivas2(int posProgServ) 
	{
		int cont = 0;
		for(int i = 0; i < detalleSuperficie.size(); i++)
		{
			if(detalleSuperficie.get(i).getProgramasOservicios().get(posProgServ).getExisteBD().isActivo())
				cont++;
		}
		return cont;
	}
	
	/**
	 * Contador del numero de programas/servicios de la pieza
	 * @return
	 */
	public int getNumeroServiciosProgramasPieza()
	{
		int contador = 0;
		for(InfoHallazgoSuperficie superficie: this.getDetalleSuperficie())
		{
			for(InfoProgramaServicioPlan progServ: superficie.getProgramasOservicios())
			{
				contador++;
			}
		}
		
		return contador;
	}
	
	
	/**
	 * @return the detalleSuperficie
	 */
	public int getNumeroSuperficiesActivas() 
	{
		int cont = 0;
		
		for(int i = 0; i < detalleSuperficie.size(); i++)
			if(detalleSuperficie.get(i).getExisteBD().isActivo())
				cont++;
		
		return cont;
	}
	
	/**
	 * Determina y actualiza el estado de modificable
	 * @param boolean modifiSoloNuevos
	 * */
	public boolean determinaYactulizaEsModificable(boolean modifiSoloNuevos)
	{
		this.getExisteBD().setEsModificable(false);
		
		if(modifiSoloNuevos && !this.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
			this.getExisteBD().setEsModificable(true);
		
		return this.getExisteBD().isEsModificable();
	}

	public String getFechaUltimaModProgServ() {
		return fechaUltimaModProgServ;
	}

	public void setFechaUltimaModProgServ(String fechaUltimaModProgServ) {
		this.fechaUltimaModProgServ = fechaUltimaModProgServ;
	}

	public String getHoraUltimaModProgServ() {
		return horaUltimaModProgServ;
	}

	public void setHoraUltimaModProgServ(String horaUltimaModProgServ) {
		this.horaUltimaModProgServ = horaUltimaModProgServ;
	}
	

	
	public ArrayList<DtoSectorSuperficieCuadrante> getArraySuperficiesDiente() {
		return arraySuperficiesDiente;
	}


	public void setArraySuperficiesDiente(
			ArrayList<DtoSectorSuperficieCuadrante> arraySuperficiesDiente) {
		this.arraySuperficiesDiente = arraySuperficiesDiente;
	}


	/**
	 * @return Retorna el atributo evaluado
	 */
	public boolean getEvaluado()
	{
		return evaluado;
	}


	/**
	 * @param evaluado Asigna el atributo evaluado
	 */
	public void setEvaluado(boolean evaluado)
	{
		this.evaluado = evaluado;
	}


	public void setAyundateTamanoDetalleSuperficie(
			int ayundateTamanoDetalleSuperficie) {
		this.ayundateTamanoDetalleSuperficie = ayundateTamanoDetalleSuperficie;
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public int getAyundateTamanoDetalleSuperficie() 
	{
		if(this.detalleSuperficie!=null)
		{
			this.ayundateTamanoDetalleSuperficie=this.detalleSuperficie.size();
		}
		return ayundateTamanoDetalleSuperficie;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public boolean presentaServiciosEvolucionados()
	{
		
		boolean retorno =Boolean.FALSE;
		
		for(InfoHallazgoSuperficie infoHallazgo :  this.getDetalleSuperficie())
		{
			 for( InfoProgramaServicioPlan dtoProgramas : infoHallazgo.getProgramasOservicios() )
			 {
				 
				 if(dtoProgramas.getListaServicios().size()>0)
				 {
					 retorno=Boolean.TRUE;
				 }
			 }
			
		}
		

		return retorno;
	}
	

	
	
	
}