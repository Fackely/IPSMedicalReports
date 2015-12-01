package com.mercury.mundo.odontologia;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import util.UtilidadBD;
import util.UtilidadTexto;

import com.mercury.dao.odontologia.OdontogramaDao;
import com.mercury.dto.odontologia.DtoOtroHallazgo;
import com.mercury.dto.odontologia.DtoTipoHallazgosPieza;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.mundo.UsuarioBasico;

public class Odontograma implements Serializable
{
    private int             codigo;
    private String          codTratamiento;
    private String          observaciones;
    private String          obsNuevas;
    private UsuarioBasico   medico;
    private String          fecha;
    private ArrayList       dientes;
    private Map             dientesMap;
    private boolean         enBD;
    
    /*
     *Atributo Utilizado solamente en la vista 
     */
    private boolean esImpresion;
    
    private String xmlOdontograma;
    
    private boolean version2008;
    /**
     * Atributo  para carga las imagenes
     */
    private String imagen;
    
    public static byte HALLAZGOS_OTROS=0;
    public static byte HALLAZGOS_BOCA=1;
    
    /**
     * Contiene los hallazgos encontrados en la secci&oacute;n otros hallazgos
     */
    private ArrayList<DtoOtroHallazgo> hallazgosOtros;
    
    /**
     * Contiene los hallazgos encontrados en la secci&oacute;n boca
     */
    private ArrayList<DtoOtroHallazgo> hallazgosBoca;

    private static OdontogramaDao odontogramaDao;
    
    /**
     * Indica si el odontograma es temporalmente creado para poder
     * postular la información del odontograma anterior.
     */
    private boolean odontogramaTemporal;
    
    private DtoTipoHallazgosPieza dtoTiposHallazgosPieza;
    
    public static OdontogramaDao getOdontogramaDao()
    {
        if(odontogramaDao==null)
        {
            String tipoBD = System.getProperty("TIPOBD" );
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            odontogramaDao=myFactory.getOdontogramaDao();
        }
        return odontogramaDao;
    }
    
    /**
	 * CARGAR ODONTOGRAMA 
	 * RECIBE UN DTO  
	 * RETORNA DTO
	 * @author Carvajal
	 * @param dtoOdontograma
	 * @return
	 */
	public static DtoOdontograma cargarOdontograma(DtoOdontograma dtoOdontograma) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().cargarOdontograma(dtoOdontograma);
	}
	
    public Odontograma()
    {
        this.codigo=-1;
        this.codTratamiento="";
        this.observaciones="";
        this.obsNuevas="";
        this.medico=new UsuarioBasico();
        this.fecha="";
        this.dientes = new ArrayList();
        this.dientesMap= new HashMap();
        this.enBD=false;
        this.imagen="";
        this.esImpresion=false;
        
        for(int k=10; k<=80; k=k+10)
        {
            for(int i=1+k; i<=(k<=40?8:5)+k; i++)
            {
                DienteOdontograma tempoDiente = new DienteOdontograma();
                tempoDiente.setNumDiente(i);
                this.dientesMap.put("diente_"+i, tempoDiente);
                this.dientes.add(tempoDiente);
            }
        }
        this.xmlOdontograma=null;
        this.setDtoTiposHallazgosPieza(new DtoTipoHallazgosPieza());
    }
    
    public void setCodigo(int codigo)
    {
        this.codigo=codigo;
    }
    
    public int getCodigo()
    {
        return this.codigo;
    }
    
    public void setEnBD(boolean enBD)
    {
        this.enBD=enBD;
    }
    
    public boolean getEnBD()
    {
        return this.enBD;
    }
    
    public void setCodTratamiento(String codTratamiento)
    {
        this.codTratamiento=codTratamiento;
    }
    
    public String getCodTratamiento()
    {
        return this.codTratamiento;
    }
    
    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }
    
    public String getObservaciones()
    {
        return this.observaciones;
    }
    
    public String getObsAnteriores()
    {
        return this.observaciones;
    }
    
    public void setObsAnteriores(String obsAnteriores)
    {
        this.observaciones=obsAnteriores;
    }
    
    public String getObsNuevas()
    {
        return this.obsNuevas;
    }
    
    public void setObsNuevas(String obsNuevas)
    {
        this.obsNuevas=obsNuevas;
    }
    
    public void setMedico(UsuarioBasico medico)
    {
        this.medico=medico;
    }
    
    public UsuarioBasico getMedico()
    {
        return this.medico;
    }
    
    public void setFecha(String fecha)
    {
        this.fecha=fecha;
    }
    
    public String getFecha()
    {
        return this.fecha;
    }
    
    /*public void addDiente(DienteOdontograma diente)
    {
        this.dientes.add(diente);
    }*/
    
    public DienteOdontograma getDienteMap(String key)
    {
        return (DienteOdontograma)this.dientesMap.get(key);
    }
    
    public void setDienteMap(String key, DienteOdontograma value)
    {
        this.dientesMap.put(key, value);
    }
    
    public DienteOdontograma getDiente(int numero)
    {
        Object[] tempoDientes = this.dientesMap.values().toArray();
        return (DienteOdontograma)tempoDientes[numero];
    }
    
    public int getNumDientes()
    {
        return this.dientesMap.values().size();
    }
    
    
    /**
     * Metodo para insertar el odontolgrama
     * @param con
     * @throws SQLException
     */
    public void insertar(Connection con) throws SQLException
    {
    	
    	/*
    	 * Insertar odontolgrama
    	 */
        this.codigo= Odontograma.getOdontogramaDao().insertar(
                con,
                this.getCodTratamiento(),
                this.getObservaciones(),
                this.getMedico().getCodigoPersona(),
                this.getFecha(),
                this.getXmlOdontograma(),
                this.getImagen()
                );
        
        /*
         * 
         */
        for(int i=0; i<this.getNumDientes(); i++)
        {
            DienteOdontograma diente = (DienteOdontograma)this.getDiente(i);
            Odontograma.getOdontogramaDao().insertarDiente(
                    con, 
                    this.getCodigo(), 
                    diente.getNumDiente(), 
                    diente.getCodEstadoInst());
            
            for(int j=0; j<diente.getNumSectores(); j++)
            {
                SectorDiente sectordiente = (SectorDiente)diente.getSector(j);
                Odontograma.getOdontogramaDao().insertarSector(
                        con,
                        this.getCodigo(),
                        diente.getNumDiente(),
                        sectordiente.getNumero(),
                        sectordiente.getCodEstadoInst());
            }
        }
        
        /*
         * 
         */
		if(hallazgosOtros!=null && !hallazgosOtros.isEmpty())
		{
			Odontograma.getOdontogramaDao().insertarOtrosHallazgos(con, hallazgosOtros, getCodigo());
		}
		/*
		 * 
		 */
		if(hallazgosBoca!=null && !hallazgosBoca.isEmpty())
		{
			Odontograma.getOdontogramaDao().insertarOtrosHallazgos(con, hallazgosBoca, getCodigo());
		}

    }
    
    
    /**
	 * Retornar Consulta Plan Tratamiento
	 * @param dto
	 * @param seccion
	 * @param codigoTarifario TODO
	 * @return
	 */
	public static String retornarConsultaOdontogramaPlanTratamiento(DtoPlanTratamientoOdo dto, String seccion , String tiposInclusionGarantia, int codigoTarifario ) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().retornarConsultaOdontogramaPlanTratamiento(dto, seccion, tiposInclusionGarantia, codigoTarifario);
	}
	
	public static ArrayList<DtoOtrosIngresosPaciente> cargarIngresos(int paciente,
			int viaIngreso, int institucion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().cargarIngresos(paciente, viaIngreso, institucion);
	}
	

	/*
	 * TODO BORRAR ESTE METODO
	*/
	public static  String retornarConsultaOdontograma(double codigoOdontograma,
			String tipoOdontograma, int ingreso, String seccion,
			boolean inclusion, boolean garantia) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().retornarConsultaOdontograma(codigoOdontograma, tipoOdontograma, ingreso, seccion, inclusion, garantia);
	}
	
	/**
	 * RETORNA LA CONSULTA DEL ODONTOGRAMA HISTORICOS
	 * @author Carvajal
	 * @param dtoPlanTratamiento
	 * @param seccion
	 * @param tiposInclusionGarantia
	 * @return
	 */
	public static String retornarConsultaOdontograma(
			DtoPlanTratamientoOdo dtoPlanTratamiento, String seccion,
			String tiposInclusionGarantia) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().retornarConsultaOdontograma(dtoPlanTratamiento, seccion, tiposInclusionGarantia);
	}
	
    /**
     * 
     * @param con
     * @throws SQLException
     */
    public void modificar(Connection con) throws SQLException
    {
        this.codigo= Odontograma.getOdontogramaDao().modificar(
                con,
                this.getCodigo(),
                this.getObservaciones());
    }

    public static Collection consultarOdontogramasTratamiento(Connection con, int codTratamiento) throws SQLException
    {
        ResultSetDecorator rs = Odontograma.getOdontogramaDao().consultarOdontogramasTratamiento(con, codTratamiento);
        return UtilidadBD.resultSet2Collection(rs);
    }    
    
    public void consultar(Connection con, int codigo) throws SQLException
    {
        Odontograma.getOdontogramaDao().consultar(
                con, 
                codigo, this);
    }
    

	/**
	 * 
	 * @param dtoPlan
	 * @author axioma
	 * @return
	 */
	public static DtoOdontograma cargarOdontogramaImagen(DtoPlanTratamientoOdo dtoPlan) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().cargarOdontogramaImagen(dtoPlan);
	}
	
	
	public static  ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().cargar(dtoWhere);
	}
	
	
	
	public static  ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere, DtoPlanTratamientoOdo dtoPlan) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOdontogramaDao().cargar(dtoWhere, dtoPlan);
	}
	

	public String getXmlOdontograma()
	{
		return xmlOdontograma;
	}

	public void setXmlOdontograma(String xmlOdontograma)
	{
		this.xmlOdontograma = xmlOdontograma;
	}

	public ArrayList<DtoOtroHallazgo> getHallazgosOtros()
	{
		return hallazgosOtros;
	}

	public void setHallazgosOtros(ArrayList<DtoOtroHallazgo> hallazgosOtros)
	{
		this.hallazgosOtros = hallazgosOtros;
	}

	public ArrayList<DtoOtroHallazgo> getHallazgosBoca()
	{
		return hallazgosBoca;
	}

	public void setHallazgosBoca(ArrayList<DtoOtroHallazgo> hallazgosBoca)
	{
		this.hallazgosBoca = hallazgosBoca;
	}

	/**
	 * Establece el valor del atributo imagen
	 *
	 * @param valor para el atributo imagen
	 */
	public void setImagen(String imagen)
	{
		this.imagen = imagen;
	}

	/**
	 * Obtiene el valor del atributo imagen
	 *
	 * @return Retorna atributo imagen
	 */
	public String getImagen()
	{
		return imagen;
	}

	/**
	 * Establece el valor del atributo esImpresion
	 *
	 * @param valor para el atributo esImpresion
	 */
	public void setEsImpresion(boolean esImpresion)
	{
		this.esImpresion = esImpresion;
	}

	/**
	 * Obtiene el valor del atributo esImpresion
	 *
	 * @return Retorna atributo esImpresion
	 */
	public boolean getEsImpresion()
	{
		return esImpresion;
	}

	/**
	 * Obtiene el valor del atributo odontogramaTemporal
	 *
	 * @return Retorna atributo odontogramaTemporal
	 */
	public boolean isOdontogramaTemporal()
	{
		return odontogramaTemporal;
	}

	/**
	 * Establece el valor del atributo odontogramaTemporal
	 *
	 * @param valor para el atributo odontogramaTemporal
	 */
	public void setOdontogramaTemporal(boolean odontogramaTemporal)
	{
		this.odontogramaTemporal = odontogramaTemporal;
	}

	/**
	 * @return the version2008
	 */
	public boolean isVersion2008() {
		if(UtilidadTexto.isEmpty(this.imagen) && UtilidadTexto.isEmpty(this.xmlOdontograma)){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * @param dtoTiposHallazgosPieza the dtoTiposHallazgosPieza to set
	 */
	public void setDtoTiposHallazgosPieza(DtoTipoHallazgosPieza dtoTiposHallazgosPieza) {
		this.dtoTiposHallazgosPieza = dtoTiposHallazgosPieza;
	}

	/**
	 * @return the dtoTiposHallazgosPieza
	 */
	public DtoTipoHallazgosPieza getDtoTiposHallazgosPieza() {
		return dtoTiposHallazgosPieza;
	}




}